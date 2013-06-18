function initRequest(){var a;
try{if(window.XMLHttpRequest){a=new XMLHttpRequest()
}else{if(window.ActiveXObject){a=new ActiveXObject("Microsoft.XMLHTTP")
}}if(a.overrideMimeType){a.overrideMimeType("text/xml")
}}catch(b){return false
}return a
}function flagSpammerEvaluator(b,c,a,d){if(b==null||b==""){addLogMessage("please specify a user");
return
}if(c!=null&&a=="false"){document.getElementById(c).className="spammer"
}else{document.getElementById(c).style.display="none"
}runAjax("userName="+b+"&evaluator="+d,"flag_spammer_evaluator")
}function addSpammer(b,c,a){if(b==null||b==""){addLogMessage("please specify a user");
return
}if(c!=null&&a=="false"){document.getElementById(c).className="spammer"
}else{document.getElementById(c).style.display="none"
}runAjax("userName="+b,"flag_spammer")
}function addSpammerInline(a){var b=document.getElementsByName(a);
for(i=0;
i<b.length;
i++){b[i].parentNode.parentNode.style.display="none"
}runAjax("userName="+a,"flag_spammer")
}function unflagSpammer(b,c,a){if(b==null||b==""){addLogMessage("please specify a user");
return
}if(c!=null&&a=="false"){document.getElementById(c).className="spamflag"
}else{document.getElementById(c).style.display="none"
}runAjax("userName="+b,"unflag_spammer")
}function unflagSpammerEvaluator(b,c,a,d){if(b==null||b==""){addLogMessage("please specify a user");
return
}if(c!=null&&a=="false"){document.getElementById(c).className="nonspammer"
}else{document.getElementById(c).style.display="none"
}runAjax("userName="+b+"&evaluator="+d,"unflag_spammer_evaluator")
}function markUncertainUser(b,c,a){if(b==null||b==""){addLogMessage("please specify a user");
return
}if(c!=null&&a=="false"){document.getElementById(c).className="uncertainUser"
}else{document.getElementById(c).style.display="none"
}runAjax("userName="+b,"mark_uncertainuser")
}function updateSettings(a,b){if(a==null||b==null||a==""||b==""){addLogMessage("please enter a valid value");
return
}runAjax("key="+a+"&value="+b,"update_settings")
}function generateApiKey(a){if(a==null||a==""){addLogMessage("please specify a user");
return
}runAjax("userName="+a,"gen_api_key")
}function runAjax(e,d){var b=initRequest();
var a="/admin/ajax?"+e;
if(b){b.open("GET",a+"&action="+d,true);
var c=ajax_updateLog(b);
b.onreadystatechange=c;
b.send(null)
}}function ajax_updateLog(a){return function(){if(4==a.readyState){addLogMessage(a.responseText)
}}
}function addLogMessage(b){var c=document.getElementById("log");
if(c){var a=document.createElement("LI");
a.innerHTML=b;
c.insertBefore(a,c.firstChild)
}}function clearFields(){document.getElementsByName("user")[0].value="";
document.getElementsByName("user")[1].value=""
};