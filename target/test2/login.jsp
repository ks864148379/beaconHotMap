<%--
  Created by IntelliJ IDEA.
  User: cxl
  Date: 2017/3/30
  Time: 11:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*"  pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <title>登录</title>

    <link href="<%=path%>/ui/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path%>/ui/font-awesome/css/font-awesome.css" rel="stylesheet">

    <link href="<%=path%>/ui/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/style.css" rel="stylesheet">
</head>
<body class="gray-bg">
    <div class="middle-box text-center loginscreen animated fadeInDown">
        <div>
            <h2>展会管理系统</h2>
            <form class="m-t" role="form" action="<%=path%>/user/login" method="post">
                <div class="form-group">
                    <input type="text" name="visitor_id"   class="form-control" placeholder="Username" required="">
                </div>
                <div class="form-group">
                    <input type="password" name="password" class="form-control" placeholder="Password" required="">
                </div>
                <button type="submit" class="btn btn-primary block full-width m-b">登录</button>
            </form>
        </div>
    </div>

    <script src="<%=path%>/ui/js/jquery-3.1.1.min.js"></script>
    <script src="<%=path%>/ui/js/bootstrap.min.js"></script>
</body>
</html>
