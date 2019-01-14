declare var $: any;
declare var moment: any;
declare var J: any;

class DailyPage {
    readonly $table = $('#page-table').loading();
    readonly fundUpdateModal = new FundUpdateModal(this);
    readonly batchFundUpdateModal = new BatchFundUpdateModal(this);
    corporation: any;
    currency: any;
    date: any;
    divideDate: any;
    balances: any[];
    funds: any[];
    private readonly $pageForm = $('#pageForm').on('submit', () => {
        const params = this.formParams();
        this.$pageForm.find('input[name="corporationId"]').val(params.corporationId);
        this.$pageForm.find('input[name="currencyId"]').val(params.currencyId);
        this.$pageForm.find('input[name="year"]').val(params.year);
        this.$pageForm.find('input[name="month"]').val(params.month);
        this.$pageForm.find('input[name="divideYear"]').val(params.divideYear);
        this.$pageForm.find('input[name="divideMonth"]').val(params.divideMonth);
        this.$pageForm.find('input[name="divideDay"]').val(params.divideDay);
    });
    private readonly weekDatas: DailyWeekData[];

    constructor(corporationId: string, currencyId: string, date: string, divideDate: string) {
        this.date = moment(date);
        this.divideDate = moment(divideDate);
        this.weekDatas = J.generateMonthWeeks(this.date).map((dates, index) => new DailyWeekData(this, dates, index + 1));

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

        J.listCorporation().then(res => {
            const corporations = res.sort((o1, o2) => o2.sortBy - o1.sortBy);
            const $corporations = $('#corporation-list');
            const corporationMenu = corporation => {
                if (corporation.id === corporationId) {
                    this.corporation = corporation;
                }
                return $(`<a href="javascript:">${corporation.name}</a>`).on('click', () => {
                    if (this.corporation.id === corporation.id) {
                        return;
                    }
                    this.corporation = corporation;
                    this.$pageForm.submit();
                });
            };
            const [rows, cols] = J.calcRowsCols(corporations.length);
            if (cols === 1) {
                corporations.forEach(it => {
                    const $li = $('<li></li>').appendTo($corporations);
                    corporationMenu(it).appendTo($li);
                });
            } else {
                $corporations.addClass('dropdown-menu-table');
                const $corporationTable = $('<table class="table table-bordered"></table>').appendTo($corporations);
                for (let row = 0; row < rows; row++) {
                    const $tr = $('<tr></tr>').appendTo($corporationTable);
                    for (let col = 0; col < cols; col++) {
                        const $td = $('<td></td>').appendTo($tr);
                        const i = row * cols + col;
                        if (i < corporations.length) {
                            corporationMenu(corporations[i]).appendTo($td);
                        }
                    }
                }
            }
            this.corporation = this.corporation || corporations[0];
            $('#corporation').text(this.corporation.name);

            const currencies = this.corporation.currencies.sort((o1, o2) => o2.sortBy - o1.sortBy);
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
            currencies.forEach(it => {
                const $li = $('<li></li>').appendTo($currencies);
                currencyMenu(it).appendTo($li);
            });
            this.currency = this.currency || currencies[0];
            $('#currency').text(this.currency.name);

            const params = this.formParams();
            const p1 = J.listBalance(params).then(it => this.balances = it);
            const p2 = J.listFundlike(params).then(it => this.funds = it);
            return $.when(p1, p2);
        }).then(() => {
            //显示table数据
            this.weekDatas.forEach(it => it.fillData());
            // 第几周跳转，放在分割日之后
            J.generateWeekScrolls($('#divide-date').parent(), this.$table.parent(), this.weekDatas);
            this.$table.loading('toggle');
        });
    }

    /**
     * 刷新页面，明细增删改后的操作
     * 不提交page表单，但重新获取余额和明细
     */
    refresh() {
        this.$table.loading();
        const params = this.formParams();
        const p1 = J.listBalance(params).then(it => this.balances = it);
        const p2 = J.listFundlike(params).then(it => this.funds = it);
        $.when(p1, p2).then(() => {
            const container = this.$table.parent();
            const scrollTop = container.scrollTop();
            this.$table.empty();
            this.weekDatas.forEach(it => it.fillData());
            container.scrollTop(scrollTop);
            this.$table.loading('toggle');
        });
    }

