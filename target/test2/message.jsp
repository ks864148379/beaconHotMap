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
    <title>通知管理</title>
    <link href="<%=path%>/ui/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path%>/ui/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/plugins/iCheck/custom.css" rel="stylesheet">
    <!-- Toastr style -->
    <link href="<%=path%>/ui/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <!-- Gritter -->
    <link href="<%=path%>/ui/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
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
                <div class="wrapper wrapper-content animated fadeInRigh">
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="ibox float-e-margins" id="ibox1">
                                <div class="ibox-title">
                                    <h5>发送通知</h5>
                                </div>
                                <div class="ibox-content">
                                    <div class="row">
                                        <div class="col-sm-12">
                                            <div class="input-group"><input type="text" placeholder="Search" class="input-sm form-control" id="stuff_search"> <span class="input-group-btn">
                                        <button type="button" class="btn btn-sm btn-primary" id="btn_search"> 查询</button> </span></div>
                                        </div>
                                    </div>
                                    <div class="sk-spinner sk-spinner-double-bounce">
                                        <div class="sk-double-bounce1"></div>
                                        <div class="sk-double-bounce2"></div>
                                    </div>
                                    <div class="table-responsive">
                                        <table class="table table-striped" id="stuff_list">
                                            <thead>
                                                <tr>
                                                    <th><input type="checkbox" class="i-checks"></th>
                                                    <th></th>
                                                    <th>姓名 </th>
                                                    <th>身份</th>
                                                    <th></th>
                                                    <th>电话</th>
                                                </tr>
                                            </thead>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-6">
                            <div class="ibox float-e-margins">
                                <div class="ibox-title">
                                    <h5>通知</h5>
                                </div>
                                <div class="ibox-content">
                                    <form method="post" class="form-horizontal" id="fm1">
                                        <div class="form-group"><label class="col-sm-2 control-label">信息内容</label>
                                            <div class="col-sm-10">
                                                <textarea class="form-control" id="send_msg" placeholder="信息内容……" rows="3"></textarea>
                                            </div>
                                        </div>
                                        <div class="hr-line-dashed"></div>
                                        <div class="form-group">
                                            <div class="col-sm-4 col-sm-offset-2">
                                                <button class="btn btn-white" type="reset">取消</button>
                                                <button class="btn btn-primary" type="submit">发送</button>
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

<!-- Peity -->
<script src="<%=path%>/ui/js/plugins/peity/jquery.peity.min.js"></script>

<!-- iCheck -->
<script src="<%=path%>/ui/js/plugins/iCheck/icheck.min.js"></script>

<!-- Peity -->
<script src="<%=path%>/ui/js/demo/peity-demo.js"></script>

<script>
    $(document).ready(function(){
        $('.i-checks').iCheck({
            checkboxClass: 'icheckbox_square-green',
        });

        $("#fm1").submit(function () {
            var checked_val = [];
            $("input:checkbox[name=input_check]:checked").each(function(){
                checked_val.push(this.value);
             });
//            console.info(JSON.stringify(checked_val));
//            console.info($("#send_msg").val());
            $.ajax({
                type: "POST",
                url: "<%=path%>/api//message/sendMessage",
                data: {msg:$("#send_msg").val(),stuffList:JSON.stringify(checked_val)},
                success:function(data){
//                    console.info(data);
                    if(data.success == true){
                        alert("成功发送！");
                        $("input:checkbox[name=input_check]").attr("checked", false);
                        $("#send_msg").attr("value","");
                    }else{
                        alert("没有成功，稍后再试！");
                    }
                },
                error:function() {
                    alert("服务器异常请联系工作人员！");
                }
            });
            return false;
        });

        $("#btn_search").click(function(){
            console.info($("#stuff_search").val());
            $("#stuff_list > tbody").empty();
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
                        for (i=0;i<data.length;i++) {
                            tabRow = $("<tr></tr>");
                            //console.info(data.list[i].image);
                            tabRow.append("<td><input type=\"checkbox\" class=\"i-checks\" name=\"input_check\" value=\""+data.list[i].visitor_id+"\"></td>");
                            tabRow.append("<td class=\"client-avatar\"><img alt=\"image\" src=\"../images/"+data.list[i].image+"\"> </td>");
                            tabRow.append("<td><a data-toggle=\"tab\" href=\"#contact-1\" class=\"client-link\">"+data.list[i].name+"</a></td>");
                            tabRow.append("<td>"+data.list[i].position+"</td>");
                            tabRow.append("<td class=\"contact-type\"><i class=\"fa fa-phone-square\"> </i></td>");
                            tabRow.append("<td><a href=\"tel:"+data.list[i].tel+"\">"+data.list[i].tel+"</td>");
                            mHtml.append(tabRow);
                        }
                        $("#stuff_list").append(mHtml);
                        $("#list_length").html(data.length);
                        $('.i-checks').iCheck({
                            checkboxClass: 'icheckbox_square-green',
                        });
                    }else if(data.length == 0){
                        var mHtml = $("<tbody><tr><td colspan='4'>查询没有记录！</td></tr></tbody>");
                        //alert("查询没有记录！");
                        $("#stuff_list").append(mHtml);
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