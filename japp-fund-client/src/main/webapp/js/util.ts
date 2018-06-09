declare var $: any;
declare var jQuery: any;
declare var moment: any;

class _J {

    static listReportsSumBalances(params: any): any {
        return $.getJSON('./api/reports/sum/balances', params);
    }

    static listReportsSumSumTypes(params: any): any {
        return $.getJSON('./api/reports/sum/sumTypes', params);
    }

    static createBatchFundlike(monthFundPlanId: string, body: string): any {
        return $.ajax({
            type: 'POST',
            url: './api/batchFundlikes?monthFundPlanId=' + monthFundPlanId,
            data: JSON.stringify(body),
            contentType: 'application/json',
            dataType: 'json',
        });
    }

    static saveFundlike(monthFundPlanId: string, fund: any): any {
        const body = JSON.stringify(fund);
        return fund.id ? _J.updateFundlike(fund.id, body, monthFundPlanId) : _J.createFundlike(monthFundPlanId, body);
    }

    private static createFundlike(monthFundPlanId: string, body: string): any {
        return $.ajax({
            type: 'POST',
            url: './api/monthFundPlans/' + monthFundPlanId + '/fundlikes',
            data: body,
            contentType: 'application/json',
            dataType: 'json',
        });
    }

    private static updateFundlike(id: string, body: string, monthFundPlanId?: string): any {
        const url = monthFundPlanId ? './api/monthFundPlans/' + monthFundPlanId + '/fundlikes/' + id : './api/fundlikes/' + id;
        return $.ajax({type: 'PUT', url, data: body, contentType: 'application/json', dataType: 'json',});
    }

    static deleteFundlike(id: string): any {
        return $.ajax({type: 'DELETE', url: './api/fundlikes/' + id,});
    }

    static listCorporation(params: any): any {
        return $.getJSON('./api/corporations', params);
    }

    static listBalance(params: any): any {
        if (params && params.currencyId === 'SSmTiaVKUe6xy37VZmacWj') {
            params = $.extend({}, params, {divideYear: params.year, divideMonth: params.month, divideDay: 1})
        }
        return $.getJSON('./api/balances', params);
    }

    static listFundlike(params: any): any {
        if (params && params.currencyId === 'SSmTiaVKUe6xy37VZmacWj') {
            params = $.extend({}, params, {divideYear: params.year, divideMonth: params.month, divideDay: 1})
        }
        return $.getJSON('./api/fundlikes', params);
    }

    static listMonthFundPlans(params: any): any {
        return $.getJSON('./api/monthFundPlans', params);
    }

    static exportXlsx(params: any): any {
        window.open('./api/exports?' + $.param(params));
    }

    static exportSumReportsXlsx(params: any): any {
        window.open('./api/exports/sumReports?' + $.param(params));
    }

    static calcRowsCols(count: number): number[] {
        const MAX_ROWS = 15;
        const result = [MAX_ROWS, 1];
        if (count <= 15) {
            return result;
        }
        result[1] = Math.ceil(count / MAX_ROWS);
        result[0] = Math.ceil(count / result[1]);
        return result;
    };

    static renderMoney(money: number): string {
        let result = $.formatMoney(money / 10000);
        const i = result.indexOf('.');
        return result.substring(0, i);
    }

    static dtp(options: any): any {
        return $.extend({
            language: 'zh-CN',
            weekStart: 1,
            autoclose: true,
            todayHighlight: true,
            format: 'yyyy-mm-dd',
            minView: 'month',
            startDate: moment([2017, 0]).toDate(),
        }, options)
    }

    static readonly WEEK_DAYS = ['周一', '周二', '周三', '周四', '周五', '周六', '周日'];

    static generateMonthWeeks(initDate: any): any[] {
        const result = [];
        const sDate = moment([initDate.year(), initDate.month()]);
        let dates = [];
        for (let date = moment(sDate); date.month() === initDate.month(); date.add(1, 'd')) {
            dates.push(moment(date));
            if (date.day() === 0) {
                if (result.length === 0) { // 第一次加，可能需要在前面补充日期
                    this.addPrefixDates(dates);
                }
                result.push(dates);
                dates = [];
            }
        }
        if (dates.length > 0) {// 可能需要在后面补充日期
            this.addSuffixDates(dates);
            result.push(dates);
        }
        return result;
    };

    private static addPrefixDates(dates: any[]) {
        if (dates.length === 7) {
            return;
        }
        let date = moment(dates[0]);
        while (date.add(-1, 'd').day() !== 0) {
            dates.unshift(moment(date));
        }
    }

    private static addSuffixDates(dates: any[]) {
        if (dates.length === 7) {
            return;
        }
        let date = moment(dates[dates.length - 1]);
        while (date.add(1, 'd').day() !== 1) {
            dates.push(moment(date));
        }
    }

    static generateWeekScrolls($next, $container, weekDatas: any[]) {
        weekDatas.forEach(weekData => {
            const $weekScroll = $('<li><a href="javascript:">第' + weekData.index + '周</a></li>').on('click', () => {
                const bcr = document.getElementById(weekData.domId).getBoundingClientRect();
                //nav 高４２
                $container.scrollTop($container.scrollTop() + bcr.top - 42);
            });
            $next.after($weekScroll);
            $next = $weekScroll;
        });
    };
}

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
            var fTmp = 0.00;//临时变量
            var iFra = 0;//小数部分
            var iInt = 0;//整数部分
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
            };

            if (typeof(iAccuracy) === 'undefined')
                iAccuracy = 2;
            bPositive = (mVal >= 0);//取出正负号
            fTmp = (isNaN(fTmp = parseFloat(mVal))) ? 0 : Math.abs(fTmp);//强制转换为绝对值数浮点
            //所有内容用正数规则处理
            iInt = parseInt('' + fTmp); //分离整数部分
            iFra = (fTmp - iInt) * Math.pow(10, iAccuracy) + 0.5; //分离小数部分(四舍五入)
            iFra = parseInt('' + iFra);

            do {
                aBuf.unshift(funZero(iInt % 1000, 3));
            } while ((iInt = parseInt('' + (iInt / 1000))));
            aBuf[0] = parseInt(aBuf[0]).toString();//最高段区去掉前导0
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