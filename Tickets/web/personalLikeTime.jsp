<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2018/6/19
  Time: 19:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="cn.edu.nju.software.models.Member" %>
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
    Member m = (Member)session.getAttribute("member");
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
                <li><a href="<s:url action="showActivities"/>">购票</a></li>
                <li><a href="<s:url action="manageAccount"/>">账号管理</a></li>
                <li><a href="<s:url action="queryRecord"/>">记录查询</a></li>
                <li><a href="<s:url action="checkOrders"/>">查看订单</a></li>
                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="#">分析<span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="<s:url action="personalLikeVenue"/>">个人偏好的场馆分析</a></li>
                        <li><a href="<s:url action="personalLikeCity"/>">个人常去的城市分析</a></li>
                        <li><a href="<s:url action="personalLikeTime"/>">个人喜爱看演出的时间分析</a></li>
                        <li><a href="<s:url action="priceRangeAccepted"/>">个人能接受的票价范围分析</a></li>
                        <li><a href="<s:url action="personalLikeType"/>">个人喜爱的演出类型分析</a></li>
                        <li><a href="<s:url action="payPercent"/>">个人的付款率分析</a></li>
                        <li><a href="<s:url action="donePercent"/>">个人的成交率分析</a></li>
                    </ul>
                </li>
            </ul>
            <form class="navbar-form navbar-left">
                <div class="form-group">
                    <input type="text" class="form-control" placeholder="演唱会">
                </div>
                <button type="submit" class="btn btn-default">搜索</button>
            </form>
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/index.jsp">退出</a></li>
                <li><a onclick="cancelQualification()">注销</a></li>
            </ul>
            <p class="navbar-text pull-right">欢迎您，<%=m.getName()%>！</p>
        </div>
    </div>
</nav>

<div id="container"
     style="width: 750px; height: 400px; margin: 0 auto;
     padding-top: 200px"></div>


<script src="dist/js/jquery.min.js"></script>
<script src="dist/js/bootstrap.min.js"></script>
<script src="dist/js/highcharts.js"></script>
<script type="text/javascript" charset="utf-8">

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
            text: '个人喜爱看演出的时间分析'
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

