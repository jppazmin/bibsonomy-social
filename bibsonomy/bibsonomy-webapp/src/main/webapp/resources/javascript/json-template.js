var log=log||function(){};
var repr=repr||function(){};
var jsontemplate=function(){function d(J){return J.replace(/([\{\}\(\)\[\]\|\^\$\-\+\?])/g,"\\$1")
}var a={};
function E(K,M){var J=K+M;
var L=a[J];
if(L===undefined){var N="("+d(K)+".*?"+d(M)+"\n?)";
L=new RegExp(N,"g")
}return L
}function F(J){return J.replace(/&/g,"&amp;").replace(/>/g,"&gt;").replace(/</g,"&lt;")
}function f(J){return J.replace(/&/g,"&amp;").replace(/>/g,"&gt;").replace(/</g,"&lt;").replace(/"/g,"&quot;")
}function A(J){if(J===null){return"null"
}return J.toString()
}function h(M,K,J){var L,N;
switch(J.length){case 0:L="";
N="s";
break;
case 1:L="";
N=J[0];
break;
case 2:L=J[0];
N=J[1];
break;
default:throw {name:"EvaluationError",message:"pluralize got too many args"}
}return(M>1)?N:L
}function n(L,K,J){return J[(L-1)%J.length]
}var c={"html":F,"htmltag":f,"html-attr-value":f,"str":A,"raw":function(J){return J
},"AbsUrl":function(K,J){return J.get("base-url")+"/"+K
}};
var k={"singular?":function(J){return J==1
},"plural?":function(J){return J>1
},"Debug?":function(J,K){try{return K.get("debug")
}catch(L){if(L.name=="UndefinedVariable"){return false
}else{throw L
}}}};
var e=function(){return{lookup:function(J){return[null,null]
}}
};
var x=function(J){return{lookup:function(L){var K=J[L]||null;
return[K,null]
}}
};
var w=function(J){return{lookup:function(L){var K=J(L);
return[K,null]
}}
};
var H=function(J){return{lookup:function(P){for(var N=0;
N<J.length;
N++){var M=J[N].name,O=J[N].func;
if(P.slice(0,M.length)==M){var L;
var K=P.charAt(M.length);
if(K===""){L=[]
}else{L=P.split(K).slice(1)
}return[O,L]
}}return[null,null]
}}
};
var l=function(J){return{lookup:function(M){for(var L=0;
L<J.length;
L++){var K=J[L].lookup(M);
if(K[0]){return K
}}return[null,null]
}}
};
function B(L,K){var J=[{context:L,index:-1}];
return{PushSection:function(M){if(M===undefined||M===null){return null
}var N;
if(M=="@"){N=J[J.length-1].context
}else{N=J[J.length-1].context[M]||null
}J.push({context:N,index:-1});
return N
},Pop:function(){J.pop()
},next:function(){var M=J[J.length-1];
if(M.index==-1){M={context:null,index:0};
J.push(M)
}var N=J[J.length-2].context;
if(M.index==N.length){J.pop();
return undefined
}M.context=N[M.index++];
return true
},_Undefined:function(M){if(K===undefined){throw {name:"UndefinedVariable",message:M+" is not defined"}
}else{return K
}},_LookUpStack:function(M){var O=J.length-1;
while(true){var Q=J[O];
if(M=="@index"){if(Q.index!=-1){return Q.index
}}else{var N=Q.context;
if(typeof N==="object"){var P=N[M];
if(P!==undefined){return P
}}}O--;
if(O<=-1){return this._Undefined(M)
}}},get:function(M){if(M=="@"){return J[J.length-1].context
}var P=M.split(".");
var O=this._LookUpStack(P[0]);
if(P.length>1){for(var N=1;
N<P.length;
N++){O=O[P[N]];
if(O===undefined){return this._Undefined(P[N])
}}}return O
}}
}var j=function(J){var K={};
K.current_clause=[];
K.Append=function(L){K.current_clause.push(L)
};
K.AlternatesWith=function(){throw {name:"TemplateSyntaxError",message:"{.alternates with} can only appear with in {.repeated section ...}"}
};
K.NewOrClause=function(L){throw {name:"NotImplemented"}
};
return K
};
var o=function(J){var K=j(J);
K.statements={"default":K.current_clause};
K.section_name=J.section_name;
K.Statements=function(L){L=L||"default";
return K.statements[L]||[]
};
K.NewOrClause=function(L){if(L){throw {name:"TemplateSyntaxError",message:"{.or} clause only takes a predicate inside predicate blocks"}
}K.current_clause=[];
K.statements["or"]=K.current_clause
};
return K
};
var D=function(J){var K=o(J);
K.AlternatesWith=function(){K.current_clause=[];
K.statements["alternate"]=K.current_clause
};
return K
};
var m=function(J){var K=j(J);
K.clauses=[];
K.NewOrClause=function(L){L=L||[function(M){return true
},null];
K.current_clause=[];
K.clauses.push([L,K.current_clause])
};
return K
};
function q(J,M,P){for(var L=0;
L<J.length;
L++){var O=J[L];
if(typeof(O)=="string"){P(O)
}else{var N=O[0];
var K=O[1];
N(K,M,P)
}}}function i(N,M,Q){var O;
O=M.get(N.name);
for(var L=0;
L<N.formatters.length;
L++){var P=N.formatters[L];
var K=P[0];
var J=P[1];
O=K(O,M,J)
}Q(O)
}function t(K,L,O){var N=K;
var M=L.PushSection(N.section_name);
var J=false;
if(M){J=true
}if(M&&M.length===0){J=false
}if(J){q(N.Statements(),L,O);
L.Pop()
}else{L.Pop();
q(N.Statements("or"),L,O)
}}function v(O,J,T){var K=O;
var Q=J.get("@");
for(var L=0;
L<K.clauses.length;
L++){var P=K.clauses[L];
var S=P[0][0];
var R=P[0][1];
var N=P[1];
var M=S(Q,J,R);
if(M){q(N,J,T);
break
}}}function g(L,N,Q){var P=L;
items=N.PushSection(P.section_name);
pushed=true;
if(items&&items.length>0){var O=items.length-1;
var J=P.Statements();
var K=P.Statements("alternate");
for(var M=0;
N.next()!==undefined;
M++){q(J,N,Q);
if(M!=O){q(K,N,Q)
}}}else{q(P.Statements("or"),N,Q)
}N.Pop()
}var z=/(repeated)?\s*(section)\s+(\S+)?/;
var u=/or(?:\s+(.+))?/;
var b=/if(?:\s+(.+))?/;
function C(J){if(!J){return new e()
}else{if(typeof J==="function"){return new w(J)
}else{if(J.lookup!==undefined){return J
}else{if(typeof J==="object"){return new x(J)
}}}}}function p(ab,Z){var av=C(Z.more_formatters);
var R=H([{name:"pluralize",func:h},{name:"cycle",func:n}]);
var ae=new l([av,x(c),R]);
var ak=C(Z.more_predicates);
var W=new l([ak,x(k)]);
var am;
if(Z.default_formatter===undefined){am="str"
}else{am=Z.default_formatter
}function aw(ax){var ay=ae.lookup(ax);
if(!ay[0]){throw {name:"BadFormatter",message:ax+" is not a valid formatter"}
}return ay
}function J(ay){var ax=W.lookup(ay);
if(!ax[0]){throw {name:"BadPredicate",message:ay+" is not a valid predicate"}
}return ax
}var ac=Z.format_char||"|";
if(ac!=":"&&ac!="|"){throw {name:"ConfigurationError",message:"Only format characters : and | are accepted"}
}var M=Z.meta||"{}";
var ar=M.length;
if(ar%2==1){throw {name:"ConfigurationError",message:M+" has an odd number of metacharacters"}
}var aq=M.substring(0,ar/2);
var ap=M.substring(ar/2,ar);
var ao=E(aq,ap);
var K=o({});
var Y=[K];
var an=aq.length;
var ah;
var ai=0;
while(true){ah=ao.exec(ab);
if(ah===null){break
}else{var T=ah[0]
}if(ah.index>ai){var P=ab.slice(ai,ah.index);
K.Append(P)
}ai=ao.lastIndex;
var af=false;
if(T.slice(-1)=="\n"){T=T.slice(null,-1);
af=true
}T=T.slice(an,-an);
if(T.charAt(0)=="#"){continue
}if(T.charAt(0)=="."){T=T.substring(1,T.length);
var O={"meta-left":aq,"meta-right":ap,"space":" ","tab":"\t","newline":"\n"}[T];
if(O!==undefined){K.Append(O);
continue
}var U,V;
var Q=T.match(z);
if(Q){var aj=Q[1];
var ad=Q[3];
if(aj){V=g;
U=D({section_name:ad})
}else{V=t;
U=o({section_name:ad})
}K.Append([V,U]);
Y.push(U);
K=U;
continue
}var X,al;
var L=T.match(u);
if(L){X=L[1];
al=X?J(X):null;
K.NewOrClause(al);
continue
}var N=false;
var ag=T.match(b);
if(ag){X=ag[1];
N=true
}else{if(T.charAt(T.length-1)=="?"){X=T;
N=true
}}if(N){al=X?J(X):null;
U=m();
U.NewOrClause(al);
K.Append([v,U]);
Y.push(U);
K=U;
continue
}if(T=="alternates with"){K.AlternatesWith();
continue
}if(T=="end"){Y.pop();
if(Y.length>0){K=Y[Y.length-1]
}else{throw {name:"TemplateSyntaxError",message:"Got too many {end} statements"}
}continue
}}var au=T.split(ac);
var aa;
var S;
if(au.length==1){if(am===null){throw {name:"MissingFormatter",message:"This template requires explicit formatters."}
}aa=[aw(am)];
S=T
}else{aa=[];
for(var at=1;
at<au.length;
at++){aa.push(aw(au[at]))
}S=au[0]
}K.Append([i,{name:S,formatters:aa}]);
if(af){K.Append("\n")
}}K.Append(ab.slice(ai));
if(Y.length!==1){throw {name:"TemplateSyntaxError",message:"Got too few {end} statements"}
}return K
}function I(J,K){if(!(this instanceof I)){return new I(J,K)
}this._options=K||{};
this._program=p(J,this._options)
}I.prototype.render=function(J,L){var K=B(J,this._options.undefined_str);
q(this._program.Statements(),K,L)
};
I.prototype.expand=function(J){var K=[];
this.render(J,function(L){K.push(L)
});
return K.join("")
};
var y=/^([a-zA-Z\-]+):\s*(.*)/;
var G=["meta","format-char","default-formatter","undefined-str"];
var s=new RegExp(G.join("|"));
function r(R,S){var P={};
var M=0,N=0;
while(true){var J=false;
N=R.indexOf("\n",M);
if(N==-1){break
}var T=R.slice(M,N);
M=N+1;
var O=T.match(y);
if(O!==null){var K=O[1].toLowerCase(),Q=O[2];
if(K.match(s)){K=K.replace("-","_");
Q=Q.replace(/^\s+/,"").replace(/\s+$/,"");
if(K=="default_formatter"&&Q.toLowerCase()=="none"){Q=null
}P[K]=Q;
J=true
}}if(!J){break
}}if(P!=={}){body=R.slice(M)
}else{body=R
}for(var L in S){P[L]=S[L]
}return I(body,P)
}return{Template:I,HtmlEscape:F,FunctionRegistry:e,SimpleRegistry:x,CallableRegistry:w,ChainedRegistry:l,fromString:r,_Section:o}
}();