var _J = /** @class */ (function () {
    function _J() {
    }
    _J.listReportsSumBalances = function (params) {
        return $.getJSON('./api/reports/sum/balances', params);
    };
    _J.listReportsSumSumTypes = function (params) {
        return $.getJSON('./api/reports/sum/sumTypes', params);
    };
    _J.createBatchFundlike = function (monthFundPlanId, body) {
        return $.ajax({
            type: 'POST',
            url: './api/batchFundlikes?monthFundPlanId=' + monthFundPlanId,
            data: JSON.stringify(body),
            contentType: 'application/json',
            dataType: 'json',
        });
    };
    _J.saveFundlike = function (monthFundPlanId, fund) {
        var body = JSON.stringify(fund);
        return fund.id ? _J.updateFundlike(fund.id, body, monthFundPlanId) : _J.createFundlike(monthFundPlanId, body);
    };
    _J.deleteFundlike = function (id) {
        return $.ajax({ type: 'DELETE', url: './api/fundlikes/' + id, });
    };
    _J.listCorporation = function (params) {
        return $.getJSON('./api/corporations', params);
    };
    _J.listBalance = function (params) {
        if (params && params.currencyId === 'SSmTiaVKUe6xy37VZmacWj') {
            params = $.extend({}, params, { divideYear: params.year, divideMonth: params.month, divideDay: 1 });
        }
        return $.getJSON('./api/balances', params);
    };
    _J.listFundlike = function (params) {
        if (params && params.currencyId === 'SSmTiaVKUe6xy37VZmacWj') {
            params = $.extend({}, params, { divideYear: params.year, divideMonth: params.month, divideDay: 1 });
        }
        return $.getJSON('./api/fundlikes', params);
    };
    _J.listMonthFundPlans = function (params) {
        return $.getJSON('./api/monthFundPlans', params);
    };
    _J.exportXlsx = function (params) {
        window.open('./api/exports?' + $.param(params));
    };
    _J.exportSumReportsXlsx = function (params) {
        window.open('./api/exports/sumReports?' + $.param(params));
    };
    _J.calcRowsCols = function (count) {
        var MAX_ROWS = 15;
        var result = [MAX_ROWS, 1];
        if (count <= 15) {
            return result;
        }
        result[1] = Math.ceil(count / MAX_ROWS);
        result[0] = Math.ceil(count / result[1]);
        return result;
    };
    ;
    _J.renderMoney = function (money) {
        var result = $.formatMoney(money / 10000);
        var i = result.indexOf('.');
        return result.substring(0, i);
    };
    _J.dtp = function (options) {
        return $.extend({
            language: 'zh-CN',
            weekStart: 1,
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            minView: 'month',
            startDate: moment([2017, 0]).toDate(),
        }, options);
    };
    _J.generateMonthWeeks = function (initDate) {
        var result = [];
        var sDate = moment([initDate.year(), initDate.month()]);
        var dates = [];
        for (var date = moment(sDate); date.month() === initDate.month(); date.add(1, 'd')) {
            dates.push(moment(date));
            if (date.day() === 0) {
                if (result.length === 0) { // 第一次加，可能需要在前面补充日期
                    this.addPrefixDates(dates);
                }
                result.push(dates);
                dates = [];
            }
        }
        if (dates.length > 0) { // 可能需要在后面补充日期
            this.addSuffixDates(dates);
            result.push(dates);
        }
        return result;
    };
    ;
    _J.generateWeekScrolls = function ($next, $container, weekDatas) {
        weekDatas.forEach(function (weekData) {
            var $weekScroll = $('<li><a href="javascript:">第' + weekData.index + '周</a></li>').on('click', function () {
                var bcr = document.getElementById(weekData.domId).getBoundingClientRect();
                //nav 高４２
                $container.scrollTop($container.scrollTop() + bcr.top - 42);
            });
            $next.after($weekScroll);
            $next = $weekScroll;
        });
    };
    ;
    _J.createFundlike = function (monthFundPlanId, body) {
        return $.ajax({
            type: 'POST',
            url: './api/monthFundPlans/' + monthFundPlanId + '/fundlikes',
            data: body,
            contentType: 'application/json',
            dataType: 'json',
        });
    };
    _J.updateFundlike = function (id, body, monthFundPlanId) {
        var url = monthFundPlanId ? './api/monthFundPlans/' + monthFundPlanId + '/fundlikes/' + id : './api/fundlikes/' + id;
        return $.ajax({ type: 'PUT', url: url, data: body, contentType: 'application/json', dataType: 'json', });
    };
    _J.addPrefixDates = function (dates) {
        if (dates.length === 7) {
            return;
        }
        var date = moment(dates[0]);
        while (date.add(-1, 'd').day() !== 0) {
            dates.unshift(moment(date));
        }
    };
    _J.addSuffixDates = function (dates) {
        if (dates.length === 7) {
            return;
        }
        var date = moment(dates[dates.length - 1]);
        while (date.add(1, 'd').day() !== 1) {
            dates.push(moment(date));
        }
    };
    _J.WEEK_DAYS = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];
    return _J;
}());
window['J'] = _J;
/**
 * 金额按千位逗号分割
 * @character_set UTF-8
 * @author Jerry.li(hzjerry@gmail.com)
 * @version 1.2014.08.24.2143
 *  Example
 *  <code>
 *      alert($.formatMoney(1234.345, 2)); //=>1,234.35
 *      alert($.formatMoney(-1234.345, 2)); //=>-1,234.35
 *      alert($.unformatMoney(1,234.345)); //=>1234.35
 *      alert($.unformatMoney(-1,234.345)); //=>-1234.35
 *  </code>
 */
