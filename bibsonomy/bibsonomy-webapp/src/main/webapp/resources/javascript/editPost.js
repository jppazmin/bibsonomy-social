function clearTagField(){var a=document.getElementById("tagField");
while(a.hasChildNodes()){a.removeChild(a.firstChild)
}}function handleRecommendedTags(a){var i=[];
var h="tagField";
var l=document.getElementById(h);
clearTagField();
var k=a.getElementsByTagName("tags").item(0);
if(k==null){alert("Invalid Ajax response: <tags/> not found.")
}for(var g=0;
g<k.childNodes.length;
g++){var c=k.childNodes.item(g);
if((c.nodeType==1)&&(c.tagName=="tag")){var b=c.getAttribute("name");
var e=c.getAttribute("score");
var j=c.getAttribute("confidence");
var m=document.createElement("a");
var f=document.createTextNode(b+" ");
m.setAttribute("href","javascript:copytag('inpf', '"+c.getAttribute("name")+"')");
m.setAttribute("tabindex","1");
m.appendChild(f);
l.appendChild(m);
var d=new Object;
d.label=b;
d.score=e;
d.confidence=j;
i.push(d)
}}populateSuggestionsFromRecommendations(i);
document.getElementById("fsReloadLink").setAttribute("href","javascript:reloadRecommendation()");
document.getElementById("fsReloadButton").setAttribute("src","/resources/image/button_reload.png")
}function reloadRecommendation(){document.getElementById("fsReloadLink").setAttribute("href","#");
document.getElementById("fsReloadButton").setAttribute("src","/resources/image/button_reload-inactive.png");
clearTagField();
$("#postForm").ajaxSubmit(tagRecoOptions)
};