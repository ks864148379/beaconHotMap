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
    <title>任务列表</title>
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
                                    <span class="text-muted small pull-right">最后的任务: <i class="fa fa-clock-o"></i> <span id="last_time"> <i class="fa fa-circle-o-notch fa-spin"></i></span></span>
                                    <h2>工作人员</h2>
                                    <p>
                                        本列表只显示前50条信息。如果查询目标不在列表内，请缩小查询范围。
                                    </p>
                                    <div class="sk-spinner sk-spinner-double-bounce">
                                        <div class="sk-double-bounce1"></div>
                                        <div class="sk-double-bounce2"></div>
                                    </div>
                                    <div class="input-group">
                                        <input type="text" placeholder="工作人员……" class="input form-control" id="stuff_search">
                                        <span class="input-group-btn">
                                            <button type="button" class="btn btn btn-primary" id="btn_search"> <i class="fa fa-search"></i> 查询</button>
                                        </span>
                                    </div>
                                    <div class="clients-list">
                                        <ul class="nav nav-tabs">
                                            <span class="pull-right small text-muted">共有 <span id="list_length">0</span> 条记录</span>
                                        </ul>
                                        <table class="table table-striped table-hover" id="stuff_list">
                                        </table>
                                    </div>
                                </div>
                            </div>

                            <div class="ibox float-e-margins animated hidden" id="ibox2">
                                <div class="ibox-title">
                                    <div class="ibox-tools">
                                        <a class="close-link" id="btn_close">
                                            <i class="fa fa-times"></i>
                                        </a>
                                    </div>
                                </div>
                                <div class="ibox-content text-center">
                                    <h1 id="stuff_name">Name</h1>
                                    <div class="m-b-sm">
                                        <img alt="image" class="img-circle" id="stuff_img" src="../images/default_player.jpg">
                                    </div>
                                    <p class="font-bold" id="stuff_position">Position</p>
                                    <!--
                                    <div class="text-center">
                                        <a class="btn btn-xs btn-white"><i class="fa fa-thumbs-up"></i> Like </a>
                                        <a class="btn btn-xs btn-primary"><i class="fa fa-heart"></i> Love</a>
                                    </div>
                                    -->
                                </div>
                                <div class="ibox-title">
                                    <h5>任务列表</h5>
                                    <div class="ibox-tools">
                                        <span class="label label-warning-light pull-right">共有 <span id="task_length">0</span> 条任务信息</span>
                                    </div>
                                </div>
                                <div class="ibox-content">
                                    <div class="feed-activity-list" id="taskList">
                                    </div>
                                    <!--<button class="btn btn-primary btn-block m-t"><i class="fa fa-arrow-down"></i> Show More</button>-->
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

        var taskClick = function(){
            $("#taskList").html("");
            $("#task_length").html("0");
            var stuff_id = $(this).attr("id");
            console.info(stuff_id);

            $("#ibox1").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceOutLeft");
            $("#ibox2").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceInRight");
            $("#ibox1").hide();
            $("#ibox2").show();

            $("#stuff_name").html($(this).find(".client-link").html());
            $("#stuff_img").attr("src",$(this).find("img").attr("src"));
            $("#stuff_position").html($(this).find("td:eq(2)").html());
            var url = "<%=path%>/api/task/getTaskByStuffId?stuff_id="+stuff_id;
            $.getJSON(url,function(data){
                if(data.success == true) {
                    //console.info(data.list);
                    var mHtml = $("<div></div>");
                    for (i=0;i<data.list.length;i++) {
                        tabRow = $("<div></div>");
                        tabRow.addClass("feed-element");
                        tabRow.append("<img alt=\"image\" class=\"img-circle pull-left\" src=\"../images/"+data.list[i].c_image+"\">");
                        mediaBody = $("<div></div>");
                        mediaBody.addClass("media-body");
                        mediaBody.append("<small class=\"pull-right text-navy\">"+data.list[i].create_time+"</small>");
                        mediaBody.append("<strong>"+data.list[i].name+"</strong>");
                        mediaBody.append("&nbsp;&nbsp;&nbsp;<small class=\"text-muted\">["+data.list[i].position+"]</small><br>");
                        mediaBody.append("<span class=\"text-success\">"+data.list[i].task_title+"</span>");
                        mediaBody.append("<p>&nbsp;&nbsp;&nbsp;&nbsp;"+data.list[i].task_content+"</p>");


                        actionBody = $("<div></div>");
                        actionBody.addClass("actions");
                        var audioList = [];
                        if(data.list[i].audio != ""){
                            audioList = JSON.parse(data.list[i].audio);
                            $.each(audioList, function(){
                                actionBody.append("<audio controls=\"controls\" src=\"/audios/"+this+"\"></audio>");
                            });
                            mediaBody.append(actionBody);
                        }

                        photosBody = $("<div></div>");
                        photosBody.addClass("photos");
                        var photoList = [];
                        if(data.list[i].t_image != ""){
                            photoList = JSON.parse(data.list[i].t_image);
                            $.each(photoList, function(){
                                photosBody.append("<a href=\"../images/" + this + "\"" +
                                    " data-gallery=\"\"> <img class=\"feed-photo\" src=\"/images/" +
                                    this + "\"></a>");
                            });
                            mediaBody.append(photosBody);
                        }

                        docBody = $("<div></div>");
                        docBody.addClass("well");
                        var docList = [];
                        if(data.list[i].task_description != ""){
                            docList = JSON.parse(data.list[i].task_description);
                            $.each(docList, function(){
                                docBody.append("<p>" + this + "</p>");
                            });
                            mediaBody.append(docBody);
                        }

                        tabRow.append(mediaBody);
                        mHtml.append(tabRow);
                    }
                    console.info(mHtml);
                    $("#taskList").html(mHtml);
                    $("#task_length").html(data.length);
                }
            });
        };

        $.getJSON("<%=path%>/api/task/getTaskLastTime",function(data){
            if(data.success == true){
                $("#last_time").html(data.time);
            }
        });
        
        $("#btn_close").click(function () {
            $("#ibox2").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceOutRight");
            $("#ibox1").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceInLeft");
            $("#ibox2").hide();
            $("#ibox1").show();
        });

        $("#btn_search").click(function(){
            console.info($("#stuff_search").val());
            $('#ibox1').children('.ibox-content').toggleClass('sk-loading');
            $.ajax({
                type: "POST",
                url: "<%=path%>/api/task/searchStuff",
                data: {param:$("#stuff_search").val()},
                success :function(data){
                    //console.info(data);
                    if(data.success == true && data.length > 0) {
                        console.info(data.list);
                        var mHtml = $("<tbody></tbody>");
                        for (i=0;i<data.list.length;i++) {
                            tabRow = $("<tr></tr>");
                            //console.info(data.list[i].image);
                            tabRow.attr("id",data.list[i].visitor_id);
                            tabRow.append("<td class=\"client-avatar\"><img alt=\"image\" src=\"../images/"+data.list[i].image+"\"> </td>");
                            tabRow.append("<td><a data-toggle=\"tab\" class=\"client-link\">"+data.list[i].name+"</a></td>");
                            tabRow.append("<td>"+data.list[i].position+"</td>");
                            tabRow.append("<td class=\"contact-type\"><i class=\"fa fa-phone-square\"> </i></td>");
                            tabRow.append("<td><a href=\"tel:"+data.list[i].tel+"\">"+data.list[i].tel+"</td>");
                            mHtml.append(tabRow);
                        }
                        $("#stuff_list").html(mHtml);
                        $("#list_length").html(data.length);

                        $("#stuff_list tbody tr").click(taskClick);

                    }else if(data.length == 0){
                        var mHtml = $("<tbody><tr><td colspan='4'>查询没有记录！</td></tr></tbody>");
                        //alert("查询没有记录！");
                        $("#stuff_list").html(mHtml);
                    }else{
                        alert("服务器异常请联系工作人员！");
                    }
                    $('#ibox1').children('.ibox-content').toggleClass('sk-loading');
                },
                error:function () {
                    alert("服务器异常请联系工作人员！");
                    $('#ibox1').children('.ibox-content').toggleClass('sk-loading');
                }
            });
        });
    });
</script>

</body>
</html>