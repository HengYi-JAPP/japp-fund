<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fasterxml.jackson.databind.JsonNode" %>
<%@ page import="com.hengyi.japp.fund.client.Api" %>
<%@ page import="static com.github.ixtf.japp.core.Constant.MAPPER" %>
<title>每日动态资金计划</title>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="./node_modules/zui/dist/css/zui.min.css" rel="stylesheet">
<link href="./node_modules/zui/dist/lib/datetimepicker/datetimepicker.min.css" rel="stylesheet">
<link href="./node_modules/jquery-easy-loading/dist/jquery.loading.min.css" rel="stylesheet">
<link href="./css/main.min.css" rel="stylesheet">
<%
    final JsonNode operatorNode = MAPPER.readTree(Api.get(request.getUserPrincipal(), Api.Urls.AUTH));
    final boolean isAdmin = operatorNode.get("admin").booleanValue();
    final String operatorName = operatorNode.get("name").textValue();
%>