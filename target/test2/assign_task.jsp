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
    <link href="<%=path%>/ui/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/style.css" rel="stylesheet">

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
                            <div class="ibox animated" id="ibox1">
                                <div class="ibox-content">
                                    <span class="text-muted small pull-right">上次分配: <i class="fa fa-clock-o"></i> <span id="last_time"> <i class="fa fa-circle-o-notch fa-spin"></i></span></span>
                                    <h2>分配任务</h2>
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
                                    <form method="post" class="form-horizontal">
                                        <div class="table-responsive">
                                            <ul class="nav nav-tabs">
                                                <span class="pull-right small text-muted">共有 <span id="list_length">0</span> 条记录</span>
                                            </ul>
                                            <table class="table table-striped">
                                                <thead>
                                                    <tr>
                                                        <th></th>
                                                        <th></th>
                                                        <th>姓名</th>
                                                        <th>职位</th>
                                                        <th></th>
                                                        <th>电话</th>
                                                        <th>状态</th>
                                                    </tr>
                                                </thead>
                                                <tbody id="stuff_list"></tbody>
                                            </table>
                                        </div>
                                        <div class="hr-line-dashed"></div>
                                        <div class="form-group">
                                            <div class="col-sm-4 pull-right">
                                                <button class="btn btn-primary" type="button" id="btn_next">下一步</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <div class="ibox animated hidden" id="ibox2">
                                <div class="ibox-content">
                                    <h2>任务目标</h2>
                                    <p>
                                        本列表只显示前50条信息。如果查询目标不在列表内，请缩小查询范围。
                                    </p>
                                    <div class="sk-spinner sk-spinner-double-bounce">
                                        <div class="sk-double-bounce1"></div>
                                        <div class="sk-double-bounce2"></div>
                                    </div>
                                    <div class="input-group">
                                        <input type="text" placeholder="特邀嘉宾……" class="input form-control" id="customer_search">
                                        <span class="input-group-btn">
                                            <button type="button" class="btn btn btn-primary" id="btn_search_2"> <i class="fa fa-search"></i> 查询</button>
                                        </span>
                                    </div>
                                    <form method="post" class="form-horizontal">
                                        <div class="clients-list">
                                            <ul class="nav nav-tabs">
                                                <span class="pull-right small text-muted">共有 <span id="list_length_2">0</span> 条记录</span>
                                            </ul>
                                            <table class="table table-striped table-hover" id="customer_list"></table>
                                        </div>
                                        <div class="hr-line-dashed"></div>
                                        <div class="form-group">
                                            <div class="col-sm-4 pull-right">
                                                <button class="btn btn-primary" type="button" id="btn_pre">上一步</button>
                                                <button class="btn btn-primary" type="button" id="btn_next_2">下一步</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                            <div class="ibox animated hidden" id="ibox3">
                                <div class="ibox-content">
                                    <h2>任务的相关信息</h2>
                                    <p>
                                        请在下面填写任务的标题和内容。
                                    </p>
                                    <form method="post" class="form-horizontal">
                                        <div class="form-group">
                                            <label class="col-sm-12">标题</label>
                                            <div class="col-sm-12">
                                                <input type="text" class="form-control" id="task_title" placeholder="任务标题……">
                                            </div>
                                        </div>
                                        <div class="form-group">
                                            <label class="col-sm-12">内容</label>
                                            <div class="col-sm-12">
                                                <textarea class="form-control" id="task_content" placeholder="任务内容……" rows="3"></textarea>
                                            </div>
                                        </div>
                                        <div class="hr-line-dashed"></div>
                                        <div class="form-group">
                                            <div class="col-sm-4 pull-right">
                                                <button class="btn btn-primary" type="button" id="btn_pre_2">上一步</button>
                                                <button class="btn btn-success" type="button" id="btn-send">分　配</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <jsp:include page="inc_footer_bar.jsp" flush="true"/>
            </div>
        </div>

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

<!-- jQuery UI -->
<script src="<%=path%>/ui/js/plugins/jquery-ui/jquery-ui.min.js"></script>

<!-- Jquery Validate -->
<script src="<%=path%>/ui/js/plugins/validate/jquery.validate.min.js"></script>

<!-- Peity -->
<script src="<%=path%>/ui/js/plugins/peity/jquery.peity.min.js"></script>

<!-- iCheck -->
<script src="<%=path%>/ui/js/plugins/iCheck/icheck.min.js"></script>

