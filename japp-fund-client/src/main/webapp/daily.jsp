<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.github.ixtf.japp.core.J" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Optional" %>
<%
    final String corporationId = Optional.ofNullable(request.getParameter("corporationId")).orElse("");
    final String currencyId = Optional.ofNullable(request.getParameter("currencyId")).orElse("");
    final int year = Optional.ofNullable(request.getParameter("year"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(LocalDate.now().getYear());
    final int divideYear = Optional.ofNullable(request.getParameter("divideYear"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(year);
    final int month = Optional.ofNullable(request.getParameter("month"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(LocalDate.now().getMonthValue());
    final int divideMonth = Optional.ofNullable(request.getParameter("divideMonth"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(month);
    final int divideDay = Optional.ofNullable(request.getParameter("divideDay"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(-1);
    final LocalDate ldPage = LocalDate.of(year, month, 1);
    final LocalDate ldDivide = divideDay == -1 ? LocalDate.now() : LocalDate.of(divideYear, divideMonth, divideDay);
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <%@include file="commonHead.jsp" %>
</head>
<body class="flex">
<form id="pageForm" hidden>
    <input name="corporationId" value="<%=corporationId%>">
    <input name="currencyId" value="<%=currencyId%>">
    <input name="year" value="<%=year%>">
    <input name="month" value="<%=month%>">
    <input name="divideYear" value="<%=divideYear%>">
    <input name="divideMonth" value="<%=divideMonth%>">
    <input name="divideDay" value="<%=divideDay%>">
</form>
<nav class="navbar navbar-default my-nav" role="navigation">
    <div class="container-fluid">
        <%--<div class="navbar-header">--%>
        <%--<a href="javascript:" class="navbar-brand">恒逸资金计划管理</a>--%>
        <%--</div>--%>
        <div class="collapse navbar-collapse">
            <ul class="nav navbar-nav">
                <li class="active"><a href="daily.jsp">每日动态</a></li>
                <li><a href="merge.jsp">合并报表</a></li>

                <li>
                    <a href="javascript:" onclick="dailyPage.monthAdd(-1)">
                        <i class="icon icon-chevron-left"></i>
                    </a>
                </li>
                <li>
                    <a id="year-month" href="javascript:" data-toggle="tooltip" title="更改月份">
                        <%=ldPage.format(DateTimeFormatter.ofPattern("yyyy-MM"))%>
                    </a>
                </li>
                <li>
                    <a href="javascript:" onclick="dailyPage.monthAdd()">
                        <i class="icon icon-chevron-right"></i>
                    </a>
                </li>

                <li class="dropdown">
                    <a href="javascript:" class="dropdown-toggle" data-toggle="dropdown">
                        <span id="corporation">公司</span> <b class="caret"></b>
                    </a>
                    <ul id="corporation-list" class="dropdown-menu"></ul>
                </li>

                <li class="dropdown">
                    <a href="javascript:" class="dropdown-toggle" data-toggle="dropdown">
                        <span id="currency">币种</span> <b class="caret"></b>
                    </a>
                    <ul id="currency-list" class="dropdown-menu"></ul>
                </li>

                <li style="display: <%=currencyId.equals("SSmTiaVKUe6xy37VZmacWj")?"none":"block"%>">
                    <a id="divide-date" href="javascript:" data-toggle="tooltip" title="选择计划日期">
                        <%=ldDivide%>
                    </a>
                </li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li class="dropdown">
                    <a href="javascript:" class="dropdown-toggle" data-toggle="dropdown">
                        <%=operatorName%> <b class="caret"></b>
                    </a>
                    <ul class="dropdown-menu" role="menu">
                        <li>
                            <a href="javascript:" onclick="dailyPage.createBatchFund()">批量</a>
                            <a href="javascript:" onclick="dailyPage.exportAllXlsx()">导出所有</a>
                            <a href="javascript:" onclick="dailyPage.exportCurrentXlsx()">导出当前</a>
                        </li>
                        <%
                            if (isAdmin) {
                        %>
                        <li class="divider"></li>
                        <li>
                            <a href="http://task.hengyi.com:8080/fund-server" target="task">管理</a>
                        </li>
                        <%
                            }
                        %>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
</nav>

<div class="data-container table-responsive">
    <table id="page-table" class="table table-condensed table-bordered table-fixed"></table>
</div>

<div id="batchFundUpdateModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span><span class="sr-only">关闭</span>
                </button>
                <h4 class="modal-title"></h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">开始日期</label>
                        <div class="col-sm-10">
                            <input name="startDate" class="form-control" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">结束日期</label>
                        <div class="col-sm-10">
                            <input name="endDate" class="form-control" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">月度计划 </label>
                        <div class="col-sm-10">
                            <select class="form-control" name="monthFundPlanId" required></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">金额(元)</label>
                        <div class="col-sm-5">
                            <input name="money" type="number" class="form-control" placeholder="请输入余额" required>
                        </div>
                        <label id="batch-money-show" class="control-label"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10">
                            <textarea name="note" class="form-control" rows="5" placeholder="请输入描述"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" name="save">
                    保存
                </button>
            </div>
        </div>
    </div>
</div>

<div id="fundUpdateModal" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span><span class="sr-only">关闭</span>
                </button>
                <h4 class="modal-title"></h4>
            </div>
            <div class="modal-body">
                <form id="fundUpdateForm" class="form-horizontal" role="form">
                    <div class="form-group">
                        <label class="col-sm-2 control-label">计划日期</label>
                        <div class="col-sm-10">
                            <input name="date" class="form-control" readonly>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">月度计划 </label>
                        <div class="col-sm-10">
                            <select class="form-control" name="monthFundPlanId" required></select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">金额(元)</label>
                        <div class="col-sm-5">
                            <input name="money" type="number" class="form-control" placeholder="请输入余额" required>
                        </div>
                        <label id="money-show" class="control-label"></label>
                    </div>
                    <div class="form-group">
                        <label class="col-sm-2 control-label">描述</label>
                        <div class="col-sm-10">
                            <textarea name="note" class="form-control" rows="5" placeholder="请输入描述"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" name="save">
                    保存
                </button>
                <button type="button" class="btn btn-danger" name="delete">
                    删除
                </button>
            </div>
        </div>
    </div>
</div>

<%@include file="commonFoot.jsp" %>
<script src="./js/daily.min.js"></script>
<script>
    const dailyPage = new DailyPage('<%=corporationId%>', '<%=currencyId%>', '<%=ldPage%>', '<%=ldDivide%>');
    //    $('[data-toggle="tooltip"]').tooltip();
</script>
</body>
</html>
