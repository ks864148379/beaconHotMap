<%@ page language="java" import="java.util.*"  pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>500 Error</title>
    <link href="<%=path%>/ui/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path%>/ui/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/style.css" rel="stylesheet">
</head>
<body class="gray-bg">
    <div class="middle-box text-center animated fadeInDown">
        <h1>500</h1>
        <h3 class="font-bold">服务器发生异常</h3>

        <div class="error-desc">
            非常抱歉的通知您：由于各种原因，服务器发生了点小问题，我们正在抓紧解决，请稍后访问！给您带来的不便，深表歉意！<br/>
            您也可以尝试访问首页，以跳出此页面：<br/><a href="<%=path%>/beaconHotMap/index" class="btn btn-primary m-t">首页</a>
        </div>
    </div>
    <!-- Mainly scripts -->
    <script src="<%=path%>/ui/js/jquery-3.1.1.min.js"></script>
    <script src="<%=path%>/ui/js/bootstrap.min.js"></script>
</body>
</html>
