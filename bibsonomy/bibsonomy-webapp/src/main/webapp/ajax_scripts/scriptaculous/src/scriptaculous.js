var Scriptaculous={Version:"1.7.0_beta2",require:function(a){document.write('<script type="text/javascript" src="'+a+'"><\/script>')
},load:function(){if((typeof Prototype=="undefined")||(typeof Element=="undefined")||(typeof Element.Methods=="undefined")||parseFloat(Prototype.Version.split(".")[0]+"."+Prototype.Version.split(".")[1])<1.5){throw ("script.aculo.us requires the Prototype JavaScript framework >= 1.5.0")
}$A(document.getElementsByTagName("script")).findAll(function(a){return(a.src&&a.src.match(/scriptaculous\.js(\?.*)?$/))
}).each(function(b){var c=b.src.replace(/scriptaculous\.js(\?.*)?$/,"");
var a=b.src.match(/\?.*load=([a-z,]*)/);
(a?a[1]:"builder,effects,dragdrop,controls,slider").split(",").each(function(d){Scriptaculous.require(c+d+".js")
})
})
}};
Scriptaculous.load();