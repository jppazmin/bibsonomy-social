var message="NEW: Discussion and Ratings! See our <a href='<a href='<a href='http://blog.bibsonomy.org/2011/07/feature-of-week-revies-and-discussion.html'>blog</a>, click on the discussion button or on the 5-Stars icon below each post.<br /> In September we will change the format of author names. <a href='http://blog.bibsonomy.org/2011/07/structure-of-authoreditor-names-in.html'>read more</a>.";
var cookieName="bibSystemMessageDiscussion";
var cookieDays=100;
var width=400;
$(window).load(function(){if(document.getElementById("inboxctr")&&!hasCookie(cookieName)){showMessage()
}});
function showMessage(){var b=document.createElement("div");
b.style.position="absolute";
b.style.width=width+"px";
b.style.left=Math.floor((getWindowWidth()-width)/2)+"px";
b.style.top="150px";
b.style.background="#eee";
b.style.padding="1em";
b.style.border="2px solid #006699";
b.id="systemmessage";
var a=document.createElement("span");
a.innerHTML=message;
b.appendChild(a);
var c=document.createElement("a");
c.style.cssFloat="right";
c.style.cursor="pointer";
c.href="javascript:void(0);";
c.onclick=hideMessage;
c.appendChild(document.createTextNode("close"));
b.appendChild(c);
document.getElementsByTagName("body")[0].appendChild(b)
}function getWindowWidth(){if(window.innerWidth){return window.innerWidth
}if(document.body.clientWidth){return document.body.clientWidth
}}function hideMessage(){var a=document.getElementById("systemmessage");
if(a){document.getElementsByTagName("body")[0].removeChild(a)
}setCookie()
}function setCookie(){var b=new Date();
var a=b.getTime()+(cookieDays*24*60*60*1000);
b.setTime(a);
document.cookie=cookieName+"=x;expires="+b.toGMTString()
}function hasCookie(a){var d=document.cookie.split("; ");
for(var b=0;
b<d.length;
b++){if(d[b].split("=")[0]==a){return true
}}return false
};