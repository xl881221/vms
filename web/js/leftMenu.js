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
  
  Menu = {
      highlightItem : null,
      autoLoad : true,
      init : function(){
          var aTags = DOM.$T("a"),oThis = this;
          for(var i=0,iLen=aTags.length;i<iLen;i++){
              var a = aTags[i];
              DOM.addEvent(a,"click",function(e){oThis.markItem(e);});
              a = null;
          }
          this.loadFirstItem(aTags[0]);
      },
      markItem : function(e,obj){
          var elm;
          if(!obj){
              var evt = e ? e : window.event;
              elm = evt.srcElement || evt.target;
          }else{
              elm = obj;
          }
          if(this.highlightItem != null)
              this.highlightItem.className = "";
          this.highlightItem = elm;
          elm.className = "highlight";
      },
      loadFirstItem : function(elm){
          //this.markItem(null,elm);
          //window.open(elm.getAttribute("href"),"mainFramePage");
          window.open(SUBSYSIMG,"mainFramePage");
      }
  }

  DOM.addEvent(window,"load",function(){Menu.init();});