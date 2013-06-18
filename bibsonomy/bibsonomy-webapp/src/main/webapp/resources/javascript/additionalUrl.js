function deleteUrl(a,b,d,c){$.ajax({type:"GET",url:"/ajax/additionalURLs",data:{action:"deleteUrl",url:b,hash:d,ckey:c},success:function(f){var e=$("status",f).text();
if("ok"==e){$(a).parent().remove()
}else{alert(f.globalErrors[0].message)
}}})
}$(function(){$(".postUrl").click(function(){var a={success:function(g){var e=$(this);
var d=$("url",g).text();
var b=$("status",g).text();
var c=$("text",g).text();
var f=$("ckey",g).text();
var h=$("hash",g).text();
if("ok"==b){$("#urlList").prepend(function(){var j=$('<a href="'+d+'">'+c+"</a>");
var i=$("<div></div>").append(j).append(" (").append($('<a href="">'+getString("post.bibtex.delete")+"</a>").click(function(){deleteUrl(this,d,h,f);
return false
})).append(")");
return i
})
}else{alert(g.globalErrors[0].message)
}},};
$("#f_addURL").ajaxSubmit(a)
})
});
function addUrlForm(){document.getElementById("f_addURL").style.display="block"
};