var tagRecoOptions={dataType:"xml",url:"/ajax/getPublicationRecommendedTags",success:function showResponse(a,b){handleRecommendedTags(a)
}};
var hide=true;
var fields=new Array("booktitle","journal","volume","number","pages","publisher","address","month","day","edition","chapter","key","type","annote","note","howpublished","institution","organization","school","series","crossref","misc");
function getRequiredFieldsForType(a){switch(a){case"article":return new Array("journal","volume","number","pages","month","note");
break;
case"book":return new Array("publisher","volume","number","series","address","edition","month","note");
break;
case"booklet":return new Array("howpublished","address","month","note");
break;
case"inbook":return new Array("chapter","pages","publisher","volume","number","series","type","address","edition","month","note");
break;
case"incollection":return new Array("publisher","booktitle","volume","number","series","type","chapter","pages","address","edition","month","note");
break;
case"inproceedings":return new Array("publisher","booktitle","volume","number","series","pages","address","month","organization","note");
break;
case"manual":return new Array("organization","address","edition","month","note");
break;
case"mastersthesis":return new Array("school","type","address","month","note");
break;
case"misc":return new Array("howpublished","month","note");
break;
case"phdthesis":return new Array("school","address","type","month","note");
break;
case"proceedings":return new Array("publisher","volume","number","series","address","month","organization","note");
break;
case"techreport":return new Array("institution","number","type","address","month","note");
break;
case"unpublished":return new Array("month","note");
break;
default:return fields;
break
}}function changeView(){if(hide==false){return
}var a=getRequiredFieldsForType(document.getElementById("post.resource.entrytype").value);
for(var b=0;
b<fields.length;
b++){showHideElement(fields[b],in_array(a,fields[b])?"":"none")
}}function showAll(){hide=false;
document.getElementById("collapse").firstChild.nodeValue=getString("post.resource.fields.detailed.show.required");
document.getElementById("collapse").href="javascript:hideElements();";
for(i=0;
i<fields.length;
i++){showHideElement(fields[i],"")
}}function hideElements(){hide=true;
document.getElementById("collapse").firstChild.nodeValue=getString("post.resource.fields.detailed.show.all");
document.getElementById("collapse").href="javascript:showAll();";
changeView()
}function showHideElement(c,b){var a=document.getElementById("post.resource."+c);
if(a.value==""){$(a).closest(".fsRow").css("display",b)
}}function in_array(c,b){for(var a=0;
a<c.length;
a++){if(c[a]==b){return true
}}return false
}function generateBibTexKey(c){var a="";
a+=getFirstPersonsLastName(document.getElementById("post.resource.author").value);
var b=document.getElementById("post.resource.year").value;
if(b!=null){a+=b.trim()
}var d=document.getElementById("post.resource.title").value;
if(d!=null){a+=getFirstRelevantWord(d).toLowerCase()
}if(a.length==0){window.alert(getString("error.field.valid.bibtexKey.generation"))
}else{document.getElementById("post.resource.bibtexKey").value=a.toLowerCase()
}}function getFirstPersonsLastName(a){if(a!=null){var d;
var e=a.indexOf("\n");
if(e<0){d=a
}else{d=a.substring(0,e)
}var c=d.search(/\s\S+$/);
var b;
if(c<0){b=d
}else{b=d.substring(c+1,d.length)
}return b
}return""
}function getFirstRelevantWord(b){split=b.split(" ");
for(i in split){var a=new RegExp("[^a-zA-Z0-9]","g");
ss=split[i].replace(a,"");
if(ss.length>4){return ss
}}return""
}$(window).load(function(){if(document.getElementById("post.resource.publisher")){changeView()
}});