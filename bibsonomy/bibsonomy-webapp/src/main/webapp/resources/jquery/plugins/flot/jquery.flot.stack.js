(function(b){var a={series:{stack:null}};
function c(f){function d(k,j){var h=null;
for(var g=0;
g<j.length;
++g){if(k==j[g]){break
}if(j[g].stack==k.stack){h=j[g]
}}return h
}function e(C,v,g){if(v.stack==null){return
}var p=d(v,C.getData());
if(!p){return
}var z=g.pointsize,F=g.points,h=p.datapoints.pointsize,y=p.datapoints.points,t=[],x,w,k,J,I,r,u=v.lines.show,G=v.bars.horizontal,o=z>2&&(G?g.format[2].x:g.format[2].y),n=u&&v.lines.steps,E=true,q=G?1:0,H=G?0:1,D=0,B=0,A;
while(true){if(D>=F.length){break
}A=t.length;
if(F[D]==null){for(m=0;
m<z;
++m){t.push(F[D+m])
}D+=z
}else{if(B>=y.length){if(!u){for(m=0;
m<z;
++m){t.push(F[D+m])
}}D+=z
}else{if(y[B]==null){for(m=0;
m<z;
++m){t.push(null)
}E=true;
B+=h
}else{x=F[D+q];
w=F[D+H];
J=y[B+q];
I=y[B+H];
r=0;
if(x==J){for(m=0;
m<z;
++m){t.push(F[D+m])
}t[A+H]+=I;
r=I;
D+=z;
B+=h
}else{if(x>J){if(u&&D>0&&F[D-z]!=null){k=w+(F[D-z+H]-w)*(J-x)/(F[D-z+q]-x);
t.push(J);
t.push(k+I);
for(m=2;
m<z;
++m){t.push(F[D+m])
}r=I
}B+=h
}else{if(E&&u){D+=z;
continue
}for(m=0;
m<z;
++m){t.push(F[D+m])
}if(u&&B>0&&y[B-h]!=null){r=I+(y[B-h+H]-I)*(x-J)/(y[B-h+q]-J)
}t[A+H]+=r;
D+=z
}}E=false;
if(A!=t.length&&o){t[A+2]+=r
}}}}if(n&&A!=t.length&&A>0&&t[A]!=null&&t[A]!=t[A-z]&&t[A+1]!=t[A-z+1]){for(m=0;
m<z;
++m){t[A+z+m]=t[A+m]
}t[A+1]=t[A-z+1]
}}g.points=t
}f.hooks.processDatapoints.push(e)
}b.plot.plugins.push({init:c,options:a,name:"stack",version:"1.2"})
})(jQuery);