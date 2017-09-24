<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <title>Dashboard</title>
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
        .wrapper-count{
            color: #1ab394;
        }
        .ibox-content{
            padding: 50px 20px 40px 20px;
        }
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

        <div class="row">
            <div class="col-lg-12">
                <div class="wrapper wrapper-content">
                    <div class="row">
                        <div class="col-lg-4" id="left-block">
                            <%--显示访问人数--%>
                            <div class="ibox float-e-margins">
                                <div class="ibox-title">
                                    <%--<span class="label label-warning pull-right">数据已更新</span>--%>
                                    <h3>访问人数</h3>
                                </div>
                                <div class="ibox-content">
                                    <div class="row">
                                        <div class="col-xs-6">
                                            <h2 class="stats-label">当前参观人数</h2>
                                            <h1 class="wrapper-count">${currentCount}</h1>
                                        </div>

                                        <div class="col-xs-6">
                                            <h2 class="stats-label">近1小时参观人数</h2>
                                            <h1 class="wrapper-count">${latestHourCount}</h1>
                                        </div>
                                    </div>
                                </div>
                                <div class="ibox-content">
                                    <div class="row">
                                        <div class="col-xs-6">
                                            <h2 class="stats-label">今日参观人数</h2>
                                            <h1 class="wrapper-count">${todayCount}</h1>
                                        </div>

                                        <div class="col-xs-6">
                                            <h2 class="stats-label">累计访问人数</h2>
                                            <h1 class="wrapper-count">${allCount}</h1>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <%--显示深度洞悉 --%>
                            <div class="ibox float-e-margins">
                                <div class="ibox-title">
                                    <h3>深度洞悉</h3>
                                </div>
                                <div class="ibox-content">
                                    <div>
                                        <canvas id="radarChart"></canvas>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <%--热力图显示--%>
                        <div class="col-lg-8">
                            <div class="ibox float-e-margins">
                                <div id="map-container" class="ibox-content ibox" style="position: relative;height: 1000px;overflow: hidden">
                                    <%--楼层选择--%>
                                    <form method="get" class="form-horizontal">
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">请选择</label>
                                            <div class="col-sm-3">
                                                <select cla ss="form-control m-b" id="floor">
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
<!-- Jvectormap -->
<script src="<%=path%>/ui/js/plugins/jvectormap/jquery-jvectormap-2.0.2.min.js"></script>
<script src="<%=path%>/ui/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>
<!-- jQuery UI -->
<script src="<%=path%>/ui/js/plugins/jquery-ui/jquery-ui.min.js"></script>
<!-- ChartJS-->
<script src="<%=path%>/ui/js/plugins/chartJs/Chart.min.js"></script>
<%--HeatmapJS--%>
<script src="<%=path%>/Downloads/heatmap.js"></script>
<%--VMap--%>
<script src="<%=path%>/Downloads/SDKjs-release/zepto-5a31069312.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/blockUI-c22b98bace.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/hammer-58b95c1ece.js"></script>
<script src="<%=path%>/Downloads/slave.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/VMap.js"></script>
<script src="<%=path%>/Downloads//DatePicker/WdatePicker.js" type="text/javascript"></script>
<script>
    $('#floor option:eq(2)').attr('selected','selected');
    var map;
    var building  ="E9F6A2DE-EADC-45AF-A42E-C7458A401339";
    var floor =$("#floor").val();
    var checkTimer;
    var wholeTimer;

    var setDeep = function(list) {
        var spotName = new Array();
        var data = new Array();
        for(var i =0;i<list.length;i++){
            spotName.push(list[i].category);
            data.push(list[i].count);
        }
        var radarData = {
            labels: spotName,
            datasets: [
                {
                    label: "访问分布",
                    backgroundColor: "rgba(26,179,148,0.2)",
                    borderColor: "rgba(26,179,148,1)",
                    data: data
                }
            ]
        };

        var radarOptions = {
            responsive: true
        };

        var ctx5 = document.getElementById("radarChart").getContext("2d");
        new Chart(ctx5, {type: 'radar', data: radarData, options: radarOptions});
        $('#map-container').css("height",$('#left-block').outerHeight()-25)
    }
    window.onload = function() {
        $('#map-container').css("height",$('#left-block').outerHeight()-25);
        var mapDiv = document.querySelector("#map");
        map= new Vmap(mapDiv,building,floor, {fontColor:"blue",publicColor:"green"});

        map.changeFloorCallBack(floor,wholeApercption);
        $.ajax({
            type:"POST",
            url:"<%=path%>/api//getExhibitionCategory",
            success:function(data) {
                //显示雷达图
                setDeep(data["list"]);
            }
        });
        //wholeApercption();
    }
    var replaceFloor = function(){
        floor = $("#floor").val();
        map.changeFloorCallBack(floor,wholeApercption);
    }
    var wholeApercption = function() {
        clearTimer();
        //$('#setviewtime').hide();
        var endTime =new Date   (new Date().getTime());
        var startTime = new Date(new Date().getTime() - 30*60*1000);
        var interval = 30*60*1000;
        //var endTime =new Date(Math.round(startTime.getTime()+interval));
        ccc(startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
        wholeTimer = setInterval(function(){
            var endTime =new Date(Math.round(startTime.getTime()+interval));
            ccc(startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            startTime = endTime;
        },10*60*1000);
    }


    var clearTimer = function(){
        clearInterval(checkTimer);
        clearInterval(wholeTimer);
    }
    /*热力图显示(优化)*/
    var ccc = function(startTime,endTime){
        map.clearOverlays();
        var heatMapInstance = h337.create({
            container:document.querySelector('#map'),
            //radius:45
            radius:35
        });
        $.ajax({
            type: "POST",
            url: "<%=path%>/api/heatPoint/getHeatPointsByTimeRangeLongTime/",
            data: {startTime:startTime,endTime:endTime,floor:$("#floor").val()},
            success :function(response){
                var heatPoints = response["list"];
                var points = [];
                var max = 0;
                for(var i=0;i<heatPoints.length;i++){
                    var val = 0;
                    var count = 0;
                    var heatPoint = heatPoints[i];
                    val = heatPoint["count"]*12.5;
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