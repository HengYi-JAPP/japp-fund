var MergePage = /** @class */ (function () {
    function MergePage(currencyId, date, divideDate) {
        var _this = this;
        this.$table = $('#page-table').loading();
        this.currencies = [
            { id: 'g111W2qibFUfXSub6MEEJ', name: '恒逸汇总（人民币）' },
            { id: 'g1zTboj6D3ddaXJrvm5AF', name: '恒逸汇总（美元）' },
            { id: 'SSmTiaVKUe6xy37VZmacWj', name: '恒逸汇总（承兑汇票）' },
        ];
        this.$pageForm = $('#pageForm').on('submit', function () {
            var params = _this.formParams();
            _this.$pageForm.find('input[name="currencyId"]').val(params.currencyId);
            _this.$pageForm.find('input[name="year"]').val(params.year);
            _this.$pageForm.find('input[name="month"]').val(params.month);
            _this.$pageForm.find('input[name="divideYear"]').val(params.divideYear);
            _this.$pageForm.find('input[name="divideMonth"]').val(params.divideMonth);
            _this.$pageForm.find('input[name="divideDay"]').val(params.divideDay);
        });
        this.date = moment(date);
        this.divideDate = moment(divideDate);
        this.weekDatas = J.generateMonthWeeks(this.date).map(function (dates, index) { return new MergeWeekData(_this, dates, index + 1); });
        $('#year-month').datetimepicker(J.dtp({
            initialDate: this.date.toDate(),
            startView: 'year',
            minView: 'year',
        })).on('changeDate', function (ev) {
            var d = moment(ev.date);
            if (_this.date.isSame(d, 'month')) {
                return;
            }
            _this.date = d;
            _this.$pageForm.submit();
        });
        $('#divide-date').datetimepicker(J.dtp({
            initialDate: this.divideDate.toDate(),
        })).on('changeDate', function (ev) {
            var d = moment(ev.date);
            if (_this.divideDate.isSame(d, 'day')) {
                return;
            }
            _this.divideDate = d;
            _this.$pageForm.submit();
        });
        var $currencies = $('#currency-list');
        var currencyMenu = function (currency) {
            if (currency.id === currencyId) {
                _this.currency = currency;
            }
            return $("<a href=\"javascript:\">" + currency.name + "</a>").on('click', function () {
                if (_this.currency.id === currency.id) {
                    return;
                }
                _this.currency = currency;
                _this.$pageForm.submit();
            });
        };
        this.currencies.forEach(function (it) {
            var $li = $('<li></li>').appendTo($currencies);
            currencyMenu(it).appendTo($li);
        });
        this.currency = this.currency || this.currencies[0];
        $('#currency').text(this.currency.name);
        var p1 = J.listReportsSumSumTypes().then(function (res) { return _this.sumTypes = res; });
        var p2 = J.listReportsSumBalances(this.formParams()).then(function (res) {
            _this.balances = res;
            _this.corporationMap = _this.balances.reduce(function (acc, cur) {
                var corporation = cur.corporation;
                acc[corporation.id] = corporation;
                return acc;
            }, {});
        });
        $.when(p1, p2).then(function () {
            //显示table数据
            _this.weekDatas.forEach(function (it) { return it.fillData(); });
            // 第几周跳转，放在分割日之后
            J.generateWeekScrolls($('#divide-date').parent(), _this.$table.parent(), _this.weekDatas);
            _this.$table.loading('toggle');
        });
    }
    MergePage.prototype.monthAdd = function (i) {
        if (i === void 0) { i = 1; }
        this.date = moment(this.date).add(i, 'M');
        this.divideDate = moment();
        this.$pageForm.submit();
    };
    MergePage.prototype.dblclickSumType = function () {
        this.weekDatas.forEach(function (weekData) {
            weekData.corporationTrs.forEach(function (it) { return it.toggle(); });
        });
    };
    MergePage.prototype.exportCurrentXlsx = function () {
        J.exportSumReportsXlsx(this.formParams());
    };
    MergePage.prototype.exportAllXlsx = function () {
        var params = this.formParams();
        delete params.currencyId;
        J.exportSumReportsXlsx(params);
    };
    MergePage.prototype.formParams = function () {
        return {
            currencyId: this.currency && this.currency.id,
            year: this.date.year(),
            month: this.date.month() + 1,
            divideYear: this.divideDate.year(),
            divideMonth: this.divideDate.month() + 1,
            divideDay: this.divideDate.date(),
        };
    };
    return MergePage;
}());
var MergeWeekData = /** @class */ (function () {
    function MergeWeekData(page, dates, index) {
        this.page = page;
        this.dates = dates;
        this.index = index;
        //双击小计，隐藏公司项
        this.corporationTrsMap = {};
        this.domId = "week-" + index;
    }
    Object.defineProperty(MergeWeekData.prototype, "corporationTrs", {
        get: function () {
            var _this = this;
            return Object.keys(this.corporationTrsMap)
                .map(function (it) { return _this.corporationTrsMap[it]; })
                .reduce(function (acc, cur) { return acc.concat(cur); }, []);
        },
        enumerable: true,
        configurable: true
    });
    MergeWeekData.prototype.fillData = function () {
        var _this = this;
        this.dayDatas = this.dates.map(function (it) { return new MergeDayData(_this.page, it); });
        var hTrs = [
            $("<tr id=\"" + this.domId + "\"><th rowspan=\"2\" class=\"week-index\">\u7B2C" + this.index + "\u5468</th></tr>"),
            $('<tr></tr>'),
            $('<tr><th>日期</th></tr>'),
        ].map(function (it) { return it.appendTo(_this.page.$table); });
        J.WEEK_DAYS.forEach(function (it, i) {
            $("<th class=\"week-day\">" + it + "</th>").appendTo(hTrs[0]);
            $('<th>金额</th>').appendTo(hTrs[1]);
            var dayOfMonth = _this.dates[i].isSame(_this.page.date, 'month') ? _this.dates[i].date() + '号' : '';
            $("<th class=\"week-day\">" + dayOfMonth + "</th>").appendTo(hTrs[2]);
        });
        this.page.sumTypes.forEach(function (sumType) {
            if (sumType.corporationIds) {
                sumType.corporationIds.map(function (it) { return _this.page.corporationMap[it]; }).filter(function (it) { return it; }).forEach(function (corporation) {
                    var $tr = $("<tr><td>" + corporation.name + "</td></tr>").appendTo(_this.page.$table);
                    _this.dayDatas.forEach(function (dayData) {
                        $tr.append(dayData.corporationTd(corporation.id, sumType));
                    });
                    var corporationTrs = _this.corporationTrsMap[sumType.type] || [];
                    corporationTrs.push($tr);
                    _this.corporationTrsMap[sumType.type] = corporationTrs;
                });
            }
            var $tr = $("<tr><td>" + sumType.name + "</td></tr>").appendTo(_this.page.$table).on('dblclick', function () {
                _this.page.dblclickSumType();
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
            _this.dayDatas.forEach(function (dayData) {
                $tr.append(dayData.sumTypeTd(sumType));
            });
        });
        var $tr = $('<tr class="balance_3"><td>本日余额总计</td></tr>').appendTo(this.page.$table).on('dblclick', function () {
            _this.page.dblclickSumType();
        });
        this.dayDatas.forEach(function (dayData) {
            $tr.append(dayData.todayBalanceTd());
        });
    };
    return MergeWeekData;
}());
var MergeDayData = /** @class */ (function () {
    function MergeDayData(page, date) {
        var _this = this;
        this.page = page;
        this.date = date;
        /**
         * 用于求合计和小计
         * @type {{}}
         */
        this.balanceMap = {};
        this.todayBalance = 0;
        this.dateInMonth = this.date.isSame(this.page.date, 'month');
        if (!this.dateInMonth) {
            return;
        }
        this.balances = this.page.balances.filter(function (it) { return _this.date.isSame(moment(it.date), 'day'); });
    }
    MergeDayData.prototype.corporationTd = function (id, sumType) {
        var _this = this;
        var $td = $('<td></td>');
        if (this.dateInMonth) {
            var balance_1 = 0;
            this.balances.forEach(function (ba) {
                if (ba.corporation.id === id) {
                    balance_1 = ba.balance;
                    $td.text(J.renderMoney(ba.balance)).addClass('money');
                    if (!_this.date.isBefore(_this.page.divideDate, 'day')) {
                        $td.addClass('divide');
                    }
                }
            });
            this.todayBalance += balance_1;
            var mapBalance = this.balanceMap[sumType.type] || 0.00;
            mapBalance += balance_1;
            this.balanceMap[sumType.type] = mapBalance;
        }
        return $td;
    };
    MergeDayData.prototype.sumTypeTd = function (sumType) {
        var _this = this;
        var $td = $('<td></td>');
        if (this.dateInMonth) {
            var balance_2 = this.balanceMap[sumType.type];
            if (!balance_2) {
                balance_2 = 0;
                Object.keys(this.balanceMap).forEach(function (k) {
                    if (k.indexOf(sumType.type) === 0) {
                        balance_2 += _this.balanceMap[k];
                    }
                });
            }
            $td.text(J.renderMoney(balance_2)).addClass('money');
        }
        return $td;
    };
    MergeDayData.prototype.todayBalanceTd = function () {
        var $td = $('<td></td>');
        if (this.dateInMonth) {
            $td.text(J.renderMoney(this.todayBalance)).addClass('money');
        }
        return $td;
    };
    return MergeDayData;
}());
