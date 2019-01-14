declare var $: any;
declare var moment: any;
declare var J: any;

class MergePage {
    readonly $table = $('#page-table').loading();
    corporationMap: { [id: string]: any };
    readonly currencies = [
        {id: 'g111W2qibFUfXSub6MEEJ', name: '恒逸汇总（人民币）'},
        {id: 'g1zTboj6D3ddaXJrvm5AF', name: '恒逸汇总（美元）'},
        {id: 'SSmTiaVKUe6xy37VZmacWj', name: '恒逸汇总（承兑汇票）'},
    ];
    currency: any;
    date: any;
    divideDate: any;
    balances: any[];
    sumTypes: any[];
    private readonly $pageForm = $('#pageForm').on('submit', () => {
        const params = this.formParams();
        this.$pageForm.find('input[name="currencyId"]').val(params.currencyId);
        this.$pageForm.find('input[name="year"]').val(params.year);
        this.$pageForm.find('input[name="month"]').val(params.month);
        this.$pageForm.find('input[name="divideYear"]').val(params.divideYear);
        this.$pageForm.find('input[name="divideMonth"]').val(params.divideMonth);
        this.$pageForm.find('input[name="divideDay"]').val(params.divideDay);
    });
    private readonly weekDatas: MergeWeekData[];

    constructor(currencyId: string, date: string, divideDate: string) {
        this.date = moment(date);
        this.divideDate = moment(divideDate);
        this.weekDatas = J.generateMonthWeeks(this.date).map((dates, index) => new MergeWeekData(this, dates, index + 1));

        $('#year-month').datetimepicker(J.dtp({
            initialDate: this.date.toDate(),
            startView: 'year',
            minView: 'year',
        })).on('changeDate', ev => {
            const d = moment(ev.date);
            if (this.date.isSame(d, 'month')) {
                return;
            }
            this.date = d;
            this.$pageForm.submit();
        });
        $('#divide-date').datetimepicker(J.dtp({
            initialDate: this.divideDate.toDate(),
        })).on('changeDate', ev => {
            const d = moment(ev.date);
            if (this.divideDate.isSame(d, 'day')) {
                return;
            }
            this.divideDate = d;
            this.$pageForm.submit();
        });

        const $currencies = $('#currency-list');
        const currencyMenu = currency => {
            if (currency.id === currencyId) {
                this.currency = currency;
            }
            return $(`<a href="javascript:">${currency.name}</a>`).on('click', () => {
                if (this.currency.id === currency.id) {
                    return;
                }
                this.currency = currency;
                this.$pageForm.submit();
            });
        };
        this.currencies.forEach(it => {
            const $li = $('<li></li>').appendTo($currencies);
            currencyMenu(it).appendTo($li);
        });
        this.currency = this.currency || this.currencies[0];
        $('#currency').text(this.currency.name);

        const p1 = J.listReportsSumSumTypes().then(res => this.sumTypes = res);
        const p2 = J.listReportsSumBalances(this.formParams()).then(res => {
            this.balances = res;
            this.corporationMap = this.balances.reduce((acc, cur) => {
                const corporation = cur.corporation;
                acc[corporation.id] = corporation;
                return acc;
            }, {});
        });
        $.when(p1, p2).then(() => {
            //显示table数据
            this.weekDatas.forEach(it => it.fillData());
            // 第几周跳转，放在分割日之后
            J.generateWeekScrolls($('#divide-date').parent(), this.$table.parent(), this.weekDatas);
            this.$table.loading('toggle');
        });
    }

    monthAdd(i = 1) {
        this.date = moment(this.date).add(i, 'M');
        this.divideDate = moment();
        this.$pageForm.submit();
    }

    dblclickSumType() {
        this.weekDatas.forEach(weekData => {
            weekData.corporationTrs.forEach(it => it.toggle());
        });
    }

    exportCurrentXlsx() {
        J.exportSumReportsXlsx(this.formParams());
    }

    exportAllXlsx() {
        const params = this.formParams();
        delete params.currencyId;
        J.exportSumReportsXlsx(params);
    }

    private formParams(): any {
        return {
            currencyId: this.currency && this.currency.id,
            year: this.date.year(),
            month: this.date.month() + 1,
            divideYear: this.divideDate.year(),
            divideMonth: this.divideDate.month() + 1,
            divideDay: this.divideDate.date(),
        }
    }
}

class MergeWeekData {
    //快速导航到周
    readonly domId: string;
    //双击小计，隐藏公司项
    readonly corporationTrsMap = {};
    private dayDatas: MergeDayData[];

