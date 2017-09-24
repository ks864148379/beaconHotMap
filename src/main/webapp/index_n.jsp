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

    <title>首页仪表盘</title>

    <link href="<%=path%>/ui/css/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path%>/ui/font-awesome/css/font-awesome.css" rel="stylesheet">

    <!-- Morris -->
    <link href="<%=path%>/ui/css/plugins/morris/morris-0.4.3.min.css" rel="stylesheet">

    <link href="<%=path%>/ui/css/animate.css" rel="stylesheet">
    <link href="<%=path%>/ui/css/style.css" rel="stylesheet">
    <style>
        body{
            font-family: "Arial";
        }
        @font-face {font-family : 'VMapPublic';src : url(<%=path%>/Downloads/VMapPublic.ttf)format('truetype');}
        #mapFont{
            position: absolute;
            top:0;
            z-index: -99;
            font-family: 'VMapPublic',serif;
        }
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

        <div class="wrapper wrapper-content">
            <div class="row">
                <div class="col-lg-2">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <span class="label label-success pull-right">人数</span>
                            <h5>当天观众</h5>
                        </div>
                        <div class="ibox-content">
                            <h1 class="no-margins" id="today_person">0</h1>
                            <div class="stat-percent font-bold text-success">到访率 98% <i class="fa fa-bolt"></i></div>
                            <small>总人数</small>
                        </div>
                    </div>
                </div>
                <div class="col-lg-2">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <span class="label label-info pull-right">人次</span>
                            <h5>当天数量</h5>
                        </div>
                        <div class="ibox-content">
                            <h1 class="no-margins" id="today_times">0</h1>
                            <div class="stat-percent font-bold text-info">20% <i class="fa fa-level-up"></i></div>
                            <small>比上一日</small>
                        </div>
                    </div>
                </div>
                <div class="col-lg-2">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <span class="label label-warning pull-right">VIP</span>
                            <h5>专业观众</h5>
                        </div>
                        <div class="ibox-content">
                            <h1 class="no-margins" id="today_vip">0</h1>
                            <small>总人数</small>
                        </div>
                    </div>
                </div>
                <div class="col-lg-2">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <span class="label label-danger pull-right">普通</span>
                            <h5>普通观众</h5>
                        </div>
                        <div class="ibox-content">
                            <h1 class="no-margins" id="today_normal">0</h1>
                            <small>总人数</small>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <span class="label label-primary pull-right"><span id="count_from">0000-00-00</span> 开始统计</span>
                            <h5>访问总量</h5>
                        </div>
                        <div class="ibox-content">

                            <div class="row">
                                <div class="col-md-6">
                                    <h1 class="no-margins" id="total_person">0</h1>
                                    <div class="font-bold text-navy"><small>总人数</small></div>
                                </div>
                                <div class="col-md-6">
                                    <h1 class="no-margins" id="total_times">0</h1>
                                    <div class="font-bold text-navy"><small>总人次</small></div>
                                 </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-8">
                    <div class="ibox float-e-margins">
                        <div class="ibox-content" id="left-block">
                        <div>
                            <span class="pull-right text-right">
                                <small>
                                    <i class="fa fa-clock-o"> </i> <span id="update_time">2017-01-01 00:00:00</span> 更新
                                </small>
                            </span>
                            <h3 class="font-bold no-margins">
                                分时段流量统计
                            </h3>
                        </div>
                        <div class="m-t-sm">
                            <div class="row">
                                <div class="col-md-8">
                                    <div>
                                        <canvas id="lineChart" height="114"></canvas>
                                    </div>
                                </div>
                                <div class="col-md-4">
                                    <div class="row" style="margin-top: 40px;">
                                        <div class="col-xs-4">
                                            <small class="stats-label">人数</small>
                                            <h4 id="hour_person">0</h4>
                                        </div>
                                        <div class="col-xs-4">
                                            <small class="stats-label">人次</small>
                                            <h4 id="hour_times">0</h4>
                                        </div>
                                        <div class="col-xs-4">
                                            <small class="stats-label">时间</small>
                                            <h4 class="text-info" id="hour_from_to">0:00-0:00</h4>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-4">
                                            <small class="stats-label">VIP人数</small>
                                            <h4 id="vip_person">0</h4>
                                        </div>
                                        <div class="col-xs-4">
                                            <small class="stats-label">VIP人次</small>
                                            <h4 id="vip_times">0</h4>
                                        </div>
                                        <div class="col-xs-4">
                                            <small class="stats-label">时间</small>
                                            <h4 class="text-info" id="vip_from_to">0:00-0:00</h4>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-xs-4">
                                            <small class="stats-label">普通观众人数</small>
                                            <h4 id="normal_person">0</h4>
                                        </div>
                                        <div class="col-xs-4">
                                            <small class="stats-label">普通观众人次</small>
                                            <h4 id="normal_times">0</h4>
                                        </div>
                                        <div class="col-xs-4">
                                            <small class="stats-label">时间</small>
                                            <h4 class="text-info" id="normal_from_to">0:00-0:00</h4>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title" id="map_title">
                            <h5>会场热力图</h5>
                            <div class="ibox-tools">
                                <a class="btn-link" href="javascript:map.setZoomScale(0.34)">
                                    <i class="fa fa-arrows"></i>
                                </a>
                                <a class="btn-link" href="javascript:map.zoomOut()">
                                    <i class="fa fa-minus-square"></i>
                                </a>
                                <a class="btn-link" href="javascript:map.zoomIn()">
                                    <i class="fa fa-plus-square"></i>
                                </a>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    <i class="fa fa-bars text-danger"></i>
                                </a>
                                <ul class="dropdown-menu dropdown-user" id="floor">
                                    <c:forEach items="${floors}" var="floor">
                                        <li><a href="javascript:replaceFloor('${floor.floor_id}')">${floor.floor_chn}</a></li>
                                    </c:forEach>
                                </ul>
                            </div>
                        </div>
                        <div class="ibox-content" id="map_container" style="position: relative;height: 300px;overflow: hidden">
                            <div id="map"></div>
                        </div>
                     </div>
                </div>
            </div>
            <div class="row">
                <div class="col-lg-4">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>总体人数分类统计</h5>
                            <div class="ibox-tools">
                                <a class="collapse-link">
                                    <i class="fa fa-chevron-up"></i>
                                </a>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    <i class="fa fa-wrench"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div id="morris-donut-chart-1" style="height: 200px;"></div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>区域人数统计</h5>
                            <div class="ibox-tools">
                                <a class="collapse-link">
                                    <i class="fa fa-chevron-up"></i>
                                </a>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    <i class="fa fa-wrench"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div id="morris-donut-chart-2" style="height: 200px;"></div>
                        </div>
                    </div>
                </div>
                <div class="col-lg-4">
                    <div class="ibox float-e-margins">
                        <div class="ibox-title">
                            <h5>观众洞悉</h5>
                            <div class="ibox-tools">
                                <a class="collapse-link">
                                    <i class="fa fa-chevron-up"></i>
                                </a>
                                <a class="dropdown-toggle" data-toggle="dropdown" href="#">
                                    <i class="fa fa-wrench"></i>
                                </a>
                            </div>
                        </div>
                        <div class="ibox-content">
                            <div id="morris-bar-chart" style="height: 200px;"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <jsp:include page="inc_footer_bar.jsp" flush="true"/>
    </div>