<!-- Peity -->
<script src="<%=path%>/ui/js/demo/peity-demo.js"></script>
<script>
    $(document).ready(function(){
        $.getJSON("<%=path%>/api/task/getTaskLastTime",function(data){
            if(data.success == true){
                $("#last_time").html(data.time);
            }
        });

        $("#btn_next").click(function (){
            var stuff_check = $("input:checkbox[name=stuff_check]:checked").val();
            console.info(stuff_check);
            if(stuff_check){
                $("#ibox1").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceOutLeft");
                $("#ibox2").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceInRight");
                $("#ibox1").hide();
                $("#ibox2").show();
            }else{
                alert("请查询后选择工作人员！");
            }
        });

        $("#btn_pre").click(function (){
            $("#ibox2").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceOutRight");
            $("#ibox1").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceInLeft");
            $("#ibox2").hide();
            $("#ibox1").show();
        });

        $("#btn_next_2").click(function (){
            var customer_check = $("input:radio[name=customer_check]:checked").val();
            console.info(customer_check);
            if(customer_check){
                $("#ibox2").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceOutLeft");
                $("#ibox3").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceInRight");
                $("#ibox2").hide();
                $("#ibox3").show();
            }else{
                alert("请查询后选择工作人员！");
            }
        });

        $("#btn_pre_2").click(function (){
            $("#ibox3").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceOutRight");
            $("#ibox2").removeAttr('class').attr('class', '').addClass('animated').addClass("bounceInLeft");
            $("#ibox3").hide();
            $("#ibox2").show();
        });

        $("#btn-send").click(function(){
            var stuff_id = [];
            $("input:checkbox[name=stuff_check]:checked").each(function(){
                stuff_id.push(this.value);
            });
            var customer_id = $("input:radio[name=customer_check]:checked").val();
            var task_title = $("#task_title").val();
            var task_content = $("#task_content").val();
            $.ajax({
                type: "POST",
                url: "<%=path%>/api/task/submitTask",
                data: {stuff_id:JSON.stringify(stuff_id),customer_id:customer_id,task_title:task_title,task_content:task_content},
                success :function(data){
                    if(data.success == true){
                        alert("任务分配成功！");
                        location.reload();
                    }else{
                        alert("没有成功，稍后再试！");
                    }
                },
                error:function () {
                    alert("服务器异常请联系工作人员！");
                }
            });
        });

        $("#btn_search").click(function(){
            console.info($("#stuff_search").val());
            $('#ibox1').children('.ibox-content').toggleClass('sk-loading');
            $.ajax({
                type: "POST",
                url: "<%=path%>/api/task/searchStuffTaskState",
                data: {param:$("#stuff_search").val()},
                success :function(data){
                    //console.info(data);
                    if(data.success == true && data.length > 0) {
                        console.info(data.list);
                        var mHtml = "";
                        for (i=0;i<data.length;i++) {
                            var tabRow = "<tr>";
                            //console.info(data.list[i].image);
                            tabRow += "<td><input type=\"checkbox\" class=\"i-checks\" name=\"stuff_check\" value=\""+data.list[i].visitor_id+"\"></td>";
                            tabRow += "<td class=\"client-avatar\"><img alt=\"image\" src=\"../images/"+data.list[i].image+"\"> </td>";
                            tabRow += "<td><a data-toggle=\"tab\" href=\"#contact-1\" class=\"client-link\">"+data.list[i].name+"</a></td>";
                            tabRow += "<td>"+data.list[i].position+"</td>";
                            tabRow += "<td class=\"contact-type\"><i class=\"fa fa-phone-square\"> </i></td>";
                            tabRow += "<td><a href=\"tel:"+data.list[i].tel+"\">"+data.list[i].tel+"</td>";
                            if(data.list[i].state === 0){
                                tabRow += "<td class=\"client-status\"><span class=\"badge badge-danger\">已分</span></td>";
                            }else if(data.list[i].state === 1){
                                tabRow += "<td class=\"client-status\"><span class=\"badge badge-warning\">进行</span></td>";
                            }else if(data.list[i].state === 2){
                                tabRow += "<td class=\"client-status\"><span class=\"badge badge-succes\">完成</span></td>";
                            }else{
                                tabRow += "<td class=\"client-status\"><span class=\"badge badge-primary\">空闲</span></td>";
                            }
                            tabRow += "</tr>";
                            mHtml += tabRow;
                        }
                        $("#stuff_list").html(mHtml);
                        $("#list_length").html(data.length);
                        $('.i-checks').iCheck({
                            checkboxClass: 'icheckbox_square-green',
                            radioClass: 'iradio_square-green',
                        });
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

        $("#btn_search_2").click(function(){
            console.info($("#customer_search").val());
            $('#ibox2').children('.ibox-content').toggleClass('sk-loading');
            $.ajax({
                type: "POST",
                url: "<%=path%>/api/task/searchCustomer",
                data: {param:$("#customer_search").val()},
                success :function(data){
                    //console.info(data);
                    if(data.success == true && data.length > 0) {
                        console.info(data.list);
                        var mHtml = $("<tbody></tbody>");
                        for (i=0;i<data.length;i++) {
                            tabRow = $("<tr></tr>");
                            //console.info(data.list[i].image);
                            tabRow.append("<td><input type=\"radio\" class=\"i-checks\" name=\"customer_check\" value=\""+data.list[i].visitor_id+"\"></td>");
                            tabRow.append("<td class=\"client-avatar\"><img alt=\"image\" src=\"../images/"+data.list[i].image+"\"> </td>");
                            tabRow.append("<td><a data-toggle=\"tab\" href=\"#contact-1\" class=\"client-link\">"+data.list[i].name+"</a></td>");
                            tabRow.append("<td>"+data.list[i].position+"</td>");
                            tabRow.append("<td class=\"contact-type\"><i class=\"fa fa-phone-square\"> </i></td>");
                            tabRow.append("<td><a href=\"tel:"+data.list[i].tel+"\">"+data.list[i].tel+"</td>");
                            mHtml.append(tabRow);
                        }
                        $("#customer_list").html(mHtml);
                        $("#list_length_2").html(data.length);
                        $('.i-checks').iCheck({
                            checkboxClass: 'icheckbox_square-green',
                            radioClass: 'iradio_square-green',
                        });
                    }else if(data.length == 0){
                        var mHtml = $("<tbody><tr><td colspan='4'>查询没有记录！</td></tr></tbody>");
                        //alert("查询没有记录！");
                        $("#stuff_list").html(mHtml);
                    }else{
                        alert("服务器异常请联系工作人员！");
                    }
                    $('#ibox2').children('.ibox-content').toggleClass('sk-loading');
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