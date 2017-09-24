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
    <title>404 Error</title>
    <link href="<%=path%>/ui/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path%>/ui/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/style.css" rel="stylesheet">
</head>
<body class="gray-bg">
    <div class="middle-box text-center animated fadeInDown">
        <h1>404</h1>
        <h3 class="font-bold">页面没有找到呀！</h3>
        <div class="error-desc">
            我们非常抱歉的告诉您：您访问的页面没有找到！<br>通常，这种情况是因为您的URL不正确。请试着按浏览器刷新按钮，或者点击以下按钮返回首页。<br>
            <a href="<%=path%>/beaconHotMap/index" class="btn btn-primary m-t">首页</a>
        </div>
    </div>
    <!-- Mainly scripts -->
    <script src="<%=path%>/ui/js/jquery-3.1.1.min.js"></script>
    <script src="<%=path%>/ui/js/bootstrap.min.js"></script>
</body>
</html>
