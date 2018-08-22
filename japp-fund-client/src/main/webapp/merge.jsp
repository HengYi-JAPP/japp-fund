<%@ page import="org.jzb.J" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="java.util.Optional" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String currencyId = Optional.ofNullable(request.getParameter("currencyId"))
            .orElse("g111W2qibFUfXSub6MEEJ");
    int year = Optional.ofNullable(request.getParameter("year"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(LocalDate.now().getYear());
    int divideYear = Optional.ofNullable(request.getParameter("divideYear"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(year);
    int month = Optional.ofNullable(request.getParameter("month"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(LocalDate.now().getMonthValue());
    int divideMonth = Optional.ofNullable(request.getParameter("divideMonth"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(month);
    int divideDay = Optional.ofNullable(request.getParameter("divideDay"))
            .filter(J::nonBlank)
            .map(Integer::parseInt)
            .orElse(-1);
    LocalDate ldPage = LocalDate.of(year, month, 1);
    LocalDate ldDivide = divideDay == -1 ? LocalDate.now() : LocalDate.of(divideYear, divideMonth, divideDay);
%>
<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <%@include file="commonHead.jsp" %>
</head>
<body class="flex">
<form id="pageForm" hidden>
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
                <li><a href="daily.jsp">每日动态</a></li>
                <li class="active"><a href="merge.jsp">合并报表</a></li>

                <li>
                    <a href="javascript:" onclick="mergePage.monthAdd(-1)">
                        <i class="icon icon-chevron-left"></i>
                    </a>
                </li>
                <li>
                    <a id="year-month" href="javascript:" data-toggle="tooltip" title="更改月份">
                        <%=ldPage.format(DateTimeFormatter.ofPattern("yyyy-MM"))%>
                    </a>
                </li>
                <li>
                    <a href="javascript:" onclick="mergePage.monthAdd(1)">
                        <i class="icon icon-chevron-right"></i>
                    </a>
                </li>

                <li class="dropdown">
                    <a href="javascript:" class="dropdown-toggle" data-toggle="dropdown">
                        <span id="currency">币种</span> <b class="caret"></b>
                    </a>
                    <ul id="currency-list" class="dropdown-menu">
                    </ul>
                </li>

                <li>
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
                            <a href="javascript:" onclick="mergePage.exportAllXlsx()">导出所有</a>
                            <a href="javascript:" onclick="mergePage.exportCurrentXlsx()">导出当前</a>
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
    <table id="page-table" class="table table-condensed table-bordered table-fixed">
    </table>
</div>

<%@include file="commonFoot.jsp" %>
<script src="./js/merge.min.js"></script>
<script>
    const mergePage = new MergePage('<%=currencyId%>', '<%=ldPage%>', '<%=ldDivide%>');
</script>
</body>
</html>
