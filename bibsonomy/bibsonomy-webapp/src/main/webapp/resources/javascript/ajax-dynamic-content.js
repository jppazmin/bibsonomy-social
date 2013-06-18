var enableCache=true;
var jsCache=new Array();
var dynamicContent_ajaxObjects=new Array();
function ajax_showContent(a,c,b){document.getElementById(a).innerHTML=dynamicContent_ajaxObjects[c].response;
if(enableCache){jsCache[b]=dynamicContent_ajaxObjects[c].response
}dynamicContent_ajaxObjects[c]=false
}function ajax_loadContent(a,d){if(enableCache&&jsCache[d]){document.getElementById(a).innerHTML=jsCache[d];
return
}var e=dynamicContent_ajaxObjects.length;
document.getElementById(a).innerHTML="Loading content - please wait";
dynamicContent_ajaxObjects[e]=new sack();
if(d.indexOf("?")>=0){dynamicContent_ajaxObjects[e].method="GET";
var c=d.substring(d.indexOf("?"));
d=d.replace(c,"");
c=c.replace("?","");
var b=c.split(/&/g);
for(var g=0;
g<b.length;
g++){var f=b[g].split("=");
if(f.length==2){dynamicContent_ajaxObjects[e].setVar(f[0],f[1])
}}d=d.replace(c,"")
}dynamicContent_ajaxObjects[e].requestFile=d;
dynamicContent_ajaxObjects[e].onCompletion=function(){ajax_showContent(a,e,d)
};
dynamicContent_ajaxObjects[e].runAJAX()
};