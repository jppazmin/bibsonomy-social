function checkUrlForTitle(){var req=new XMLHttpRequest();
req.open("GET","/generalAjax?action=getTitleForUrl&pageURL="+encodeURIComponent(document.getElementById("post.resource.url").value),true);
req.onreadystatechange=function(){if(req.readyState==4){if(req.status==200){var result=eval("("+req.responseText+")");
if((result.pageTitle!="")&&(document.getElementById("post.resource.title").value=="")){addSuggestionLink("resource.title",result.pageTitle)
}if(result.pageDescription!=""){addSuggestionLink("description",result.pageDescription)
}}}};
req.send(null)
}function addSuggestionLink(c,d){var b=document.createElement("a");
b.onclick=function(){document.getElementById("post."+c).value=d;
document.getElementById("suggestion."+c).innerHTML=""
};
b.appendChild(document.createTextNode(d));
b.style.cursor="pointer";
document.getElementById("suggestion."+c).appendChild(b)
}var tagRecoOptions={url:"/ajax/getBookmarkRecommendedTags",success:function showResponse(a,b){handleRecommendedTags(a)
}};