    constructor(private readonly page: MergePage, private readonly dates: any[], private readonly index: number) {
        this.domId = `week-${index}`;
    }

    get corporationTrs() {
        return Object.keys(this.corporationTrsMap)
            .map(it => this.corporationTrsMap[it])
            .reduce((acc, cur) => acc.concat(cur), [])
    }

    fillData() {
        this.dayDatas = this.dates.map(it => new MergeDayData(this.page, it));

        const hTrs = [
            $(`<tr id="${this.domId}"><th rowspan="2" class="week-index">第${this.index}周</th></tr>`),
            $('<tr></tr>'),
            $('<tr><th>日期</th></tr>'),
        ].map(it => it.appendTo(this.page.$table));
        J.WEEK_DAYS.forEach((it, i) => {
            $(`<th class="week-day">${it}</th>`).appendTo(hTrs[0]);
            $('<th>金额</th>').appendTo(hTrs[1]);
            const dayOfMonth = this.dates[i].isSame(this.page.date, 'month') ? this.dates[i].date() + '号' : '';
            $(`<th class="week-day">${dayOfMonth}</th>`).appendTo(hTrs[2]);
        });

        this.page.sumTypes.forEach(sumType => {
            if (sumType.corporationIds) {
                sumType.corporationIds.map(it => this.page.corporationMap[it]).filter(it => it).forEach(corporation => {
                    const $tr = $(`<tr><td>${corporation.name}</td></tr>`).appendTo(this.page.$table);
                    this.dayDatas.forEach(dayData => {
                        $tr.append(dayData.corporationTd(corporation.id, sumType));
                    });
                    const corporationTrs = this.corporationTrsMap[sumType.type] || [];
                    corporationTrs.push($tr);
                    this.corporationTrsMap[sumType.type] = corporationTrs;
                });
            }

            const $tr = $(`<tr><td>${sumType.name}</td></tr>`).appendTo(this.page.$table).on('dblclick', () => {
                this.page.dblclickSumType();
            });
            switch (sumType.type) {
                case 'SS':
                case 'FSS': {
                    $tr.addClass('balance_2');
                    break;
                }
                default: {
                    $tr.addClass('balance_1');
                    break;
                }
            }
            this.dayDatas.forEach(dayData => {
                $tr.append(dayData.sumTypeTd(sumType));
            });
        });

        const $tr = $('<tr class="balance_3"><td>本日余额总计</td></tr>').appendTo(this.page.$table).on('dblclick', () => {
            this.page.dblclickSumType();
        });
        this.dayDatas.forEach(dayData => {
            $tr.append(dayData.todayBalanceTd());
        });
    }
}

class MergeDayData {
    private readonly dateInMonth: boolean;
    private readonly balances: any[];
    /**
     * 用于求合计和小计
     * @type {{}}
     */
    private balanceMap = {};
    private todayBalance = 0;

    constructor(private readonly page: MergePage, readonly date: any) {
        this.dateInMonth = this.date.isSame(this.page.date, 'month');
        if (!this.dateInMonth) {
            return;
        }
        this.balances = this.page.balances.filter(it => this.date.isSame(moment(it.date), 'day'));
    }

    corporationTd(id: string, sumType: any) {
        const $td = $('<td></td>');
        if (this.dateInMonth) {
            let balance = 0;
            this.balances.forEach(ba => {
                if (ba.corporation.id === id) {
                    balance = ba.balance;
                    $td.text(J.renderMoney(ba.balance)).addClass('money');
                    if (!this.date.isBefore(this.page.divideDate, 'day')) {
                        $td.addClass('divide');
                    }
                }
            });
            this.todayBalance += balance;

            let mapBalance = this.balanceMap[sumType.type] || 0.00;
            mapBalance += balance;
            this.balanceMap[sumType.type] = mapBalance;
        }
        return $td;
    }

    sumTypeTd(sumType: any) {
        const $td = $('<td></td>');
        if (this.dateInMonth) {
            let balance = this.balanceMap[sumType.type];
            if (!balance) {
                balance = 0;
                Object.keys(this.balanceMap).forEach(k => {
                    if (k.indexOf(sumType.type) === 0) {
                        balance += this.balanceMap[k];
                    }
                });
            }
            $td.text(J.renderMoney(balance)).addClass('money');
        }
        return $td;
    }

    todayBalanceTd() {
        const $td = $('<td></td>');
        if (this.dateInMonth) {
            $td.text(J.renderMoney(this.todayBalance)).addClass('money');
        }
        return $td;
    }
}
