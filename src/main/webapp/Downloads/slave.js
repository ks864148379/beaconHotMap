var serverUrl = "123.57.46.160:8080";

function VPoint(x, y, floorIndex, color) {
    var type = "point";
    var dColor = color || "red";

    this.x = parseInt(x * 1000) / 1000;
    this.y = parseInt(y * 1000) / 1000;
    this.floorIndex = floorIndex;

    this.color = function() {
        return dColor;
    };
    this.type = function() {
        return type;
    }
}

function VPosition(left, top) {
    this.left = left;
    this.top = top;
}

function Vsize(width, height) {
    this.width = width;
    this.height = height;
}

function VPolyline(vpoints, lineWidth, lineColor, lineId) {
    //alert("line");
    var points = null;
    var floorIndex = null;
    //var color = "rgba(138,43,226,0.5)";
    var color = "rgba(255,102,0,0.5)";
    var width = 0.7;
    var type = "line";
    var id = null;
    if (vpoints === null) {
        return undefined;
    } else {
        points = vpoints;
        floorIndex = points[0].floorIndex;
    }
    if (lineWidth) {
        width = lineWidth;
    }
    if (lineColor) {
        color = lineColor;
    }
    if (lineId) {
        id = lineId;
    }
    this.type = function() {
        return type;
    };
    this.color = function() {
        return color;
    };
    this.points = function() {
        return points;
    };
    this.floorIndex = function() {
        return floorIndex;
    };
    this.width = function() {
        return width;
    }
}
function VPolyDashline(vpoints, lineWidth, lineColor, lineId) {
    //alert("dashline");
    var points = null;
    var floorIndex = null;
    //var color = "rgba(138,43,226,0.5)";
    var color = "rgba(255,102,0,0.5)";
    var width = 1.0;
    var type = "dashline";
    var id = null;
    if (vpoints === null) {
        return undefined;
    } else {
        points = vpoints;
        floorIndex = points[0].floorIndex;
    }
    if (lineWidth) {
        width = lineWidth;
    }
    if (lineColor) {
        color = lineColor;
    }
    if (lineId) {
        id = lineId;
    }
    this.type = function() {
        return type;
    };
    this.color = function() {
        return color;
    };
    this.points = function() {
        return points;
    };
    this.floorIndex = function() {
        return floorIndex;
    };
    this.width = function() {
        return width;
    }
}

function VCircle(vpoint, vradius, lineWidth, lineColor, lineId) {
    var point = null;
    var floorIndex = null;
    var color = "red";
    var width = 2;
    var type = "circle";
    var id = null;
    var radius = 0;
    if (vpoint === null) {
        return undefined;
    } else {
        point = vpoint;
    }
    if (lineWidth) {
        width = lineWidth;
    }
    if (lineColor) {
        color = lineColor;
    }
    if (lineId) {
        id = lineId;
    }
    if (vradius === null) {
        return undefined;
    } else {
        radius = vradius;
    }

    this.type = function() {
        return type;
    };
    this.color = function() {
        return color;
    };
    this.point = function() {
        return point;
    };
    this.floorIndex = function() {
        return point.floorIndex;
    };
    this.width = function() {
        return width;
    };
    this.radius = function() {
        return radius;
    }
}

//文字
function VText(vpoint, vcontent, vcolor, width) {
    var point = null;
    var color = "black";
    var type = "text";
    var content = "";
    var lineWidth = 1;
    if (vpoint === null) {
        return undefined;
    } else {
        point = vpoint;
    }
    if (vcolor !== null) {
        color = vcolor;
    }
    if (width !== null) {
        lineWidth = width;
    }
    if (vcontent === null || vcontent === "") {
        return undefined;
    } else {
        content = vcontent;
    }

    this.type = function() {
        return type;
    };
    this.color = function() {
        return color;
    };
    this.lineWidth = function() {
        return lineWidth;
    };
    this.point = function() {
        return point;
    };
    this.floorIndex = function() {
        return point.floorIndex;
    };
    this.content = function() {
        return content;
    }
}

