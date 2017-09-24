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
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>通知列表</title>
    <link href="<%=path%>/ui/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path%>/ui/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/plugins/iCheck/custom.css" rel="stylesheet">
    <!-- Toastr style -->
    <link href="<%=path%>/ui/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <!-- Gritter -->
    <link href="<%=path%>/ui/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/plugins/steps/jquery.steps.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/style.css" rel="stylesheet">

    <link href="<%=path%>/ui/css/plugins/blueimp/css/blueimp-gallery.min.css" rel="stylesheet">

    <style>
        .wrapper-count{
            color: #1ab394;
        }
    </style>
</head>
<body>
<div id="wrapper">
    <!-- 包含菜单页面 James-->
    <jsp:include page="inc_side_bar.jsp" flush="true"/>
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <!-- 包含顶部菜单 James-->
        <jsp:include page="inc_top_bar.jsp" flush="true"/>

        <div class="row">
            <div class="col-lg-12">
                <div class="wrapper wrapper-content animated fadeInRight">
                    <div class="row">
                        <div class="col-sm-12">
                            <div class="ibox" id="ibox1">
                                <div class="ibox-content">
                                    <h2>通知列表</h2>
                                    <p>
                                        这里显示所有的通知信息，暂不支持翻页。
                                    </p>
                                    <div class="sk-spinner sk-spinner-double-bounce">
                                        <div class="sk-double-bounce1"></div>
                                        <div class="sk-double-bounce2"></div>
                                    </div>
                                    <div class="project-list">
                                        <ul class="nav nav-tabs">
                                            <span class="pull-right small text-muted">共有 <span id="list_length">0</span> 条记录</span>
                                        </ul>
                                        <table class="table table-hover" id="message_list">
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <jsp:include page="inc_footer_bar.jsp" flush="true"/>
            </div>
        </div>

    </div>
    <!-- The Gallery as lightbox dialog, should be a child element of the document body -->
    <div id="blueimp-gallery" class="blueimp-gallery">
        <div class="slides"></div>
        <h3 class="title"></h3>
        <a class="prev">‹</a>
        <a class="next">›</a>
        <a class="close">×</a>
        <a class="play-pause"></a>
        <ol class="indicator"></ol>
    </div>
</div>

<!-- Mainly scripts -->
<script src="<%=path%>/ui/js/jquery-3.1.1.min.js"></script>
<script src="<%=path%>/ui/js/bootstrap.min.js"></script>
<script src="<%=path%>/ui/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="<%=path%>/ui/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Custom and plugin javascript -->
<script src="<%=path%>/ui/js/inspinia.js"></script>
<script src="<%=path%>/ui/js/plugins/pace/pace.min.js"></script>
<!-- Jvectormap -->
<script src="<%=path%>/ui/js/plugins/jvectormap/jquery-jvectormap-2.0.2.min.js"></script>
<script src="<%=path%>/ui/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>

<!-- jQuery UI -->
<script src="<%=path%>/ui/js/plugins/jquery-ui/jquery-ui.min.js"></script>

<!-- ChartJS-->
<script src="<%=path%>/ui/js/plugins/chartJs/Chart.min.js"></script>
<%--<script src="<%=path%>/ui/js/demo/chartjs-demo.js"></script>--%>

<!-- Jquery Validate -->
<script src="<%=path%>/ui/js/plugins/validate/jquery.validate.min.js"></script>

<!-- blueimp gallery -->
<script src="<%=path%>/ui/js/plugins/blueimp/jquery.blueimp-gallery.min.js"></script>
<script>
    $(document).ready(function(){
        $('#ibox1').children('.ibox-content').toggleClass('sk-loading');
        $.getJSON("<%=path%>/api/message/msgList",function(data){
            if(data.success == true){
//                $("#last_time").html(data.time);
                console.info(data);
                var mHtml = $("<tbody></tbody>");
                for (i=0;i<data.list.length;i++) {
                    tabRow = $("<tr></tr>");
                    var time = data.list[i].createTime;
                    tabRow.append("<td class=\"project-title\"><a href=\"#\">"+data.list[i].content
                        +"</a><br/><small>"+(time.year+1900)+"-"+(time.month+1)+"-"+time.date+" "
                        +time.hours+":"+time.minutes+":"+time.seconds+"</small></td>");
                    var imgCircle = $("<td class=\"project-people\">"+data.list[i].stuffId+"</td>");
                    tabRow.append(imgCircle);
                    mHtml.append(tabRow);
                }
                $("#message_list").html(mHtml);
                $("#list_length").html(data.length);
            }
            $('#ibox1').children('.ibox-content').toggleClass('sk-loading');
        });
    });
</script>

</body>
</html>