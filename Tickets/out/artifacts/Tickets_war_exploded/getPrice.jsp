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
    Map total = (Map) request.getAttribute("total");
    Map average = (Map) request.getAttribute("average");
%>
<div>
    <%
        String x_total = "";
        String y_total = "";
        for(Object t : total.entrySet()) {
            x_total = x_total + ((Map.Entry) t).getKey() + ",";
            y_total = y_total + ((Map.Entry) t).getValue() + ",";
        }
        x_total = x_total.substring(0, x_total.length()-1);
        y_total = y_total.substring(0, y_total.length()-1);
    %>
    <p id="x_total"><%=x_total%></p>
    <p id="y_total"><%=y_total%></p>


    <%
        String x_average = "";
        String y_average = "";
        for(Object t : average.entrySet()) {
            x_average = x_average + ((Map.Entry) t).getKey() + ",";
            y_average = y_average + ((Map.Entry) t).getValue() + ",";
        }
        x_average = x_average.substring(0, x_average.length()-1);
        y_average = y_average.substring(0, y_average.length()-1);
    %>
    <p id="x_average"><%=x_average%></p>
    <p id="y_average"><%=y_average%></p>

</div>

</body>
</html>
<script type="text/javascript">

</script>