</div>

<!-- Mainly scripts -->
<script src="<%=path%>/ui/js/jquery-3.1.1.min.js"></script>
<script src="<%=path%>/ui/js/bootstrap.min.js"></script>
<script src="<%=path%>/ui/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="<%=path%>/ui/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<!-- Morris -->
<script src="<%=path%>/ui/js/plugins/morris/raphael-2.1.0.min.js"></script>
<script src="<%=path%>/ui/js/plugins/morris/morris.js"></script>
<!-- Flot -->
<script src="<%=path%>/ui/js/plugins/flot/jquery.flot.js"></script>
<script src="<%=path%>/ui/js/plugins/flot/jquery.flot.tooltip.min.js"></script>
<script src="<%=path%>/ui/js/plugins/flot/jquery.flot.spline.js"></script>
<script src="<%=path%>/ui/js/plugins/flot/jquery.flot.resize.js"></script>
<script src="<%=path%>/ui/js/plugins/flot/jquery.flot.pie.js"></script>
<script src="<%=path%>/ui/js/plugins/flot/jquery.flot.symbol.js"></script>
<script src="<%=path%>/ui/js/plugins/flot/curvedLines.js"></script>

<!-- Peity -->
<script src="<%=path%>/ui/js/plugins/peity/jquery.peity.min.js"></script>
<script src="<%=path%>/ui/js/demo/peity-demo.js"></script>

