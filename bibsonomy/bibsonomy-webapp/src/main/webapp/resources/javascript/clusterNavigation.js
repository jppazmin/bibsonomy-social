function loadContentCallback(b,a,c){}function reloadContent(){startWaiting("#wait_reloadContent");
var b=currentResourceOrdering;
var a="";
for(i=0;
i<numberOfClusters;
i++){a+="clusters["+i+"].clusterID="+clusterSettings.clusters[i].clusterID;
a+="&clusters["+i+"].weight="+clusterSettings.clusters[i].weight;
a+="&"
}a+="ordering="+b;
$.getJSON(communityBaseUrl+"/queryResources?"+a+"&format=json",function(c){clusterResources=c;
updateContent();
stopWaiting("#wait_reloadContent")
})
}function updateContent(){currentResourceRanking(clusterResources);
var a=renderJson(bibTexEntries.items,"bibTexList");
var b=renderJson(bookmarkEntries.items,"bookmarkList");
document.getElementById("bmEntries").innerHTML=b;
document.getElementById("btEntries").innerHTML=a
}function loadClusterResources(a,b){if(clusterResources==undefined){alert("Clusters not loaded");
return
}else{if(clusterResources.clusters[b].bibtex==undefined||clusterResources.clusters[b].bibtex.length==0||clusterResources.clusters[b].bookmarks==undefined||clusterResources.clusters[b].bookmarks.length==0){queryParams="";
queryParams+="clusters[0].clusterID="+clusterResources.clusters[b].clusterID;
queryParams+="&clusters[0].weight="+clusterResources.clusters[b].weight;
queryParams+="&limit=6&offset=0";
$.getJSON(communityBaseUrl+"/queryResources?"+queryParams+"&format=json",function(d){clusterResources.clusters[b].bibtex=d.clusters[0].bibtex;
clusterResources.clusters[b].bookmarks=d.clusters[0].bookmarks;
stopWaiting("wait_"+a);
var c=renderJson(clusterResources.clusters[b],"resourcesPreview");
document.getElementById(a).innerHTML=c
})
}}}var resourceRankings={"weight":rankPostsByWeight,"random":rankPostsByRandom,"date":rankPostsByDate};
var resourceOrderings={"weight":"POPULAR","random":"RANDOM","date":"ADDED"};
var currentResourceRanking=rankPostsByWeight;
var currentResourceOrdering=resourceOrderings["weight"];
function setRanking(a){if(a in resourceRankings){currentResourceRanking=resourceRankings[a];
currentResourceOrdering=resourceOrderings[a]
}reloadContent()
}function rankPostsByWeight(c){function a(e,d){return e.weight<d.weight?1:(e.weight>d.weight?-1:0)
}function b(e,d,f,g){for(i=0;
i<d.length;
i++){normalizePost(d[i],f);
d[i].weight=d[i].weight*g;
e.push(d[i])
}}clusters=c.clusters;
bibTexEntries.items=new Array();
bookmarkEntries.items=new Array();
updateWeights();
normalizeWeights(c);
weightSum=0;
for(i=0;
i<numberOfClusters;
i++){weightSum+=clusterSettings.clusters[i].weight
}for(j=0;
j<clusters.length;
j++){cluster=clusters[j];
clusterPostCount=Math.round(numberOfPosts*(clusterSettings.clusters[j].weight/weightSum));
clusterBibTexCount=Math.min(clusterPostCount,cluster.bibtex.length);
clusterBookmarkCount=Math.min(clusterPostCount,cluster.bookmarks.length);
cluster.bibtex.sort(a);
b(bibTexEntries.items,cluster.bibtex.slice(0,clusterBibTexCount),j.toString(),clusterSettings.clusters[j].weight);
cluster.bookmarks.sort(a);
b(bookmarkEntries.items,cluster.bookmarks.slice(0,clusterBookmarkCount),j.toString(),clusterSettings.clusters[j].weight)
}bibTexEntries.items.sort(a);
bookmarkEntries.items.sort(a)
}function rankPostsByRandom(d){clusters=d.clusters;
bibTexEntries.items=new Array();
bookmarkEntries.items=new Array();
updateWeights();
weightSum=0;
for(i=0;
i<numberOfClusters;
i++){weightSum+=clusterSettings.clusters[i].weight
}for(i=0;
i<clusters.length;
i++){cluster=clusters[i];
cluster.rndBibTexSeq=new Array();
cluster.rndBookmarkSeq=new Array();
clusterPostCount=Math.round(numberOfPosts*(clusterSettings.clusters[i].weight/weightSum));
clusterBibTexCount=Math.min(clusterPostCount,cluster.bibtex.length);
clusterBookmarkCount=Math.min(clusterPostCount,cluster.bookmarks.length);
var a=new Array();
for(j=0;
j<clusterBibTexCount;
j++){var b;
while((!(b=Math.floor(Math.random()*cluster.bibtex.length)) in a)){}a[b]=1;
post=cluster.bibtex[b];
post.clusterPos=cluster.clusterPos.toString();
if(post.journal==undefined){post.journal=""
}bibTexEntries.items.push(post)
}var c=new Array();
for(j=0;
j<clusterBookmarkCount;
j++){var b;
while((!(b=Math.floor(Math.random()*cluster.bookmarks.length)) in c)){}c[b]=1;
post=cluster.bookmarks[b];
post.clusterPos=cluster.clusterPos.toString();
bookmarkEntries.items.push(post)
}}shuffleArray(bibTexEntries.items);
shuffleArray(bookmarkEntries.items)
}function rankPostsByDate(c){function b(f,e){var d=compareDates(e.date,dateFormat,f.date,dateFormat);
return d
}function a(e,d,f,g){for(i=0;
i<d.length;
i++){normalizePost(d[i],f);
d[i].weight=d[i].weight*g;
e.push(d[i])
}}clusters=c.clusters;
bibTexEntries.items=new Array();
bookmarkEntries.items=new Array();
updateWeights();
weightSum=0;
for(i=0;
i<numberOfClusters;
i++){weightSum+=clusterSettings.clusters[i].weight
}for(j=0;
j<clusters.length;
j++){cluster=clusters[j];
clusterPostCount=Math.round(numberOfPosts*(clusterSettings.clusters[j].weight/weightSum));
clusterBibTexCount=Math.min(clusterPostCount,cluster.bibtex.length);
clusterBookmarkCount=Math.min(clusterPostCount,cluster.bookmarks.length);
a(bibTexEntries.items,cluster.bibtex.slice(0,clusterBibTexCount),j.toString(),clusterSettings.clusters[j].weight);
a(bookmarkEntries.items,cluster.bookmarks.slice(0,clusterBookmarkCount),j.toString(),clusterSettings.clusters[j].weight)
}bibTexEntries.items.sort(b);
bookmarkEntries.items.sort(b)
}function normalizePost(a,b){a.clusterPos=b;
if(a.journal==undefined){a.journal=""
}}function normalizeWeights(e){clusters=e.clusters;
for(var c=0;
c<clusters.length;
c++){cluster=clusters[c];
var a=0;
var d=0;
for(var b=0;
b<cluster.bibtex.length;
b++){if(clusters[c].bibtex[b].weight>a){a=clusters[c].bibtex[b].weight
}}for(var b=0;
b<cluster.bookmarks.length;
b++){if(clusters[c].bookmarks[b].weight>d){d=clusters[c].bookmarks[b].weight
}}if(a>0){for(var b=0;
b<cluster.bibtex.length;
b++){clusters[c].bibtex[b].weight=clusters[c].bibtex[b].weight/a
}}if(d>0){for(var b=0;
b<cluster.bookmarks.length;
b++){clusters[c].bookmarks[b].weight=clusters[c].bookmarks[b].weight/d
}}}}function initializeSettings(){function b(c){id=c;
weight=clusterSettings.clusters[c].weight;
return{value:weight,change:function(d,e){changeClusterWeight(id,e.value)
}}
}for(i=0;
i<numberOfClusters;
i++){var a="#slider"+i;
$(document).ready(function(){$(a).slider(b(i))
})
}}function changeClusterWeight(a,b){clusterSettings.clusters[a].weight=b;
updateContent()
}function updateWeights(){for(i=0;
i<numberOfClusters;
i++){sliderName="#slider"+i;
value=$(sliderName).slider("option","value");
clusterSettings.clusters[i].weight=value
}}function reloadSettings(){reloadSettings(false)
}function reloadSettings(a){startWaiting("#wait_reloadSettings");
$.getJSON(communityBaseUrl+"/clusterSettings?format=json",function(c){clusterSettings=c;
normalizeClusterSettings(clusterSettings.clusters);
numberOfClusters=clusterSettings.clusters.length;
var b=renderJson(c,"clusterSettings");
document.getElementById("clusterSettings").innerHTML=b;
initializeSettings();
stopWaiting("#wait_reloadSettings");
if(a){reloadContent()
}if(clusterSettings.clusters.length<maxClusterCount){$("#addClustersNavigation").show()
}else{$("#addClustersNavigation").hide()
}})
}function normalizeClusterSettings(b){for(var a=0;
a<b.length;
a++){b[a]["clusterPos"]=a
}}function normalizeTags(c){for(var b=0;
b<c.length;
b++){var d=-1;
for(var a=0;
a<c[b].tags.length;
a++){if(d<c[b].tags[a].globalcount){d=c[b].tags[a].globalcount
}}for(var a=0;
a<c[b].tags.length;
a++){c[b].tags[a]["maxTagCount"]=d
}}}function normalizeUsers(c){for(var b=0;
b<c.length;
b++){var d=-1;
for(var a=0;
a<c[b].users.length;
a++){if(d<c[b].users[a].weight){d=c[b].users[a].weight
}}for(var a=0;
a<c[b].users.length;
a++){c[b].users[a]["maxUserWeight"]=d
}}}function resetClusterView(){if(clusterResources!=undefined&&"listView" in clusterResources){delete clusterResources.listView
}}function showClusterPage(){if(clusterResources!=undefined&&clusterResources.listView!=undefined){limit=clusterResources.listView.limit;
offset=clusterResources.listView.offset;
showClusters(offset,limit)
}else{showClusters(0,6)
}}function showClusters(b,a){startWaiting("#wait_showClusterPage");
$.getJSON(communityBaseUrl+"/clusterList?limit="+a+"&offset="+b+"&format=json",function(d){clusterResources=d;
normalizeTags(clusterResources.clusters);
normalizeUsers(clusterResources.clusters);
var c=renderJson(d,"clusterOverview");
document.getElementById("bmEntries").innerHTML=c;
document.getElementById("btEntries").innerHTML="";
stopWaiting("#wait_showClusterPage")
})
}function showNextClusters(){var a=clusterResources.listView.limit;
var b=1*clusterResources.listView.offset+1*a;
showClusters(b,a)
}function showPreviousClusters(){var a=clusterResources.listView.limit;
var b=clusterResources.listView.offset-1*a;
showClusters(b,a)
}function showClusterTooltip(a){}function addCluster(a,b){$.getJSON("/bibsonomy-community-servlet/clusterSettings?action=ADDCLUSTERS&clusters[0].clusterID="+a+"&clusters[0].weight="+b+"&format=json",function(c){clusterSettings=c;
reloadSettings(true);
showClusterPage()
})
}function addRecommendedCluster(){$.getJSON("/bibsonomy-community-servlet/clusterSettings?action=ADDRECOMMENDEDCLUSTER&format=json",function(a){clusterSettings=a;
reloadSettings(true)
})
}function removeCluster(a){$.getJSON("/bibsonomy-community-servlet/clusterSettings?action=REMOVECLUSTERS&clusters[0].clusterID="+a+"&format=json",function(b){clusterSettings=b;
reloadSettings();
showClusterPage()
})
}function reloadClustering(){$.getJSON("/bibsonomy-community-servlet/clusterSettings?action=CHANGEALGORITHM&format=json",function(a){clusterSettings=a;
reloadSettings();
showClusterPage()
})
}function saveClusterSettings(){var a="";
for(i=0;
i<numberOfClusters;
i++){a+="clusters["+i+"].clusterID="+clusterSettings.clusters[i].clusterID;
a+="&clusters["+i+"].weight="+clusterSettings.clusters[i].weight;
if(i<numberOfClusters-1){a+="&"
}}$.getJSON(communityBaseUrl+"/clusterSettings?action=SAVECLUSTERSETTINGS&"+a+"&format=json",function(b){})
}function shuffleArray(b){i=b.length;
if(i==0){return false
}while(--i){var a=Math.floor(Math.random()*(i+1));
var d=b[i];
var c=b[a];
b[i]=c;
b[a]=d
}}function startWaiting(a){$(a).show()
}function stopWaiting(a){$(a).hide()
}function setUp(){reloadSettings(true)
}$(document).ready(function(){setUp()
});