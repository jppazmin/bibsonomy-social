var SOAPClient=(function(){var c={};
var b=null;
var a={Proxy:"",SOAPServer:"",ContentType:"text/xml",CharSet:"utf-8",ResponseXML:null,ResponseText:"",Status:0,ContentLength:0,Timeout:0,SetHTTPHeader:function(d,f){var e=/^[\w]{1,20}$/;
if((typeof(d)==="string")&&e.test(d)){c[d]=f
}},Namespace:function(d,e){return{"name":d,"uri":e}
},SendRequest:function(e,h){if(!!SOAPClient.Proxy){SOAPClient.ResponseText="";
SOAPClient.ResponseXML=null;
SOAPClient.Status=0;
var f=e.toString();
SOAPClient.ContentLength=f.length;
function g(j){if(!!b){clearTimeout(b)
}SOAPClient.Status=d.status;
SOAPClient.ResponseText=d.responseText;
SOAPClient.ResponseXML=d.responseXML;
if(typeof(h)==="function"){var i=$.xmlToJSON(j);
h(i)
}if(typeof window[h]=="function"){var i=$.xmlToJSON(j);
window[h](i)
}}var d=$.ajax({type:"POST",url:SOAPClient.Proxy,dataType:"xml",processData:false,data:f,success:g,contentType:SOAPClient.ContentType+'; charset="'+SOAPClient.CharSet+'"',beforeSend:function(k){k.setRequestHeader("Method","POST");
k.setRequestHeader("Content-Length",SOAPClient.ContentLength);
k.setRequestHeader("SOAPServer",SOAPClient.SOAPServer);
k.setRequestHeader("SOAPAction",e.Action);
if(!!c){var i=null,j=null;
for(i in c){if(!c.hasOwnProperty||c.hasOwnProperty(i)){j=c[i];
k.setRequestHeader(i,j.value)
}}}}})
}},ToXML:function(h){var f=[];
var l=false;
try{if(!!h&&typeof(h)==="object"&&h.typeOf==="SOAPObject"){if(!!h.ns){if(typeof(h.ns)==="object"){l=true;
f.push("<"+h.ns.name+":"+h.name);
f.push(" xmlns:"+h.ns.name+'="'+h.ns.uri+'"')
}else{f.push("<"+h.name);
f.push(' xmlns="'+h.ns+'"')
}}else{f.push("<"+h.name)
}if(h.attributes.length>0){var k;
var d=h.attributes.length-1;
do{k=h.attributes[d];
if(l){f.push(" "+h.ns.name+":"+k.name+'="'+k.value+'"')
}else{f.push(" "+k.name+'="'+k.value+'"')
}}while(d--)
}f.push(">");
if(h.hasChildren()){var i,g;
for(i in h.children){g=h.children[i];
if(typeof(g)==="object"){f.push(SOAPClient.ToXML(g))
}}}if(!!h.value){f.push(h.value)
}if(l){f.push("</"+h.ns.name+":"+h.name+">")
}else{f.push("</"+h.name+">")
}return f.join("")
}}catch(j){alert("Unable to process SOAPObject! Object must be an instance of SOAPObject")
}}};
return a
})();
var SOAPRequest=function(d,c){this.Action=d;
var b=[];
var e=[];
var a=(!!c)?[c]:[];
this.addNamespace=function(f,g){b.push(new SOAPClient.Namespace(f,g))
};
this.addHeader=function(f){e.push(f)
};
this.addBody=function(f){a.push(f)
};
this.toString=function(){var f=new SOAPObject("soapenv:Envelope");
f.attr("xmlns:soapenv","http://schemas.xmlsoap.org/soap/envelope/");
if(b.length>0){var h,j;
for(h in b){if(!b.hasOwnProperty||b.hasOwnProperty(h)){j=b[h];
if(typeof(j)==="object"){f.attr("xmlns:"+j.name,j.uri)
}}}}if(e.length>0){var l=f.appendChild(new SOAPObject("soapenv:Header"));
var k;
for(k in e){if(!e.hasOwnProperty||e.hasOwnProperty(k)){l.appendChild(e[k])
}}}if(a.length>0){var i=f.appendChild(new SOAPObject("soapenv:Body"));
var g;
for(g in a){if(!a.hasOwnProperty||a.hasOwnProperty(g)){i.appendChild(a[g])
}}}return f.toString()
}
};
var SOAPObject=function(a){this.typeOf="SOAPObject";
this.ns=null;
this.name=a;
this.attributes=[];
this.children=[];
this.value=null;
this.attr=function(b,c){this.attributes.push({"name":b,"value":c});
return this
};
this.appendChild=function(b){this.children.push(b);
return b
};
this.hasChildren=function(){return(this.children.length>0)?true:false
};
this.val=function(b){if(!b){return this.value
}else{this.value=b;
return this
}};
this.toString=function(){return SOAPClient.ToXML(this)
}
};