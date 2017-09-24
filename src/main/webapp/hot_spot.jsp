<%--
  Created by IntelliJ IDEA.
  User: cxl
  Date: 2017/4/6
  Time: 9:46
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

    <title>热力分布</title>

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
        #setviewtime{margin-bottom: 0;}
        .ibox-content{padding: 15px 20px 0px 20px;}
        #timeshow{padding-top: 7px;  margin-bottom: 0;  text-align: right;}
    </style>
</head>
<body>
<div id="wrapper">
    <div id="mapFont">1</div>
    <!-- 包含菜单页面 James-->
    <jsp:include page="inc_side_bar.jsp" flush="true"/>
    <div id="page-wrapper" class="gray-bg dashbard-1">
        <!-- 包含顶部菜单 James-->
        <jsp:include page="inc_top_bar.jsp" flush="true"/>
        <%--<div class="col-lg-12">--%>
        <div class="row">
            <div class="col-lg-12">
                <div id="setviewtime" class="ibox float-e-margins">
                    <div class="ibox-content">
                        <div class="row">
                            <div class="col-md-2">
                                <form method="get" class="form-horizontal">
                                    <div class="form-group">
                                            <select class="form-control m-b" id="history" onchange="ChooseWay()">
                                                <option value="2">历史回顾</option>
                                                <option value="1">历史统览</option>
                                            </select>
                                    </div>
                                </form>
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
                                <button type="button" class="btn btn-primary" onclick="GetHistory()">查看</button>
                            </div>
                            <div class="col-md-2">
                                <h3 id="timeshow"></h3>
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
    var floor = $("#floor").val();
    var checkTimer;
    var wholeTimer;
    window.onload = function() {
        var mapDiv = document.querySelector("#map");
        map= new Vmap(mapDiv,building,floor, {fontColor:"blue",publicColor:"green"});
        map.changeFloor(floor);
    }
    var callback=function(){
    }
    var replaceFloor = function(){
        floor = $("#floor").val();
        //map.changeFloorCallBack(floor,GetHistory);
        map.changeFloor(floor);

    }
    var ChooseWay = function(){
        var way = document.all['history'].value;
        if(way==1)
            viewALLBack();
        else if(way==2)
            viewBack();
    }
    var clearTimer = function(){
        clearInterval(checkTimer);
        clearInterval(wholeTimer);
    }
    /*热力图显示统揽(优化)*/
    var cccLongTime = function(startTime,endTime){
        map.clearOverlays();
        var type = $('#history').attr("type");
        var heatMapInstance = h337.create({
            container:document.querySelector('#map'),
            //radius:45
            radius:35
        });
        $.ajax({
            type: "POST",
            url: "<%=path%>/api/heatPoint/getHeatPointsByTimeRangeLongTime/",
            data: {startTime:startTime, endTime:endTime, floor:$("#floor").val() },
            success :function(response){
                var heatPoints = response["list"];
                var points = [];
                var max = 0;
                for(var i=0;i<heatPoints.length;i++){
                    var val = 0;
                    var count = 0;
                    var heatPoint = heatPoints[i]
                    if(type=="2"){
                        val = heatPoint["count"]*12.5+20;//历史回顾，+20效果更明显
                    }else{
                        val = heatPoint["count"]*12.5;
                        //val = heatPoint["count"] * 50;
                    }
                    count = heatPoint["count"];
                    max = Math.max(max,val);
                    var point ={
                        x: Math.floor(heatPoint["x"] * map.getDelta()),
                        y: Math.floor(heatPoint["y"] * map.getDelta()),
                        value:val,
                        count:count
                    };
                    points.push(point);
                }
                var data = {
                    max: max==0?100:max,
                    data:points
                };
                heatMapInstance.setData(data);
                var canvas1 = document.getElementById("floorCanvas");
                var canvas2 = $('.heatmap-canvas')[0];
                canvas1.getContext("2d").drawImage(canvas2, 0, 0);
                canvas2.remove();
            }
        });
    }
    /*热力图显示回顾(优化)*/
    var cccLookBack = function(startTime,endTime){
        map.clearOverlays();
        var type = $('#history').attr("type");
        var heatMapInstance = h337.create({
            container:document.querySelector('#map'),
            radius:35
            //radius:25
        });
        $.ajax({
            type: "POST",
            url: "<%=path%>/api/heatPoint/getHeatPointsByTimeRange/",
            data: {startTime:startTime, endTime:endTime, floor:$("#floor").val() },
            success :function(response){
                var heatPoints = response["list"];
                var points = [];
                var max = 0;
                for(var i=0;i<heatPoints.length;i++){
                    var val = 0;
                    var count = 0;
                    var heatPoint = heatPoints[i]
                    if(type=="2"){
                        val = heatPoint["count"]*12.5+20;//历史回顾，+20效果更明显
                    }else{
                        val = heatPoint["count"]*12.5;
                    }
                    count = heatPoint["count"];
                    max = Math.max(max,val);
                    var point ={
                        x: Math.floor(heatPoint["x"] * map.getDelta()),
                        y: Math.floor(heatPoint["y"] * map.getDelta()),
                        value:val,
                        count:count
                    };
                    points.push(point);
                }
                var data = {
                    max: max==0?100:max,
                    data:points
                };
                heatMapInstance.setData(data);
                var canvas1 = document.getElementById("floorCanvas");
                var canvas2 = $('.heatmap-canvas')[0];
                canvas1.getContext("2d").drawImage(canvas2, 0, 0);
                canvas2.remove();
            }
        });
    }
    var viewBack = function(){
        clearTimer()
        if($('#history').attr("type")=="2"){//历史回顾
            $('#StartTime').val("");
            $('#StopTime').val("");
        }
        $('#history').attr("type","2")
        $('#history').show();
        $('#timeshow').show();
    }
    var viewALLBack = function(){
        clearTimer()
        if($('#history').attr("type")=="1"){//历史统览
            $('#StartTime').val("");
            $('#StopTime').val("");
        }
        $('#history').attr("type","1");
        $('#history').show();
        $('#timeshow').hide();
    }
    var GetHistory = function(){
        clearTimer();
        var type = $('#history').attr("type");
        var sTime=$('#StartTime').val();
        var stopTime = $('#StopTime').val();
        sTime = new Date(sTime.replace(/-/g,"/"));
        stopTime = new Date(stopTime.replace(/-/g,"/"));

        if(type=="1"){//历史统览
            var startTime = sTime;
            var endTime = stopTime;
            //bbb (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            cccLongTime(startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));

        }else{
            var startTime = sTime;
            var interval = 2*60*1000;
            var endTime = new Date(Math.round(startTime.getTime()+interval));
            $('#timeshow').text(startTime.format('yyyy-MM-dd hh:mm'));
            //bbb (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            cccLookBack(startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));

            wholeTimer = setInterval(function() {
                if(startTime > stopTime) {
                    //startTime=sTime;
                    clearInterval(wholeTimer);
                    alert("回放结束");
                } else {
                    var endTime = new Date(Math.round(startTime.getTime() + interval));
                    $('#timeshow').text(startTime.format('yyyy-MM-dd hh:mm'));
                    cccLookBack(startTime.format('yyyy-MM-dd hh:mm:ss'), endTime.format('yyyy-MM-dd hh:mm:ss'));
                    startTime = endTime;
                }
            }, 3*1000);

//            while (startTime <= stopTime) {
//                wholeTimer = setTimeout(function() {
//                    var endTime = new Date(Math.round(startTime.getTime() + interval));
//                    $('#timeshow').text(startTime.format('yyyy-MM-dd hh:mm'));
//                    cccLookBack(startTime.format('yyyy-MM-dd hh:mm:ss'), endTime.format('yyyy-MM-dd hh:mm:ss'));
//                    startTime = endTime;
//                }, 3 * 1000);
//            }
//            alert("回放结束");
        }
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
