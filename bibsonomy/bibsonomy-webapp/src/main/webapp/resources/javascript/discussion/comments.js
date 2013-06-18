var COMMENTS_URL="/ajax/comments";
var REPLY_SELECTOR="a.reply";
var EDIT_COMMENT_LINKS_SELECTOR="a.commentEditLink";
var DELETE_COMMENT_LINKS_SELECTOR="a.commentDeleteLink";
var TOGGLE_REPLY_SELECTOR="a.toggleReplies";
$(function(){$(REPLY_SELECTOR).click(reply);
$(TOGGLE_REPLY_SELECTOR).click(function(){var a=$(this).parent().parent().siblings("ul.subdiscussionItems");
var d=a.is(":visible");
a.toggle("slow");
var c=getString("post.resource.discussion.replies.show");
var b=getString("post.resource.discussion.replies.show.title");
if(!d){c=getString("post.resource.discussion.replies.hide");
b=getString("post.resource.discussion.replies.hide.title")
}$(this).text(c).attr("title",b);
return false
});
$(EDIT_COMMENT_LINKS_SELECTOR).click(showEditCommentForm);
$(DELETE_COMMENT_LINKS_SELECTOR).click(deleteComment)
});
function reply(){showDiscussion();
var b=$(this).parent().parent().parent();
removeAllOtherDiscussionForms();
var a=getHash($(this));
var d=$("#createComment").clone();
d.attr("id",REPLY_FORM_ID);
var c=d.find("form");
c.append($("<input />").attr("name","discussionItem.parentHash").attr("type","hidden").attr("value",a));
c.submit(createComment);
c.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).click(onAbstractGroupingClick);
if(a!=undefined){b.append(d)
}else{$(DISCUSSION_SELECTOR).prepend(d)
}d.show();
c.find("textarea").TextAreaResizer();
scrollTo(REPLY_FORM_ID);
return false
}function showEditCommentForm(){var h=$(this).parent().parent().parent();
removeAllOtherDiscussionForms();
var g=$("#createComment").clone();
g.attr("id",EDIT_COMMENT_FORM_ID);
var d=g.find("form");
populateFormWithGroups(d,getAbstractGrouping(h),getGroups(h));
var c=h.find(".text:first").text();
d.find("textarea").attr("value",c);
if(h.hasClass(ANONYMOUS_CLASS)){d.find(ANONYMOUS_SELECTOR).attr("checked","checked")
}var b=getHash($(this));
d.append($("<input />").attr("name","discussionItem.hash").attr("type","hidden").attr("value",b));
d.append($("<input />").attr("name","_method").attr("type","hidden").attr("value","PUT"));
var a=getString("post.resource.comment.actions.edit");
g.find("h4").text(a);
d.find('input[type="submit"]').attr("value",a);
var f=d.find(".spinner");
var e=f.find("img");
f.empty().append(e).append(getString("post.resource.comment.action.update"));
d.find("textarea").TextAreaResizer();
d.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).click(onAbstractGroupingClick);
d.submit(updateComment);
h.append(g);
g.show("slow");
scrollTo(EDIT_COMMENT_FORM_ID);
return false
}function createComment(){var c=$(this);
c.unbind("submit");
var a=c.parent("#"+REPLY_FORM_ID);
var g=c.find(".spinner");
g.show();
var d=c.serialize();
var b=c.find("textarea");
var h=b.val();
var f=c.find(ANONYMOUS_SELECTOR).is(":checked");
var i=c.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+":checked").val();
var e=c.find(OTHER_GROUPING_CLASS_SELECTOR).val();
if(e==null){e=new Array()
}else{e=new Array(e)
}$.ajax({url:COMMENTS_URL,type:"POST",data:d,dataType:"json",success:function(k){var l=$("#commentTemplate").clone();
l.removeAttr("id");
updateCommentView(l,k.hash,h,f,i,e);
var j=$('<li class="comment"></li>');
j.append(l);
l.find("a.reply").click(reply);
l.find("a.editLink").click(showEditCommentForm);
if($(DISCUSSION_SELECTOR).children(REPLY_FORM_SELECTOR).length>0){$(DISCUSSION_SELECTOR+" ul.subdiscussionItems:first").prepend(j)
}else{a.parent().children(".subdiscussionItems").append(j)
}l.show();
a.remove();
g.hide();
c.submit(createComment);
showReviewForm();
highlight(j)
},error:function(j,k,l){handleAjaxErrors(c,jQuery.parseJSON(j.responseText));
c.submit(createComment)
}});
return false
}function updateCommentView(g,b,c,d,a,f){g.find(".text:first").text(c);
updateHash(g,b);
g.find("."+GROUPS_CLASS).remove();
var e=buildGroupView(a,f);
g.find(".info:first").append(e);
if(d){g.addClass(ANONYMOUS_CLASS)
}else{g.removeClass(ANONYMOUS_CLASS)
}}function updateComment(){var b=$(this);
var e=b.parent().parent();
b.unbind("submit");
var g=b.find(".spinner");
g.show();
var c=b.serialize();
var a=b.find("textarea");
var h=a.val();
var f=b.find(ANONYMOUS_SELECTOR).is(":checked");
var i=b.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+":checked").val();
var d=b.find(OTHER_GROUPING_CLASS_SELECTOR).val();
if(d==null){d=new Array()
}else{d=new Array(d)
}$.ajax({url:COMMENTS_URL,type:"POST",dataType:"json",data:c,success:function(j){updateCommentView(e,j.hash,h,f,i,d);
b.parent().remove();
highlight(e);
showReviewForm()
},error:function(j,k,l){handleAjaxErrors(b,jQuery.parseJSON(j.responseText));
b.submit(updateComment)
},});
return false
}function deleteComment(){var b=$(this);
b.siblings(".deleteInfo").show();
var d=getHash($(this));
var a=getInterHash();
var c=COMMENTS_URL+"?ckey="+ckey+"&hash="+a+"&discussionItem.hash="+d;
var e=b.parent().parent().parent();
b.remove();
$.ajax({url:c,type:"DELETE",success:function(){deleteDiscussionItemView(e)
},})
};