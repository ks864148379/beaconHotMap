<%--
  Created by IntelliJ IDEA.
  User: cxl
  Date: 2017/4/10
  Time: 9:35
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

    <title>人员动线</title>

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
        #setviewtime{margin-bottom: 0;}
        .ibox-content{padding: 15px 20px 0px 20px;}
    </style>
<body>
<div id="wrapper">
    <div id="mapFont">1</div>
    <!-- 包含菜单页面 James-->
    <jsp:include page="inc_side_bar.jsp" flush="true"/>
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <!-- 包含顶部菜单 James-->
        <jsp:include page="inc_top_bar.jsp" flush="true"/>
        <div class="row">
            <div class="col-lg-12">
                <div id="setviewtime" class="ibox float-e-margins">
                    <div class="ibox-content">
                        <div class="row">
                            <div class="col-md-4">
                                <div class="form-group">
                                    <div>
                                        <select data-placeholder="搜索" class="chosen-select"  tabindex="2" id="visitor_id">
                                            <option ></option>
                                            <c:forEach items="${visitors}" var="visitor">
                                            <option value="${visitor.visitor_id}">${visitor.name}</option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group" >
                                    <div class="input-group date" >
                                        <span class="input-group-addon">
                                            <%--<i class="fa fa-calendar"></i>--%>
                                            <lable class="font-normal">开始时间</lable>
                                        </span>
                                        <input  type="text" class="form-control" id="StartTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',autoSize: true})">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-3">
                                <div class="form-group" >
                                    <div class="input-group date" >
                                        <span class="input-group-addon">
                                            <%--<i class="fa fa-calendar"></i>--%>
                                            <lable class="font-normal">结束时间</lable>
                                        </span>
                                        <input  type="text" class="form-control" id="StopTime" onclick="WdatePicker({dateFmt: 'yyyy-MM-dd HH:mm:ss',autoSize: true})">
                                    </div>
                                </div>
                            </div>
                            <div class="col-md-1">
                                <button type="button" class="btn btn-primary" onclick="GetHistory()" id="toView">查看</button>
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
                                    <button type="button" class="btn btn-primary" onclick="replaceFloor()" id="change_floor">查看</button>
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
    var checkTimer;
    var timeoutProcess ;
    var wholeTimer;
    var flag_i=0;
    var timeout=0;
    var setCheckView = document.getElementById("toView");
    var changeFloor = document.getElementById("change_floor");

    window.onload = function() {
        var mapDiv = document.querySelector("#map");
        map= new Vmap(mapDiv,building,floor, {fontColor:"blue",publicColor:"green"});
        map.changeFloor(floor);
    }
    var callback = function(){}
    var clearTimer = function(){
        clearInterval(checkTimer);
        clearInterval(wholeTimer);
        clearTimeout(timeoutProcess);
    }
    var replaceFloor = function(){
        floor = $("#floor").val();
       /* map.changeFloor(floor);
        GetHistory();*/
        map.changeFloorCallBack(floor,GetHistory);
    }
    var GetHistory = function(){
        map.clearOverlays();
        clearTimer();
        clearTimeout(timeoutProcess);
        var sTime=$('#StartTime').val();
        var stopTime = $('#StopTime').val();
        sTime = new Date(sTime.replace(/-/g,"/"));
        stopTime = new Date(stopTime.replace(/-/g,"/"));

        var startTime =  sTime;
        var endTime = stopTime;
        eee(startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));

    }
    var eee = function(startTime,endTime){
        var visitorId =String($('#visitor_id').val());
        map.clearOverlays();
        $.ajax({
            type:"POST",
            url:"<%=path%>/api/track/getTrackByVisitorId",
            data:{visitorId:visitorId, startTime:startTime, endTime:endTime, floor:floor},
            success:function(response){
                setCheckView.disabled=true;
                changeFloor.disabled=true;
                var trackPoints = response["list"];
                if(trackPoints.length==1){
                    var trackPoint = trackPoints[0];
                    var p = new VPoint(trackPoint.x,trackPoint.y,floor);
                    var vmaker = new VMarker(p,"<%=path%>/images/marker.png");
                    map.addOverlay(vmaker);
                    setCheckView.disabled=false;
                    changeFloor.disabled=false;
                }else {
                    draw(trackPoints);
                }
            }
        });
    }
    var draw = function(trackPoints){
        if(flag_i>=trackPoints.length){alert("结束");flag_i=0;timeout=0;setCheckView.disabled=false;changeFloor.disabled=false;return;}
        if(flag_i==0){
            var trackPoint1=trackPoints[0];
            var p0 = new VPoint(trackPoint1.x,trackPoint1.y,floor);
            drawTrack(p0,p0);
            var trackPoint1=trackPoints[flag_i];
            var trackPoint2=trackPoints[flag_i+1];
            var interval =trackPoint2.time-trackPoint1.time;
            if(interval<=1*60*1000){
                timeout=1*1000;
            }else if(interval>1000 && interval <=2000){
                timeout=2*60*1000;
            }else if(interval>2000 && interval <=3000){
                timeout=2*60*1000;
            }else if(interval>3000 && interval <=4000){
                timeout=3*60*1000;
            }else{
                timeout=3*1000;
            }
        }

        if(flag_i>0){
            var trackPoint1=trackPoints[flag_i-1];
            var trackPoint2=trackPoints[flag_i];
            var interval =trackPoint2.time-trackPoint1.time;
            if(interval<=1*60*1000){
                timeout=1*1000;
            }else if(interval>1*60*1000 && interval <=2*60*1000){
                timeout=2*1000;
            }else if(interval>2*60*1000 && interval <=3*60*1000){
                timeout=2*1000;
            }else if(interval>3*60*1000 && interval <=4*60*1000){
                timeout=3*1000;
            }else{
                timeout=3*1000;
            }
            var p1 = new VPoint(trackPoint1.x,trackPoint1.y,floor);
            var p2 = new VPoint(trackPoint2.x,trackPoint2.y,floor);
            drawTrack(p1,p2);
        }
        //drawTrack(p1,p2);
        flag_i++;
        setTimeout(function(){draw(trackPoints)},timeout);
    }
    var drawTrack = function(p1,p2){
        var points = [];
        var vmaker = new VMarker(p2,"<%=path%>/images/marker.png");
        map.addOverlay(vmaker);
        points.push(p1);
        points.push(p2);
        var line = new VPolyline(points);
        map.addOverlay(line);
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
    $('.chosen-select').chosen({width: "100%"});
</script>
</body>
</html>
