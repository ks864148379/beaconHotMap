<%--
  Created by IntelliJ IDEA.
  User: cxl
  Date: 2017/4/12
  Time: 14:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">

    <title>重点人员</title>

    <link href="<%=path%>/ui/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path%>/ui/font-awesome/css/font-awesome.css" rel="stylesheet">
    <!-- Toastr style -->
    <link href="<%=path%>/ui/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <!-- Gritter -->
    <link href="<%=path%>/ui/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/style.css" rel="stylesheet">

    <link href="<%=path%>/ui/css/plugins/chosen/bootstrap-chosen.css" rel="stylesheet">
    <style>
        #map{font-family:VMapPublic,serif;overflow: hidden;}
        @font-face{font-family:VMapPublic;src:url(<%=path%>/Downloads/VMapPublic.ttf)}
        .ibox-content{padding: 15px 20px 0px 20px;}
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
                <div class="ibox float-e-margins" style="margin-bottom: 0;">
                    <div class="ibox-content">
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <div>
                                        <select data-placeholder="搜索" class="chosen-select" multiple style="width:350px;" tabindex="4" id="visitor_id">
                                            <c:forEach items="${visitors}" var="visitor">
                                                <option value="${visitor.visitor_id}">${visitor.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-1">
                                <button type="button" class="btn btn-primary" onclick="getLocation()" id="toView" >查看</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-lg-12">
                <%--热力图显示--%>
                <div class="ibox float-e-margins">
                    <div id="map-container" class="ibox-content ibox" style="position: relative;height: 1000px;overflow: hidden">
                        <%--楼层选择--%>
                        <form method="get" class="form-horizontal">
                            <div class="form-group">
                                <label class="col-sm-2 control-label">请选择</label>
                                <div class="col-sm-3">
                                    <select class="form-control m-b" id="floor">
                                        <c:forEach items="${floors}" var="floor">
                                            <option value="${floor.floor_id}">${floor.floor_chn}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <div class="col-sm-2">
                                    <button type="button" class="btn btn-primary" onclick="replaceFloor()">查看</button>
                                </div>
                            </div>
                        </form>
                        <%--地图显示    --%>
                        <div id="map"></div>
                    </div>
                </div>
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
<!-- ChartJS-->
<script src="<%=path%>/ui/js/plugins/chartJs/Chart.min.js"></script>
<%--<!-- Data picker -->--%>
<script type="text/javascript" src="<%=path%>/Downloads/DatePicker/WdatePicker.js"></script>
<%--HeatmapJS--%>
<script src="<%=path%>/Downloads/heatmap.js"></script>
<%--VMap--%>
<script src="<%=path%>/Downloads/SDKjs-release/zepto-5a31069312.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/blockUI-c22b98bace.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/hammer-58b95c1ece.js"></script>
<script src="<%=path%>/Downloads/slave.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/VMap.js"></script>
<!-- Chosen -->
<script src="<%=path%>/ui/js/plugins/chosen/chosen.jquery.js"></script>
<script>
    $('#floor option:eq(2)').attr('selected','selected');
    var map;
    var building  ="E9F6A2DE-EADC-45AF-A42E-C7458A401339";
    var floor =$("#floor").val();

    window.onload = function() {
        var mapDiv = document.querySelector("#map");
        map= new Vmap(mapDiv,building,floor, {fontColor:"blue",publicColor:"green"});
        map.changeFloor(floor);
    }
    var callback = function(){}
    $('.chosen-select').chosen({width: "100%"});
    var replaceFloor = function(){
        floor = $("#floor").val();
        map.changeFloor(floor);
    }
    var getLocation = function(){
        map.clearOverlays();
        var visitor_id = String($('#visitor_id').val());
        var locations;
        $.ajax({
            type:"POST",
            url:"<%=path%>/api/getLocations",
            data:{visitor_id:visitor_id},
            success:function(response){
                locations=response["list"];
                for(var i=0;i<locations.length;i++){
                    var location =locations[i];
                    var p = new VPoint(location.x,location.y,floor);
                    var vmaker = new VMarker(p,"<%=path%>/images/marker.png",null,location.name+" "+new Date(location.time.time).format('yyyy-MM-dd hh:mm'));
                    map.addOverlay(vmaker);
                }
            }
        });
    }

    Date.prototype.format = function(format) {
        var date = {
            "M+": this.getMonth() + 1,
            "d+": this.getDate(),
            "h+": this.getHours(),
            "m+": this.getMinutes(),
            "s+": this.getSeconds(),
            "q+": Math.floor((this.getMonth() + 3) / 3),
            "S+": this.getMilliseconds()
        };
        if (/(y+)/i.test(format)) {
            format = format.replace(RegExp.$1, (this.getFullYear() + '').substr(4 - RegExp.$1.length));
        }
        for (var k in date) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1
                        ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
            }
        }
        return format;
    }
</script>
</body>
</html>
