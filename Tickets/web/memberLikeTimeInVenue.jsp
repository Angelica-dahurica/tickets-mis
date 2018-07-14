<%@ page import="cn.edu.nju.software.models.Venue" %>
<%@ page import="java.util.Map" %>
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
    Venue venue = (Venue) session.getAttribute("venue");
    Map time = (Map) request.getAttribute("time");
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
                <li class="active"><a href="<s:url action="venueStatistics"/>">场馆统计<span class="sr-only">(current)</span></a></li>
                <li><a href="<s:url action="planActivities"/>">发布计划</a></li>
                <li><a href="<s:url action="manageVenue"/>">场馆管理</a></li>
                <li><a href="<s:url action="checkTickets"/>">检票登记</a></li>
            </ul>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/index.jsp">退出</a></li>
            </ul>
            <p class="navbar-text pull-right">欢迎您，<%=venue.getPlace()%>！</p>
        </div>
    </div>
</nav>

<div style="padding-top: 50px;">
    <ul class="nav nav-tabs">
        <li role="presentation"><a href="<s:url action="venueStatistics"/>">已发布的活动</a></li>
        <li role="presentation"><a href="<s:url action="venueRecord"/>">财务情况</a></li>
        <li role="presentation" class="dropdown">
            <a class="dropdown-toggle" data-toggle="dropdown" href="#">分析<span class="caret"></span></a>
            <ul class="dropdown-menu">
                <li><a href="<s:url action="memberLikeTimeInVenue"/>">会员喜爱看演出的时间分析</a></li>
                <li><a href="<s:url action="priceInVenue"/>">分析场馆各年、各月的订单总额/平均订单额</a></li>
                <li><a href="<s:url action="typePercentInVenue"/>">各类型演出订单数的比例分析</a></li>
                <li><a href="<s:url action="soldOutPercentInVenue"/>">活动售票率分析</a></li>
                <li><a href="<s:url action="sitDownPercentInVenue"/>">活动入座率分析</a></li>
                <li><a href="<s:url action="pointMember"/>">常光顾的会员群体</a></li>
            </ul>
        </li>
    </ul>
</div>

<div id="container"
     style="width: 750px; height: 400px; margin: 0 auto; padding-top: 100px"></div>

<script src="dist/js/jquery.min.js"></script>
<script src="dist/js/bootstrap.min.js"></script>
<script src="dist/js/highcharts.js"></script>
<script type="text/javascript">

    $(document).ready(function() {
        var xdata = [];
        var ydata = [];

        <%
        for(Object t : time.entrySet()) {
        %>
        xdata.push('<%=((Map.Entry) t).getKey()%>');
        ydata.push(<%=((Map.Entry)t).getValue()%>);
        <%
        }
        %>

        var chart = {
            zoomType: 'x'
        };

        var title = {
            text: '会员喜爱看演出的时间分析'
        };

        var xAxis = {
            type: 'linear',
            categories: xdata
        };

        var yAxis = {
            min: 0,
            title: {
                text: '次数'
            }
        };

        var legend = {
            enabled: false
        };

        var credits = {
            enabled: false
        };

        var plotOptions = {
            area: {
                fillColor: {
                    linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
                    stops: [
                        [0, Highcharts.getOptions().colors[0]],
                        [1, Highcharts.Color(Highcharts.getOptions().colors[0]).setOpacity(0).get('rgba')]
                    ]
                },
                marker: {
                    radius: 2
                },
                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            }
        };

        var series = [{
            type: 'area',
            name: '演出次数',
            data: ydata
        }];

        var json = {};
        json.chart = chart;
        json.title = title;
        json.xAxis = xAxis;
        json.yAxis = yAxis;
        json.legend = legend;
        json.series = series;
        json.plotOptions = plotOptions;
        json.credits = credits;
        $('#container').highcharts(json);
    });

</script>
</body>
</html>