    monthAdd(i = 1) {
        this.date = moment(this.date).add(i, 'M');
        this.divideDate = moment();
        this.$pageForm.submit();
    }

    createBatchFund() {
        this.batchFundUpdateModal.show();
    }

    exportCurrentXlsx() {
        J.exportXlsx(this.formParams());
    }

    exportAllXlsx() {
        const params = this.formParams();
        delete params.corporationId;
        delete params.currencyId;
        J.exportXlsx(params);
    }

    private formParams(): any {
        return {
            corporationId: this.corporation && this.corporation.id,
            currencyId: this.currency && this.currency.id,
            year: this.date.year(),
            month: this.date.month() + 1,
            divideYear: this.divideDate.year(),
            divideMonth: this.divideDate.month() + 1,
            divideDay: this.divideDate.date(),
        }
    }
}

class DailyWeekData {
    readonly domId: string;
    private dayDatas: DailyDayData[];

    constructor(private readonly page: DailyPage, private readonly dates: any[], readonly index: number) {
        this.domId = `week-${index}`;
    }

    fillData() {
        this.dayDatas = this.dates.map(it => new DailyDayData(this.page, it));
        // 每周的前面3行数据
        const hTrs = [
            $(`<tr id="${this.domId}"><th rowspan="2" class="week-index">第${this.index}周</th></tr>`),
            $('<tr></tr>'),
            $('<tr><th>日期</th></tr>'),
        ].map(it => it.appendTo(this.page.$table));
        J.WEEK_DAYS.forEach((it, i) => {
            $(`<th colspan="2" class="week-day">${it}</th>`).appendTo(hTrs[0]);
            $('<th>金额</th>').appendTo(hTrs[1]);
            $('<th>简述</th>').appendTo(hTrs[1]);
            const dayOfMonth = this.dates[i].isSame(this.page.date, 'month') ? this.dates[i].date() + '号' : '';
            $(`<th colspan="2" class="week-day">${dayOfMonth}</th>`).appendTo(hTrs[2]);
        });

        const $preBalance = $('<tr class="balance_1"><td>上日余额</td></tr>').appendTo(this.page.$table);
        this.dayDatas.forEach(dayData => {
            dayData.preBalanceTds().forEach(it => $preBalance.append(it));
        });
        const outFundsCount = this.dayDatas.reduce((acc, cur) => Math.max(acc, cur.outFundsCount()), 0);
        for (let i = 0; i <= outFundsCount; i++) {
            const $tr = $(`<tr><td>支出${(i + 1)}</td></tr>`).appendTo(this.page.$table);
            this.dayDatas.forEach(dayData => {
                dayData.outFundTds(i).forEach(it => $tr.append(it));
            });
        }

        const $lackBalance = $('<tr class="balance_2"><td>资金余缺</td></tr>').appendTo(this.page.$table);
        this.dayDatas.forEach(dayData => {
            dayData.lackBalanceTds().forEach(it => $lackBalance.append(it));
        });
        const inFundsCount = this.dayDatas.reduce((acc, cur) => Math.max(acc, cur.inFundsCount()), 0);
        for (let i = 0; i <= inFundsCount; i++) {
            const $tr = $(`<tr><td>收入${(i + 1)}</td></tr>`).appendTo(this.page.$table);
            this.dayDatas.forEach(dayData => {
                dayData.inFundTds(i).forEach(it => $tr.append(it));
            });
        }

        const $todayBalance = $('<tr class="balance_3"><td>本日余额</td></tr>').appendTo(this.page.$table);
        this.dayDatas.forEach(dayData => {
            dayData.todayBalanceTds().forEach(it => $todayBalance.append(it));
        });
    }
}

class DailyDayData {
    /**
     * 标记这一天是否在所选的月当中
     * 日期生成的时候会前补充和后补充，凑成7天，1周
     */
    private readonly dateInMonth: boolean;
    private preBalance: number;
    private readonly lackBalance: number;
    private todayBalance: number;
    private readonly outFunds = [];
    private readonly inFunds = [];
    // 实际计算的余额，用于debug
    private calcTodayBalance: number;

