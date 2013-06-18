function hideSubmitButtons(c,b,a){$(b).find(":submit").hide();
$(b).find(".progressGif").show()
}function successSyncForm(b,c,d,a){$(a).find(":submit").show();
$(a).find(".progressGif").hide();
if(b.syncData){showSyncData(a,b.syncData)
}if(b.syncPlan){showSyncPlan(a,b.syncPlan)
}return false
}function showSyncPlan(c,k){var a=$(c).find(".syncPlan");
a.empty();
var j=$(c).find("input[name='serviceName']").val();
var n=location.hostname;
var h=document.createElement("dl");
for(var d in k){var e=document.createElement("dt");
h.appendChild(e);
e.appendChild(document.createTextNode(getString("resourceType."+d+".plural")));
var m=document.createElement("dd");
h.appendChild(m);
var i=document.createElement("ul");
m.appendChild(i);
var g=k[d];
var l=document.createElement("li");
l.appendChild(document.createTextNode(g["CLIENT"]));
i.appendChild(l);
var b=document.createElement("li");
b.appendChild(document.createTextNode(g["SERVER"]));
i.appendChild(b);
var f=document.createElement("li");
f.appendChild(document.createTextNode(g["OTHER"]));
i.appendChild(f)
}a.append(h);
$(c).find(".synchronizeBtn").show()
}function showSyncData(f,h){$(f).find(".syncPlan").empty();
$(f).find(".synchronizeBtn").hide();
var e=$(f).find(".syncData");
var g="";
e.empty();
for(key in h){var c=h[key].error;
var a="";
if(c!=undefined){alert(key+": "+c);
a="ERROR"
}else{a=h[key]
}if(key=="BibTex"){resourceType=getString("publications")
}else{resourceType=getString("bookmarks")
}var d=$("<div class='fsRow'></div");
var b=$("<dl><dt>"+resourceType+":</dt><dd>"+a+"</dd></dl>");
b.appendTo(d);
d.appendTo(e)
}}function getSyncPlan(a){$(a).parents("form").find("input[name='_method']").val("GET")
}function doSync(a){$(a).parents("form").find("input[name='_method']").val("POST")
}function confirmReset(a){if(confirm(getString("synchronization.server.reset.confirm"))){$(a).parents("form").find("input[name='_method']").val("DELETE")
}else{return false
}}function errorSyncForm(b,e,c,a){alert("error: "+c);
var d=a.find("input[name='_method']").val();
if("POST"==d){$(a).find(".resourceDiv").each(function(g,h){var f=$(this).find("dd");
f.empty();
$(f).append(getString("error"));
$(a).find(".syncPlan").empty()
})
}else{if("GET"==d){}else{alert("error on unknown method")
}}$(a).find(":submit").show();
$(a).find(".synchronizeBtn").hide();
$(a).find(".progressGif").hide()
}$(document).ready(function(){$("form").each(function(a,c){var b=$(this);
$(this).ajaxForm({dataType:"json",beforeSubmit:hideSubmitButtons,success:successSyncForm,error:function(d,f,e){errorSyncForm(d,f,e,b)
}})
})
});