//标记点
function VMarker(vpoint, id, offset,text) { //id可以传入1,2或者有效的图片url
    //默认偏移
    var defaultLeft = -30;
    var defaultTop = -52;
    //是否为隐藏
    var hidden = false;
    //类型
    var type = "marker";
    //位置
    var point = null;
    //链接
    var url = null;
    //是否有位置
    if (vpoint) {
        point = vpoint;
    } else {
        return undefined;
    }
    //构建dom
    var marker = document.createElement("div");
    marker.style.position = "absolute";
    var img = document.createElement("img");
    img.style.position = "absolute";
    img.style.opacity="0.7";
    img.style.zIndex=222;
    marker.appendChild(img);
    if(text){
        //创建文本浮层
        var popText=document.createElement("div");
        popText.id="popText";
        popText.style.position="absolute";
        popText.style.padding="6px";
        popText.style.color="#fff";
        popText.style.fontFamily="serif";
        popText.style.backgroundColor="#228b22";
        popText.style.border="border: 1px solid #008000";
        popText.style.opacity=0.7;
        var span=document.createElement("span");
        span.innerHTML=text;
        span.style.width=text.length*10+'px';
        popText.appendChild(span);
        marker.appendChild(popText);
        popText.style.width=span.style.width;
        $(popText).css({
            //left: point.x - 100+ "px",
            top: point.y -70 + "px",
            "z-index": 10,
            "text-align": "center"
        })
    }else {
        console.log("未传入数据");
    }
    //图片的位置
    if(id == 1){//id=代表目标点，为黄色
        img.src = "/China_Stm/static/images/line/start.png";
    }else if(id == 2){ //id=2或空，表示原点，为红色
        img.src = "/China_Stm/static/images/line/end.png";
    }else if(id == 3){ //当前位置
        img.src = "/China_Stm/static/images/marker/LocationPoint.png";
    }else if(id == 4){ //电梯
        img.src = "/China_Stm/static/images/line/DT.png";
    }else if(id == 5){ //当前位置图标
        img.src = "/China_Stm/static/images/marker/LocationPoint.png";
    }else if(id == 6){ //展厅图标
        img.src = "/China_Stm/static/images/marker/hall.png";
    }else if(id == 7){ //楼梯
        img.src = "/China_Stm/static/images/line/LT.png";
    }else if(id == 8){ //扶梯
        img.src = "/China_Stm/static/images/line/FT.png";
    }else{
        img.src = id;
        url = id;
    }

    //对齐图片的偏移量
    if (offset) {
        $(img).css({
            left: offset.left + 18 + "px",
            top: offset.top + 26 + "px",
            width: 25+ "px",
            height: 30 + "px",
            "z-index": 10,
            "text-align": "center"
        })
    } else {
        $(img).css({
            left: defaultLeft + 15 + "px",
            top: defaultTop + 26 + "px",
            width: 25 + "px",
            height: 30 + "px",
            "text-align": "center",
            "z-index": 10
        })
    }

    //方法
    //隐藏
    function hide(t) {
        if (t === null) {
            t = 0;
        }
        $(marker).hide(t, function() {
            hidden = true;
        })
    }
    //显示
    function show(t) {
        if (t === null) {
            t = 0;
        }
        $(marker).show(t, function() {
            hidden = false;
        })
    }

    function isHidden() {
        return hidden;
    }

    function getPosition() {
        return point;
    }

    function setPosition(p) {
        $(marker).css({
            left: p.left + "px",
            top: p.top + "px"
        });
        point = p;
    }

    function getImgUrl() {
        return url;
    }

    function setImgUrl(newUrl) {
        url = newUrl;
        img.src = url;
    }

    function getZIndex() {
        return $(marker).css("zIndex");
    }

    function setZIndex(zindex) {
        $(marker).css("zIndex", zindex);
    }

    function enableMarkerClick(mallId) {
        $(marker).on("click", function() {
            markerClickHandler(mallId);
        });
    }

    function unableMarkerClick() {
        $(marker).off("click");
    }

    function markerClickHandler(mallId) { //这里的ajax功能可以考虑动态传入的函数回调，实现点击后的不同效果

        //console.log(getPosition());
        var p = getPosition();
        var x = p.x,
            y = p.y,
            floor = p.floorIndex;
        var url = "http://" + serverUrl + "/beacon/position!pos2add?client=824&vkey=FFE58998-B203-B44E-A95B-8CA2D6CBCD65&x=" + x + "&y=" + y + "&floor=" + floor + "&place=" + mallId + "&jsoncallback=?";
        $.getJSON(
            url,
            function(result) {
                if (!result.success){
                    alert("无地点信息"+ p.x+":"+ p.y);
                    return;
                }
                var data = result.rows[0];
                console.log(data);
                var name = data.unit_name;
                var parent_name = data.parent_name;
                document.getElementById("markername").innerHTML=name;
                $("#marker").modal("show");
                //alert(name);
            }
        );
    }
    //函数指针指向
    this.show = show;
    this.hide = hide;
    this.isHidden = isHidden;
    this.getPosition = getPosition;
    this.setPosition = setPosition;
    this.getImgUrl = getImgUrl;
    this.setImgUrl = setImgUrl;
    this.getZIndex = getZIndex;
    this.setZIndex = setZIndex;
    this.dom = marker;
    this.enableMarkerClick = enableMarkerClick;
    this.unableMarkerClick = unableMarkerClick;

    this.type = function() {
        return type;
    };

    this.floorIndex = function() {
        return point.floorIndex;
    };
    this.point = function() {
        return point;
    };
    this.click = null;
}