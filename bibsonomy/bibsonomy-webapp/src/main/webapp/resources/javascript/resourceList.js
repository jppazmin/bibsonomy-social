function expandBookmarkList(){$("#bibtexList").animate({width:0,opacity:0},"slow").hide("1");
$("#bookmarkList").animate({width:"97%",opacity:1},"slow",function(){$(this).css("position","static")
}).show("1");
$("#optionExpandBookmark").addClass("hiddenElement");
$("#optionShowBoth").removeClass("hiddenElement");
$("#optionExpandBibtex").removeClass("hiddenElement")
}function expandBibTexList(){$("#bookmarkList").animate({width:0,opacity:0},"slow",function(){$(this).css("position","absolute")
}).hide("1");
$("#bibtexList").animate({width:"97%",opacity:1},"slow").show("1");
$("#optionExpandBookmark").removeClass("hiddenElement");
$("#optionShowBoth").removeClass("hiddenElement");
$("#optionExpandBibtex").addClass("hiddenElement")
}function showBothLists(){$("#bibtexList").animate({width:"47%",opacity:1},"slow",function(){$(this).css("position","static")
}).show("1");
$("#bookmarkList").animate({width:"47%",opacity:1},"slow",function(){$(this).css("position","static")
}).show("1");
$("#optionExpandBookmark").removeClass("hiddenElement");
$("#optionShowBoth").addClass("hiddenElement");
$("#optionExpandBibtex").removeClass("hiddenElement")
};