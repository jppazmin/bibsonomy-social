(function(c){var h,i;
var d=0;
var a=32;
var e;
c.fn.TextAreaResizer=function(){return this.each(function(){if(!c(this).hasClass("textarea-resizer-processed")){c(this).addClass("textarea-resizer-processed");
c(this).wrap('<div class="resizable-textarea"><span></span></div>').parent().append(c('<div class="grippie"></div>'))
}h=c(this);
i=null;
var k=c("div.grippie",c(this).parent())[0];
k.style.marginRight=(k.offsetWidth-c(this)[0].offsetWidth)+"px";
c(k).bind("mousedown",{el:this},b)
})
};
function b(k){h=c(k.data.el);
h.blur();
d=j(k).y;
i=h.height()-d;
h.css("opacity",0.25);
c(document).mousemove(g).mouseup(f);
return false
}function g(m){var k=j(m).y;
var l=i+k;
if(d>=(k)){l-=5
}d=k;
l=Math.max(a,l);
h.height(l+"px");
if(l<a){f(m)
}return false
}function f(k){c(document).unbind("mousemove",g).unbind("mouseup",f);
h.css("opacity",1);
h.focus();
h=null;
i=null;
d=0
}function j(k){return{x:k.clientX+document.documentElement.scrollLeft,y:k.clientY+document.documentElement.scrollTop}
}})(jQuery);