var dom=(document.getElementById)?true:false;
var ns5=(!document.all&&dom||window.opera)?true:false;
var ie5=((navigator.userAgent.indexOf("MSIE")>-1)&&dom)?true:false;
var ie4=(document.all&&!dom)?true:false;
var nodyn=(!ns5&&!ie4&&!ie5&&!dom)?true:false;
var origWidth,origHeight;
if(nodyn){event="nope"
}var tipFollowMouse=true;
var tipWidth=210;
var offX=20;
var offY=12;
var tipFontSize="20px";
var tipFontColor="#069";
var tipBgColor="#FFFFFF";
var tipBorderColor="#069";
var tipBorderWidth=1;
var tipBorderStyle="solid";
var tipPadding=4;
var messages=new Array();
if(document.images){var theImgs=new Array();
for(var i=0;
i<messages.length;
i++){theImgs[i]=new Image();
theImgs[i].src=messages[i][0]
}}var startStr='<table width="'+tipWidth+'"><tr><td align="center" width="100%"><img src="';
var midStr='" border="0"></td></tr><tr><td valign="top">';
var endStr="</td></tr></table>";
var tooltip,tipcss;
function initTip(){if(nodyn){return
}tooltip=(ie4)?document.all["tipDiv"]:(ie5||ns5)?document.getElementById("tipDiv"):null;
tipcss=tooltip.style;
if(ie4||ie5||ns5){tipcss.fontSize=tipFontSize;
tipcss.color=tipFontColor;
tipcss.backgroundColor=tipBgColor;
tipcss.borderColor=tipBorderColor;
tipcss.borderWidth=tipBorderWidth+"px";
tipcss.padding=tipPadding+"px";
tipcss.borderStyle=tipBorderStyle
}if(tooltip&&tipFollowMouse){document.onmousemove=trackMouse
}}window.onload=initTip;
var t1,t2;
var tipOn=false;
function preDoTooltip(a){doTooltip(xget_event(a))
}function doTooltip(a){tipText=a.firstChild.data;
tipCount=a.getAttribute("title");
if(!tooltip){return
}if(t1){clearTimeout(t1)
}if(t2){clearTimeout(t2)
}tipOn=true;
curBgColor=tipBgColor;
curFontColor=tipFontColor;
if(ie4||ie5||ns5){var b='<span style="font-size:'+tipFontSize+"; color:"+curFontColor+';">'+tipText+": "+tipCount+"</span>"+endStr;
tooltip.innerHTML=b
}if(!tipFollowMouse){positionTip(a)
}else{t1=setTimeout("tipcss.visibility='visible'",100)
}}var mouseX,mouseY;
function trackMouse(a){standardbody=(document.compatMode=="CSS1Compat")?document.documentElement:document.body;
mouseX=(ns5)?a.pageX:window.event.clientX+standardbody.scrollLeft;
mouseY=(ns5)?a.pageY:window.event.clientY+standardbody.scrollTop;
if(tipOn){positionTip(a)
}}function positionTip(b){if(!tipFollowMouse){mouseX=(ns5)?b.pageX:window.event.clientX+standardbody.scrollLeft;
mouseY=(ns5)?b.pageY:window.event.clientY+standardbody.scrollTop
}var c=(ie4||ie5)?tooltip.clientWidth:tooltip.offsetWidth;
var a=(ie4||ie5)?tooltip.clientHeight:tooltip.offsetHeight;
var e=(ns5)?window.innerWidth-20+window.pageXOffset:standardbody.clientWidth+standardbody.scrollLeft;
var d=(ns5)?window.innerHeight-20+window.pageYOffset:standardbody.clientHeight+standardbody.scrollTop;
if((mouseX+offX+c)>e){tipcss.left=mouseX-(c+offX)+"px"
}else{tipcss.left=mouseX+offX+"px"
}if((mouseY+offY+a)>d){tipcss.top=d-(a+offY)+"px"
}else{tipcss.top=mouseY+offY+"px"
}if(!tipFollowMouse){t1=setTimeout("tipcss.visibility='visible'",100)
}}function hideTip(){if(!tooltip){return
}t2=setTimeout("tipcss.visibility='hidden'",100);
tipOn=false
}document.write('<div id="tipDiv" style="position:absolute; visibility:hidden; z-index:100"></div>');