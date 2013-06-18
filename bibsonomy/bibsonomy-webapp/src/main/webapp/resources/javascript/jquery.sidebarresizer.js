(function(d){var a=null;
var k=null;
var e=0;
var b=null;
var i=(navigator.appVersion.indexOf("X11")!=-1)?"ew-resize":"e-resize";
d.fn.SideBarResizer=function(l){if(l!=null){a=d(this);
d(a).parent().css("overflow","hidden");
d(l).bind("mousedown",c)
}return this
};
function c(l){if(b==null){b=parseInt(d(a).width())
}a.css("opacity",0.7).parent().css("cursor",i);
d(window).bind("resize",f);
e=j(l).x;
d(document).mousemove(h).mouseup(g);
return false
}function h(m){var l=j(m).x;
var n=l-e;
e=l;
if((n>0&&parseInt(a.width())-n>20)||(n<0&&((parseInt(a.width())+Math.abs(n))<parseInt(b)))){a.width((parseInt(a.width())-n))
}return false
}function f(l){d(window).unbind("resize",f);
d(a).width("");
b=null
}function g(l){d(document).unbind("mousemove",h).unbind("mouseup",g);
e=0;
if(a!=null){a.css("opacity",1).parent().css("cursor","default")
}return false
}function j(l){return{x:l.clientX+document.documentElement.scrollLeft,y:l.clientY+document.documentElement.scrollTop}
}})(jQuery);