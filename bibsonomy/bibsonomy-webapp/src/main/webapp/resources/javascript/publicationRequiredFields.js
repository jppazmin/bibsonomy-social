function highlightMatches(d,a){var c=a.split(" ");
for(var b=0;
b<c.length;
b++){d=highlightMatch(d,c[b])
}return d
}function highlightMatch(b,a){return b.replace(new RegExp("(?![^&;]+;)(?!<[^<>]*)("+$.ui.autocomplete.escapeRegex(a)+")(?![^<>]*>)(?![^&;]+;)","gi"),"<strong>$1</strong>")
}function myownTagInit(b,a){var c=/((^|[ ])myown($|[ ]))/gi;
if(!(b.length>0&&a.length>0)){return
}if(a.val().search(c)!=-1){b[0].checked=true
}a.keyup(function(){if(a.val().search(c)!=-1){b[0].checked=true;
return
}b[0].checked=false
});
b.click(function(){clear_tags();
if(this.checked&&a.val().search(c)==-1){a.removeClass("descriptiveLabel").val("myown "+a.val())
}else{if(!this.checked){a.val(a.val().replace(c," ").replace(/^[ ]?/,""))
}}}).parent().removeClass("hiddenElement")
}$(document).ready(function(){myownTagInit($("#myownChkBox"),$("#inpf"))
});
function initSuggestionForPartTitles(a){a.each(function(b){$(this).autocomplete({source:function(d,c){$.ajax({url:"/json/tag/"+createParameters(d.term),data:{items:10,resourcetype:"publication",duplicates:"no"},dataType:"jsonp",success:function(e){c($.map(e.items,function(f){return{label:(highlightMatches(f.label,d.term)+" ("+f.year+")"),value:f.interHash,url:"hash="+f.intraHash+"&user="+f.user+"&copytag="+f.tags,author:(concatArray(f.author,40," "+getString("and")+" ")),user:f.user,tags:f.tags}
}))
}})
},minLength:3,select:function(c,d){window.location.href="/editPublication?"+d.item.url;
return false
},focus:function(c,d){return false
}}).data("autocomplete")._renderItem=function(c,d){return $("<li></li>").data("item.autocomplete",d).append($("<a></a>").html(d.label+'<br><span class="ui-autocomplete-subtext">'+d.author+" "+getString("by")+" "+d.user+"</span>")).appendTo(c)
}
})
};