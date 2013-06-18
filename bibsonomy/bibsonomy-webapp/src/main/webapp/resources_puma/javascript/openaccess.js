(function(c){c.fn.dump=function(){return c.dump(this)
};
c.dump=function(f){var e=function(l,m){if(!m){m=0
}var k="",j="";
for(i=0;
i<m;
i++){j+="\t"
}t=g(l);
switch(t){case"string":return'"'+l+'"';
break;
case"number":return l.toString();
break;
case"boolean":return l?"true":"false";
case"date":return"Date: "+l.toLocaleString();
case"array":k+="Array ( \n";
c.each(l,function(o,n){k+=j+"\t"+o+" => "+e(n,m+1)+"\n"
});
k+=j+")";
break;
case"object":k+="Object { \n";
c.each(l,function(o,n){k+=j+"\t"+o+": "+e(n,m+1)+"\n"
});
k+=j+"}";
break;
case"jquery":k+="jQuery Object { \n";
c.each(l,function(o,n){k+=j+"\t"+o+" = "+e(n,m+1)+"\n"
});
k+=j+"}";
break;
case"regexp":return"RegExp: "+l.toString();
case"error":return l.toString();
case"document":case"domelement":k+="DOMElement [ \n"+j+"\tnodeName: "+l.nodeName+"\n"+j+"\tnodeValue: "+l.nodeValue+"\n"+j+"\tinnerHTML: [ \n";
c.each(l.childNodes,function(o,n){if(o<1){var p=0
}if(g(n)=="string"){if(n.textContent.match(/[^\s]/)){k+=j+"\t\t"+(o-(p||0))+" = String: "+a(n.textContent)+"\n"
}else{p--
}}else{k+=j+"\t\t"+(o-(p||0))+" = "+e(n,m+2)+"\n"
}});
k+=j+"\t]\n"+j+"]";
break;
case"function":var h=l.toString().match(/^(.*)\(([^\)]*)\)/im);
h[1]=a(h[1].replace(new RegExp("[\\s]+","g")," "));
h[2]=a(h[2].replace(new RegExp("[\\s]+","g")," "));
return h[1]+"("+h[2]+")";
case"window":default:k+="N/A: "+t;
break
}return k
};
var g=function(j){var h=typeof(j);
if(h!="object"){return h
}switch(j){case null:return"null";
case window:return"window";
case document:return"document";
case window.event:return"event";
default:break
}if(j.jquery){return"jquery"
}switch(j.constructor){case Array:return"array";
case Boolean:return"boolean";
case Date:return"date";
case Object:return"object";
case RegExp:return"regexp";
case ReferenceError:case Error:return"error";
case null:default:break
}switch(j.nodeType){case 1:return"domelement";
case 3:return"string";
case null:default:break
}return"Unknown"
};
return e(f)
};
function a(e){return d(b(e))
}function d(e){return e.replace(new RegExp("^[\\s]+","g"),"")
}function b(e){return e.replace(new RegExp("[\\s]+$","g"),"")
}})(jQuery);
var oaBaseUrl="/ajax/checkOpenAccess";
var classificationURL="/ajax/classificatePublication";
var swordURL="/ajax/swordService";
var GET_AVAILABLE_CLASSIFICATIONS="AVAILABLE_CLASSIFICATIONS";
var SAVE_CLASSIFICATION_ITEM="SAVE_CLASSIFICATION_ITEM";
var SAVE_ADDITIONAL_METADATA="SAVE_ADDITIONAL_METADATA";
var GET_ADDITIONAL_METADATA="GET_ADDITIONAL_METADATA";
var REMOVE_CLASSIFICATION_ITEM="REMOVE_CLASSIFICATION_ITEM";
var GET_POST_CLASSIFICATION_LIST="GET_POST_CLASSIFICATION_LIST";
var GET_CLASSIFICATION_DESCRIPTION="GET_CLASSIFICATION_DESCRIPTION";
var GET_SENT_REPOSITORIES="GET_SENT_REPOSITORIES";
var publication_intrahash="";
var publication_interhash="";
var metadataChanged=false;
var metadatafields=Array();
var autoSaveMetadataCounter=0;
function setMetadatafields(a){metadatafields=a
}function getMetadatafields(){return metadatafields
}function empty(b){var a;
if(b===""||b===0||b==="0"||b===null||b===false||typeof b==="undefined"){return true
}if(typeof b=="object"){for(a in b){return false
}return true
}return false
}function _removeSpecialChars(a){a=a.replace(/[^a-zA-Z0-9]/g,"");
return a
}function initialiseOpenAccessClassification(b,e){var d=b+"Container";
var a=b+"List";
var c=b+"Select";
if((null==document.getElementById(a))||(null==document.getElementById(a))){return
}if(null!=$("#"+d)){$("#"+d).show()
}if(null!=$("#"+a)){$("#"+a).show()
}if(null!=$("#"+c)){$("#"+c).show()
}publication_intrahash=$("#openAccessCurrentPublicationHash").val();
publication_interhash=$("#openAccessCurrentPublicationInterHash").val();
initClassifications(c,a)
}function sentPublicationToRepository(b,d){if(document.getElementById("authorcontractconfirm").checked){var a=document.createElement("img");
a.setAttribute("src","/resources_puma/image/ajax-loader.gif");
var c=swordURL+"?resourceHash="+d;
if(isMetadataChanged()){sendAdditionalMetadataFields(false)
}$.ajax({url:c,dataType:"json",beforeSend:function(e){$("#swordresponse").remove();
$("#pumaSword").append(a);
$("#oasendtorepositorybutton").addClass("oadisabledsend2repositorybutton");
document.getElementById(b).disabled=true
},success:function(e){$(a).remove();
$.each(e,function(g,f){if(null==e||null==e.response){}else{var h=createNode({tag:"div",parent:null,child:null,childNodes:null,parentID:null,id:"swordresponse",className:"ajaxresponse"+e.response.statuscode});
h.appendChild(document.createTextNode(e.response.localizedMessage));
$("#pumaSword").append(h);
swordResponseStatusCode=e.response.statuscode;
if(e.response.statuscode==0){$(b).removeClass("oadisabledsend2repositorybutton");
document.getElementById(b).disabled=false
}}})
},error:function(g,f,h){$(a).remove()
}})
}}function checkOpenAccess(){if($("#oasherparomeo").length>0){var a=$("#oasherparomeo");
var b=oaBaseUrl+$("#oaRequestPublisherUrlParameter").val();
$.ajax({url:b,dataType:"json",success:function(d){if((d.publishers.length>0)&&(undefined!=d.publishers)&&(d.publishers!="")){a.empty();
var c=document.createElement("ul");
c.className="oa-publishers";
$.each(d.publishers,function(f,h){var e=document.createElement("li");
e.className="oa-"+h.colour;
var g=document.createElement("span");
g.appendChild(document.createTextNode(h.name));
g.className="oa-publisher";
e.appendChild(g);
var j=document.createElement("ul");
j.className="oa-conditions";
$.each(h.conditions,function(k,m){var l=document.createElement("li");
l.appendChild(document.createTextNode(m));
j.appendChild(l)
});
e.appendChild(j);
c.appendChild(e)
});
a.append(c);
a.fadeIn()
}},error:function(d,c,f){}})
}}function initClassifications(c,a){var b=classificationURL+"?action="+GET_AVAILABLE_CLASSIFICATIONS;
$.ajax({dataType:"json",url:b,success:function(d){doInitialise(c,a,d)
},error:function(f,d,g){}})
}function doInitialise(b,a,c){$.each(c.available,function(h,g){var e=document.createElement("div");
e.setAttribute("id",g.name+"saved");
var d=document.createElement("div");
d.setAttribute("class","help");
var m=document.createElement("b");
m.setAttribute("class","smalltext");
var k=document.createElement("a");
k.setAttribute("href",g.url);
k.appendChild(document.createTextNode("?"));
m.appendChild(k);
var j=document.createElement("div");
j.appendChild(document.createTextNode(g.desc));
d.appendChild(m);
d.appendChild(j);
var f=document.createElement("div");
f.setAttribute("id",g.name);
var l=document.createElement("div");
l.setAttribute("id",g.name+"_input");
l.setAttribute("class","classificationInput");
f.appendChild(l);
f.appendChild(d);
$("#"+a).append(e);
$("#"+b).append(f);
populate(g.name,g.name)
})
}function populate(c,a){var b=classificationURL+"?classificationName="+c;
$.ajax({dataType:"json",url:b,success:function(d){createSubSelect(null,d,c,"",a)
},error:function(f,d,g){}})
}function removeNode(a){a.parentNode.removeChild(a);
return a
}function createOptionsTag(e){var a=e.tag;
var c=e.value;
var d=e.text;
var b=document.createElement(a);
b.value=c;
b.text=d;
return b
}function createNode(h){var a=h.tag;
var c=h.parent;
var g=h.childNodes;
delete h.tag;
delete h.parent;
delete h.childNodes;
var d=document.createElement(a);
if(g!=null){for(var b=0;
b<g.length;
b++){d.appendChild(createOptionsTag(g[b]))
}}for(var b in h){try{d[b]=h[b]
}catch(f){alert(f)
}}if(c){c.child=d
}return d
}function createNewClassField(a){var c=document.createElement("div");
c.id=a+"1";
var b=document.createElement("input");
b.type="text";
b.size="25";
b.id=c.id+"_input";
b.disabled="true";
c.appendChild(b);
$("#classifications").append(c);
populate("JEL",c.id)
}function _addClassificationItemToList(g,f){var b=document.createElement("div");
b.className="classificationListItemContainer";
var c=document.createElement("div");
var e=_removeSpecialChars(g+f);
c.setAttribute("id","classificationListItemElement"+e);
c.setAttribute("class","classificationListItem");
var d=document.createElement("input");
d.type="button";
d.className="ajaxButton btnspace";
d.value=getString("post.resource.openaccess.button.removeclassification");
d.id="classificationListItemRemove"+e;
var a=document.createElement("div");
a.setAttribute("class","classificationListItemDescriptionContainer");
var j=document.createElement("div");
j.setAttribute("id","classificationListItemElementDescription"+e);
j.setAttribute("class","classificationListItemDescription");
b.appendChild(c);
a.appendChild(j);
a.appendChild(d);
b.appendChild(a);
$("#"+g+"saved").append(b);
$("#"+"classificationListItemElement"+e).text(g+" "+f+" ");
var h=classificationURL+"?action="+GET_CLASSIFICATION_DESCRIPTION+"&key="+g+"&value="+f;
$.ajax({dataType:"json",url:h,success:function(k){$("#classificationListItemElementDescription"+_removeSpecialChars(k.name+k.value)).text(k.description)
},error:function(l,k,m){$("#classificationListItemElementDescription"+e).text("-")
}});
d.onclick=function(){var k=document.createElement("img");
k.setAttribute("src","/resources_puma/image/ajax-loader.gif");
var l=classificationURL+"?action="+REMOVE_CLASSIFICATION_ITEM+"&hash="+publication_intrahash+"&key="+g+"&value="+f;
$.ajax({dataType:"json",url:l,beforeSend:function(m){$("#classificationListItemRemove"+e).parent().append(k)
},success:function(m){$(b).remove();
$(k).remove()
},error:function(n,m,o){$(k).remove()
}})
}
}function addSaved(b,d,c){if(!$("#classificationListItemElement"+_removeSpecialChars(b+d)).length){var e=classificationURL+"?action="+SAVE_CLASSIFICATION_ITEM+"&hash="+publication_intrahash+"&key="+b+"&value="+d;
var a=document.createElement("img");
a.setAttribute("src","/resources_puma/image/ajax-loader.gif");
$.ajax({dataType:"json",url:e,beforeSend:function(f){$("#"+b).append(a)
},success:function(f){$(a).remove();
_addClassificationItemToList(b,d)
},error:function(g,f,h){$(a).remove()
}})
}}function createSaveButton(c,d,a){var b=createNode({tag:"input",className:"ajaxButton btnspace",parent:c,child:null,type:"button",value:getString("post.resource.openaccess.button.saveclassification"),childNodes:null,parentID:d,onclick:function(){addSaved(a,d,c.text)
},deleteChild:function(){if(this.child){if(this.child.deleteChild){this.child.deleteChild()
}removeNode(this.child);
this.child=null
}}});
$("#"+a).append(b)
}function createSubSelect(d,e,g,f,a){if(!e){return
}$("#"+a+"_input").text(f);
var b=[{tag:"option",value:"",text:g}];
$.each(e.children,function(h,j){b.push({tag:"option",value:j.id,text:j.id+" - "+j.description})
});
if(b.length==1){createSaveButton(d,f,a);
return
}var c=createNode({tag:"select",className:"classificationSelect",data:e,parent:d,child:null,size:"1",childNodes:b,parentID:f,classification:g,onchange:function(){this.deleteChild();
if(this.value==""){return
}var k=f+this.value;
var j=classificationURL+"?classificationName="+g+"&id="+k;
var h=document.createElement("img");
h.setAttribute("src","/resources_puma/image/ajax-loader.gif");
$.ajax({dataType:"json",url:j,beforeSend:function(l){$("#"+a).append(h)
},success:function(l){$(h).remove();
createSubSelect(c,l,g,k,a)
},error:function(m,l,n){$(h).remove()
}})
},deleteChild:function(){if(this.child){if(this.child.deleteChild){this.child.deleteChild()
}removeNode(this.child);
this.child=null
}}});
if(d){d.child=c
}$("#"+a).append(c)
}function sendAdditionalMetadataFields(){sendAdditionalMetadataFields(true)
}function sendAdditionalMetadataFields(e){if(e!=true){e=false
}var c="sendMetadataMarker";
var b=Array();
var d=0;
b=getMetadatafields();
var g="{ ";
var f={};
for(var d=0;
d<b.length;
d++){g+='"'+b[d]+'":"'+$("#"+(b[d].replace(/\./g,"\\."))).val()+'", '
}g+=" } ";
var h=classificationURL;
var a=document.createElement("img");
a.setAttribute("src","/resources_puma/image/ajax-loader.gif");
$.ajax({dataType:"json",url:h,async:e,data:{"action":SAVE_ADDITIONAL_METADATA,"hash":publication_intrahash,"value":g},type:"post",beforeSend:function(j){$("#"+elementId).append(a)
},success:function(j){$(a).remove();
setMetadataChanged(false)
},error:function(k,j,l){$(a).remove()
}})
}function loadAdditionalMetadataFields(){var a=classificationURL+"?action="+GET_ADDITIONAL_METADATA+"&hash="+publication_intrahash;
$.ajax({dataType:"json",url:a,success:function(b){$.each(b,function(c,d){$.each(d,function(e,f){$("#"+(c.replace(/\./g,"\\."))).val(f)
})
})
},error:function(c,b,d){}})
}function loadSentRepositories(){var a=oaBaseUrl+"?action="+GET_SENT_REPOSITORIES+"&interhash="+publication_interhash;
$.ajax({dataType:"json",url:a,success:function(b){if(!empty(b.posts)){$("#oaRepositorySent").append('<div id="oaRepositorySentHeader">'+getString("post.resource.openaccess.repository.sent.info")+":</div>");
$.each(b.posts,function(c,d){$.each(d.repositories,function(f,j){var g=new Date(j.date.time);
var h=g.getDate()+"."+(g.getMonth()+1)+"."+g.getFullYear();
var e=getString("post.resource.openaccess.repository.sent.versions");
$("#oaRepositorySent").append("<div>"+getString("post.resource.openaccess.repository.sent.date")+": "+h+'. <a href="/bibtex/2'+c+'">'+e+"</a>"+(d.selfsent==1?"":+getString("post.resource.openaccess.repository.sent.other"))+"</div")
})
});
$("#oaRepositorySentInfo").append("<div>"+getString("post.resource.openaccess.repository.sent.info")+".</div>");
foldUnfold("openAccessContainer")
}},error:function(c,b,d){}})
}function autoSaveMetadata(a){booleanValue=a?true:false;
if(booleanValue){if(autoSaveMetadataCounter>0){autoSaveMetadataCounter=4;
return
}autoSaveMetadataCounter=4
}else{autoSaveMetadataCounter--
}if(autoSaveMetadataCounter<1){sendAdditionalMetadataFields(false);
autoSaveMetadataCounter=0
}else{setTimeout("autoSaveMetadata()",700)
}}function setMetadataChanged(a){booleanValue=a?true:false;
elementId="sendMetadataMarker";
elementClass="highlight";
if(booleanValue==true){if(!$("#"+elementId).hasClass(elementClass)){$("#"+elementId).addClass(elementClass)
}setTimeout("autoSaveMetadata(true)",100)
}else{if($("#"+elementId).hasClass(elementClass)){$("#"+elementId).removeClass(elementClass)
}}metadataChanged=booleanValue
}function isMetadataChanged(){return metadataChanged
}function loadStoredClassificationItems(){var a=classificationURL+"?action="+GET_POST_CLASSIFICATION_LIST+"&hash="+publication_intrahash;
$.ajax({dataType:"json",url:a,success:function(b){$.each(b,function(c,d){$.each(d,function(e,f){if(!$("#classificationListItemElement"+_removeSpecialChars(c+f)).length){_addClassificationItemToList(c,f)
}})
})
},error:function(c,b,d){alert("There seems to be an error in the ajax request, openaccess.js::loadStoredClassificationItems")
}})
}function setBackgroundColor(a,b){$("#"+a).css("background-color",b)
}function checkauthorcontractconfirm(){if(document.getElementById("authorcontractconfirm").checked){if($("#oasendtorepositorybutton").hasClass("oadisabledsend2repositorybutton")){$("#oasendtorepositorybutton").removeClass("oadisabledsend2repositorybutton");
document.getElementById("oasendtorepositorybutton").disabled=false
}}else{if(!$("#oasendtorepositorybutton").hasClass("oadisabledsend2repositorybutton")){$("#oasendtorepositorybutton").addClass("oadisabledsend2repositorybutton");
document.getElementById("oasendtorepositorybutton").disabled=true
}}};