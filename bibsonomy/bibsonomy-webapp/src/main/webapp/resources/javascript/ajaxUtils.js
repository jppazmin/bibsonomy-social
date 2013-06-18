var ERROR_CLASS="dissError";
var FIELD_DATA_KEY="field";
function handleAjaxErrors(a,b){a.find(".spinner").hide();
if(b.globalErrors){$.each(b.globalErrors,function(d,c){alert(decodeHTML(c.message))
})
}if(b.fieldErrors){$.each(b.fieldErrors,function(e,d){var g=d.message;
var h=d.field;
var c="[name="+escapeSelector(h)+"]:first";
var f=a.find(c);
if(f.length!=0){var i=$("<div></div>").addClass(ERROR_CLASS).css("display","block");
i.html(g);
i.data(FIELD_DATA_KEY,h);
f.after(i)
}else{}});
prepareAjaxErrorBoxes(ERROR_CLASS)
}}function escapeSelector(a){return a.replace(/\./g,"\\.")
}function decodeHTML(a){return $("<div/>").html(a).text()
}function prepareAjaxErrorBoxes(a){$("."+a).each(function(){var b=$(this);
if(parseInt(b.html().length)==0){return true
}b.mouseover(function(){$(this).fadeOut("slow")
});
var c=b.data(FIELD_DATA_KEY);
b.siblings("[name="+escapeSelector(c)+"]").bind("change keyup",function(){b.fadeOut("slow")
})
})
};