    constructor(private readonly page: DailyPage, readonly date: any) {
        this.dateInMonth = this.date.isSame(this.page.date, 'month');
        if (!this.dateInMonth) {
            return;
        }

        let outFundsSum = 0.00;
        let inFundsSum = 0.00;
        this.page.funds.filter(fund => this.date.isSame(fund.date, 'day')).forEach(fund => {
            if (fund.money > 0) {
                this.inFunds.push(fund);
                inFundsSum += fund.money;
            } else {
                this.outFunds.push(fund);
                outFundsSum += fund.money;
            }
        });

        const preDate = moment(this.date).add(-1, 'd');
        this.page.balances.forEach(it => {
            if (this.date.isSame(it.date, 'day')) {
                this.todayBalance = it.balance;
            } else if (preDate.isSame(it.date, 'day')) {
                this.preBalance = it.balance;
            }
        });
        /**
         * 页面数据获取只取本月的数据
         * 所以每月1号的上日余额，需要计算
         */
        if (!this.preBalance) {
            this.preBalance = this.todayBalance - inFundsSum - outFundsSum;
        }
        this.lackBalance = this.preBalance + outFundsSum;
        this.calcTodayBalance = this.preBalance + inFundsSum + outFundsSum;
    }

    preBalanceTds(): any[] {
        return this.balanceTds(this.preBalance);
    }

    lackBalanceTds(): any[] {
        return this.balanceTds(this.lackBalance);
    }

    todayBalanceTds(): any[] {
        return this.balanceTds(this.todayBalance);
    }

    outFundTds(rowIndex: number): any[] {
        return this.fundTds(this.outFunds, rowIndex);
    }

    outFundsCount(): number {
        return this.outFunds ? this.outFunds.length : 0;
    }

    inFundTds(rowIndex: number): any[] {
        return this.fundTds(this.inFunds, rowIndex);
    }

    inFundsCount(): number {
        return this.inFunds ? this.inFunds.length : 0;
    }

    private balanceTds(balance: number): any[] {
        const result = [$('<td></td>'), $('<td></td>')];
        const [$td] = result;
        if (this.dateInMonth && balance) {
            $td.text(J.renderMoney(balance)).addClass('money');
        }
        return result;
    }

    private fundTds(funds: any[], index: number): any[] {
        const result = [$('<td></td>'), $('<td></td>')];
        const [$money, $note] = result;
        const fund = funds && funds[index];
        if (fund) {
            $money.text(J.renderMoney(fund.money)).addClass('money');
            $note.text(fund.note).addClass('note text-ellipsis').attr('title', fund.note);
        }
        if (this.dateInMonth) {
            result.forEach(it => {
                it.on('dblclick', () => this.page.fundUpdateModal.show(fund, this.date));
            });
            if (!this.date.isBefore(this.page.divideDate, 'day')) {
                $money.addClass('divide');
                $note.addClass('divide');
            }
        }
        return result;
    }
}

class BatchFundUpdateModal {
    private readonly $modal = $('#batchFundUpdateModal');
    private readonly $form = this.$modal.find('form');
    private readonly $startDate = this.$form.find('input[name="startDate"]');
    private readonly $endDate = this.$form.find('input[name="endDate"]');
    private readonly $monthFundPlanList = this.$form.find('select[name="monthFundPlanId"]');
    private readonly $note = this.$form.find('textarea[name="note"]');
    private readonly $moneyDisplay = this.$form.find('#batch-money-show');
    private readonly $money = this.$form.find('input[name="money"]').on('input propertychange', () => {
        const v = this.$money.val();
        this.$moneyDisplay.text($.formatMoney(v));
    });
    private readonly $save = this.$modal.find('button[name="save"]').on('click', () => {
        J.createBatchFundlike(this.$monthFundPlanList.val(), {
            startDate: moment(this.$startDate.val()).toDate(),
            endDate: moment(this.$endDate.val()).toDate(),
            money: this.$money.val(),
            note: this.$note.val(),
        }).then(() => {
            this.page.refresh();
            this.$modal.modal('hide');
        });
    });

