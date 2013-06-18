var REVIEWS_URL="/ajax/reviews";
var STAR_WIDTH=16;
var RATING_STEPS=11;
var STEP_RATING=2;
var RATING_AVG_DIV_SELECTOR="#ratingAvg";
var RATING_AVG_SELECTOR=RATING_AVG_DIV_SELECTOR+" span[property=v\\:average]";
var REVIEW_EDIT_LINK_SELECTOR="a.reviewEditLink";
var REVIEW_DELETE_LINK_SELECTOR="a.reviewDeleteLink";
var REVIEW_TEXTAREA_SELECTOR='textarea[name="discussionItem\\.text"]';
var REVIEW_ANONYMOUS_SELECTOR='input[name="discussionItem\\.anonymous"]';
var REVIEW_RATING_SELECTOR=".reviewrating";
$(function(){plotRatingDistribution();
initStars();
$(REVIEW_UPDATE_FORM_SELECTOR).hide();
if($("#noReviewInfo").length>0){$(RATING_AVG_DIV_SELECTOR).hide();
$("#ratingDistribution").hide()
}$(CREATE_REVIEW_LINKS_SELECTOR).click(showReviewForm);
$(REVIEW_CREATE_FORM_SELECTOR).submit(createReview);
$(REVIEW_UPDATE_FORM_SELECTOR).submit(updateReview);
$(REVIEW_OWN_SELECTOR).find(REVIEW_DELETE_LINK_SELECTOR).click(deleteReview);
$(REVIEW_OWN_SELECTOR).find(REVIEW_EDIT_LINK_SELECTOR).click(showUpdateReviewForm)
});
function showUpdateReviewForm(){removeAllOtherDiscussionForms();
$(REVIEW_UPDATE_FORM_SELECTOR).toggle("slow")
}function initStars(){$(".reviewrating").stars({split:STEP_RATING})
}function getReviewCount(){return parseInt($("#review_info_rating span[property=v\\:count]").text())
}function getAvg(){return Number($(RATING_AVG_SELECTOR).text().replace(",","."))
}function setAvg(b){b=b.toFixed(2);
$(RATING_AVG_SELECTOR).text(b);
var a=getStarsWidth(b);
$("#review_info_rating .stars-on-1").css("width",a);
$(PUBLICATION_LIST_SELECTOR+" .stars-on-0.75").css("width",a);
$(BOOKMARK_LIST_SELECTOR+" .stars-on-0.75").css("width",a)
}function showReviewForm(){removeAllOtherDiscussionForms();
showDiscussion();
$(REVIEW_CREATE_FORM_SELECTOR).parent().show();
return true
}function setReviewCount(a){var b=getString("post.resource.review.review");
if(a>1){b=getString("post.resource.review.reviews")
}$("#review_info_rating span[property=v\\:count]").text(a);
$("#review_info_rating span[property=v\\:count]").next("span").text(b)
}function getOwnReviewRating(){return Number($("#ownReview .rating").data("rating"))
}function validateRating(a){var b=getRating(a);
if(b==0){if(!confirm(getString("post.resource.review.rating0"))){return false
}}return true
}function plotRatingDistribution(){if($("#ratingDistributionGraph").length==0){return
}var c=[];
var g=[];
var f=[];
$(".subdiscussionItems li").not("#newReview").find(".rating").each(function(){var h=$(this).data("rating");
if(c[h]){c[h]+=1
}else{c[h]=1
}});
for(var b=0;
b<RATING_STEPS;
b++){var a=b/STEP_RATING;
var e=0;
if(c[a]){e=c[a]
}var d=getReviewCount();
if(e>0){e=e/d*100
}else{e=Number.NaN
}f.push([a,e]);
g.push(a)
}$.plot($("#ratingDistributionGraph"),[f],{bars:{show:true,align:"center",barWidth:0.2,fill:0.7,},xaxis:{ticks:g,tickDecimals:1,tickColor:"transparent",autoscaleMargin:0.02,},yaxis:{show:false,min:0,max:110,},grid:{markings:[{xaxis:{from:getAvg(),to:getAvg()},yaxis:{from:0,to:110},color:"#bb0000"}]}})
}function createReview(){var f=$(this);
f.unbind("submit");
var a=f.find(REVIEW_RATING_SELECTOR);
if(!validateRating(a)){f.submit(createReview);
return false
}var i=f.find(".spinner");
i.show("slow");
var d=f.find(REVIEW_TEXTAREA_SELECTOR).val();
var g=f.find(REVIEW_ANONYMOUS_SELECTOR).is(":checked");
var e=getRating(a);
var c=f.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+":checked").val();
var b=f.find(OTHER_GROUPING_CLASS_SELECTOR).val();
if(b==null){b=new Array()
}else{b=new Array(b)
}var h=f.serialize();
$.ajax({url:REVIEWS_URL,type:"POST",dataType:"json",data:h,success:function(n){var p=$("#newReview").remove();
p.attr("id","ownReview");
updateReviewView(p,d,e,c,b);
$(DISCUSSION_SELECTOR+" .subdiscussionItems:first").prepend(p);
updateHash(p,n.hash);
highlight(p);
var m=$(REVIEW_UPDATE_FORM_SELECTOR);
m.find(REVIEW_TEXTAREA_SELECTOR).text(d);
m.find(REVIEW_RATING_SELECTOR).stars({split:STEP_RATING,});
m.find(REVIEW_RATING_SELECTOR).stars("select",e.toFixed(1));
if(g){p.addClass(ANONYMOUS_CLASS);
m.find(REVIEW_ANONYMOUS_SELECTOR).attr("checked","checked")
}m.submit(updateReview);
populateFormWithGroups(m,c,b);
m.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR).click(onAbstractGroupingClick);
removeReviewActions();
p.find(REVIEW_EDIT_LINK_SELECTOR).click(showUpdateReviewForm);
p.find(REVIEW_DELETE_LINK_SELECTOR).click(deleteReview);
p.find(REPLY_SELECTOR).click(reply);
var l=getReviewCount();
var j=getAvg();
var o=l+1;
var k=(j*l+e)/o;
setReviewCount(o);
setAvg(k);
plotRatingDistribution();
$("#noReviewInfo").hide();
$("#ratingAvg").show("slow");
$("#ratingDistribution").show("slow");
scrollTo(REVIEW_OWN_ID)
},error:function(j,k,l){handleAjaxErrors(f,jQuery.parseJSON(j.responseText));
f.submit(updateReview)
},});
return false
}function updateReview(){var f=$(this);
f.unbind("submit");
var a=f.find(REVIEW_RATING_SELECTOR);
if(!validateRating(a)){f.submit(updateReview);
return false
}var c=f.find(ABSTRACT_GROUPING_RADIO_BOXES_SELECTOR+":checked").val();
var b=f.find(OTHER_GROUPING_CLASS_SELECTOR).val();
if(b==null){b=new Array()
}else{b=new Array(b)
}var j=f.find(".spinner");
j.show("slow");
var d=f.find(REVIEW_TEXTAREA_SELECTOR).val();
var g=f.find(REVIEW_ANONYMOUS_SELECTOR).is(":checked");
var e=getRating(a);
var i=getOwnReviewRating();
var h=f.serialize();
$.ajax({url:REVIEWS_URL,type:"POST",dataType:"json",data:h,success:function(m){f.hide("slow");
j.hide("slow");
var o=$(REVIEW_OWN_SELECTOR);
updateHash(o,m.hash);
if(g){o.addClass(ANONYMOUS_CLASS)
}else{o.removeClass(ANONYMOUS_CLASS)
}updateReviewView(o,d,e,c,b);
highlight(o);
var n=getReviewCount();
var k=getAvg();
var l=(k*n-i+e)/n;
setAvg(l);
plotRatingDistribution();
f.submit(updateReview)
},error:function(k,l,m){handleAjaxErrors(f,jQuery.parseJSON(k.responseText));
f.submit(updateReview)
},});
return false
}function deleteReview(){var d=$(this);
$(this).siblings(".deleteInfo").show();
var f=getInterHash();
var a=$(REVIEW_OWN_SELECTOR);
var e=a.find(".info").data(DISCUSSIONITEM_DATA_KEY);
var c=getOwnReviewRating();
d.remove();
var b=REVIEWS_URL+"?hash="+f+"&ckey="+ckey+"&discussionItem.hash="+e;
$.ajax({url:b,type:"DELETE",success:function(g){deleteDiscussionItemView(a,function(){var j=getReviewCount();
var h=getAvg();
var i=0;
var k=j-1;
if(k>0){i=(h*j-c)/k
}setReviewCount(j-1);
setAvg(i);
plotRatingDistribution();
addReviewActions()
})
},});
return false
}function updateReviewView(h,g,c,f,a){var e=getStarsWidth(c);
var d=h.find(".rating");
d.data("rating",c);
h.find(".review.text").text(g);
d.find(".stars-on-1").css("width",e);
h.find("."+GROUPS_CLASS).remove();
var b=buildGroupView(f,a);
h.find(".info").append(b)
}function getRating(b){var a=$(b).data("stars");
return parseFloat(a.options.value)
}function getStarsWidth(a){return STAR_WIDTH*a
};