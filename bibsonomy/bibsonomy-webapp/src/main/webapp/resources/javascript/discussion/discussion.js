var ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR='input[name="abstractGrouping"]';
var OTHER_GROUPING_CLASS_SELECTOR=".otherGroupsBox";
var DISCUSSION_MENU_SELECTOR="#discussionMainMenu";
var DISCUSSION_SELECTOR="#discussion";
var REVIEW_INFO_SELECTOR="#review_info_rating";
var DISCUSSION_TOGGLE_LINK_SELECTOR="#toggleDiscussion";
var REVIEW_OWN_ID="ownReview";
var REVIEW_OWN_SELECTOR="#"+REVIEW_OWN_ID;
var REVIEW_UPDATE_FORM_SELECTOR="form.editreview";
var REPLY_FORM_ID="replyForm";
var REPLY_FORM_SELECTOR="#"+REPLY_FORM_ID;
var EDIT_COMMENT_FORM_ID="editcomment";
var EDIT_FORM_SELECTOR="#"+EDIT_COMMENT_FORM_ID;
var CREATE_REVIEW_LINKS_SELECTOR="a.createReview";
var REVIEW_CREATE_FORM_SELECTOR="form.createreview";
var ANONYMOUS_CLASS="anonymous";
var ANONYMOUS_SELECTOR="input[name=discussionItem\\.anonymous]";
var BOOKMARK_LIST_SELECTOR="#bookmarkList";
var PUBLICATION_LIST_SELECTOR="#bibtexList";
var GROUPS_CLASS="groups";
var PUBLIC_GROUPING="public";
var PRIVATE_GROUPING="private";
var OTHER_GROUPING="other";
var FRIENDS_GROUP_NAME="friends";
var DISCUSSIONITEM_DATA_KEY="discussionItemHash";
$(function(){if($(REVIEW_OWN_SELECTOR).length>0){removeReviewActions()
}$(DISCUSSION_TOGGLE_LINK_SELECTOR).click(function(){$(REVIEW_INFO_SELECTOR).toggle("slow");
$(DISCUSSION_SELECTOR).toggle("slow",updateDiscussionToggleLink);
return false
});
$(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).click(onAbstractGroupingClick);
$.each($(".abstractGroupingGroup"),function(a,b){toggleGroupBox(b)
})
});
function updateDiscussionToggleLink(){var b=$(DISCUSSION_SELECTOR).is(":visible");
var a=getString("post.resource.discussion.actions.show");
if(b){a=getString("post.resource.discussion.actions.hide")
}$(DISCUSSION_TOGGLE_LINK_SELECTOR).text(a)
}function showDiscussion(){$(DISCUSSION_SELECTOR).show();
$(REVIEW_INFO_SELECTOR).show();
updateDiscussionToggleLink()
}function removeAllOtherDiscussionForms(){$(EDIT_FORM_SELECTOR).remove();
$(REVIEW_UPDATE_FORM_SELECTOR).hide();
$(REVIEW_CREATE_FORM_SELECTOR).parent().hide();
$(REPLY_FORM_SELECTOR).remove()
}function showReviewForm(){$(REVIEW_CREATE_FORM_SELECTOR).parent().show()
}function removeReviewActions(){$(CREATE_REVIEW_LINKS_SELECTOR).parent().hide();
$(REVIEW_CREATE_FORM_SELECTOR).parent().remove()
}function addReviewActions(){$(CREATE_REVIEW_LINKS_SELECTOR).parent().show()
}function onAbstractGroupingClick(){toggleGroupBox($(this).parent())
}function toggleGroupBox(c){var a=$(c).children("input:checked");
var b=$(c).siblings(OTHER_GROUPING_CLASS_SELECTOR);
if(!a.hasClass("otherGroups")){b.attr("disabled","disabled")
}else{b.removeAttr("disabled")
}}function populateFormWithGroups(b,c,a){b.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).removeAttr("checked");
b.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+'[value="'+c+'"]').attr("checked","checked");
var d=b.find(OTHER_GROUPING_CLASS_SELECTOR);
if(a.length>0){d.removeAttr("disabled");
d.find("input").removeAttr("selected");
$.each(a,function(e,f){d.find('[value="'+f+'"]').attr("selected","selected")
})
}else{d.attr("disabled","disabled")
}}function getGroups(d){var e=d.find(".groups:first");
var b=e.text();
var a=new Array();
if(b!=""){var c=e.find("a");
if((c.length==0)&&(b.indexOf(getString("post.groups.private"))!=-1)){return a
}if(b.indexOf(getString("post.groups.friends"))!=-1){a.push(FRIENDS_GROUP_NAME)
}$.each(c,function(f,g){a.push($(g).text())
});
return a
}return a
}function getAbstractGrouping(c){var d=c.find(".groups:first");
var a=d.text();
if(a!=""){var b=d.find("a");
if(b.length==0){if(a.indexOf(getString("post.groups.private"))!=-1){return PRIVATE_GROUPING
}return OTHER_GROUPING
}return OTHER_GROUPING
}return PUBLIC_GROUPING
}function buildGroupView(c,a){if(c!=="public"){var b=$("<span></span>").addClass(GROUPS_CLASS);
b.append(getString("post.resource.comment.groups")+" ");
if(c=="private"){b.append(getString("post.groups.private"))
}else{var d=a.length;
$.each(a,function(f,g){if(g==FRIENDS_GROUP_NAME){b.append(getString("post.groups.friends"));
return
}var e=$("<a></a>").attr("href","/group/"+g);
e.html(g);
b.append(e);
if(d!=1&&(d-1)==f){b.append(", ")
}})
}return b
}return""
}function deleteDiscussionItemView(a,c){if(a.find("ul.subdiscussionItems:first li").length>0){a.removeAttr("id");
a.removeClass();
a.addClass("discussionitem");
a.find("img:first").remove();
a.find(".details:first").remove();
a.find(".createReview:first").parent().remove();
a.find(".deleteInfo:first").parent().remove();
a.find("a.editLink:first").parent().remove();
a.find("a.reply:first").parent().remove();
var b=$('<div class="deletedInfo"></div').text(getString("post.resource.discussion.info"));
a.prepend(b);
highlight(b);
if(c!=undefined){c()
}}else{a.fadeOut(1000,function(){$(this).remove();
if(c!=undefined){c()
}})
}}function updateHash(a,b){$(a).find("div.info:first").data(DISCUSSIONITEM_DATA_KEY,b);
$(a).find('input[name="discussionItem\\.hash"]:first').attr("value",b)
}function getInterHash(){return $(DISCUSSION_SELECTOR).data("interHash")
}function getHash(a){return $(a).parent().parent().siblings(".details").find(".info").data(DISCUSSIONITEM_DATA_KEY)
}function highlight(a){$(a).css("background-color","#fff735").animate({backgroundColor:"#ffffff"},1000)
}function scrollTo(b){var a=$("#"+b);
if(a.length){$("html,body").animate({scrollTop:a.offset().top-100},"slow")
}};