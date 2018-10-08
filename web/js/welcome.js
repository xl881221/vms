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
	      $D : function(eleName, id, name, attrObject) {
				var elm = window.document.createElement(eleName);
				elm.setAttribute(id,name);
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


/*
 * 功 能 : 首页功能处理 
 * 作 者 : 史纯华 
 * 日 期 : 2009-7-30
 */

__MOVECMD = null; // 左上模块setTimeout变量
//__ISIE = /msie/gi.test(navigator.userAgent); // 浏览器验证
__ISIE = true; //兼容性问题,暂时默认为true;
__SINGLEHEIGHT = screen.availHeight - 311; // 左上模块的单个模块高度
PAGE_CACHE = {}; // 页面全局静态变量
__URL_TAB = "tab.xml"; // TAB信息取数据地址(后台编写好后换成后台地址)
__URL_TASK = "data_format.xml"; // 需要处理的问题 单个模块取数地址(同上)
__URL_INFO = "fmss.xml"; // 通告信息 单个模块取数地址(同上)

// ==============================================================
// 数据初始化,构建左上模块TAB
// ==============================================================
function init() {
    var s = new ScrollTab();
    var con = DOM.$G("mainTab");
    s.container = con;
    s.dataType = 2;
    s.data = PAGE_CACHE.tabA;
    s.singleWidth = 70;
    s.width = (screen.availWidth -64) / 2 - 42;
    s.clickEvt = function(elm) {
        var dataId = elm.getAttribute("dataId");
        loadData(this, dataId);
    }
    s.init(0);
    buildForm(s);
    buildInfoBlock();
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

// ==============================================================
// 加载左上模块
// ==============================================================
function buildForm(obj) {
    var tabContent = DOM.$G("mainTabTarget");
    var firstID = null, count = 0;
    for (var item in PAGE_CACHE.tabA) {
        var curItem = PAGE_CACHE.tabA[item];
        var id = curItem.id;
        var name = curItem.name;
        if (count == 0)
            firstID = id;

        var div = DOM.$C("div", {
            id : obj.container.id + "_rollCon_" + id,
            dataId : id,
            order : count,
            innerHTML : "loading...",
            className : "singleBlock"
        });
        DOM.$A(div, tabContent);
        div.style.height = (__ISIE ? __SINGLEHEIGHT : __SINGLEHEIGHT - 11)  + "px";
        div.style.overflow = "auto";

        count++;
    }
     if (firstID) loadData(obj, firstID);
}

// ==============================================================
// 左上模块OVER时动画操作
// ==============================================================
function loadData(obj, id) {
    var div = DOM.$G(obj.container.id + "_rollCon_" + id), con = DOM.$G("mainTabTarget");
    var order = div.getAttribute("order");
    if (null != __MOVECMD)
        clearTimeout(__MOVECMD);
    moveToView(con, order * __SINGLEHEIGHT, div);
}

// ==============================================================
// 左上模块OVER时动画操作
// ==============================================================
function moveToView(o, t, div) {
    if (!__ISIE) {
        o.scrollTop = t;
        return;
    }
    var s = Math.floor(o.scrollTop);
    var marge = Math.floor(Math.abs(s - t)) / 5 + 1;
    if (s != t) {
        if (s > t) {
            o.scrollTop = s - marge;
        } else if (s < t) {
            o.scrollTop = s + marge;
        }
        __MOVECMD = setTimeout(function() {
            moveToView(o, t, div);
        }, 20);
    } else {
        loadDetailData(div);
    }
}

// ==============================================================
// 左上模块OVER动画过后取单个模块数据
// ==============================================================
function loadDetailData(div) {
    var loaded = div.getAttribute("_loaded");
    if (loaded == "true")
        return;
    //div.innerHTML = "loading...";
    div.innerHTML = "欢迎登陆";
    var id = div.getAttribute("dataId");
    var p = new ajax();
    p.url = PAGE_CACHE.DWR[id].url;
    p.onresult = function() {
        if (null == this.data)
            return;
        fillTaskBlock(this.data, div, id);
    }
    p.send();
}

// ==============================================================
// 左上模块填充单个模块数据
// ==============================================================
function fillTaskBlock(data, div, id) {
    var result = data.selectSingleNode(".//result");
    var items = result.selectNodes(".//item"), iLen = items.length;
    if (iLen != 0) {
        div.innerHTML = "";
            var ul = DOM.$C("ul", {
            className : "taskUL"
        });
        DOM.$A(ul, div);
    }
    for (var i = 0; i < iLen; i++) {
        var curItem = items[i];
        var name = curItem.getAttribute("name");
        var value = curItem.getAttribute("value");
        var url = curItem.getAttribute("url");
        var sysid = curItem.getAttribute("sysid");
        var menuid = curItem.getAttribute("menuid");
        var targetUrl = PAGE_CACHE.DWR[id].root + url;
        var li = DOM.$C("li", {
            className : "taskLI",
            innerHTML : name + ((name.indexOf(":")!=-1||name.indexOf("：")!=-1)?"":"：") 
            //+ "<a href='javascript:void(0);' onclick='parent.window.frames[\"cbody\"].JumpToFocusItem(\"" + sysid + "\",\"" + menuid + "\",\"" + targetUrl + "\")' target='_self'>" 
            //+ value + "</a>"
            + value
        });
        DOM.$A(li, ul);
    }
    var table = result.selectSingleNode(".//table");
    buildTaskTable(table, div, id);
}
// ==============================================================
// 构建左上单个模块里TABEL
// ==============================================================
function buildTaskTable(data, div, id) {
    if (null == data)
        return;
    var tUrl = data.getAttribute("url");
    var thead = data.selectSingleNode("./thead");
    if (null == thead)
        return;
    var headCells = thead.selectNodes("./cell");
    if (headCells.length != 0) {
        var d = DOM.$C("div");
        DOM.$A(d, div);
        var t = DOM.$C("table", {
            cellSpacing : 0,
            cellPadding : 0,
            className : "taskTable"
        });
        DOM.$A(t, d);
        var tb = DOM.$C("tbody");
        DOM.$A(tb, t);
        var tr = DOM.$C("tr");
        DOM.$A(tr, tb);
    }

    for (var i = 0, iLen = headCells.length; i < iLen; i++) {
        var th = DOM.$C("th", {
            innerHTML : headCells[i].getAttribute("name")
        });
        DOM.$A(th, tr);
    }

    var b = data.selectSingleNode("./tbody");
    if (null == b)
        return;
    var rows = b.selectNodes("./row");
    for (var i = 0, iLen = rows.length; i < iLen; i++) {
        var curRow = rows[i];
        var bTR = DOM.$C("tr", {
            className : i % 2 == 0 ? "rowA" : "rowB"
        });
        DOM.$A(bTR, tb);
        var cells = curRow.selectNodes("./cell");
        for (var j = 0, jLen = cells.length; j < jLen; j++) {
            var cell = cells[j];
            var value = cell.getAttribute("value");
            var target = cell.getAttribute("target");
            var key = cell.getAttribute("key") || "";
            var cUrl = cell.getAttribute("url");
            var sysid = cell.getAttribute("sysid") || "";
			var menuid = cell.getAttribute("menuid") || "";
            var html = value;
            //存放每个条目具体的跳转地址
            var targetUrl = "";
            if (target == "true") {
                if (cUrl) {
                    targetUrl = PAGE_CACHE.DWR[id].root + (cUrl + key);
                } else {
                    targetUrl = PAGE_CACHE.DWR[id].root + (tUrl + key);
                }
                if (sysid != "" && menuid != "")
	            //html = "<a href='javascript:void(0);' onclick='JumpToFocusItem(\"" + sysid + "\",\"" + menuid + "\",\"" + targetUrl + "\")'";
                //html += " target='_self'>" + value + "</a>";
                html = value;
            }
            var td = DOM.$C("td", {
                innerHTML : html
            });
            DOM.$A(td, bTR);
        }
    }
}

// ==============================================================
// 左下通知模块构建
// ==============================================================
function buildInfoBlock() {
    var s = new ScrollTab();
    var con = s.$("infoBlock");
    s.dataType = 2;
    s.container = con;
    s.data = PAGE_CACHE.tabB;
    s.singleWidth = 70;
    s.width = (screen.availWidth - 64) / 2 - 42;;
    s.init(1);
    s.clickEvt = function(elm) {
        var dataId = elm.getAttribute("dataId");
        loadInfo(dataId);
    }

    loadInfo(0);
}

// ==============================================================
// 左下模块TAB页OVER后取单个模块数据
// ==============================================================
function loadInfo(id) {
    // 这个方法传进来的id就是DWR数据的数组索引,需要第几个JSON对象就可以用PAGE_CACHE.DWR[id],如果要取当前的KEY:PAGE_CACHE.DWR[id].key;
    var con = document.getElementById("infoBlockContent");
    con.innerHTML = "loading...";
    dwrAsynService.getNotices(PAGE_CACHE.DWR[id].key, function(ns) {
        con.innerHTML = "";
        buildTaskTable2(ns[0], con, ns[1], id);
    });
}

// ==============================================================
// 构建左下单个模块里TABEL
// ==============================================================
function buildTaskTable2(data, div, num, id) {
    if (data && data.length) {
        var d = DOM.$C("div"); DOM.$A(d, div);
        var t = DOM.$C("table", { cellSpacing : 0, cellPadding : 0, className : "taskTable" }); DOM.$A(t, d);
        var tb = DOM.$C("tbody"); DOM.$A(tb, t);

        t.style.border = 0;
        t.style.background = "";

        var p = DOM.$A(DOM.$D("span", "style","visibility:hidden"), document.body);
        for (var i = 0; i < data.length; i++) {
            var bTR = DOM.$C("tr"); DOM.$A(bTR, tb);
            var bTD = DOM.$A(DOM.$C("td"), bTR);
            bTD.style.height = 28;

			if (id == 0) {
				var bTD1 = DOM.$A(DOM.$D("td","nowrap", "nowrap", {width: 70, innerHTML: data[i].type}), bTR);
	        }
            var bTD2 = DOM.$A(DOM.$D("td","nowrap", "nowrap", {width: 140, innerHTML: dateFormat(data[i].createTime, "yyyy-MM-dd hh:mm:ss")}), bTR);

            var L = bTD.offsetWidth - 45;
            p.innerHTML = data[i].title;
            bTD.innerHTML = "<a" + (p.offsetWidth>L?" style='overflow-x:hidden;text-overflow:ellipsis;width:"+L+"px;'":"")
            + " href='javascript:;' onclick=\"OpenWin('viewNotice.action?id=" 
            + data[i].id + "',null,null,700,500)\"" + (p.offsetWidth>L?" title='" + data[i].title + "'":"") 
            + ">" + data[i].title + "</a>";
            if (data[i].status == "true")
                bTD.innerHTML += " <img align='absmiddle' src='" + rootPath + "img/new.gif'>";
        }
        if (data.length < num) {
            var d = DOM.$C("div", {align: "right"}); 
            DOM.$A(d, div);
            d.style.paddingTop = 2;
            d.style.paddingRight = 10;
            d.innerHTML = "<a href='listSNotice.action?notice.type="+PAGE_CACHE.DWR[id].key+"' target='_self'>more...</a>";
        }
        p.removeNode(true);
    } else {
        var d = DOM.$C("div", {innerHTML: "无通告信息！"}); 
        DOM.$A(d, div);
        d.style.padding = 10;
    }
}

function dateFormat(d, f) { // 格式化日期
	if(!d || d.length==0){
		return "";
	}
	
    var o = {
        "M+" : d.getMonth()+1, //month
        "d+" : d.getDate(),    //day
        "h+" : d.getHours(),   //hour
        "m+" : d.getMinutes(), //minute
        "s+" : d.getSeconds(), //second
        "q+" : Math.floor((d.getMonth()+3)/3),  //quarter
        "S" : d.getMilliseconds() //millisecond
    }
    if (/(y+)/.test(f))
        f = f.replace(RegExp.$1, (d.getFullYear()+"").substr(4-RegExp.$1.length));
    for (var k in o)
        if (new RegExp("("+ k +")").test(f))
            f = f.replace(RegExp.$1, RegExp.$1.length==1?o[k]:("00"+o[k]).substr((""+o[k]).length));
    return f;
}

// ==============================================================
// DOM操作方法封装
// ==============================================================
var DOM = {
	$G : function(id) {
		return document.getElementById(id);
	},
	$C : function(tagName, attrObject) {
		var elm = window.document.createElement(tagName);
		var spArr = ["innerHTML", "className", "cellSpacing", "cellPadding",
				"id", "colSpan", "rowSpan"];
		if (null != attrObject && typeof(attrObject) == "object") {
			for (var item in attrObject) {
				if (spArr.indexOf(item) != -1)
					elm[item] = attrObject[item];
				else
					elm.setAttribute(item, attrObject[item]);
			}
		}
		return elm;
	},
	$D : function(eleName, id, name, attrObject) {
		var elm = window.document.createElement(eleName);
		elm.setAttribute(id,name);
		var spArr = ["innerHTML", "className", "cellSpacing", "cellPadding",
				"id", "colSpan", "rowSpan"];
		if (null != attrObject && typeof(attrObject) == "object") {
			for (var item in attrObject) {
				if (spArr.indexOf(item) != -1)
					elm[item] = attrObject[item];
				else
					elm.setAttribute(item, attrObject[item]);
			}
		}
		return elm;
	},
	$A : function(elm, pElm) {
		return pElm ? pElm.appendChild(elm) : document.body.appendChild(elm);
	},
	$I : function(elm, oElm, pElm) {
		return pElm ? pElm.insertBefore(elm, oElm) : document.body
				.insertBefore(elm, oElm);
	},
	$R : function(elm, pElm) {
		return pElm ? pElm.removeChild(elm) : document.body.removeChild(elm);
	},
	$T : function(tagName, pElm) {
		return pElm ? pElm.getElementsByTagName(tagName) : document
				.getElementsByTagName(tagName);
	},
	$M : function(att) {
		if (att && typeof(att) == "string") {
			var reg = /(^\s*)|(\s*$)/g;
			var a = att.split("=")[0].replace(reg, "");
			var v = att.split("=")[1].replace(reg, "");
		} else {
			return [];
		}
		var arr = [];
		var elms = this.$T("*");
		for (var i = 0, iLen = elms.length; i < iLen; i++) {
			if (elms[i].getAttribute(a) == v) {
				arr.push(elms[i]);
			}
		}
		return arr;
	}
}

if (![].indexOf) {
	Array.prototype.indexOf = function(key) {
		var index = -1;
		for (var i = 0, iLen = this.length; i < iLen; i++) {
			if (this[i] == key) {
				index = i;
				break;
			}
		}
		return index;
	}
}

// ==============================================================
// 根据JS文件名取文件地址
// ==============================================================
PATH = {
	GetScriptPath : function(jsName) {
		var scripts = DOM.$T("script"), reg = new RegExp(jsName.replace(".","\.") + "\?.*", "i"), path = "";
		for (var i = 0, iLen = scripts.length; i < iLen; i++) {
			var curScript = scripts[i];
			var reg
			if (reg.test(curScript.src)) {
				path = curScript.src.replace(reg, "");
				break;
			}
		}
		return path;
	}
}

// ==============================================================
// 解析DWR数据
// ==============================================================
function analysisData(data) {
	var t1 = [], t2 = [];
	for (var i = 0, iLen = data.length; i < iLen; i++) {
		var json = data[i];
		var key = json.key;
		var value = json.value;
		var url = json.url;
		var root = json.root;

		if (i > 1) {
			t1.push(i + ":{id:'" + i + "',name:'" + value + "'}");
		}
		t2.push(i + ":{id:'" + i + "',name:'" + value + "'}");
		if (i == 2)
			PAGE_CACHE.rootPath = root;
	}
	eval("PAGE_CACHE.tabA={" + t1.join(",") + "}");
	eval("PAGE_CACHE.tabB={" + t2.join(",") + "}");
	PAGE_CACHE.DWR = data;
}

// ==============================================================
// 添加onload事件
// ==============================================================
window.onload = function() {
	dwrAsynService.getSubSystems(true, function(tabs) {
		analysisData(tabs);
		init();
	});
//	dwrAsynService.getUserInfos(function (infos) {
//		buildUITable(infos);
//	});
};

// ==============================================================
// 构建右侧TABEL
// ==============================================================
function buildUITable(data) {
  var block = DOM.$G("userInfoBlock");
	var p = DOM.$G("userInfoBlock");
  p.style.width = block.offsetWidth + "px";
  p.style.height = block.parentNode.offsetHeight - 10 + "px";
  p.style.overflowX = "auto";

//	var w = p.offsetWidth;
	var d = DOM.$C("div"); DOM.$A(d, p);
	var t = DOM.$C("table", { cellSpacing : 0, cellPadding : 0, className : "taskTable" }); DOM.$A(t, d);
	var tb = DOM.$C("tbody"); DOM.$A(tb, t);
	var tr = DOM.$C("tr"); DOM.$A(tr, tb);
	for (var v in data[0]) {
		var bTR = DOM.$C("tr", { className : "rowB" }); DOM.$A(bTR, tb);
		DOM.$A(DOM.$D("td","nowrap", "nowrap", { width: "40%", align: "right", innerHTML : v + "：" }), bTR);
		DOM.$A(DOM.$C("td", { innerHTML : data[0][v] }), bTR);
		bTR.cells[0].style.height = "27px";
	}
	
	d = DOM.$C("div"); DOM.$A(d, p);
//	d.style.width = w - 10;
//	d.style.overflow = "auto";
	t = DOM.$C("table", { cellSpacing : 0, cellPadding : 0, className : "taskTable" }); DOM.$A(t, d);
	tb = DOM.$C("tbody"); DOM.$A(tb, t);
	tr = DOM.$C("tr"); DOM.$A(tr, tb);

	DOM.$A(DOM.$C("th", { innerHTML : "时间" }), tr);
	DOM.$A(DOM.$C("th", { innerHTML : "IP地址" }), tr);
	DOM.$A(DOM.$C("th", { innerHTML : "事件" }), tr);
	if (data.length > 1 && data[1].length) {
		for (var i = 0; i < data[1].length; i++) {
			var bTR = DOM.$C("tr", { className : i % 2 == 0 ? "rowA" : "rowB" }); DOM.$A(bTR, tb);
			DOM.$A(DOM.$D("td","nowrap", "nowrap", { innerHTML : dateFormat(data[1][i].execTime, "yyyy-MM-dd hh:mm") }), bTR);
			DOM.$A(DOM.$D("td","nowrap", "nowrap", { innerHTML : data[1][i].ip }), bTR);
			DOM.$A(DOM.$D("td","nowrap", "nowrap", { innerHTML : data[1][i].description }), bTR);
			bTR.cells[0].style.height = "28px";
		}
	}
}

BTN.addEvent(window,"resize",function(){
    //var block = DOM.$G("userInfoBlock");
    //var p = DOM.$G("userInfoBlock");
    //p.style.width = block.offsetWidth + "px";
    //p.style.overflow = "auto";
});