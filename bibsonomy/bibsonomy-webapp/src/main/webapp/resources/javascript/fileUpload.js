$(function(){$(".deleteDocument").click(deleteLinkClicked)
});
function deleteLinkClicked(){var a=$(this);
$.get($(a).attr("href"),{},function(c){var b=$("status",c).text();
if(b=="error"){alert($("reason",c).text())
}else{var d=$(a).next(".documentFileName").text();
alert($("response",c).text());
$(a).parent(".fsRow").remove()
}},"xml");
return false
}$(function(){$(".addDocument").click(function(){var b=false;
$(".fu").each(function(){if($(this).val()==""){b=true
}});
if(b){return false
}var c=$(".intraHash").val();
var a="<form class='upform' action='/ajax/documents?ckey="+fuCkey+"&temp=false&intraHash="+c+"' method='POST' enctype='multipart/form-data'>"+"<input class='fu' type='file' name='file'/></form>";
$("#inputDiv").append($(a));
$(".fu").change(fileSelected);
return false
})
});
function fileSelected(b){var g=$(b.currentTarget).val();
var a=$(b.currentTarget).parent(".upform");
g=g.replace("C:\\fakepath\\","");
var d=false;
$(".documentFileName").each(function(){var h=jQuery.trim($(this).text());
if(h==g){d=true
}});
if(d){alert(getString("post.bibtex.fileExists"));
$(a).remove();
return
}$(a).ajaxSubmit({dataType:"xml",success:uploadRequestSuccessful});
$(a).hide();
var f=replaceInvalidChrs(g);
var c=$(".resdir");
var e="<div class='fsRow' id='"+f+"' >"+g+"<img alt='uploading...' src='"+c.val()+"/image/ajax_loader.gif' /></div>";
$("#files").append($(e))
}function replaceInvalidChrs(b){var d="!@#$%^&*()+=-[]\\';,./{}|\":><?~_";
var a="";
for(var c=0;
c<b.length;
c++){if(d.indexOf(b.charAt(c))!=-1){a+=b.charCodeAt(c)
}else{a+=b.charAt(c)
}}return a
}function uploadRequestSuccessful(f){var d=$("status",f).text();
if(d=="error"){alert($("reason",f).text());
var c=$("filename",f).text();
var b=replaceInvalidChrs(c);
$("#"+b).remove();
return
}if(d=="ok"){var h=$("fileid",f).text();
var i=$("filehash",f).text();
var c=$("filename",f).text();
var e=$(".resdir").val();
var g=getString("bibtex.actions.private_document.delete");
var a="<div class='fsRow'><a class='documentFileName' href='/documents/"+fuIntraHash+"/"+fuUserName+"/"+c+"'><img alt='"+getString("bibtex.actions.private_document.download")+"' src='"+e+"/image/document-txt-blue.png' style='float: left;'/>"+c+"</a> (<a class='deleteDocument' href='/ajax/documents?intraHash="+fuIntraHash+"&fileName="+c+"&ckey="+ckey+"&temp=false&action=delete'>"+g+"</a>)</div>";
$("#files").append(a);
$(".deleteDocument").click(deleteLinkClicked);
var b=replaceInvalidChrs(c);
$("#"+b).remove();
return
}};