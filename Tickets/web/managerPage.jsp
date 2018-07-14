<%@ page import="java.util.ArrayList" %>
<%@ page import="cn.edu.nju.software.models.Venue" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Tickets</title>
    <link href="./dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body>

<%
    ArrayList venueList = (ArrayList) request.getAttribute("venueList");
%>

<nav class="navbar navbar-default navbar-fixed-top">
    <div class="container-fluid">
        <div class="navbar-header">
            <a class="navbar-brand" href="#">
                <img alt="Brand" src="img/brand.jpg" style="height: 20px; border-radius: 5px; margin-bottom: 15px">
            </a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li class="active"><a href="<s:url action="systemStatistics"/>">网站统计<span
                        class="sr-only">(current)</span></a></li>
                <li><a href="<s:url action="checkInfo"/>">审批信息</a></li>
                <li><a href="<s:url action="settleFinance"/>">财务结算</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/index.jsp">退出</a></li>
            </ul>
        </div>
    </div>
</nav>

<div style="padding-top: 50px;">
    <ul class="nav nav-tabs">
        <li role="presentation"><a href="<s:url action="systemStatistics"/>">场馆统计</a></li>
        <li role="presentation"><a href="<s:url action="memberStatistics"/>">会员统计</a></li>
        <li role="presentation"><a href="<s:url action="financeStatistics"/>">财务状况</a></li>
        <li role="presentation" class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">分析<span class="caret"></span></a>
            <ul class="dropdown-menu">
                <li><a href="<s:url action="memberLikeVenue"/>">会员偏好的场馆分析</a></li>
                <li><a href="<s:url action="memberLikeCity"/>">会员常去的城市分析</a></li>
                <li><a href="<s:url action="memberLikeTimeInPlatform"/>">会员喜爱看演出的时间分析</a></li>
                <li><a href="<s:url action="priceInPlatform"/>">分析场馆各年、各月的订单总额/平均订单额</a></li>
                <li><a href="<s:url action="typePercentInPlatform"/>">各类型演出订单数的比例分析</a></li>
                <li><a href="<s:url action="soldOutPercentInPlatform"/>">平台售票率分析</a>
            </ul>       
        </li>
    </ul>
</div>

<div style="margin-top: 100px">
    <%
        int i = 0;
        for (Object anAlist : venueList) {
            Venue venue = (Venue) anAlist;
            pageContext.setAttribute("item", venue);
            i++;
            if (i % 4 == 0) {
    %>
    <div class="row">
        <%
            }
        %>
        <div class="col-md-3">
            <div class="thumbnail">
                <div class="well">
                    <jsp:useBean id="item" class="cn.edu.nju.software.models.Venue" scope="page"/>
                    <p>场馆地点：
                        <jsp:getProperty name="item" property="place"/>
                    </p>
                    <p>场馆描述：
                        <jsp:getProperty name="item" property="description"/>
                    </p>
                </div>
            </div>
        </div>
        <%
            if (i % 4 == 0) {
        %>
    </div>
    <%
            }
        }
    %>
</div>

<script src="dist/js/jquery.min.js"></script>
<script src="dist/js/bootstrap.min.js"></script>
<script type="text/javascript">
    $(document).ready(function () {
        $('.dropdown-toggle').dropdown();
    });
</script>
</body>
</html>
