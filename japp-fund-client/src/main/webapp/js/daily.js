var DailyPage = /** @class */ (function () {
    function DailyPage(corporationId, currencyId, date, divideDate) {
        var _this = this;
        this.$table = $('#page-table').loading();
        this.fundUpdateModal = new FundUpdateModal(this);
        this.batchFundUpdateModal = new BatchFundUpdateModal(this);
        this.$pageForm = $('#pageForm').on('submit', function () {
            var params = _this.formParams();
            _this.$pageForm.find('input[name="corporationId"]').val(params.corporationId);
            _this.$pageForm.find('input[name="currencyId"]').val(params.currencyId);
            _this.$pageForm.find('input[name="year"]').val(params.year);
            _this.$pageForm.find('input[name="month"]').val(params.month);
            _this.$pageForm.find('input[name="divideYear"]').val(params.divideYear);
            _this.$pageForm.find('input[name="divideMonth"]').val(params.divideMonth);
            _this.$pageForm.find('input[name="divideDay"]').val(params.divideDay);
        });
        this.date = moment(date);
        this.divideDate = moment(divideDate);
        this.weekDatas = J.generateMonthWeeks(this.date).map(function (dates, index) { return new DailyWeekData(_this, dates, index + 1); });
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
        J.listCorporation().then(function (res) {
            var corporations = res.sort(function (o1, o2) { return o2.sortBy - o1.sortBy; });
            var $corporations = $('#corporation-list');
            var corporationMenu = function (corporation) {
                if (corporation.id === corporationId) {
                    _this.corporation = corporation;
                }
                return $('<a href="javascript:">' + corporation.name + '</a>').on('click', function () {
                    if (_this.corporation.id === corporation.id) {
                        return;
                    }
                    _this.corporation = corporation;
                    _this.$pageForm.submit();
                });
            };
            var _a = J.calcRowsCols(corporations.length), rows = _a[0], cols = _a[1];
            if (cols === 1) {
                corporations.forEach(function (it) {
                    var $li = $('<li></li>').appendTo($corporations);
                    corporationMenu(it).appendTo($li);
                });
            }
            else {
                $corporations.addClass('dropdown-menu-table');
                var $corporationTable = $('<table class="table table-bordered"></table>').appendTo($corporations);
                for (var row = 0; row < rows; row++) {
                    var $tr = $('<tr></tr>').appendTo($corporationTable);
                    for (var col = 0; col < cols; col++) {
                        var $td = $('<td></td>').appendTo($tr);
                        var i = row * cols + col;
                        if (i < corporations.length) {
                            corporationMenu(corporations[i]).appendTo($td);
                        }
                    }
                }
            }
            _this.corporation = _this.corporation || corporations[0];
            $('#corporation').text(_this.corporation.name);
            var currencies = _this.corporation.currencies.sort(function (o1, o2) { return o2.sortBy - o1.sortBy; });
            var $currencies = $('#currency-list');
            var currencyMenu = function (currency) {
                if (currency.id === currencyId) {
                    _this.currency = currency;
                }
                return $('<a href="javascript:">' + currency.name + '</a>').on('click', function () {
                    if (_this.currency.id === currency.id) {
                        return;
                    }
                    _this.currency = currency;
                    _this.$pageForm.submit();
                });
            };
            currencies.forEach(function (it) {
                var $li = $('<li></li>').appendTo($currencies);
                currencyMenu(it).appendTo($li);
            });
            _this.currency = _this.currency || currencies[0];
            $('#currency').text(_this.currency.name);
            var params = _this.formParams();
            var p1 = J.listBalance(params).then(function (it) { return _this.balances = it; });
            var p2 = J.listFundlike(params).then(function (it) { return _this.funds = it; });
            return $.when(p1, p2);
        }).then(function () {
            //显示table数据
            _this.weekDatas.forEach(function (it) { return it.fillData(); });
            // 第几周跳转，放在分割日之后
            J.generateWeekScrolls($('#divide-date').parent(), _this.$table.parent(), _this.weekDatas);
            _this.$table.loading('toggle');
        });
    }
    /**
     * 刷新页面，明细增删改后的操作
     * 不提交page表单，但重新获取余额和明细
     */
    DailyPage.prototype.refresh = function () {
        var _this = this;
        this.$table.loading();
        var params = this.formParams();
        var p1 = J.listBalance(params).then(function (it) { return _this.balances = it; });
        var p2 = J.listFundlike(params).then(function (it) { return _this.funds = it; });
        $.when(p1, p2).then(function () {
            var container = _this.$table.parent();
            var scrollTop = container.scrollTop();
            _this.$table.empty();
            _this.weekDatas.forEach(function (it) { return it.fillData(); });
            container.scrollTop(scrollTop);
            _this.$table.loading('toggle');
        });
    };
    DailyPage.prototype.monthAdd = function (i) {
        if (i === void 0) { i = 1; }
        this.date = moment(this.date).add(i, 'M');
        this.divideDate = moment();
        this.$pageForm.submit();
    };
    DailyPage.prototype.createBatchFund = function () {
        this.batchFundUpdateModal.show();
    };
    DailyPage.prototype.exportCurrentXlsx = function () {
        J.exportXlsx(this.formParams());
    };
    DailyPage.prototype.exportAllXlsx = function () {
        var params = this.formParams();
        delete params.corporationId;
        delete params.currencyId;
        J.exportXlsx(params);
    };
    DailyPage.prototype.formParams = function () {
        return {
            corporationId: this.corporation && this.corporation.id,
            currencyId: this.currency && this.currency.id,
            year: this.date.year(),
            month: this.date.month() + 1,
            divideYear: this.divideDate.year(),
            divideMonth: this.divideDate.month() + 1,
            divideDay: this.divideDate.date(),
        };
    };
    return DailyPage;
}());
var DailyWeekData = /** @class */ (function () {
    function DailyWeekData(page, dates, index) {
        this.page = page;
        this.dates = dates;
        this.index = index;
        this.domId = 'week-' + index;
    }
    DailyWeekData.prototype.fillData = function () {
        var _this = this;
        this.dayDatas = this.dates.map(function (it) { return new DailyDayData(_this.page, it); });
        // 每周的前面3行数据
        var hTrs = [
            $('<tr id="' + this.domId + '"><th rowspan="2" class="week-index">第' + this.index + '周</th></tr>'),
            $('<tr></tr>'),
            $('<tr><th>日期</th></tr>'),
        ].map(function (it) { return it.appendTo(_this.page.$table); });
        J.WEEK_DAYS.forEach(function (it, i) {
            $('<th colspan="2" class="week-day">' + it + '</th>').appendTo(hTrs[0]);
            $('<th>金额</th>').appendTo(hTrs[1]);
            $('<th>简述</th>').appendTo(hTrs[1]);
            var dayOfMonth = _this.dates[i].isSame(_this.page.date, 'month') ? _this.dates[i].date() + '号' : '';
            $('<th colspan="2" class="week-day">' + dayOfMonth + '</th>').appendTo(hTrs[2]);
        });
        var $preBalance = $('<tr class="balance_1"><td>上日余额</td></tr>').appendTo(this.page.$table);
        this.dayDatas.forEach(function (dayData) {
            dayData.preBalanceTds().forEach(function (it) { return $preBalance.append(it); });
        });
        var outFundsCount = this.dayDatas.reduce(function (acc, cur) { return Math.max(acc, cur.outFundsCount()); }, 0);
        var _loop_1 = function (i) {
            var $tr = $('<tr><td>支出' + (i + 1) + '</td></tr>').appendTo(this_1.page.$table);
            this_1.dayDatas.forEach(function (dayData) {
                dayData.outFundTds(i).forEach(function (it) { return $tr.append(it); });
            });
        };
        var this_1 = this;
        for (var i = 0; i <= outFundsCount; i++) {
            _loop_1(i);
        }
        var $lackBalance = $('<tr class="balance_2"><td>资金余缺</td></tr>').appendTo(this.page.$table);
        this.dayDatas.forEach(function (dayData) {
            dayData.lackBalanceTds().forEach(function (it) { return $lackBalance.append(it); });
        });
        var inFundsCount = this.dayDatas.reduce(function (acc, cur) { return Math.max(acc, cur.inFundsCount()); }, 0);
        var _loop_2 = function (i) {
            var $tr = $('<tr><td>收入' + (i + 1) + '</td></tr>').appendTo(this_2.page.$table);
            this_2.dayDatas.forEach(function (dayData) {
                dayData.inFundTds(i).forEach(function (it) { return $tr.append(it); });
            });
        };
        var this_2 = this;
        for (var i = 0; i <= inFundsCount; i++) {
            _loop_2(i);
        }
        var $todayBalance = $('<tr class="balance_3"><td>本日余额</td></tr>').appendTo(this.page.$table);
        this.dayDatas.forEach(function (dayData) {
            dayData.todayBalanceTds().forEach(function (it) { return $todayBalance.append(it); });
        });
    };
    return DailyWeekData;
}());
var DailyDayData = /** @class */ (function () {
    function DailyDayData(page, date) {
        var _this = this;
        this.page = page;
        this.date = date;
        this.outFunds = [];
        this.inFunds = [];
        this.dateInMonth = this.date.isSame(this.page.date, 'month');
        if (!this.dateInMonth) {
            return;
        }
        var outFundsSum = 0.00;
        var inFundsSum = 0.00;
        this.page.funds.filter(function (fund) { return _this.date.isSame(fund.date, 'day'); }).forEach(function (fund) {
            if (fund.money > 0) {
                _this.inFunds.push(fund);
                inFundsSum += fund.money;
            }
            else {
                _this.outFunds.push(fund);
                outFundsSum += fund.money;
            }
        });
        var preDate = moment(this.date).add(-1, 'd');
        this.page.balances.forEach(function (it) {
            if (_this.date.isSame(it.date, 'day')) {
                _this.todayBalance = it.balance;
            }
            else if (preDate.isSame(it.date, 'day')) {
                _this.preBalance = it.balance;
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
    DailyDayData.prototype.preBalanceTds = function () {
        return this.balanceTds(this.preBalance);
    };
    DailyDayData.prototype.lackBalanceTds = function () {
        return this.balanceTds(this.lackBalance);
    };
    DailyDayData.prototype.todayBalanceTds = function () {
        return this.balanceTds(this.todayBalance);
    };
    DailyDayData.prototype.outFundTds = function (rowIndex) {
        return this.fundTds(this.outFunds, rowIndex);
    };
    DailyDayData.prototype.outFundsCount = function () {
        return this.outFunds ? this.outFunds.length : 0;
    };
    DailyDayData.prototype.inFundTds = function (rowIndex) {
        return this.fundTds(this.inFunds, rowIndex);
    };
    DailyDayData.prototype.inFundsCount = function () {
        return this.inFunds ? this.inFunds.length : 0;
    };
    DailyDayData.prototype.balanceTds = function (balance) {
        var result = [$('<td></td>'), $('<td></td>')];
        var $td = result[0];
        if (this.dateInMonth && balance) {
            $td.text(J.renderMoney(balance)).addClass('money');
        }
        return result;
    };
    DailyDayData.prototype.fundTds = function (funds, index) {
        var _this = this;
        var result = [$('<td></td>'), $('<td></td>')];
        var $money = result[0], $note = result[1];
        var fund = funds && funds[index];
        if (fund) {
            $money.text(J.renderMoney(fund.money)).addClass('money');
            $note.text(fund.note).addClass('note text-ellipsis').attr('title', fund.note);
        }
        if (this.dateInMonth) {
            result.forEach(function (it) {
                it.on('dblclick', function () { return _this.page.fundUpdateModal.show(fund, _this.date); });
            });
            if (!this.date.isBefore(this.page.divideDate, 'day')) {
                $money.addClass('divide');
                $note.addClass('divide');
            }
        }
        return result;
    };
    return DailyDayData;
}());
var BatchFundUpdateModal = /** @class */ (function () {
    function BatchFundUpdateModal(page) {
        var _this = this;
        this.page = page;
        this.$modal = $('#batchFundUpdateModal');
        this.$form = this.$modal.find('form');
        this.$startDate = this.$form.find('input[name="startDate"]');
        this.$endDate = this.$form.find('input[name="endDate"]');
        this.$monthFundPlanList = this.$form.find('select[name="monthFundPlanId"]');
        this.$note = this.$form.find('textarea[name="note"]');
        this.$moneyDisplay = this.$form.find('#batch-money-show');
        this.$money = this.$form.find('input[name="money"]').on('input propertychange', function () {
            var v = _this.$money.val();
            _this.$moneyDisplay.text($.formatMoney(v));
        });
        this.$save = this.$modal.find('button[name="save"]').on('click', function () {
            J.createBatchFundlike(_this.$monthFundPlanList.val(), {
                startDate: moment(_this.$startDate.val()).toDate(),
                endDate: moment(_this.$endDate.val()).toDate(),
                money: _this.$money.val(),
                note: _this.$note.val(),
            }).then(function () {
                _this.page.refresh();
                _this.$modal.modal('hide');
            });
        });
        var startDate = moment(this.page.date);
        this.$startDate.val(startDate.format('YYYY-MM-DD'));
        this.$startDate.datetimepicker(J.dtp({
            initialDate: startDate.toDate(),
        })).on('changeDate', function (ev) {
            _this.$endDate.datetimepicker('setStartDate', ev.date);
        });
        var endDate = moment(startDate).add(1, 'M').add(-1, 'd');
        this.$endDate.val(endDate.format('YYYY-MM-DD'));
        this.$endDate.datetimepicker(J.dtp({
            initialDate: endDate.toDate(),
        }));
    }
    BatchFundUpdateModal.prototype.show = function () {
        var _this = this;
        J.listMonthFundPlans({
            corporationId: this.page.corporation.id,
            currencyId: this.page.currency.id,
            year: this.page.date.year(),
            month: this.page.date.month() + 1,
        }).then(function (monthFundPlans) {
            _this.$monthFundPlanList.empty();
            monthFundPlans.forEach(function (it) {
                $('<option value="' + it.id + '">' + it.purpose.name + '</option>').appendTo(_this.$monthFundPlanList);
            });
            _this.$monthFundPlanList.val(monthFundPlans[0].id);
            _this.$money.val('');
            _this.$money.trigger('input');
            _this.$note.val('');
            _this.$modal.modal({ moveable: true, rememberPos: true });
        });
    };
    return BatchFundUpdateModal;
}());
var FundUpdateModal = /** @class */ (function () {
    function FundUpdateModal(page) {
        var _this = this;
        this.page = page;
        this.$modal = $('#fundUpdateModal');
        this.$modalTitle = this.$modal.find('.modal-title');
        this.$form = $('#fundUpdateForm');
        this.$date = this.$form.find('input[name="date"]').datetimepicker(J.dtp({
            maxView: 'year',
        }));
        this.$monthFundPlanList = this.$form.find('select[name="monthFundPlanId"]');
        this.$moneyDisplay = this.$form.find('#money-show');
        this.$note = this.$form.find('textarea[name="note"]');
        this.$money = this.$form.find('input[name="money"]').on('input propertychange', function () {
            var v = _this.$money.val();
            _this.$moneyDisplay.text($.formatMoney(v));
        });
        this.$save = this.$modal.find('button[name="save"]').on('click', function () {
            $.extend(_this.fund, {
                date: moment(_this.$date.val()).toDate(),
                money: _this.$money.val(),
                note: _this.$note.val(),
            });
            J.saveFundlike(_this.$monthFundPlanList.val(), _this.fund).then(function () {
                _this.page.refresh();
                _this.$modal.modal('hide');
            });
        });
        this.$delete = this.$modal.find('button[name="delete"]').on('click', function () {
            if (!confirm('删除确认？')) {
                return;
            }
            J.deleteFundlike(_this.fund.id).then(function () {
                _this.page.refresh();
                _this.$modal.modal('hide');
            });
        });
    }
    FundUpdateModal.prototype.show = function (orignFund, date) {
        var _this = this;
        J.listMonthFundPlans({
            corporationId: this.page.corporation.id,
            currencyId: this.page.currency.id,
            year: this.page.date.year(),
            month: this.page.date.month() + 1,
        }).then(function (monthFundPlans) {
            _this.$monthFundPlanList.empty();
            monthFundPlans.forEach(function (it) {
                $('<option value="' + it.id + '">' + it.purpose.name + '</option>').appendTo(_this.$monthFundPlanList);
            });
            if (orignFund && orignFund.id) {
                _this.fund = $.extend({}, orignFund);
                _this.$modalTitle.text('更新');
                _this.$delete.show();
            }
            else {
                _this.fund = { monthFundPlan: monthFundPlans[0], date: date };
                _this.$modalTitle.text('新增');
                _this.$delete.hide();
            }
            _this.$date.val(moment(_this.fund.date).format('YYYY-MM-DD'));
            _this.$monthFundPlanList.val(_this.fund.monthFundPlan.id);
            _this.$money.val(_this.fund.money);
            _this.$money.trigger('input');
            _this.$note.val(_this.fund.note);
            _this.$modal.modal({ moveable: true, rememberPos: true });
        });
    };
    return FundUpdateModal;
}());
