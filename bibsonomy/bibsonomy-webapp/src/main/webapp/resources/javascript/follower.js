function addFollower(a,c){$.ajax({type:"POST",url:"/ajax/handleFollower",data:"requestedUserName="+a+"&action=addFollower&ckey="+c,complete:function b(){document.getElementById("followLink").style.visibility="hidden";
document.getElementById("followLink").style.display="none";
document.getElementById("removeLink").style.visibility="visible";
document.getElementById("removeLink").style.display=""
}})
}function removeFollower(a,c){$.ajax({type:"POST",url:"/ajax/handleFollower",data:"requestedUserName="+a+"&action=removeFollower&ckey="+c,complete:function b(){document.getElementById("removeLink").style.visibility="hidden";
document.getElementById("removeLink").style.display="none";
document.getElementById("followLink").style.visibility="visible";
document.getElementById("followLink").style.display=""
}})
}function removeFollowerFollowerPage(a,d,c){$.ajax({type:"POST",url:"/ajax/handleFollower",data:"requestedUserName="+a+"&action=removeFollower&ckey="+c,complete:function b(){if(document.getElementById("posts.refresh")){document.getElementById("posts.refresh").style.visibility="visible";
document.getElementById("posts.refresh").style.display=""
}d.setAttribute("href","");
d.parentNode.style.display="none";
d.parentNode.style.visibility="hidden"
}})
}function addFollowerFollowerPage(a,d,c){$.ajax({type:"POST",url:"/ajax/handleFollower",data:"requestedUserName="+a+"&action=addFollower&ckey="+c,complete:function b(){if(document.getElementById("posts.refresh")){document.getElementById("posts.refresh").style.visibility="visible";
document.getElementById("posts.refresh").style.display=""
}d.setAttribute("href","");
d.parentNode.setAttribute("class","");
document.getElementById("followedUsers").appendChild(d.parentNode);
d.parentNode.removeChild(d)
}})
};