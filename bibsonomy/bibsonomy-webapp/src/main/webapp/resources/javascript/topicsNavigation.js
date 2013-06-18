function loadContentCallback(b,a,c){}function reloadContent(){startWaiting("#wait_reloadContent");
var b=currentResourceOrdering;
var a="";
for(i=0;
i<numberOfClusters;
i++){a+="clusters["+i+"].clusterID="+clusterSettings.clusters[i].clusterID;
a+="&clusters["+i+"].weight="+(clusterSettings.clusters[i].weight/100);
a+="&"
}a+="ordering="+b;
$.getJSON(communityBaseUrl+"/queryTopics?"+a+"&format=json",function(d){clusterResources=d;
updateContent();
stopWaiting("#wait_reloadContent");
for(i=0;
i<numberOfClusters;
i++){var c="#slider"+i;
$(document).ready(function(){$(c).slider("option","disabled",false)
})
}})
}function updateContent(){currentResourceRanking(clusterResources);
var a=renderJson(bibTexEntries.items,"bibTexList");
var b=renderJson(bookmarkEntries.items,"bookmarkList");
document.getElementById("bmEntries").innerHTML=b;
document.getElementById("btEntries").innerHTML=a
}function loadClusterResources(a,b){if(clusterResources==undefined){alert("Clusters not loaded");
return
}else{if(clusterResources.clusters[b].bibtex==undefined||clusterResources.clusters[b].bibtex.length==0||clusterResources.clusters[b].bookmarks==undefined||clusterResources.clusters[b].bookmarks.length==0){queryParams="";
queryParams+="clusters[0].clusterID="+clusterResources.clusters[b].clusterID;
queryParams+="&clusters[0].weight="+(clusterResources.clusters[b].weight/100);
queryParams+="&limit=6&offset=0";
$.getJSON(communityBaseUrl+"/queryTopics?"+queryParams+"&format=json",function(d){clusterResources.clusters[b].bibtex=d.clusters[0].bibtex;
clusterResources.clusters[b].bookmarks=d.clusters[0].bookmarks;
stopWaiting("wait_"+a);
var c=renderJson(clusterResources.clusters[b],"resourcesPreview");
document.getElementById(a).innerHTML=c
})
}}}function loadClusterResources(a,b,c){if(clusterResources==undefined){alert("Clusters not loaded");
return
}else{if(clusterResources.clusters[b].bibtex==undefined||clusterResources.clusters[b].bibtex.length==0||clusterResources.clusters[b].bookmarks==undefined||clusterResources.clusters[b].bookmarks.length==0){queryParams="";
queryParams+="clusters[0].clusterID="+clusterResources.clusters[b].clusterID;
queryParams+="&clusters[0].weight="+(c/100);
queryParams+="&limit=6&offset=0";
$.getJSON(communityBaseUrl+"/queryTopics?"+queryParams+"&format=json",function(e){clusterResources.clusters[b].bibtex=e.clusters[0].bibtex;
clusterResources.clusters[b].bookmarks=e.clusters[0].bookmarks;
stopWaiting("wait_"+a);
var d=renderJson(clusterResources.clusters[b],"resourcesPreview");
document.getElementById(a).innerHTML=d
})
}}}var resourceRankings={"weight":rankPostsByWeight,"random":rankPostsByRandom,"date":rankPostsByDate};
var resourceOrderings={"weight":"POPULAR","random":"RANDOM","date":"ADDED"};
var currentResourceRanking=rankPostsByWeight;
var currentResourceOrdering=resourceOrderings["weight"];
function setRanking(a){if(a in resourceRankings){currentResourceRanking=resourceRankings[a];
currentResourceOrdering=resourceOrderings[a]
}reloadContent()
}var myMaxClusterCount=50;
function rankPostsByWeight(c){function a(e,d){return e.weight<d.weight?1:(e.weight>d.weight?-1:0)
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
for(j=0;
j<clusters.length;
j++){cluster=clusters[j];
myWeight=cluster.weight;
myBibtexCount=cluster.bibtex.length;
myBookmarkCount=cluster.bookmarks.length;
if(myBibtexCount>myMaxClusterCount){b(bibTexEntries.items,cluster.bibtex.slice(0,myMaxClusterCount),j.toString(),myWeight)
}else{b(bibTexEntries.items,cluster.bibtex.slice(0,myBibtexCount),j.toString(),myWeight)
}if(myBookmarkCount>myMaxClusterCount){b(bookmarkEntries.items,cluster.bookmarks.slice(0,myMaxClusterCount),j.toString(),myWeight)
}else{b(bookmarkEntries.items,cluster.bookmarks.slice(0,myBookmarkCount),j.toString(),myWeight)
}}bibTexEntries.items.sort(a);
bookmarkEntries.items.sort(a)
}function rankPostsByRandom(b){function a(d,c,e,f){for(i=0;
i<c.length;
i++){normalizePost(c[i],e);
c[i].weight=c[i].weight*f;
d.push(c[i])
}}clusters=b.clusters;
bibTexEntries.items=new Array();
bookmarkEntries.items=new Array();
updateWeights();
for(j=0;
j<clusters.length;
j++){cluster=clusters[j];
myWeight=cluster.weight;
myBibtexCount=cluster.bibtex.length;
myBookmarkCount=cluster.bookmarks.length;
if(myBibtexCount>myMaxClusterCount){a(bibTexEntries.items,cluster.bibtex.slice(0,myMaxClusterCount),j.toString(),myWeight)
}else{a(bibTexEntries.items,cluster.bibtex.slice(0,myBibtexCount),j.toString(),myWeight)
}if(myBookmarkCount>myMaxClusterCount){a(bookmarkEntries.items,cluster.bookmarks.slice(0,myMaxClusterCount),j.toString(),myWeight)
}else{a(bookmarkEntries.items,cluster.bookmarks.slice(0,myBookmarkCount),j.toString(),myWeight)
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
for(j=0;
j<clusters.length;
j++){cluster=clusters[j];
myWeight=cluster.weight;
myBibtexCount=cluster.bibtex.length;
myBookmarkCount=cluster.bookmarks.length;
if(myBibtexCount>myMaxClusterCount){a(bibTexEntries.items,cluster.bibtex.slice(0,myMaxClusterCount),j.toString(),myWeight)
}else{a(bibTexEntries.items,cluster.bibtex.slice(0,myBibtexCount),j.toString(),myWeight)
}if(myBookmarkCount>myMaxClusterCount){a(bookmarkEntries.items,cluster.bookmarks.slice(0,myMaxClusterCount),j.toString(),myWeight)
}else{a(bookmarkEntries.items,cluster.bookmarks.slice(0,myBookmarkCount),j.toString(),myWeight)
}}bibTexEntries.items.sort(b);
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
}}function changeClusterWeight(b,c){clusterSettings.clusters[b].weight=c;
for(b=0;
b<numberOfClusters;
b++){var a="#slider"+b;
$(document).ready(function(){$(a).slider("option","disabled",true)
})
}reloadContent()
}function updateWeights(){for(i=0;
i<numberOfClusters;
i++){sliderName="#slider"+i;
value=$(sliderName).slider("option","value");
clusterSettings.clusters[i].weight=value
}}function reloadSettings(){reloadSettings(false)
}function reloadSettings(a){startWaiting("#wait_reloadSettings");
$.getJSON(communityBaseUrl+"/queryTopics?format=json",function(b){clusterResources=b;
if(clusterResources.clusters.length>0&&clusterResources.clusters[0].clusterID==-1){stopWaiting("#wait_reloadSettings");
alert("topic search down")
}else{$.getJSON(communityBaseUrl+"/topicsSettings?format=json",function(d){clusterSettings=d;
normalizeClusterSettings(clusterSettings.clusters);
numberOfClusters=clusterSettings.clusters.length;
var c=renderJson(d,"topicsSettings");
document.getElementById("topicsSettings").innerHTML=c;
initializeSettings();
stopWaiting("#wait_reloadSettings");
if(a){reloadContent()
}if(clusterSettings.clusters.length<maxClusterCount){$("#addClustersNavigation").show()
}else{$("#addClustersNavigation").hide()
}})
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
$.getJSON(communityBaseUrl+"/topicsList?limit="+a+"&offset="+b+"&format=json",function(d){clusterResources=d;
normalizeTags(clusterResources.clusters);
normalizeUsers(clusterResources.clusters);
var c=renderJson(d,"topicsOverview");
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
}function showClusterTooltip(a){}function addCluster(a,b){$.getJSON("/bibsonomy-community-servlet/topicsSettings?action=ADDCLUSTERS&clusters[0].clusterID="+a+"&clusters[0].weight="+b+"&format=json",function(c){clusterSettings=c;
reloadSettings(true);
showClusterPage()
})
}function addRecommendedCluster(){var a=confirm("This will replace the currently selected clusters. Do you want to proceed?");
if(a){$.getJSON("/bibsonomy-community-servlet/topicsSettings?action=ADDRECOMMENDEDCLUSTER&format=json",function(b){clusterSettings=b;
reloadSettings(true)
})
}}function removeCluster(a){$.getJSON("/bibsonomy-community-servlet/topicsSettings?action=REMOVECLUSTERS&clusters[0].clusterID="+a+"&format=json",function(b){clusterSettings=b;
reloadSettings();
showClusterPage()
})
}function reloadClustering(){$.getJSON("/bibsonomy-community-servlet/topicsSettings?action=CHANGEALGORITHM&format=json",function(a){clusterSettings=a;
reloadSettings();
showClusterPage()
})
}function saveClusterSettings(){var a="";
for(i=0;
i<numberOfClusters;
i++){a+="clusters["+i+"].clusterID="+clusterSettings.clusters[i].clusterID;
a+="&clusters["+i+"].weight="+clusterSettings.clusters[i].weight;
if(i<numberOfClusters-1){a+="&"
}}$.getJSON(communityBaseUrl+"/topicsSettings?action=SAVECLUSTERSETTINGS&"+a+"&format=json",function(b){})
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