    constructor(private page: DailyPage) {
        const startDate = moment(this.page.date);
        this.$startDate.val(startDate.format('YYYY-MM-DD'));
        this.$startDate.datetimepicker(J.dtp({
            initialDate: startDate.toDate(),
        })).on('changeDate', ev => {
            this.$endDate.datetimepicker('setStartDate', ev.date);
        });

        const endDate = moment(startDate).add(1, 'M').add(-1, 'd');
        this.$endDate.val(endDate.format('YYYY-MM-DD'));
        this.$endDate.datetimepicker(J.dtp({
            initialDate: endDate.toDate(),
        }));
    }

    show() {
        J.listMonthFundPlans({
            corporationId: this.page.corporation.id,
            currencyId: this.page.currency.id,
            year: this.page.date.year(),
            month: this.page.date.month() + 1,
        }).then(monthFundPlans => {
            this.$monthFundPlanList.empty();
            monthFundPlans.forEach(it => {
                $(`<option value="${it.id}">${it.purpose.name}</option>`).appendTo(this.$monthFundPlanList)
            });
            this.$monthFundPlanList.val(monthFundPlans[0].id);
            this.$money.val('');
            this.$money.trigger('input');
            this.$note.val('');
            this.$modal.modal({moveable: true, rememberPos: true});
        });
    }

}

class FundUpdateModal {
    private readonly $modal = $('#fundUpdateModal');
    private readonly $modalTitle = this.$modal.find('.modal-title');
    private readonly $form = $('#fundUpdateForm');
    private readonly $date = this.$form.find('input[name="date"]').datetimepicker(J.dtp({
        maxView: 'year',
    }));
    private readonly $monthFundPlanList = this.$form.find('select[name="monthFundPlanId"]');
    private readonly $moneyDisplay = this.$form.find('#money-show');
    private readonly $note = this.$form.find('textarea[name="note"]');
    private readonly $money = this.$form.find('input[name="money"]').on('input propertychange', () => {
        const v = this.$money.val();
        this.$moneyDisplay.text($.formatMoney(v));
    });
    private fund: any;
    private readonly $save = this.$modal.find('button[name="save"]').on('click', () => {
        $.extend(this.fund, {
            date: moment(this.$date.val()).toDate(),
            money: this.$money.val(),
            note: this.$note.val(),
        });
        J.saveFundlike(this.$monthFundPlanList.val(), this.fund).then(() => {
            this.page.refresh();
            this.$modal.modal('hide');
        });
    });
    private readonly $delete = this.$modal.find('button[name="delete"]').on('click', () => {
        if (!confirm('删除确认？')) {
            return;
        }
        J.deleteFundlike(this.fund.id).then(() => {
            this.page.refresh();
            this.$modal.modal('hide');
        });
    });

    constructor(private page: DailyPage) {
    }

    show(orignFund: any, date: any) {
        J.listMonthFundPlans({
            corporationId: this.page.corporation.id,
            currencyId: this.page.currency.id,
            year: this.page.date.year(),
            month: this.page.date.month() + 1,
        }).then(monthFundPlans => {
            this.$monthFundPlanList.empty();
            monthFundPlans.forEach(it => {
                $(`<option value="${it.id}">${it.purpose.name}</option>`).appendTo(this.$monthFundPlanList)
            });

            if (orignFund && orignFund.id) {
                this.fund = $.extend({}, orignFund);
                this.$modalTitle.text('更新');
                this.$delete.show();
            } else {
                this.fund = {monthFundPlan: monthFundPlans[0], date};
                this.$modalTitle.text('新增');
                this.$delete.hide();
            }
            this.$date.val(moment(this.fund.date).format('YYYY-MM-DD'));
            this.$monthFundPlanList.val(this.fund.monthFundPlan.id);
            this.$money.val(this.fund.money);
            this.$money.trigger('input');
            this.$note.val(this.fund.note);
            this.$modal.modal({moveable: true, rememberPos: true});
        });
    }

}