<!-- Custom and plugin javascript -->
<script src="<%=path%>/ui/js/inspinia.js"></script>
<script src="<%=path%>/ui/js/plugins/pace/pace.min.js"></script>

<!-- jQuery UI -->
<script src="<%=path%>/ui/js/plugins/jquery-ui/jquery-ui.min.js"></script>

<!-- Jvectormap -->
<script src="<%=path%>/ui/js/plugins/jvectormap/jquery-jvectormap-2.0.2.min.js"></script>
<script src="<%=path%>/ui/js/plugins/jvectormap/jquery-jvectormap-world-mill-en.js"></script>

<!-- Sparkline -->
<script src="<%=path%>/ui/js/plugins/sparkline/jquery.sparkline.min.js"></script>

<!-- Sparkline demo data  -->
<script src="<%=path%>/ui/js/demo/sparkline-demo.js"></script>

<!-- ChartJS-->
<script src="<%=path%>/ui/js/plugins/chartJs/Chart.min.js"></script>

<script src="<%=path%>/ui/js/plugins/animateNumber/jquery.animateNumber.min.js"></script>

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
    $(document).ready(function() {
        var lineData = {
            labels: ["9:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00"],
            datasets: [
                {
                    label: "人数",
                    backgroundColor: "rgba(26,179,148,0.5)",
                    borderColor: "rgba(26,179,148,0.7)",
                    pointBackgroundColor: "rgba(26,179,148,1)",
                    pointBorderColor: "#fff",
                    data: []
                },
                {
                    label: "人次",
                    backgroundColor: "rgba(220,220,220,0.5)",
                    borderColor: "rgba(220,220,220,1)",
                    pointBackgroundColor: "rgba(220,220,220,1)",
                    pointBorderColor: "#fff",
                    data: []
                }
            ]
        };

        var lineOptions = {
            responsive: true
        };

        var donut1 = Morris.Donut({
            element: 'morris-donut-chart-1',
            data: [{ label: "普通观众", value: 100 }],
            resize: true,
            colors: ['#87d6c6', '#54cdb4','#1ab394'],
        });

        var donut2 = Morris.Donut({
            element: 'morris-donut-chart-2',
            data: [{ label: "展位区", value: 100 }],
            resize: true,
            colors: ['#d178e2', '#1ab394','#8395ee'],
        });

        var barChart = Morris.Bar({
            element: 'morris-bar-chart',
            data: [{ label: '无人机', value: 60}],
            xkey: 'label',
            ykeys: ['value'],
            labels: ['人数'],
            hideHover: 'auto',
            resize: true,
            barColors: ['#1ab394'],
        });

        var aniNum = function(obj, num, timeout){
            setTimeout(function(){
                obj.prop('number', Number(obj.html())).animateNumber({ number: Number(num)});
            },timeout);
        }

        var updateDayCount = function () {
            $.ajax({
                type: "GET",
                url: "<%=path%>/api/infocount/getDayCount",
                success: function(response){
                    if(response.success){

                        aniNum($('#today_person'), response.today_person, 200);
                        aniNum($('#today_times'), response.today_times, 400);
                        aniNum($('#today_vip'), response.today_vip, 600);
                        aniNum($('#today_normal'), response.today_normal, 800);
                        aniNum($('#total_person'), response.total_person, 1000);
                        aniNum($('#total_times'), response.total_times, 1200);

                        $("#count_from").html(response.count_from);
                    }
                }
            });
        }

        var updateHourCount = function () {
            $.ajax({
                type: "GET",
                url: "<%=path%>/api/infocount/getHourCount",
                success: function(response){
                    if(response.success){
                        setTimeout(function(){$('#hour_from_to').html(response.hour_from_to);}, 200);
                        aniNum($('#hour_person'), response.hour_person, 200);
                        aniNum($('#hour_times'), response.hour_times, 400);
                        setTimeout(function(){$('#vip_from_to').html(response.vip_from_to);}, 600);
                        aniNum($('#vip_person'), response.vip_person, 600);
                        aniNum($('#vip_times'), response.vip_times, 800);
                        setTimeout(function(){$('#normal_from_to').html(response.normal_from_to);}, 1000);
                        aniNum($('#normal_person'), response.normal_person, 1000);
                        aniNum($('#normal_times'), response.normal_times, 1200);

                        $("#update_time").html(response.update_time);

                        lineData.datasets[0].data = response.person_list;
                        lineData.datasets[1].data = response.times_list;
                        var ctx = document.getElementById("lineChart").getContext("2d");
                        new Chart(ctx, {type: 'line', data: lineData, options:lineOptions});
                    }
                }
            });
        }

        var updateNonut1 = function () {
            $.ajax({
                type: "GET",
                dataType: 'json',
                url: "<%=path%>/api/infocount/getDonutChartData1",
                success: function(response){
                    if(response.success){
                        donut1.setData(response.data);
                    }
                }
            });
        }

        var updateNonut2 = function () {
            $.ajax({
                type: "GET",
                dataType: 'json',
                url: "<%=path%>/api/infocount/getDonutChartData2",
                success: function(response){
                    if(response.success){
                        donut2.setData(response.data);
                    }
                }
            });
        }

        var updateBarCart = function () {
            $.ajax({
                type: "GET",
                dataType: 'json',
                url: "<%=path%>/api/infocount/getBarChartData",
                success: function(response){
                    if(response.success){
                        barChart.setData(response.data);
                    }
                }
            });
        }

        updateDayCount();
        setInterval(updateDayCount, 2000);
        updateHourCount();
        setInterval(updateHourCount, 10000);
        updateNonut1();
        setInterval(updateNonut1, 10000);
        updateNonut2();
        setInterval(updateNonut2, 10000);
        updateBarCart();
        setInterval(updateBarCart, 10000);
    });
