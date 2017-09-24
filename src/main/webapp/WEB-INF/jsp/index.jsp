<%--
  Created by IntelliJ IDEA.
  User: linhanda
  Date: 2016/10/13
  Time: 下午8:37
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1.0,user-scalable=no">

    <link rel="stylesheet" href="<%=path%>/Downloads/unicorn.main.css">
    <%--<link rel="stylesheet" href="${pagecontext.request.contextpath}/Downloads/bootstrap.min.css">--%>

    <title>展会信息</title>
    <style>
        *{margin:0;padding:0}
        body{position:absolute;width:100%;height:100%}
        #btnGroup{position:fixed;top:20px;z-index:10}
        #btnGroup button{display:block;margin-bottom:5px;font-size:20px}
        @font-face{font-family:VMapPublic;src:url(<%=path%>/Downloads/VMapPublic.ttf)}
        #map{font-family:VMapPublic,serif}
        .tab{display:flex;width:500px;list-style:none}
        .tab li{height:50px;border:1px solid #ccc;flex:1}
        .guest-index{padding:0 10px;height:40px;color:#fff;line-height:40px}
        .wrapper{position:relative;float:left;margin:0 auto;width:190px;height:5pc;color:#fff}
        .wrapper p:first-child{margin:0 auto;height:30px;color:#fff;text-align:center;line-height:30px}
        .wrapper p:last-child{margin:0 auto;height:50px;color:#fff;text-align:center;line-height:50px}
        .label-container{background-color:#3993ba;padding: 20px;box-sizing: border-box;}

        .wrapper-block{width: 100%;color: #fff;display: flex;margin-top: 60px;}
        .wrapper-title{flex:8;font-size: 30px;float: left;}
        .wrapper-count{flex:3;font-size: 30px;float: left;}

        .echat1,.echat2,.label-container{width:380px;height:300px}
        .echat2,.echat3{margin:0}
        .echat3{width:380px;height:380px}
        .echat{margin:10px auto}

        @media(min-width:1400px){.block-1{left:0;float:left}
            .block-1,.block-2{position:absolute;top:0;box-sizing:border-box;padding:10px;width:25pc}
            .block-2{right:0;float:right}
            #out-frame{position:relative;overflow:hidden;margin:0 auto;width:calc(100% - 50pc);height:80%}
        }
        @media(max-width:1399px){.block-1{position:absolute;top:0;left:0;width:25pc}
            .block-1,.block-2{float:left;box-sizing:border-box;padding:10px}
            .block-2{margin-top:30px;width:100%}
            .block-2 .echat{float:left;margin-right:75pt}
            #out-frame{position:relative;overflow:hidden;margin-left:25pc;width:calc(100% - 25pc);height:80%}
        }
        @media(max-width:900px){.block-1{position:absolute;top:0;left:0;width:25pc}
            .block-1,.block-2{float:left;box-sizing:border-box;padding:10px}
            .block-2{margin-top:30px;width:100%}
            .block-2 .echat{float:left}
            #out-frame{position:relative;overflow:hidden;margin-left:25pc;width:calc(100% - 25pc);height:80%}
        }
        .setviewtime{display:none;padding-left:140px;width:100%;height:50px;background:#fff;padding-top: 20px}
        .setviewtime input[type=text]{margin-right:30px;padding:5px;width:150px;height:20px;border:1px solid #ccc;border-radius:4px;line-height:20px}
        .confirm-btn{width:5pc;height:35px;border:0;border-radius:4px;background:#3498db;color:#fff;cursor: pointer;}
        .header{height: 190px;width: 100%;position: relative;}
        .banner{background: url("<%=path%>/Downloads/images/logo1.png") no-repeat;background-size:100% 100%;height: 150px;}
        .menu{width:100%;position: absolute;bottom: 0;right: 0;background: #34495e;}
        .menu-list{display:flex;float:right; width:50%;list-style:none}
        .menu-list>li{padding-top:9pt;width:5pc;height:30px;background:#34495e;color:#fff;flex:1}
        .menu-list>li a{color:#fff;text-decoration:none}
        .menu-bg,.menu-list>li.active{background:#2d3e4e}
        .menu-bg{width:100%;height:50px}
        .menu-list2{display:none;margin-top:8px;width:100%;list-style:none;z-index: 9;position: relative;}
        .menu-list2>li{padding-top:9pt;width:100%;height:30px;background:#34495e;cursor:pointer}
        .menu-list>li:hover .menu-list2{display:block}
        .menu-list li:hover{background:#2d3e4e}

    </style>
</head>
<body>
    <div class="header">
        <div class="banner"></div>
        <div  style="text-align: center;" class="menu">
            <ul class="menu-list">
                <li class="active" onclick="checkBroadcastStations()">
                    <a href="#">
                        <span>基站监控</span>
                    </a>
                </li>
                <li>
                    <span>全场统览</span>
                    <ul class="menu-list2">
                        <li onclick="wholeApercption()">实时数据</li>
                        <li onclick="viewBack()">历史回顾</li>
                        <li onclick="viewALLBack()">历史统览</li>
                    </ul>
                </li>
                <li onclick="ccc()">
                    <a href="shopQuery">
                        <span>专区透视</span>
                    </a>
                </li>
                <li onclick="ddd()">
                    <a href="peoples">
                        <span>动线分析</span>
                    </a>
                </li>
            </ul>
        </div>
    </div>

    <div id="setviewtime" class="setviewtime">
        开始时间：<input type="text" id="StartTime"/>
        结束时间：<input type="text" id="StopTime"/>
        <input type="button" value="确定" onclick="GetHistory()" class="confirm-btn"/>
        <span id="timeshow" style="padding:5px;"></span>
    </div>
    <div style="position: relative;height: 100%;">
        <div id="out-frame">
            <div id="map" style="height: 500px"></div>
        </div>
        <!-- 为 ECharts 准备一个具备大小（宽高）的 DOM -->
        <div class="block-1">
            <div class="label-container echat">
               <%-- <p class="guest-index" id="currentTimeRange">时段</p>--%>
                <div class="wrapper-block">
                    <p class="wrapper-title">累计访问人数:</p>
                    <p class="wrapper-count" id="firstNum">${allCount}人</p>
                </div>
                <div class="wrapper-block">
                    <p class="wrapper-title">当前总参观人数:</p>
                    <p class="wrapper-count" id="totalNum">${currentCount}人</p>
                </div>
            </div>
            <div id="main" class="echat echat1" ></div>
        </div>
        <div class="block-2">
            <div id="container" class="echat echat2"></div>
            <div id="leida-img" class="echat echat3"></div>
        </div>

    </div>
<div id="btnGroup">
    <%--<button id="changefloor3">3楼</button>--%>
    <%--<button id="changefloor1">1楼</button>--%>
    <%--<button id="getfloor">当前楼层</button>--%>
    <%--<button id="zoomIn">放大</button>--%>
    <%--<button id="zoomOut">缩小</button>--%>
    <%--<button id="line">画线</button>--%>
    <%--<button id="circle">画圆</button>--%>
    <%--<button id="clear">清除</button>--%>
    <%--<button id="enableMarkerClick">开启标记点击</button>--%>
    <%--<button id="unableMarkerClick">关闭标记点击</button>--%>
    <%--<button id="getScaleCtrl">显示比例尺</button>--%>
    <%--<button id="removeScaleCtrl">去除比例尺</button>--%>
    <%--<button id="enableMoveWithTap">开启中心移动</button>--%>
    <%--<button id="unableMoveWithTap">关闭中心移动</button>--%>
    <%--<button id="setZoomScale">设置缩放级别</button>--%>
</div>

<script src="<%=path%>/Downloads/echarts.js"></script>
<script src="<%=path%>/Downloads/heatmap.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/zepto-5a31069312.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/blockUI-c22b98bace.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/hammer-58b95c1ece.js"></script>
<%--<script src="${pagecontext.request.contextpath}/Downloads/SDKjs-release/slave-4070808c1b.js"></script>--%>
<%--<script src="${pagecontext.request.contextpath}/Downloads/SDKjs-release/VMap-d35c80c1e2.js"></script>--%>

<script src="<%=path%>/Downloads/slave.js"></script>
<script src="<%=path%>/Downloads/SDKjs-release/VMap.js"></script>
<script type="text/javascript" src="<%=path%>/Downloads//DatePicker/WdatePicker.js"></script>

<script>
    var map;
/*
    var building  ="E9F6A2DE-EADC-45AF-A42E-C7458A401339";
    var floor = "Floor9";
*/
    var building  ="dadc22ee-2303-4ea2-b260-f2ba0a8e4bc1";
    var floor = "Floor1";

    var checkTimer;
    var wholeTimer;
    var timeoutProcess ;
    var deviceInfos;
    var setRank = function(list){
        var myChart = echarts.init(document.getElementById('main'));
        var spotName = new Array();
        var data = new Array();
        for(var i =0;i<list.length&&i<=3;i++){
            spotName.push(list[i].spotName);
            //data.push(list[i].count)
            data.push(list[i].visitorCount)
        }
        // 指定图表的配置项和数据
        var option = {
            title: {
                text: '访问排名'
            },
            tooltip: {},
            legend: {
                data:['人数']
            },
            xAxis: {
                data:spotName
            },
            yAxis: {},
            series: [{
                name: '人数',
                type: 'bar',
                data: data
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
    var setDeep = function(list){
        var dom = document.getElementById("leida-img");
        var myChart = echarts.init(dom);
        var spotName = new Array();
        var max = new Array();
        var data = new Array();
        for(var i =0;i<list.length;i++){
            spotName.push(list[i].spotName);
            //data.push(list[i].count)
            data.push(list[i].visitorCount)
            max.push({name:list[i].spotName,max:800})
        }
        option = {
            title: {
                text: '深度洞悉'
            },
            tooltip: {},
            legend: {
                data: ['访问分布']
//                data: ['预算分配（Allocated Budget）', '实际开销（Actual Spending）']

            },
            radar: {
                // shape: 'circle',
                indicator: max
            },
            series: [{
//                name: '预算 vs 开销（Budget vs spending）',
                name: '访问分布',
                type: 'radar',
                // areaStyle: {normal: {}},
                data : [
                    {
                        value : data,
                        name : '访问分布'
                    }
                ]
            }]
        };
        myChart.setOption(option);
    }
    var setTodayVisitor = function(list){
        console.log(list)
        if(list.length<=0) return;
        var dom = document.getElementById("container");
        var myChart = echarts.init(dom);
        var app = {};
        option = null;
        var date = [];
        var i=0;
        var data = new Array();

        function addData(shift,da) {
            if(da!=null) {
                date.push(new Date(da.time.time).format('hh:mm'));
                data.push(da.count);
            }
            if (shift) {
                date.shift();
                data.shift();
            }
        }

        for (i = 0; i < list.length&&i<=5; i++) {
            addData(false,list[i]);
        }

        option = {
            title: {
                text: '实时人数'
            },
            xAxis: {
                type: 'category',
                boundaryGap: false,
                data: date,
                axisLabel:{
                    interval:0 //0：表示全部显示不间隔；auto:表示自动根据刻度个数和宽度自动设置间隔个数
                }
            },
            yAxis: {
                boundaryGap: [0, '50%'],
                type: 'value'
            },
            series: [
                {
                    name:'人数',
                    type:'line',
                    smooth:true,
                    symbol: 'none',
                    stack: 'a',
                    areaStyle: {
                        normal: {}
                    },
                    data: data
                }
            ]
        };

        app.timeTicket = setInterval(function () {
            if(i>=list.length) i=0;
            addData(true,list[i++]);
            myChart.setOption({
                xAxis: {
                    data: date
                },
                series: [{
                    name:'人数',
                    data: data
                }]
            });
        }, 1500);
        if (option && typeof option === "object") {
            myChart.setOption(option, true);
        }
    }
    //init
    window.onload = function() {
        $.ajax({
            type:"POST",
            url:"<%=path%>/api/beaconInfo/getDeviceInfoList/",
            data:{building:building, floor:floor},
            success:function(response1) {
                //获取所有部署到地图上的基站
                deviceInfos = response1["list"];
            }
        });
        $.ajax({
            type:"POST",
            url:"<%=path%>/api/device/getRankMapList/",
            success:function(data) {
                //获取所有部署到地图上的基站
                // 基于准备好的dom，初始化echarts实例
                setRank(data["list"])
                setDeep(data["list"]);
            }
        });
        $.ajax({
            type:"POST",
            url:"<%=path%>/api/visitor/getTodayVisitorCountList/",
            success:function(data) {
                //获取所有部署到地图上的基站
                // 基于准备好的dom，初始化echarts实例
                setTodayVisitor(data["list"])
            }
        });


        var mapDiv = document.querySelector("#map");
       map= new Vmap(mapDiv,building,floor, {fontColor:"blue",publicColor:"green",movex:140,movey:100});
//        map= new Vmap(mapDiv,"dadc22ee-2303-4ea2-b260-f2ba0a8e4bc1","Floor1", {fontColor:"blue",publicColor:"green"});
        map.changeFloor(floor);
    }
    $('.menu li').click(function(){
        $('.menu li').removeClass('active')
        $(this).addClass('active')
    })
    $("#StartTime").focus(function () {
        WdatePicker({
            dateFmt: 'yyyy-MM-dd HH:mm:ss',
            maxDate: '#F{$dp.$D(\'StopTime\')}',
            autoSize: true
        })
    })
    $("#StopTime").focus(function () {
        WdatePicker({
            dateFmt: 'yyyy-MM-dd HH:mm:ss',
            minDate: '#F{$dp.$D(\'StartTime\')}',
            autoSize: true
        })
    })



    var clearTimer = function(){
        clearInterval(checkTimer);
        clearInterval(wholeTimer);
        clearTimeout(timeoutProcess);
    }
    //event
    var checkBroadcastStations = function() {
        clearTimer()
        $('#setviewtime').hide();
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
                            pictureName = "broadcastStationOn.png";
                        }
                    }
                    var p = new VPoint(position_x,position_y,floor);
                    if (i == 0) {
                        map.clearOverlays();
                        map.changeFloor(floor);
                    }
                    var marker = new VMarker(p,"<%=path%>/images/"+pictureName);
                    map.addOverlay(marker);
                }
            }

        });
    }
    //统揽
    var wholeApercption = function() {
        clearTimer()
        $('#setviewtime').hide();
        var startTime = new Date(new Date().getTime() - 20*60*1000);
        //var startTime = new Date("2016-11-09 12:20:17");
        var interval = 10*60*1000;
        var endTime =new Date(Math.round(startTime.getTime()+interval));
        //bbb (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
        ccc (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
        wholeTimer = setInterval(function(){
            var endTime =new Date(Math.round(startTime.getTime()+interval));
            //bbb (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            ccc (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            startTime = endTime;
        },10*60*1000);
    }
    var viewBack = function(){
        clearTimer()
        if($('#setviewtime').attr("type")=="2"){
            $('#StartTime').val("");
            $('#StopTime').val("");
        }
        $('#setviewtime').attr("type","1")
        $('#setviewtime').show();
    }
    var viewALLBack = function(){
        clearTimer()
        if($('#setviewtime').attr("type")=="1"){
            $('#StartTime').val("");
            $('#StopTime').val("");
        }
        $('#setviewtime').attr("type","2")
        $('#setviewtime').show();
    }

    var GetHistory = function(){
        map.clearOverlays();
        clearTimer();
        clearTimeout(timeoutProcess);
        var type = $('#setviewtime').attr("type");
        var sTime=$('#StartTime').val();
        var stopTime = $('#StopTime').val();
        sTime = new Date(sTime.replace(/-/g,"/"));
        stopTime = new Date(stopTime.replace(/-/g,"/"));

        if(type=="1"){
            var startTime =  sTime;
            var interval = 2*60*1000;
            var endTime =new Date(Math.round(startTime.getTime()+interval));
            $('#timeshow').text(startTime.format('yyyy-MM-dd hh:mm'));
            //bbb (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            ccc (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            wholeTimer = setInterval(function(){
                if(startTime >stopTime) {startTime=sTime;alert("回放结束");}
                var endTime =new Date(Math.round(startTime.getTime()+interval));
                $('#timeshow').text(startTime.format('yyyy-MM-dd hh:mm'));
                //bbb (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
                ccc (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
                startTime =endTime;
            },3*1000);
        }else{
            var startTime =  sTime;
            var endTime = stopTime;
            //bbb (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            //ccc (startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            //ddd(startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
            eee(startTime.format('yyyy-MM-dd hh:mm:ss'),endTime.format('yyyy-MM-dd hh:mm:ss'));
        }
    }

    var bbb = function(startTime,endTime) {
        map.clearOverlays();
        var heatmapInstance = h337.create({
            // only container is required, the rest will be defaults
            container: document.querySelector('#map'),
            radius:30
        });

        console.log(startTime,endTime);

        //document.getElementById('currentTimeRange').innerHTML = '时段：'+startTime+' ——'+endTime.substring(11,19);
//        var startTime = "2016-10-23 19:15:00";
//        var endTime = "2016-10-23 19:15:10";

        $.ajax({
            type: "POST",
            url: "<%=path%>/api/beaconInfo/getInfoByTimeRange/",
            data: {startTime: startTime, endTime: endTime},
            success: function (response) {
                var beanconInfos = response["list"];
                var names = new Array();
                for (var i = 0; i < beanconInfos.length; i++) {
                    var beacon = beanconInfos[i];
                    var isResist = false;
                    for (var j = 0; j < names.length; j++) {
                        var mac_id = names[j];
                        if (mac_id == beacon["mac_id"]) {
                            isResist = true;
                        }
                    }
                    if (!isResist) {
                        names.push(beacon["mac_id"]);
                    }
                }
                var validInfos = new Array;
                for (var x = 0; x < names.length; x++) {
                    var mac_id = names[x];
                    var rssi = -200;
                    var index = -1;
                    for (var y = 0; y < beanconInfos.length; y++) {
                        var beacon = beanconInfos[y];
                        if (rssi < beacon["rssi"] && mac_id == beacon["mac_id"]) {
                            rssi = beacon["rssi"];
                            index = y;
                        }
                    }
                    if(index >= 0) {
                        var validInfo = beanconInfos[index];
                        validInfos.push(validInfo);
                    }

                }
                var points = [];
                var width = 840;
                var height = 400;
                var max = 0;

                for (var i = 0; i < deviceInfos.length; i++) {
                    var val = 0;
                    var count = 0;
                    var shop = deviceInfos[i];
                    for (var j = 0; j < validInfos.length; j++) {
                        var info = validInfos[j];
                        if (shop["device_id"] == info["device_id"]) {
                            val += 12.5;
                            count ++;
                        }
                    }
                    max = Math.max(max,val);
                    var point = {
                        x: Math.floor(shop["position_x"] * map.getDelta()),
                        y: Math.floor(shop["position_y"] * map.getDelta()),
                        value: val,
                        count:count
                    };

                    points.push(point);

                }
                /*document.getElementById('firstNum').innerHTML = points[0]['count'].toString();
                document.getElementById('secondNum').innerHTML = points[1]['count'].toString();
                document.getElementById('thirdNum').innerHTML = points[2]['count'].toString();
*/
//                heatmapInstance.clear();
                var data = {
                    max: max == 0?100:max,
                    data: points
                };
                // if you have a set of datapoints always use setData instead of addData
                // for data initialization
                heatmapInstance.setData(data);

                var canvas1 = document.getElementById("floorCanvas");
                var canvas2 = $('.heatmap-canvas')[0];
                canvas1.getContext("2d").drawImage(canvas2, 0, 0);
                canvas2.remove();
            }
        });

    }

    /*热力图显示(优化)*/
    var ccc = function(startTime,endTime){
        var type = $('#setviewtime').attr("type");
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
                    var heatPoint = heatPoints[i];
                    if(type=="1"){
                        val = heatPoint["count"]*12.5+30;//历史回顾，+30效果更明显
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

    //轨迹点显示测试
    var ddd = function(startTime,endTime){
        var visitorId= "10000001";
        var type = $('#setviewtime').attr("type");
        map.clearOverlays();
        var heatMapInstance = h337.create({
            container:document.querySelector('#map'),
            radius:8
        });
        $.ajax({
            type:"POST",
            url:"<%=path%>/api/track/getTrackByVisitorId",
            data:{visitorId:visitorId,startTime:startTime,endTime:endTime},
            success:function(response){
                var trackPoints = response["list"];
                var points = [];
                var max = 0;
                for(var i=0;i<trackPoints.length;i++){
                    var val = 10;
                    var count = 1;
                    var trackPoint = trackPoints[i];
                    max = Math.max(max,val);
                    var point ={
                        x: Math.floor(trackPoint["x"] * map.getDelta()),
                        y: Math.floor(trackPoint["y"] * map.getDelta()),
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

    //轨迹点连线测试
    var i=0;
    var timeout=0;
    var eee = function(startTime,endTime){
        var visitorId = "10000001";
        map.clearOverlays();
        $.ajax({
            type:"POST",
            url:"<%=path%>/api/track/getTrackByVisitorId",
            data:{visitorId:visitorId,startTime:startTime,endTime:endTime},
            success:function(response){
                var trackPoints = response["list"];
                if(trackPoints.length==1){
                    var trackPoint = trackPoints[0];
                    var p = new VPoint(trackPoint.x,trackPoint.y,floor);
                    var vmaker = new VMarker(p,"<%=path%>/images/marker.png");
                    map.addOverlay(vmaker);
                }else {
                    draw(trackPoints);
                }
            }
        });
    }

    var draw = function(trackPoints){
        if(i>=trackPoints.length){alert("结束");i=0;timeout=0;return;}
        if(i==0){
            var trackPoint1=trackPoints[0];
            var p0 = new VPoint(trackPoint1.x,trackPoint1.y,floor);
            drawTrack(p0,p0);
            var trackPoint1=trackPoints[i];
            var trackPoint2=trackPoints[i+1];
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

        if(i>0){
            var trackPoint1=trackPoints[i-1];
            var trackPoint2=trackPoints[i];
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
        i++;
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
</script>
</body>
</html>