(function ($) {
    $.extend({
        /**
         * 数字千分位格式化
         * @public
         * @param mixed mVal 数值
         * @param int iAccuracy 小数位精度(默认为2)
         * @return string
         */
        formatMoney: function (mVal, iAccuracy) {
            var fTmp = 0.00; //临时变量
            var iFra = 0; //小数部分
            var iInt = 0; //整数部分
            var aBuf = new Array(); //输出缓存
            var bPositive = true; //保存正负值标记(true:正数)
            /**
             * 输出定长字符串，不够补0
             * <li>闭包函数</li>
             * @param int iVal 值
             * @param int iLen 输出的长度
             */
            function funZero(iVal, iLen) {
                var sTmp = iVal.toString();
                var sBuf = new Array();
                for (var i = 0, iLoop = iLen - sTmp.length; i < iLoop; i++)
                    sBuf.push('0');
                sBuf.push(sTmp);
                return sBuf.join('');
            }
            ;
            if (typeof (iAccuracy) === 'undefined')
                iAccuracy = 2;
            bPositive = (mVal >= 0); //取出正负号
            fTmp = (isNaN(fTmp = parseFloat(mVal))) ? 0 : Math.abs(fTmp); //强制转换为绝对值数浮点
            //所有内容用正数规则处理
            iInt = parseInt('' + fTmp); //分离整数部分
            iFra = (fTmp - iInt) * Math.pow(10, iAccuracy) + 0.5; //分离小数部分(四舍五入)
            iFra = parseInt('' + iFra);
            do {
                aBuf.unshift(funZero(iInt % 1000, 3));
            } while ((iInt = parseInt('' + (iInt / 1000))));
            aBuf[0] = parseInt(aBuf[0]).toString(); //最高段区去掉前导0
            return ((bPositive) ? '' : '-') + aBuf.join(',') + '.' + ((0 === iFra) ? '00' : funZero(iFra, iAccuracy));
        },
        /**
         * 将千分位格式的数字字符串转换为浮点数
         * @public
         * @param string sVal 数值字符串
         * @return float
         */
        unformatMoney: function (sVal) {
            var fTmp = parseFloat(sVal.replace(/,/g, ''));
            return (isNaN(fTmp) ? 0 : fTmp);
        },
    });
})(jQuery);
