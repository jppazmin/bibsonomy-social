var ms_table_id="";
var ms_table_col="";
var ms_tab="";
var ms_table_tbody="";
var ms_oldstyles="";
function ms_init(f,c){ms_table_id=f;
ms_table_col=c;
ms_tab=document.getElementById(ms_table_id);
var d;
var a=0;
var e="";
for(d=0;
d<ms_tab.childNodes.length;
d++){if(ms_tab.childNodes[d].nodeName=="TBODY"){ms_table_tbody=ms_tab.childNodes[d];
break
}}if(ms_table_tbody!=""){for(d=0;
d<ms_table_tbody.childNodes.length;
d++){if(ms_table_tbody.childNodes[d].nodeName=="TR"){a=0;
for(var b=0;
b<ms_table_tbody.childNodes[d].childNodes.length;
b++){if(ms_table_tbody.childNodes[d].childNodes[b].nodeName=="TD"){a++;
if(a==ms_table_col){e="x";
if(ms_table_tbody.childNodes[d].childNodes[b].childNodes.length>0){e=ms_table_tbody.childNodes[d].childNodes[b].childNodes[0].nodeValue
}else{e="none"
}ms_table_tbody.childNodes[d].childNodes[b].setAttribute("onclick",'ms_marksame("'+e+'")')
}}}}}}}function ms_marksame(c){var a=0;
if(ms_table_tbody!=""){for(i=0;
i<ms_table_tbody.childNodes.length;
i++){if(ms_table_tbody.childNodes[i].nodeName=="TR"){countTD=0;
for(var b=0;
b<ms_table_tbody.childNodes[i].childNodes.length;
b++){if(ms_table_tbody.childNodes[i].childNodes[b].nodeName=="TD"){countTD++;
if(countTD==ms_table_col){if(ms_table_tbody.childNodes[i].childNodes[b].childNodes.length>0){if(ms_table_tbody.childNodes[i].childNodes[b].childNodes[0].nodeValue==c){ms_table_tbody.childNodes[i].childNodes[b].style.backgroundColor="#FFFFAA";
a++
}else{ms_table_tbody.childNodes[i].childNodes[b].style.backgroundColor=""
}}else{}}}}}}if(a>1){top.status="IP address "+c+" occurs in entries: "+a
}else{top.status="IP address "+c+" does not occur in more than this entry."
}}};