(function(a){a.fn.fadeBox=function(b){b.className=b.className;
a(this).each(function(){var d=((b.element&&typeof b.element!="undefined")?a(b.element).addClass(b.className):a('<div style="position:absolute" class="'+b.className+'"></div>'));
var c=0;
var f=((typeof b.contentCallback==="string")?b.contentCallback:b.contentCallback(this));
var e=this;
var g=b.timeout;
var i=function(){c=setTimeout(function(){d.fadeOut("slow")
},g)
};
var h=function(){var k=((b.leftOffset&&b.leftOffset!="undefined")?b.leftOffset(a(e)):((a(e).offset().left+a(e).width()/2-d.width()/2>0)?((a(document).width()>(a(e).offset().left+a(e).width()/2)-d.width()/2)?a(e).offset().left+a(e).width()/2-d.width()/2:a(document).width()-d.width()):0));
var j=((b.topOffset&&typeof b.topOffset!="number")?b.topOffset(a(e)):a(e).offset().top-b.topOffset);
d.css({"left":k+"px","top":j+"px"});
clearTimeout(c);
d.show().fadeIn("slow")
};
a("body").append(d);
d.html(f);
a(e).mouseover(function(){g=b.timeout;
h()
}).mouseout(i).css("cursor","pointer").click(function(){g=3*b.timeout;
h();
i()
});
d.mouseover(function(){clearTimeout(c)
}).mouseout(i)
})
}
})(jQuery);
function sysTagFadeBox(a){$(".hiddenSystemTag").fadeBox({className:"initiallyHidden listhButtons",contentCallback:function(b){var d=($(b).attr("tags")).replace(/^\s|\s$/g,"").split(" ");
var c=$(b).attr("url");
var e=0;
var f='<a href="'+c+d[e]+'">'+d[e]+"</a>";
while(++e<d.length){f+=' <a href="'+c+d[e]+'">'+d[e]+"</a>"
}return f
},timeout:1500,topOffset:25})
};