(function(a){a.fn.fontResize=function(b){var c=this;
currentFontSize=parseInt(a(c).css("font-size"));
method=(currentFontSize-b>0)?-1:1;
if(method*currentFontSize>=method*b){return c
}a(c).css("font-size",(method+currentFontSize)+"px");
window.setTimeout(function(){a(c).fontResize(b)
},30)
}
})(jQuery);