</script>
<script>
//    $('#floor li:eq(2)').attr('selected','selected');
    var map;
    var building  ="E9F6A2DE-EADC-45AF-A42E-C7458A401339";
    var floor = "${floors.get(2).floor_id}";
    var checkTimer;
    var wholeTimer;

    window.onload = function() {
//        console.info(floor);
        var mapDiv = document.querySelector("#map");
        map= new Vmap(mapDiv, building, floor, {fontColor:"white",publicColor:"#D0404C",publicSize:45,initScale:0.40});
        map.changeFloorCallBack(floor,wholeApercption);
    }

    var replaceFloor = function(floor){
        map.changeFloorCallBack(floor,wholeApercption);
    }
    var wholeApercption = function() {
        clearTimer();
        //$('#setviewtime').hide();
        var endTime =new Date(new Date().getTime());
        var startTime = new Date(new Date().getTime() - 30*60*1000);
        var interval = 30*60*1000;
        //var endTime =new Date(Math.round(startTime.getTime()+interval));
        ccc(startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
        wholeTimer = setInterval(function(){
            var endTime =new Date(Math.round(startTime.getTime()+interval));
            ccc(startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            startTime = endTime;
        },10*60*1000);
        map.setZoomScale(0.40);
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
            radius:45
        });
        $.ajax({
            type: "POST",
            url: "<%=path%>/api/heatPoint/getHeatPointsByTimeRange/",
            data: {startTime:startTime,endTime:endTime},
            success :function(response){
                var heatPoints = response["list"];
                var points = [];
                var max = 0;
                for(var i=0;i<heatPoints.length;i++){
                    var val = 0;
                    var count = 0;
                    var heatPoint = heatPoints[i]
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
                map.setZoomScale(0.40);
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
    //延时设定地图高度
    setTimeout(function(){
        var map_height = $('#left-block').outerHeight() - $('#map_title').outerHeight();
        $('#map_container').css("height", map_height+"px");
        map.setZoomScale(0.40);
    },3000);
</script>
</body>
</html>
