<%@ page language="java" import="java.util.*"  pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<nav class="navbar-default navbar-static-side" role="navigation">
    <div class="sidebar-collapse">
        <ul class="nav metismenu" id="side-menu">
            <li class="nav-header">
                <div class="dropdown profile-element">
                    <span>
                        <img alt="image" class="img-circle" src="<%=path%>/ui/img/profile_small.jpg" />
                    </span>
                    <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                        <span class="clear">
                            <span class="block m-t-xs">
                                <strong class="font-bold">一哥</strong>
                            </span>
                            <span class="text-muted text-xs block">管理员<b class="caret"></b>
                            </span>
                        </span>
                    </a>
                </div>
                <div class="logo-element">
                    Ex+
                </div>
            </li>
            <li>
                <a href="index"><i class="fa fa-diamond"></i> <span class="nav-label">首页</span></a>
            </li>
            <li>
                <a href="index.html"><i class="fa fa-globe"></i> <span class="nav-label">地图查询</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level">
                    <li><a href="hot_spot">热力地图</a></li>
                    <li><a href="customer_flow">动线地图</a></li>
                    <li><a href="find_customer">寻找人员</a></li>
                    <li><a href="key_personnel">重点人员</a></li>
                    <li><a href="baseStation_monitoring">基站监控</a></li>
                </ul>
            </li>
            <li>
                <a href="index.html"><i class="fa fa-sitemap"></i> <span class="nav-label">任务调度</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level">
                    <li><a href="assignTask">分配任务</a></li>
                    <li><a href="taskList">任务数据</a></li>
                    <li><a href="findStaff">人员查看</a></li>
                </ul>
            </li>
            <li>
                <a href="index.html"><i class="fa fa-comment-o"></i> <span class="nav-label">通知管理</span> <span class="fa arrow"></span></a>
                <ul class="nav nav-second-level">
                    <li><a href="message">发送通知</a></li>
                    <li><a href="msgList">通知查看</a></li>
                </ul>
            </li>
            <li>
                <a href="cardScan"><i class="fa fa-address-book-o"></i> <span class="nav-label">名片信息</span></a>
            </li>
            <li>
                <a href="#"><i class="fa fa-bar-chart-o"></i> <span class="nav-label">分析报告</span></a>
            </li>
            <li>
                <a href="#"><i class="fa fa-database"></i> <span class="nav-label">数据导入</span></a>
            </li>
            <li>
                <a href="login"><i class="fa fa-sign-out"></i> <span class="nav-label">退出</span></a>
            </li>
        </ul>
    </div>
</nav>
<script src="<%=path%>/ui/js/jquery-3.1.1.min.js"></script>
<script>
    //标记导航栏
    $(document).ready(function(){
        var href = window.location.href;
        href = href.substring(href.lastIndexOf("/")+1);
        $("#side-menu > li").has("[href='"+href+"']").addClass("active");
        $("#side-menu > li > ul > li").has("[href='"+href+"']").addClass("active");
    });
</script>
