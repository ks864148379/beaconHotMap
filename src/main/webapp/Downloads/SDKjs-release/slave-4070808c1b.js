function VPoint(n,t,i,e){var r="point",o=e||"red";this.x=parseInt(1e3*n)/1e3,this.y=parseInt(1e3*t)/1e3,this.floorIndex=i,this.color=function(){return o},this.type=function(){return r}}function VPosition(n,t){this.left=n,this.top=t}function Vsize(n,t){this.width=n,this.height=t}function VPolyline(n,t,i,e){var r=null,o=null,u="rgba(138,43,226,0.5)",l=2,s="line",c=null;null!==n&&(r=n,o=r[0].floorIndex,t&&(l=t),i&&(u=i),e&&(c=e),this.type=function(){return s},this.color=function(){return u},this.points=function(){return r},this.floorIndex=function(){return o},this.width=function(){return l})}function VCircle(n,t,i,e,r){var o=null,u="red",l=2,s="circle",c=null,f=0;null!==n&&(o=n,i&&(l=i),e&&(u=e),r&&(c=r),null!==t&&(f=t,this.type=function(){return s},this.color=function(){return u},this.point=function(){return o},this.floorIndex=function(){return o.floorIndex},this.width=function(){return l},this.radius=function(){return f}))}function VText(n,t,i,e){var r=null,o="black",u="text",l="",s=1;null!==n&&(r=n,null!==i&&(o=i),null!==e&&(s=e),null!==t&&""!==t&&(l=t,this.type=function(){return u},this.color=function(){return o},this.lineWidth=function(){return s},this.point=function(){return r},this.floorIndex=function(){return r.floorIndex},this.content=function(){return l}))}function VMarker(n,t,i){function e(n){null===n&&(n=0),$(y).hide(n,function(){I=!0})}function r(n){null===n&&(n=0),$(y).show(n,function(){I=!1})}function o(){return I}function u(){return k}function l(n){$(y).css({left:n.left+"px",top:n.top+"px"}),k=n}function s(){return v}function c(n){v=n,b.src=v}function f(){return $(y).css("zIndex")}function h(n){$(y).css("zIndex",n)}function p(n){$(y).on("click",function(){d(n)})}function a(){$(y).off("click")}function d(n){var t=u(),i=t.x,e=t.y,r=t.floorIndex,o="http://"+serverUrl+"/beacon/position!pos2add?client=824&vkey=FFE58998-B203-B44E-A95B-8CA2D6CBCD65&x="+i+"&y="+e+"&floor="+r+"&place="+n+"&jsoncallback=?";$.getJSON(o,function(n){if(n.success){var t=n.rows[0],i=t.unit_name;t.parent_name;alert("标记信息："+i)}})}var x=-26,g=-50,I=!1,m="marker",k=null,v=null;if(n){k=n;var y=document.createElement("div");y.style.position="absolute";var b=document.createElement("img");b.style.position="absolute",y.appendChild(b),1==t?(b.src="./img/marker.png",v="./img/marker.png"):2==t?(b.src="./img/tap.png",v="./img/tap.png"):(b.src=t,v=t),i?$(b).css({left:i.left+15+"px",top:i.top+23+"px",width:"23px",height:"25px","z-index":10,"text-align":"center"}):$(b).css({left:x+15+"px",top:g+23+"px",width:"23px",height:"25px","text-align":"center","z-index":10}),this.show=r,this.hide=e,this.isHidden=o,this.getPosition=u,this.setPosition=l,this.getImgUrl=s,this.setImgUrl=c,this.getZIndex=f,this.setZIndex=h,this.dom=y,this.enableMarkerClick=p,this.unableMarkerClick=a,this.type=function(){return m},this.floorIndex=function(){return k.floorIndex},this.point=function(){return k},this.click=null}}var serverUrl="123.57.46.160:8080";