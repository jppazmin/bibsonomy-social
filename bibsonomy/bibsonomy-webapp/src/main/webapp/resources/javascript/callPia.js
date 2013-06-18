var PIAObject=null;
function PIARequest(b,a){this.callback=null;
this.container=null;
this.count=10;
this.serviceURL="/piaWebservice/";
this.url=a;
this.method=b;
this.makeRequest=function(){var d=new SOAPObject(b);
d.appendChild(new SOAPObject("url").val(this.url));
d.appendChild(new SOAPObject("count").val(this.count));
var c=new SOAPRequest(this.method,d);
SOAPClient.Proxy=this.serviceURL+this.method+"?url="+this.url+"&count="+this.count;
SOAPClient.SendRequest(c,this.callback)
};
PIAObject=this
};