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
    <title>名片信息</title>
    <link href="<%=path%>/ui/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path%>/ui/font-awesome/css/font-awesome.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/plugins/iCheck/custom.css" rel="stylesheet">
    <!-- Toastr style -->
    <link href="<%=path%>/ui/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <!-- Gritter -->
    <link href="<%=path%>/ui/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/plugins/steps/jquery.steps.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/plugins/dataTables/datatables.min.css" rel="stylesheet">
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
                        <div class="col-lg-12">
                            <div class="ibox float-e-margins">
                                <div class="ibox-title">
                                    <h5>这里是通过作战指挥App扫描提交上来的客户名片信息。</h5>
                                </div>
                                <div class="ibox-content">
                                    <div class="sk-spinner sk-spinner-double-bounce">
                                        <div class="sk-double-bounce1"></div>
                                        <div class="sk-double-bounce2"></div>
                                    </div>
                                    <div class="table-responsive">
                                        <table class="table table-striped table-bordered table-hover dataTables-example">
                                            <thead>
                                                <tr>
                                                    <th>姓名</th>
                                                    <th>职位</th>
                                                    <th>单位</th>
                                                    <th>Tel</th>
                                                    <th>Email</th>
                                                </tr>
                                            </thead>
                                            <tbody id="card_list">
                                            </tbody>
                                            <tfoot>
                                                <tr>
                                                    <th>姓名</th>
                                                    <th>职位</th>
                                                    <th>单位</th>
                                                    <th>Tel</th>
                                                    <th>Email</th>
                                                </tr>
                                            </tfoot>
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
<script src="<%=path%>/ui/js/plugins/dataTables/datatables.min.js"></script>
<!-- Custom and plugin javascript -->
<script src="<%=path%>/ui/js/inspinia.js"></script>
<script src="<%=path%>/ui/js/plugins/pace/pace.min.js"></script>
<script>
    $(document).ready(function(){

        $('#ibox1').children('.ibox-content').toggleClass('sk-loading');
        $.getJSON("<%=path%>/api/card/cardList",function(data){
            $("#card_list").empty();
            if(data.success == true){
//                $("#last_time").html(data.time);
                console.info(data);
                var mHtml = $("");
                for (i=0;i<data.list.length;i++) {
                    mHtml += "<tr class=\"gradeA\">";
                    mHtml += "<td>"+data.list[i].name+"</td>";
                    mHtml += "<td>"+data.list[i].title.replace("[","").replace("]","").replace(/"([^"]*)"/g,"$1")+"</td>";
                    mHtml += "<td>"+data.list[i].organization.replace("null","")+"</td>";
                    mHtml += "<td>"+data.list[i].tel.replace("[","").replace("]","").replace(/"([^"]*)"/g,"$1")+"</td>";
                    mHtml += "<td>"+data.list[i].email.replace("[","").replace("]","").replace(/"([^"]*)"/g,"$1")+"</td>";
                    mHtml += "</tr>";
                }
                $("#card_list").html(mHtml);
            }
            $('#ibox1').children('.ibox-content').toggleClass('sk-loading');
            dataFunc();
        });

        var dataFunc = function() {
            $('.dataTables-example').DataTable({
                pageLength: 25,
                responsive: true,
                dom: '<"html5buttons"B>lTfgitp',
                buttons: [
                    {extend: 'copy'},
//                    {extend: 'csv'},
                    {extend: 'excel', title: 'ExampleFile'},
//                    {extend: 'pdf', title: 'ExampleFile'},

                    {
                        extend: 'print',
                        customize: function (win) {
                            $(win.document.body).addClass('white-bg');
                            $(win.document.body).css('font-size', '10px');

                            $(win.document.body).find('table')
                                .addClass('compact')
                                .css('font-size', 'inherit');
                        }
                    }
                ]

            });
        };
    });
</script>
</body>
</html>