function errorBoxData(a){this.msg=null;
this.parentId=a
}function displayFileErrorBox(a){$(a.parentId).children("div:first").fadeOut("slow",function(){$(this).fadeIn("slow").children(":first").html(a.msg)
})
}var errorData=new errorBoxData("#upload");
(function(c){var a;
c.fn.documentUploader=function(){a=c(".ck").val();
c(this).change(function(j){var f=c(".counter").val();
var k=c(this).val();
var i=false;
f++;
k=k.replace("C:\\fakepath\\","");
c(".upform").each(function(){var e=c(this).find("#fu").val();
if(k==e){i=true
}});
c(".documentFileName").each(function(){var e=jQuery.trim(c(this).text());
if(k==e){i=true
}});
if(i){errorData.msg=getString("post.bibtex.fileExists");
displayFileErrorBox(errorData);
c("#fu").replaceWith(c("<input id='fu' type='file' name='file'/>"));
c("#fu").documentUploader();
return
}c("#upload").children("ul").append(c("<li class='loading' id='file_"+f+"'><span class='documentFileName'>"+k+"</span></li>"));
var h=("<form class='upform' id='uploadForm_"+f+"' action='/ajax/documents?ckey="+a+"&amp;temp=true' method='POST' enctype='multipart/form-data'></form>");
c("#hiddenUpload").append(h);
h="uploadForm_"+f;
c(this).appendTo(c("#"+h));
c("<input class='fileID' type='hidden' name='fileID' value='"+f+"'/>").appendTo(c("#"+h));
c("#upload").show();
c(".counter").val(f);
var g={dataType:"xml",success:b};
c("#"+h).ajaxSubmit(g);
c("#inputDiv").append(c("<input id='fu' type='file' name='file'/>"));
c("#fu").documentUploader()
})
};
function b(f){if(c("status",f).text()=="ok"){return d(f)
}var e=prepareFileErrorBox(f);
c("#file_"+e).removeClass("loading").addClass("fileError")
}function d(h){var e=c("fileid",h).text();
var g=c("filehash",h).text();
var i=c("filename",h).text();
var f=c("<a class='deleteTempDocument' href='/ajax/documents?fileHash="+g+"&amp;ckey="+a+"&amp;temp=true&amp;fileID="+e+"&amp;action=delete'>"+getString("post.bibtex.delete")+"</a>").click(function(){return deleteFunction(this)
});
c("#file_"+e).append("<input type='hidden' class='tempFileName' name='fileName' value='"+g+i+"'/>").append(" (").append(f).append(")").removeClass("loading")
}})(jQuery);
function deleteFunction(a){$.get($(a).attr("href"),{},function(c){var b=$("fileid",c).text();
if("ok"==$("status",c).text()||"deleted"==$("status",c).text()){errorData.msg=$("response",c).text();
$(a).parent().remove();
if(b!=""){$("#file_"+b).remove();
$("#uploadForm_"+b).remove()
}}else{errorData.msg=$("reason",c).text()
}displayFileErrorBox(c)
},"xml");
return false
}function prepareFileErrorBox(b){var a="NaN";
var c="Unknown Error!";
if($("status",b).text()=="error"){a=$("fileid",b).text();
c=$("reason",b).text()
}errorData.msg=c;
displayFileErrorBox(errorData);
return a
};