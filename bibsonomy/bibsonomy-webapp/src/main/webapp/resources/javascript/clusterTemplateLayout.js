function htmlParameterList(b){var c="";
for(var a=0;
a<b.length;
a++){c+=encodeURIComponent(b[a].toString());
if(a<b.length-1){c+="+"
}}return c
}function isClusterEnabled(a){if(clusterSettings==undefined){alert("No cluster settings loaded");
return false
}else{for(var b=0;
b<clusterSettings.clusters.length;
b++){if(clusterSettings.clusters[b].clusterID==a){return true
}}return false
}}function getTagSize(a){var d=a.globalcount;
var c=a.maxTagCount;
if(d==0||c==0){return"tagtiny"
}var b=((d*100)/c);
if(b<25){return"tagtiny"
}else{if(b>=25&&b<50){return"tagnormal"
}else{if(b>=50&&b<75){return"taglarge"
}else{if(b>=75){return"taghuge"
}}}}return""
}function getUserSize(b){var d=b.weight;
var c=b.maxUserWeight;
if(d==0||c==0){return"usertiny"
}var a=((d*100)/c);
if(a<25){return"usertiny"
}else{if(a>=25&&a<50){return"usernormal"
}else{if(a>=50&&a<75){return"userlarge"
}else{if(a>=75){return"userhuge"
}}}}return""
}function getTagFontSize(a){var b=a.globalcount;
var c=a.maxTagCount;
f=1*b/c*100;
t=f>100?100:f;
t/=15;
t=Math.log(t)*100;
return(Math.round(t)<100)?100:Math.round(t)
}function generateListAnchors(a){var d="";
var c=1;
for(var b=0;
b<a.total;
b+=1*a.limit){d+="<a class='clusterNavigation";
if(b>=a.offset&&b<a.offset+a.limit){d+=" clusterNavigationActive"
}d+="' onclick='showClusters("+b+","+a.limit+");'>"+c+"</a>";
if(b<a.total){d+="&nbsp;|&nbsp;"
}c++
}return d
};