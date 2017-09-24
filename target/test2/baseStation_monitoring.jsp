<%--
  Created by IntelliJ IDEA.
  User: cxl
  Date: 2017/4/19
  Time: 10:34
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

    <title>基站监控</title>

    <link href="<%=path%>/ui/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path%>/ui/font-awesome/css/font-awesome.css" rel="stylesheet">
    <!-- Toastr style -->
    <link href="<%=path%>/ui/css/plugins/toastr/toastr.min.css" rel="stylesheet">
    <!-- Gritter -->
    <link href="<%=path%>/ui/js/plugins/gritter/jquery.gritter.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/style.css" rel="stylesheet">

    <style>
        body{
            font-family: Arial;
        }
        #mapFont{
            position: absolute;
            top:0;
            z-index: -99;
            font-family: 'VMapPublic',serif;
        }
        @font-face {font-family : 'VMapPublic';src : url(<%=path%>/Downloads/VMapPublic.ttf)format('truetype');}
        #map{font-family:VMapPublic,serif}
    </style>
</head>
<body>
<div id="mapFont">1</div>
<div id="wrapper">
    <!-- 包含菜单页面 James-->
    <jsp:include page="inc_side_bar.jsp" flush="true"/>
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <!-- 包含顶部菜单 James-->
        <jsp:include page="inc_top_bar.jsp" flush="true"/>
        <%--<div class="col-lg-12">--%>
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
<!-- Data picker -->
<%--<script src="<%=path%>/ui/js/plugins/datapicker/bootstrap-datepicker.js"></script>--%>
<%--<script language="javascript" src="<%=path%>/ui/js/jquery-ui-timepicker-addon.js"></script>--%>
<%--HeatmapJS--%>
<script src="<%=path%>/Downloads/heatmap.js"></script>
<%--VMap--%>
<script src="<%=path%>/Downloads/SDKjs-release/zepto-5a31069312.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/blockUI-c22b98bace.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/hammer-58b95c1ece.js"></script>
<script src="<%=path%>/Downloads/slave.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/VMap.js"></script>

<script>
    $('#floor option:eq(2)').attr('selected','selected');
    var map;
    var building  ="E9F6A2DE-EADC-45AF-A42E-C7458A401339";
    var floor =$("#floor").val();
    var checkTimer;
    var wholeTimer;
    window.onload = function() {
        var mapDiv = document.querySelector("#map");
        map= new Vmap(mapDiv,building,floor, {fontColor:"blue",publicColor:"green"});
        map.changeFloorCallBack(floor,checkBroadcastStations);
    }
    var clearTimer = function(){
        clearInterval(checkTimer);
        clearInterval(wholeTimer);
    }
    var replaceFloor = function(){
        floor = $("#floor").val();
        map.changeFloorCallBack(floor,checkBroadcastStations);
    }
    var checkBroadcastStations = function() {
        clearTimer();
        $.ajax({
            type:"POST",
            url:"<%=path%>/api/beaconInfo/getDeviceInfoList/",
            data:{building:building, floor:floor},
            success:function(response1) {
                //获取所有部署到地图上的基站
                deviceInfos = response1["list"];
                aaa();
                checkTimer = setInterval(function(){aaa()},600000);
            }
        });

    }
    var aaa= function(){
        //从现在到过去10分钟内的所有数据
        var nowDateStamp = Date.parse(new Date());
        var startTime = new Date();
        startTime.setTime(nowDateStamp-600000);
        startTime = startTime.format('yyyy-MM-dd hh:mm:ss');
        var endTime = new Date();
        endTime.setTime(nowDateStamp);
        endTime = endTime.format('yyyy-MM-dd hh:mm:ss');

        $.ajax({
            type:"POST",
            url:"<%=path%>/api/beaconInfo/getInfoByTimeRange/",
            data:{startTime:startTime,endTime:endTime},
            success:function(response2) {
                //取出过去一小时所有的beacon记录
                var beaconInfos = response2["list"];
                //从一小时的记录中取出所有出现过的deviceid
                var deviceIds = new Array();
                for (var i = 0; i < beaconInfos.length; i++) {
                    var beacon = beaconInfos[i];
                    var isResist = false;
                    for (var j = 0; j < deviceIds.length; j++) {
                        var device_id = deviceIds[j];
                        if (device_id == beacon["device_id"]) {
                            isResist = true;
                        }
                    }
                    if (!isResist) {
                        deviceIds.push(beacon["device_id"]);
                    }
                }

                //根据已部署的基站进行地图上的显示
                for(var i=0; i<deviceInfos.length;i++) {
                    var deviceInfo = deviceInfos[i];
                    var position_x = deviceInfo["position_x"];
                    var position_y = deviceInfo["position_y"];
                    var floor = deviceInfo["floor"];
                    //设置默认为未工作状态，如果在一小时内记录去除的deviceId相同则显示为工作状态
                    var pictureName = "broadcastStationOff.png";
                    for(var j=0;j<deviceIds.length;j++) {
                        var deviceId = deviceIds[j];
                        if(deviceId == deviceInfo["device_id"]) {
                            pictureName = "broadcastStationOn1.png";
                        }
                    }
                    var p = new VPoint(position_x,position_y,floor);
                    if (i == 0) {
                        map.clearOverlays();
                        //map.changeFloor(floor);
                    }
                    var marker = new VMarker(p,"<%=path%>/images/"+pictureName);
                    map.addOverlay(marker);
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
