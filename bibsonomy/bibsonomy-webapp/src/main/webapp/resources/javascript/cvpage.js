$(function(){$("a.hand").each(function(a,b){$(b).click(function(){var c=$(this).next(".details").toggle();
if($(c).is(":visible")){$(this).html(" "+getString("cv.options.hide_details"))
}else{$(this).html(" "+getString("cv.options.show_details"))
}})
})
});