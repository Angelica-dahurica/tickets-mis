<%@ page import="cn.edu.nju.software.models.Activity" %>
<%@ page import="cn.edu.nju.software.models.Bank" %>
<%@ page import="cn.edu.nju.software.models.Member" %>
<%@ page import="cn.edu.nju.software.models.Venue" %>
<%@ page import="java.util.List" %>
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
    Member m = (Member) session.getAttribute("member");
    Venue venue = (Venue) request.getAttribute("venue");
    List banks = (List) request.getAttribute("banks");
    String[] price = (String[]) request.getAttribute("price");
    String[] quantity = (String[]) request.getAttribute("quantity");
    String[] type = (String[]) request.getAttribute("type");
    Activity activity = (Activity) request.getAttribute("activity");

    session.setAttribute("activity", activity);
    session.setAttribute("price", price);
    session.setAttribute("quantity", quantity);
    session.setAttribute("type", type);
    session.setAttribute("banks", banks);
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

<div style="margin-top: 100px">
    <div class="thumbnail">
        <div class="well">
            <div class="modal-header">
                <h4 class="modal-title" id="registerVenueModalLabel">审核订单</h4>
            </div>
            <form id="submitOrderForm" action="<s:url action="submitOrder"/>" method="post" class="form-horizontal">
                <div class="modal-body">
                    <table class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th>演出名称</th>
                                <th>地点</th>
                                <th>类型</th>
                                <th>数量</th>
                                <th>价格（元）</th>
                            </tr>
                        </thead>
                        <tbody>
                        <%
                            double total = 0;
                            for(int i = 0; i < price.length; i++){
                        %>
                        <tr>
                            <td><%=activity.getName()%></td>
                            <td><%=venue.getPlace()%></td>
                            <td><%=type[i]%></td>
                            <td><%=quantity[i]%></td>
                            <td><%=price[i]%></td>
                        </tr>
                        <%
                                total += Double.parseDouble(quantity[i]) * Double.parseDouble(price[i]);
                            }
                        %>
                        </tbody>
                    </table>
                    <p>总价：<%=total%></p>
                </div>
                <div class="modal-footer">
                    <input id="submit-order-submit" type="submit" class="btn btn-primary" value="提交订单">
                </div>
            </form>
        </div>
    </div>
</div>

<script src="dist/js/jquery.min.js"></script>
<script src="dist/js/bootstrap.min.js"></script>

</body>
</html>
