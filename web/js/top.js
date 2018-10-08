// JavaScript Document
var firstId=null;
function init(){	
	firstId=document.getElementById("firstId");	
	if(firstId==null)return;
	firstId.className="on";
}

//处理点击Class
function tabChang(p){
    if(firstId==null)
       firstId=document.getElementById("firstId");
    if(firstId!=null) 
	   firstId.className="";
	p.parentNode.className="on";
	firstId=p.parentNode;

  
	// 解决兼容性问题 2009-08-03 10:46 ShiCH
	if(p.parentNode.id=="firstId"){
		//window.parent.frames['frame'].cols="0,0,*";
		ifrm.cols="0,0,*";
	}
	else{
		//window.parent.frames['frame'].cols="180,5,*";
		ifrm.cols="230,10,*";
	}
}	
	
	
/*************************************************/
	var oTemp=null;
	function isfocus(url,leftUrl){
	
		if(oTemp!=null)
			oTemp.className="navNormal";
		oTemp=event.srcElement;
		oTemp.className="navFocus";
				
		window.open(url,'mainFrame');
		if(leftUrl!=null) {




			window.open(leftUrl,'leftFrame');
		}
		
	}
	function over(){
		if(event.srcElement==oTemp) return;
		event.srcElement.className="navOver";
	}
	function out(){
		if(event.srcElement==oTemp) return;
		event.srcElement.className="navNormal";
	}	
	function windowClose()
{
  window.top.opener=null;
  window.top.close();
  window.open('Login.html');
}



/* 添加首页功能跳转 ShiCH 2009-8-19 */
  DOM = {
      $ : function(id){
          return document.getElementById(id);
      },
      $C : function(tagName,attrObject){
          var elm = window.document.createElement(tagName);
          if(null != attrObject && typeof(attrObject) == "object"){
              for(var item in attrObject){
                  if(item == "innerHTML" || item == "className")
                      elm[item] = attrObject[item];
                  else
                      elm.setAttribute(item,attrObject[item]);
              }
          }
          return elm;
      },
      $A : function(elm,pElm){
          return pElm ? pElm.appendChild(elm) : document.body.appendChild(elm);
      },
      $R : function(elm,pElm){
          return pElm ? pElm.removeChild(elm) : document.body.removeChild(elm);
      },
      $T : function(tagName,pElm){
          return pElm ? pElm.getElementsByTagName(tagName) : document.getElementsByTagName(tagName);
      },
      $M : function(att){
          if(att && typeof(att) == "string"){
              var reg = /(^\s*)|(\s*$)/g;
              var a = att.split("=")[0].replace(reg,"");
              var v = att.split("=")[1].replace(reg,"");
          }else{
              return [];
          }
          var arr = [];
          var elms = this.$T("*");
          for(var i=0,iLen=elms.length;i<iLen;i++){
              var _temp = IS_IE ? elms[i][a] : elms[i].getAttribute(a);
              if(_temp == v){
                  arr.push(elms[i]);
              }
          }
          return arr;
      },
      addEvent : function(elm,type,handler,useCapture){
          try{
              elm.attachEvent("on"+type,function(){handler();});
          }catch(e){
              try{
                  if(null == useCapture) useCapture = false;
                  elm.addEventListener(type,handler,useCapture);
              }catch(e1){
                  elm["on"+type] = function(){
                      handler;
                  }
              }
          }
      }
  }

  function JumpToFocusItem(menuID,subMenuID,targetUrl){
      targetUrl+="&systemId="+btoa(menuID);
      var table = DOM.$T("table")[0];
      if(!table) return;
      var aTags = DOM.$T("a",table),reg = new RegExp("(\\?|#|&)subSysId=([^&#]*)(&|#|$)","i"),canGo = false;
      for(var i=0,iLen=aTags.length;i<iLen;i++){
          var a = aTags[i],actionStr = "";
          for(var j=0,jLen=a.attributes.length;j<jLen;j++){
              if(a.attributes[j].name.toUpperCase() == "ONCLICK"){
                  actionStr = a.attributes[j].value;
                  break;
              }
          }
          var match = actionStr.match(reg);
          if((!match ? "" : match[2]) == menuID){
              if(!/msie/i.test(navigator.userAgent)){
                  var aReg = new RegExp("^.*\)(?=;\w)");
                  var mA = actionStr.match(aReg);
                  if(mA)
                      eval("window."+mA[0]);
                  tabChang(a);
              }else{                
                  a.click();
              }
              loadLeftMenu(subMenuID,targetUrl);
              break;
          }
          a = null;
      }
  }

  function loadLeftMenu(subMenuID,targetUrl){
      var iframe = parent.document.getElementById("leftFramePage"),count = 0;
      iframe.onload = iframe.onreadystatechange = function() {
          if (this.readyState && this.readyState != "complete"){
                return;
          }else{
              if(count == 0)
                  setTimeout(function(){focusLeftMenu(subMenuID,targetUrl);},100);
              count ++;
          }
      }
  }

  function focusLeftMenu(subMenuID,targetUrl){
      var iframe = parent.window.frames["leftFramePage"];
      var aTags = iframe.DOM.$T("a");
      for(var i=0,iLen=aTags.length;i<iLen;i++){
          var a = aTags[i];
          if(a.getAttribute("subMenuID") == subMenuID){
              iframe.Menu.autoLoad = false;
              iframe.Menu.markItem(null,a);
              var mFrame = parent.document.getElementById("mainFramePage");
              if(targetUrl){
	              	//if(mFrame.src != targetUrl){
	              	  	mFrame.src = targetUrl;
	              	//}
              }else{
	              	if(mFrame.src != a.getAttribute("href")){
	              	  	mFrame.src = a.getAttribute("href");
	              	}
              }
              
              setTimeout(function(){                 
                  var bar = iframe.DOM.$T("td",a.parentNode.parentNode.parentNode.previousSibling)[0];
                  if(!/msie/i.test(navigator.userAgent)){
                      var moveCmd = bar.getAttribute("onclick");
                      eval("parent.window.frames['leftFramePage']."+moveCmd);
                  }else{
                      bar.click();
                  };
              },200);              
              break;
          }
      }
  }
  function btoa(str) {
    var chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
    var encoded = [];
    var c = 0;
    while (c < str.length) {
        var b0 = str.charCodeAt(c++);
        var b1 = str.charCodeAt(c++);
        var b2 = str.charCodeAt(c++);
        var buf = (b0 << 16) + ((b1 || 0) << 8) + (b2 || 0);
        var i0 = (buf & (63 << 18)) >> 18;
        var i1 = (buf & (63 << 12)) >> 12;
        var i2 = isNaN(b1) ? 64 : (buf & (63 << 6)) >> 6;
        var i3 = isNaN(b2) ? 64 : (buf & 63);
        encoded[encoded.length] = chars.charAt(i0);
        encoded[encoded.length] = chars.charAt(i1);
        encoded[encoded.length] = chars.charAt(i2);
        encoded[encoded.length] = chars.charAt(i3);
    }
    return encoded.join('');
 }