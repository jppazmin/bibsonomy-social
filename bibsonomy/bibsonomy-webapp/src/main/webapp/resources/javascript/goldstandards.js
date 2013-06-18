var FADE_DURATION=1000;
var GOLD_REFERENCE_URL="/ajax/goldstandards/references";
$(function(){$("#gold_exports").tabs();
if($("li.reference").length==1){$("#gold_references").hide()
}});
function editReferences(){if($(".reference_menu").length>0){removeReferenceMenu()
}else{$("#gold_references").show();
addReferenceMenu()
}}function addReferenceMenu(){$("li.reference").each(function(){var c=$('<span class="reference_menu"><a href="#">'+getString("post.actions.edit.gold.references.delete")+"</a></span>");
c.hide();
$(this).append(c);
c.fadeIn(FADE_DURATION);
c.click(deleteReference)
});
var b=$('<form class="reference_menu"></form>');
var a=$('<input type="text" />');
b.append($("<label>"+getString("post.actions.edit.gold.references.add")+": </label>"));
b.append(a);
a.autocomplete({source:function(d,c){$.ajax({url:"/json/tag/"+createParameters(d.term),data:{items:10,resourcetype:"goldStandardPublication",duplicates:"no"},dataType:"json",success:function(e){c($.map(e.items,function(f){return{label:f.label+" ("+f.year+")",value:f.interHash,author:(concatArray(f.author,40," "+getString("and")+" ")),authors:f.author,editors:f.editor,year:f.year,title:f.label}
}))
}})
},minLength:3,select:function(c,d){addReference(d.item);
return false
},focus:function(c,d){return false
}}).data("autocomplete")._renderItem=function(c,d){if(d.value==getGoldInterHash()){return c
}return $("<li></li>").data("item.autocomplete",d).append($("<a></a>").html(d.label+'<br><span class="ui-autocomplete-subtext">'+d.author+"</span>")).appendTo(c)
};
b.hide();
$("#gold_references").append(b);
b.fadeIn(FADE_DURATION)
}function removeReferenceMenu(){$(".reference_menu").fadeOut(FADE_DURATION,function(){$(this).remove()
})
}function deleteReference(){var b=$(this).parent("li");
var a=b.data("interhash");
if(confirm(getString("post.actions.edit.gold.references.delete.confirm"))){$.ajax({url:GOLD_REFERENCE_URL+"?ckey="+getCKey()+"&hash="+getGoldInterHash()+"&references="+a,type:"DELETE",success:function(c){b.fadeOut(FADE_DURATION,function(){b.remove()
})
}})
}return false
}function addReference(a){var b=a.value;
$.ajax({url:GOLD_REFERENCE_URL,data:{ckey:getCKey(),hash:getGoldInterHash(),references:b},type:"POST",success:function(e){var d=$("#referenceTemplate").clone();
d.attr("id","");
d.attr("data-interhash",b);
var c=d.find(".authorEditorList");
c.append(getAuthorsEditors(a.authors,a.editors));
var f=d.find(".publicationLink");
f.attr("href","/bibtex/"+b);
f.text(a.title);
d.find(".year").text(a.year);
$("#gold_references ol").append(d);
d.show();
$("span.reference_menu a").click(deleteReference)
}})
}function getAuthorsEditors(a,b){if(a.length>0){return getPersonList(a)
}return getPersonList(b)+" ("+getString("bibtex.editors.abbr")+")"
}function getPersonList(g){var b=new Array();
for(var e=0;
e<g.length;
e++){if(e!=0){b.push(", ")
}if(e==(g.length-1)&&e!=0){b.push(getString("and"));
b.push(" ")
}var f=g[e];
var a=f.split(" ");
for(var d=0;
d<a.length-1;
d++){b.push(a[d]);
b.push(" ")
}var c=a[a.length-1];
b.push('<a href="/author/'+c+'">');
b.push(c);
b.push("</a>")
}return b.join("")
}function getCKey(){return $("#gold_menu").data("ckey")
}function getGoldInterHash(){return $("#gold_title").data("interhash")
};