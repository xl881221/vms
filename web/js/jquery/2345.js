//===============================================rili.core.js================//

var JSON = function () {
    var m = {
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        },
        s = {
            'boolean': function (x) {
                return String(x);
            },
            number: function (x) {
                return isFinite(x) ? String(x) : 'null';
            },
            string: function (x) {
                if (/["\\\x00-\x1f]/.test(x)) {
                    x = x.replace(/([\x00-\x1f\\"])/g, function(a, b) {
                        var c = m[b];
                        if (c) {
                            return c;
                        }
                        c = b.charCodeAt();
                        return '\\u00' +
                            Math.floor(c / 16).toString(16) +
                            (c % 16).toString(16);
                    });
                }
                return '"' + x + '"';
            },
            object: function (x) {
                if (x) {
                    var a = [], b, f, i, l, v;
                    if (x instanceof Array) {
                        a[0] = '[';
                        l = x.length;
                        for (i = 0; i < l; i += 1) {
                            v = x[i];
                            f = s[typeof v];
                            if (f) {
                                v = f(v);
                                if (typeof v == 'string') {
                                    if (b) {
                                        a[a.length] = ',';
                                    }
                                    a[a.length] = v;
                                    b = true;
                                }
                            }
                        }
                        a[a.length] = ']';
                    } else if (x instanceof Object) {
                        a[0] = '{';
                        for (i in x) {
                            v = x[i];
                            f = s[typeof v];
                            if (f) {
                                v = f(v);
                                if (typeof v == 'string') {
                                    if (b) {
                                        a[a.length] = ',';
                                    }
                                    a.push(s.string(i), ':', v);
                                    b = true;
                                }
                            }
                        }
                        a[a.length] = '}';
                    } else {
                        return;
                    }
                    return a.join('');
                }
                return 'null';
            }
        };
    return {
        copyright: '(c)2005 JSON.org',
        license: 'http://www.crockford.com/JSON/license.html',
/*
    Stringify a JavaScript value, producing a JSON text.
*/
        stringify: function (v) {
            var f = s[typeof v];
            if (f) {
                v = f(v);
                if (typeof v == 'string') {
                    return v;
                }
            }
            return null;
        },
/*
    Parse a JSON text, producing a JavaScript value.
    It returns false if there is a syntax error.
*/
        parse: function (text) {
            try {
                return !(/[^,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t]/.test(
                        text.replace(/"(\\.|[^"\\])*"/g, ''))) &&
                    eval('(' + text + ')');
            } catch (e) {
                return false;
            }
        }
    };
}();
/**
 * JavaScript Common Templates(jCT) 3(第3版)
 * http://code.google.com/p/jsct/
 *
 * licensed under the MIT license.
 * http://www.opensource.org/licenses/mit-license.php
 *
 * Author achun (achun.shx at gmail.com)
 * Create Date: 2008-6-23
 * Last Date: 2008-10-29
 * Revision:3.8.10.29
 */
function jCT(txt,path){//构建jCT对象,仅仅准备基础数据
	this.Fn = new arguments.callee.Instance(txt,path);
	for (var i in this)
		this.Fn.Reserve+=','+i+',';
}
jCT.prototype={
	Extend:function(jct){//扩展自己
		for (var i in jct){
			if(this[i] && this[i].Fn && this[i].Fn.JavaScriptCommonTemplates && this[i].Extend )
				this[i].Extend(jct[i]);
			else if(this.Fn.Reserve.indexOf(','+i+',')==-1)//防止保留成员被覆盖
				this[i]=jct[i];
		}
		if(typeof jct.RunNow=='function')
			jct.RunNow.call(this);
		return this;
	},
	ExtendTo:function(jct){//附加到其他对象上
		for (var i in this){
			if(this.Fn.Reserve.indexOf(','+i+',')>0 && jct[i]) continue;
			if(jct[i]==null)
				jct[i]=this[i];
			else if(this[i].Fn && this[i].Fn.JavaScriptCommonTemplates && this[i].ExtendTo)
				this[i].ExtendTo(jct[i]);
		}
		if(typeof jct.RunNow=='function')
			jct.RunNow();
		return this;
	},
	ExecChilds:function(childs,exec){//执行childs对象所列成员里的某个方法，默认是Exec方法
		if(typeof childs=='string'){
			exec=childs;
			childs=0;
		}else
			exec=exec||'Exec';
		if(!childs){
			childs={};
			for (var c in this)
				if(this[c].Fn && this[c].Fn.JavaScriptCommonTemplates)
					childs[c]=false;
		}
		for(var c in childs)
			if(this[c] && (typeof this[c][exec]=='function')){
				this[c][exec](childs[c]);
		}
		return this;
	},
	BuildChilds:function(childs){//构建子jCT对象,并执行RunNow
		if(undefined==childs) childs=[];
		if (typeof childs=='string') childs=childs.split(',');
		var cs={};
		for(var i=0;i<childs.length;i++) cs[childs[i]]=true;
		for (var i in this)
		if(this[i].Fn && this[i].Fn.JavaScriptCommonTemplates && (childs.length==0 || cs[i]))
			this[i].Build();
		return this;
	},
	GetView:function(){return 'Invalid templates';},//得到装配数据后的视图，此方法会在Build的过程中重建,并且清除输出缓存
	GetViewContinue:function(){return 'Invalid templates';},//得到装配数据后的视图，此方法会在Build的过程中重建
	Build:function(txt,path){//构建实例
		this.Fn.Init(txt,path);
		this.Fn.Build(this);
		return this;
	}
};
jCT.Instance=function(txt,path){
	this.Src=txt||'';
	this.Path=path||'';
};
jCT.Instance.prototype={
	JavaScriptCommonTemplates:3.0,
	Reserve:'',//保留成员
	Tags:{//几种不同的模板定义风格
		comment:{//注释标签风格
			block:{begin:'<!---',end:'-->'},//语法块标记
			exp:{begin:'+-',end:'-+'},//取值表达式
			member:{begin:'/*+',end:'*/'},//定义成员语法标记
			memberend:{begin:'/*-',end:'*/'},//定义成员结束语法标记
			clean:{begin:'<!--clean',end:'/clean-->'}//清理标记
		},
		script:{//脚本标签风格
			block:{begin:'<script type="text/jct">',end:'</script>'},
			exp:{begin:'+-',end:'-+'},
			member:{begin:'/*+',end:'*/'},
			memberend:{begin:'/*-',end:'*/'},
			clean:{begin:'<!--clean',end:'/clean-->'}
		},
		code:{//code标签风格
			block:{begin:'<code class="jct">',end:'</code>'},
			exp:{begin:'+-',end:'-+'},
			member:{begin:'/*+',end:'*/'},
			memberend:{begin:'/*-',end:'*/'},
			clean:{begin:'<!--clean',end:'/clean-->'}
		}
	},
	Init:function(txt,path){
		if(txt!=undefined) this.Src=txt;
		if(path!=undefined) this.Path=path;
		for (var tag in this.Tags)//自动判断模板风格
			if (this.Src.indexOf(this.Tags[tag].block.begin)>=0) break;
		this.Tag=this.Tags[tag];
		this.A=[];//由src转换的模板数组
		this.V=[];//执行的文本结果,以数组形式存放
		this.EXEC=[];//
		var a=[];
		var p=[0,0,0,0,0];
		var max=this.Src.length;
		while (this.Slice(this.Tag.clean,p[4],p,max))
			a.push(this.Src.slice(p[0],p[1]));
		if(a.length){
			a.push(this.Src.slice(p[4]));
			this.Src = a.join('');
		}
	},
	Build:function(self){
		this.EXEC=[];
		this.Parse(self);
		try{
			var code=this.EXEC.join('\n');
			self.GetViewContinue=new Function(code);
			self.GetView=function(){this.Fn.V=[];this.GetViewContinue.apply(this,arguments);return this.Fn.V.join('');};
		}catch (ex){
			this.V=['jCT Parse Error'];
			self.ERROR={message:ex.message + '\n'+ (ex.lineNumber || ex.number),code:code};
		}
		if(self.RunNow)
			self.RunNow();
	},
	Parse:function(self){
		var tag = this.Tag,A = this.A,E=this.EXEC,max= this.Src.length,p=[0,0,0,0,0],p1=[0,0,0,0,0];
		while (this.Slice(tag.block,p[4],p,max)){//语法分2段
			p1=[0,0,0,0,p[0]];
			while (this.Slice(tag.exp,p1[4],p1,p[1])){//处理取值表达式
				E.push('this.Fn.V.push(this.Fn.A['+A.length+']);');
				A.push(this.Src.slice(p1[0],p1[1]));
				E.push('this.Fn.V.push('+this.Src.slice(p1[2],p1[3])+');');
			}
			E.push('this.Fn.V.push(this.Fn.A['+A.length+']);');
			A.push(this.Src.slice(p1[4],p[1]));
			if (this.Slice(tag.member,p[2],p1,p[3])){//处理扩展语法
				var str=this.Src.slice(p1[2],p1[3]);
				var foo=this.Src.slice(p1[4],p[3]);
				if (str.slice(0,1)=='@'){//子模板
					var child=tag.block.begin+tag.memberend.begin+str+tag.memberend.end+tag.block.end;
					var tmp = this.Src.indexOf(child,p[4]);
					if (tmp>0){
						var njct=new jCT(this.Src.slice(p[4],tmp),this.Path);
						if(!self[str.slice(1)]) self[str.slice(1)]={};
						for (var j in njct) 
							self[str.slice(1)][j]=njct[j];
						p[4] = tmp + child.length;
					}
				}else if (str.slice(0,1)=='$'){//成员对象
					var obj=new Function('return '+foo+';');
					self[str.slice(1)]=obj.call(self);
				}else //成员函数
					self[str]=new Function(foo);
			}else//javascript语句
				E.push(this.Src.slice(p[2],p[3]));
		}
		p1=[0,0,0,0,p[4]];p[1]=max;
		while (this.Slice(tag.exp,p1[4],p1,p[1])){//处理取值表达式
			E.push('this.Fn.V.push(this.Fn.A['+A.length+']);');
			A.push(this.Src.slice(p1[0],p1[1]));
			E.push('this.Fn.V.push('+this.Src.slice(p1[2],p1[3])+');');
		}
			E.push('this.Fn.V.push(this.Fn.A['+A.length+']);');
			A.push(this.Src.slice(p1[4],p[1]));
	},
	Slice:function(tn,b1,p,max){//把string第2段分成2段
		var begin=tn.begin;
		var end=tn.end;
		var e1,b2,e2;
		e1=this.Src.indexOf(begin,b1);
		if (e1<0 || e1>=max) return false;
		b2=e1+begin.length;
		if (b2<0 || b2>=max) return false;
		e2=this.Src.indexOf(end,b2);
		if (e2<0 || e2>=max) return false;
		p[0]=b1;p[1]=e1;
		p[2]=b2;p[3]=e2;
		p[4]=e2+end.length;
		return true;
	}
};

var ua=navigator.userAgent.toLowerCase();
var isOpera=(ua.indexOf('opera')>-1);
var isSafari=(ua.indexOf('safari')>-1);
var isIE=(!isOpera&&ua.indexOf('msie')>-1);
var isNs=ua.indexOf('mozilla')>-1;

//常量定义，请勿直接使用值!!
var ORDER_ASC = 'asc';
var ORDER_DESC = 'desc';
var TYPE_LUNAR = 'lunar';


var ieBody = (document.compatMode && document.compatMode != "BackCompat") ? document.documentElement : document.body;


var bind = function(func, obj){
	var __method = func;
	return function(){
		var args = [];
		for (var i = 0; i < arguments.length; i++) 
			args[i] = arguments[i];
		return __method.apply(obj, args);
	}
};

/* jQuery DOM */
jQuery.dom = function(elementId){
	return document.getElementById(elementId);
};
jQuery.fn.extend({
	innerHTML: function(html){
		if (arguments.length == 0) {
			return this[0].innerHTML;
		}
		this[0].innerHTML = html;
		return this;
	}
});
/**
* hoverIntent r5 // 2007.03.27 // jQuery 1.1.2+
* <http://cherne.net/brian/resources/jquery.hoverIntent.html>
* 
* @param  f  onMouseOver function || An object with configuration options
* @param  g  onMouseOut function  || Nothing (use configuration options object)
* @author    Brian Cherne 
*/
(function($){$.fn.hoverIntent=function(f,g){var cfg={sensitivity:7,interval:100,timeout:0};cfg=$.extend(cfg,g?{over:f,out:g}:f);var cX,cY,pX,pY;var track=function(ev){cX=ev.pageX;cY=ev.pageY;};var compare=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);if((Math.abs(pX-cX)+Math.abs(pY-cY))<cfg.sensitivity){$(ob).unbind("mousemove",track);ob.hoverIntent_s=1;return cfg.over.apply(ob,[ev]);}else{pX=cX;pY=cY;ob.hoverIntent_t=setTimeout(function(){compare(ev,ob);},cfg.interval);}};var delay=function(ev,ob){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);ob.hoverIntent_s=0;return cfg.out.apply(ob,[ev]);};var handleHover=function(e){var p=(e.type=="mouseover"?e.fromElement:e.toElement)||e.relatedTarget;while(p&&p!=this){try{p=p.parentNode;}catch(e){p=this;}}if(p==this){return false;}var ev=jQuery.extend({},e);var ob=this;if(ob.hoverIntent_t){ob.hoverIntent_t=clearTimeout(ob.hoverIntent_t);}if(e.type=="mouseover"){pX=ev.pageX;pY=ev.pageY;$(ob).bind("mousemove",track);if(ob.hoverIntent_s!=1){ob.hoverIntent_t=setTimeout(function(){compare(ev,ob);},cfg.interval);}}else{$(ob).unbind("mousemove",track);if(ob.hoverIntent_s==1){ob.hoverIntent_t=setTimeout(function(){delay(ev,ob);},cfg.timeout);}}};return this.mouseover(handleHover).mouseout(handleHover);};})(jQuery);
/* jQuery cookie plugin start */
/* jQuery cookie plugin end */
function StringBuffer(){
	this._strings = new Array();
}

StringBuffer.prototype.append = function(str){
	this._strings.push(str);
	return this;
};
StringBuffer.prototype.toString = function(){
	var str = arguments.length == 0 ? '' : arguments[0];
	return this._strings.join(str);
};
String.prototype.leftpad = function(len, str){
	if (!str) {
		str = '0';
	}
	
	var s = '';
	for (var i = 0; i < len - this.length; i++) {
		s += str;
	}
	return s + this;
}
if (!window.rili) {
	window.rili = new Object();
}
if (!window.rili.controls) {
	window.rili.controls = new Object();
}
if (!window.rili.util) {
	window.rili.util = new Object();
}
var $breakEvent=new Object();
//===============================================huangli.hs===================//
var HuangLi = {};
HuangLi.y2010=JSON.parse('{}');
//===============================================calendarObj.js========================//
/*****************************************************************************
                                   日期资料
*****************************************************************************/

var lunarInfo=new Array(
0x4bd8,0x4ae0,0xa570,0x54d5,0xd260,0xd950,0x5554,0x56af,0x9ad0,0x55d2,
0x4ae0,0xa5b6,0xa4d0,0xd250,0xd295,0xb54f,0xd6a0,0xada2,0x95b0,0x4977,
0x497f,0xa4b0,0xb4b5,0x6a50,0x6d40,0xab54,0x2b6f,0x9570,0x52f2,0x4970,
0x6566,0xd4a0,0xea50,0x6a95,0x5adf,0x2b60,0x86e3,0x92ef,0xc8d7,0xc95f,
0xd4a0,0xd8a6,0xb55f,0x56a0,0xa5b4,0x25df,0x92d0,0xd2b2,0xa950,0xb557,
0x6ca0,0xb550,0x5355,0x4daf,0xa5b0,0x4573,0x52bf,0xa9a8,0xe950,0x6aa0,
0xaea6,0xab50,0x4b60,0xaae4,0xa570,0x5260,0xf263,0xd950,0x5b57,0x56a0,
0x96d0,0x4dd5,0x4ad0,0xa4d0,0xd4d4,0xd250,0xd558,0xb540,0xb6a0,0x95a6,
0x95bf,0x49b0,0xa974,0xa4b0,0xb27a,0x6a50,0x6d40,0xaf46,0xab60,0x9570,
0x4af5,0x4970,0x64b0,0x74a3,0xea50,0x6b58,0x5ac0,0xab60,0x96d5,0x92e0,
0xc960,0xd954,0xd4a0,0xda50,0x7552,0x56a0,0xabb7,0x25d0,0x92d0,0xcab5,
0xa950,0xb4a0,0xbaa4,0xad50,0x55d9,0x4ba0,0xa5b0,0x5176,0x52bf,0xa930,
0x7954,0x6aa0,0xad50,0x5b52,0x4b60,0xa6e6,0xa4e0,0xd260,0xea65,0xd530,
0x5aa0,0x76a3,0x96d0,0x4afb,0x4ad0,0xa4d0,0xd0b6,0xd25f,0xd520,0xdd45,
0xb5a0,0x56d0,0x55b2,0x49b0,0xa577,0xa4b0,0xaa50,0xb255,0x6d2f,0xada0,
0x4b63,0x937f,0x49f8,0x4970,0x64b0,0x68a6,0xea5f,0x6b20,0xa6c4,0xaaef,
0x92e0,0xd2e3,0xc960,0xd557,0xd4a0,0xda50,0x5d55,0x56a0,0xa6d0,0x55d4,
0x52d0,0xa9b8,0xa950,0xb4a0,0xb6a6,0xad50,0x55a0,0xaba4,0xa5b0,0x52b0,
0xb273,0x6930,0x7337,0x6aa0,0xad50,0x4b55,0x4b6f,0xa570,0x54e4,0xd260,
0xe968,0xd520,0xdaa0,0x6aa6,0x56df,0x4ae0,0xa9d4,0xa4d0,0xd150,0xf252,
0xd520);

var solarMonth=new Array(31,28,31,30,31,30,31,31,30,31,30,31);
var Gan=new Array("甲","乙","丙","丁","戊","己","庚","辛","壬","癸");
var Zhi=new Array("子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥");
var Animals=new Array("鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪");
var solarTerm = new Array("小寒","大寒","立春","雨水","惊蛰","春分","清明","谷雨","立夏","小满","芒种","夏至","小暑","大暑","立秋","处暑","白露","秋分","寒露","霜降","立冬","小雪","大雪","冬至");
var sTermInfo = new Array(0,21208,42467,63836,85337,107014,128867,150921,173149,195551,218072,240693,263343,285989,308563,331033,353350,375494,397447,419210,440795,462224,483532,504758);
var nStr1 = new Array('日','一','二','三','四','五','六','七','八','九','十');
var nStr2 = new Array('初','十','廿','卅','□');
var monthName = new Array("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC");
var cmonthName = new Array('正','二','三','四','五','六','七','八','九','十','十一','腊');

//公历节日 *表示放假日
var sFtv = new Array(
"0101*新年元旦",
"0202 世界湿地日",
"0207 国际声援南非日",
"0210 国际气象节",
"0214 情人节",
"0301 国际海豹日",
"0303 全国爱耳日",
"0308 国际妇女节",
"0312 植树节 孙中山逝世纪念日",
"0314 国际警察日",
"0315 国际消费者权益日",
"0317 中国国医节 国际航海日",
"0321 世界森林日 消除种族歧视国际日",
"0321 世界儿歌日",
"0322 世界水日",
"0323 世界气象日",
"0324 世界防治结核病日",
"0325 全国中小学生安全教育日",
"0330 巴勒斯坦国土日",
"0401 愚人节 全国爱国卫生运动月(四月) 税收宣传月(四月)",
"0407 世界卫生日",
"0422 世界地球日",
"0423 世界图书和版权日",
"0424 亚非新闻工作者日",
"0501 国际劳动节",
"0504 中国五四青年节",
"0505 碘缺乏病防治日",
"0508 世界红十字日",
"0512 国际护士节",
"0515 国际家庭日",
"0517 世界电信日",
"0518 国际博物馆日",
"0520 全国学生营养日",
"0523 国际牛奶日",
"0531 世界无烟日", 
"0601 国际儿童节",
"0605 世界环境日",
"0606 全国爱眼日",
"0617 防治荒漠化和干旱日",
"0623 国际奥林匹克日",
"0625 全国土地日",
"0626 国际反毒品日",
"0701 中国共产党建党日 世界建筑日",
"0702 国际体育记者日",
"0707 中国人民抗日战争纪念日",
"0711 世界人口日",
"0730 非洲妇女日",
"0801 中国建军节",
"0808 中国男子节(爸爸节)",
"0815 日本正式宣布无条件投降日",
"0908 国际扫盲日 国际新闻工作者日",
"0910 教师节",
"0914 世界清洁地球日",
"0916 国际臭氧层保护日",
"0918 九·一八事变纪念日",
"0920 国际爱牙日",
"0927 世界旅游日",
"1001*国庆节 世界音乐日 国际老人节",
"1001 国际音乐日",
"1002 国际和平与民主自由斗争日",
"1004 世界动物日",
"1008 全国高血压日",
"1008 世界视觉日",
"1009 世界邮政日 万国邮联日",
"1010 辛亥革命纪念日 世界精神卫生日",
"1013 世界保健日 国际教师节",
"1014 世界标准日",
"1015 国际盲人节(白手杖节)",
"1016 世界粮食日",
"1017 世界消除贫困日",
"1022 世界传统医药日",
"1024 联合国日 世界发展信息日",
"1031 世界勤俭日",
"1107 十月社会主义革命纪念日",
"1108 中国记者日",
"1109 全国消防安全宣传教育日",
"1110 世界青年节",
"1111 国际科学与和平周(本日所属的一周)",
"1112 孙中山诞辰纪念日",
"1114 世界糖尿病日",
"1117 国际大学生节 世界学生节",
"1121 世界问候日 世界电视日",
"1129 国际声援巴勒斯坦人民国际日",
"1201 世界艾滋病日",
"1203 世界残疾人日",
"1205 国际经济和社会发展志愿人员日",
"1208 国际儿童电视日",
"1209 世界足球日",
"1210 世界人权日",
"1212 西安事变纪念日",
"1213 南京大屠杀(1937年)纪念日！紧记血泪史！",
"1221 国际篮球日",
"1224 平安夜",
"1225 圣诞节",
"1229 国际生物多样性日");

//某月的第几个星期几。 5,6,7,8 表示到数第 1,2,3,4 个星期几
var wFtv = new Array(
"0110 黑人日",
"0150 世界麻风日", //一月的最后一个星期日（月倒数第一个星期日）
"0520 国际母亲节",
"0530 全国助残日",
"0630 父亲节",
"0932 国际和平日",
"0940 国际聋人节 世界儿童日",
"0950 世界海事日",
"1011 国际住房日",
"1013 国际减轻自然灾害日(减灾日)",
"1144 感恩节");

//农历节日
var lFtv = new Array(
"0101*春节",
"0115 元宵节",
"0202 龙抬头节",
"0323 妈祖生辰 (天上圣母诞辰)",
"0505 端午节",
"0707 七七中国情人节",
"0815 中秋节",
"0909 重阳节",
"1208 腊八节",
"1223 小年",
"0100*除夕");


/*****************************************************************************
                                      日期计算
*****************************************************************************/

//====================================== 返回农历 y年的总天数
function lYearDays(y) {
 var i, sum = 348;
 for(i=0x8000; i>0x8; i>>=1) sum += (lunarInfo[y-1900] & i)? 1: 0;
 return(sum+leapDays(y));
}

//====================================== 返回农历 y年闰月的天数
function leapDays(y) {
 if(leapMonth(y)) return( (lunarInfo[y-1899]&0xf)==0xf? 30: 29);
 else return(0);
}

//====================================== 返回农历 y年闰哪个月 1-12 , 没闰返回 0
function leapMonth(y) {
 var lm = lunarInfo[y-1900] & 0xf;
 return(lm==0xf?0:lm);
}

//====================================== 返回农历 y年m月的总天数
function monthDays(y,m) {
 return( (lunarInfo[y-1900] & (0x10000>>m))? 30: 29 );
}



//====================================== 算出农历, 传入日期控件, 返回农历日期控件
//                                       该控件属性有 .year .month .day .isLeap
function Lunar(objDate) {

   var i, leap=0, temp=0;
   var offset   = (Date.UTC(objDate.getFullYear(),objDate.getMonth(),objDate.getDate()) - Date.UTC(1900,0,31))/86400000;

   for(i=1900; i<2100 && offset>0; i++) { temp=lYearDays(i); offset-=temp; }

   if(offset<0) { offset+=temp; i--; }

   this.year = i;

   leap = leapMonth(i); //闰哪个月
   this.isLeap = false;

   for(i=1; i<13 && offset>0; i++) {
      //闰月
      if(leap>0 && i==(leap+1) && this.isLeap==false)
         { --i; this.isLeap = true; temp = leapDays(this.year); }
      else
         { temp = monthDays(this.year, i); }

      //解除闰月
      if(this.isLeap==true && i==(leap+1)) this.isLeap = false;

      offset -= temp;
   }

   if(offset==0 && leap>0 && i==leap+1)
      if(this.isLeap)
         { this.isLeap = false; }
      else
         { this.isLeap = true; --i; }

   if(offset<0){ offset += temp; --i; }

   this.month = i;
   this.day = offset + 1;
}

function getSolarDate(lyear, lmonth, lday, isLeap) {
  var offset = 0;
  
  // increment year
  for(var i = 1900; i < lyear; i++) {
    offset += lYearDays(i);
  }

  // increment month
  // add days in all months up to the current month
  for (var i = 1; i < lmonth; i++) {
    // add extra days for leap month
    if (i == leapMonth(lyear)) {
      offset += leapDays(lyear);
    }
    offset += monthDays(lyear, i);
  }
  // if current month is leap month, add days in normal month
  if (isLeap) {
    offset += monthDays(lyear, i);
  }
   
  // increment 
  offset += parseInt(lday) - 1;

  var baseDate = new Date(1900,0,31);
  var solarDate = new Date(baseDate.valueOf() + offset * 86400000);
  return solarDate;
}


//==============================返回公历 y年某m+1月的天数
function solarDays(y,m) {
   if(m==1)
      return(((y%4 == 0) && (y%100 != 0) || (y%400 == 0))? 29: 28);
   else
      return(solarMonth[m]);
}

//============================== 传入 offset 返回干支, 0=甲子
function cyclical(num) {
   return(Gan[num%10]+Zhi[num%12]);
}


//============================== 阴历属性
function calElement(sYear,sMonth,sDay,week,lYear,lMonth,lDay,isLeap,cYear,cMonth,cDay) {

      this.isToday    = false;
      //瓣句
      this.sYear      = sYear;   //公元年4位数字
      this.sMonth     = sMonth;  //公元月数字
      this.sDay       = sDay;    //公元日数字
      this.week       = week;    //星期, 1个中文
      //农历
      this.lYear      = lYear;   //公元年4位数字
      this.lMonth     = lMonth;  //农历月数字
      this.lDay       = lDay;    //农历日数字
      this.isLeap     = isLeap;  //是否为农历闰月?
      //八字
      this.cYear      = cYear;   //年柱, 2个中文
      this.cMonth     = cMonth;  //月柱, 2个中文
      this.cDay       = cDay;    //日柱, 2个中文

      this.color      = '';

      this.lunarFestival = ''; //农历节日
      this.solarFestival = ''; //公历节日
      this.solarTerms    = ''; //节气
}

//===== 某年的第n个节气为几日(从0小寒起算)
function sTerm(y,n) {
   var offDate = new Date( ( 31556925974.7*(y-1900) + sTermInfo[n]*60000  ) + Date.UTC(1900,0,6,2,5) );
   return(offDate.getUTCDate());
}





//============================== 返回阴历控件 (y年,m+1月)
/*
功能说明: 返回整个月的日期资料控件

使用方式: OBJ = new calendar(年,零起算月);

  OBJ.length      返回当月最大日
  OBJ.firstWeek   返回当月一日星期

  由 OBJ[日期].属性名称 即可取得各项值

  OBJ[日期].isToday  返回是否为今日 true 或 false

  其他 OBJ[日期] 属性参见 calElement() 中的注解
*/
function calendar(y,m) {

   var sDObj, lDObj, lY, lM, lD=1, lL, lX=0, tmp1, tmp2, tmp3;
   var cY, cM, cD; //年柱,月柱,日柱
   var lDPOS = new Array(3);
   var n = 0;
   var firstLM = 0;

   sDObj = new Date(y,m,1,0,0,0,0);    //当月一日日期

   this.length    = solarDays(y,m);    //公历当月天数
   this.firstWeek = sDObj.getDay();    //公历当月1日星期几

   ////////年柱 1900年立春后为庚子年(60进制36)
   if(m<2) cY=cyclical(y-1900+36-1);
   else cY=cyclical(y-1900+36);
   var term2=sTerm(y,2); //立春日期

   ////////月柱 1900年1月小寒以前为 丙子月(60进制12)
   var firstNode = sTerm(y,m*2) //返回当月「节」为几日开始
   cM = cyclical((y-1900)*12+m+12);

   //当月一日与 1900/1/1 相差天数
   //1900/1/1与 1970/1/1 相差25567日, 1900/1/1 日柱为甲戌日(60进制10)
   var dayCyclical = Date.UTC(y,m,1,0,0,0,0)/86400000+25567+10;

   for(var i=0;i<this.length;i++) {

      if(lD>lX) {
         sDObj = new Date(y,m,i+1);    //当月一日日期
         lDObj = new Lunar(sDObj);     //农历
         lY    = lDObj.year;           //农历年
         lM    = lDObj.month;          //农历月
         lD    = lDObj.day;            //农历日
         lL    = lDObj.isLeap;         //农历是否闰月
         lX    = lL? leapDays(lY): monthDays(lY,lM); //农历当月最后一天

         if(n==0) firstLM = lM;
         lDPOS[n++] = i-lD+1;
      }

      //依节气调整二月分的年柱, 以立春为界
      if(m==1 && (i+1)==term2) cY=cyclical(y-1900+36);
      //依节气月柱, 以「节」为界
      if((i+1)==firstNode) cM = cyclical((y-1900)*12+m+13);
      //日柱
      cD = cyclical(dayCyclical+i);

      //sYear,sMonth,sDay,week,
      //lYear,lMonth,lDay,isLeap,
      //cYear,cMonth,cDay
      this[i] = new calElement(y, m+1, i+1, nStr1[(i+this.firstWeek)%7],
                               lY, lM, lD++, lL,
                               cY ,cM, cD );
   }

   //节气
   tmp1=sTerm(y,m*2  )-1;
   tmp2=sTerm(y,m*2+1)-1;
   this[tmp1].solarTerms = solarTerm[m*2];
   this[tmp2].solarTerms = solarTerm[m*2+1];
   //if(m==3) this[tmp1].color = 'red'; //清明颜色

   //公历节日
   for(i in sFtv)
      if(sFtv[i].match(/^(\d{2})(\d{2})([\s\*])(.+)$/))
         if(Number(RegExp.$1)==(m+1)) {
            this[Number(RegExp.$2)-1].solarFestival += RegExp.$4 + ' ';
            if(RegExp.$3=='*') this[Number(RegExp.$2)-1].color = 'red';
         }

   //月周节日
   for(i in wFtv)
      if(wFtv[i].match(/^(\d{2})(\d)(\d)([\s\*])(.+)$/))
         if(Number(RegExp.$1)==(m+1)) {
            tmp1=Number(RegExp.$2);
            tmp2=Number(RegExp.$3);
            if(tmp1<5)
               this[((this.firstWeek>tmp2)?7:0) + 7*(tmp1-1) + tmp2 - this.firstWeek].solarFestival += RegExp.$5 + ' ';
            else {
               tmp1 -= 5;
               tmp3 = (this.firstWeek+this.length-1)%7; //当月最后一天星期?
               this[this.length - tmp3 - 7*tmp1 + tmp2 - (tmp2>tmp3?7:0) - 1 ].solarFestival += RegExp.$5 + ' ';
            }
         }

   //农历节日
   for(i in lFtv)
      if(lFtv[i].match(/^(\d{2})(.{2})([\s\*])(.+)$/)) {
         tmp1=Number(RegExp.$1)-firstLM;
         if(tmp1==-11) tmp1=1;
         if(tmp1 >=0 && tmp1<n) {
            tmp2 = lDPOS[tmp1] + Number(RegExp.$2) -1;
            if( tmp2 >= 0 && tmp2<this.length && this[tmp2].isLeap!=true) {
               this[tmp2].lunarFestival += RegExp.$4 + ' ';
               if(RegExp.$3=='*') this[tmp2].color = 'red';
            }
         }
      }


   //复活节只出现在3或4月
   if(m==2 || m==3) {
      var estDay = new easter(y);
      if(m == estDay.m)
         this[estDay.d-1].solarFestival = this[estDay.d-1].solarFestival+' 复活节 Easter Sunday';
   }

   //黑色星期五
   if((this.firstWeek+12)%7==5)
      this[12].solarFestival += '黑色星期五';

   //今日
   //if(y==g_tY && m==g_tM) {this[g_tD-1].isToday = true;}

}




//======================================= 返回该年的复活节(春分后第一次满月周后的第一主日)
function easter(y) {

   var term2=sTerm(y,5); //取得春分日期
   var dayTerm2 = new Date(Date.UTC(y,2,term2,0,0,0,0)); //取得春分的公历日期控件(春分一定出现在3月)
   var lDayTerm2 = new Lunar(dayTerm2); //取得取得春分农历

   if(lDayTerm2.day<15) //取得下个月圆的相差天数
      var lMlen= 15-lDayTerm2.day;
   else
      var lMlen= (lDayTerm2.isLeap? leapDays(y): monthDays(y,lDayTerm2.month)) - lDayTerm2.day + 15;

   //一天等于 1000*60*60*24 = 86400000 毫秒
   var l15 = new Date(dayTerm2.getTime() + 86400000*lMlen ); //求出第一次月圆为公历几日
   var dayEaster = new Date(l15.getTime() + 86400000*( 7-l15.getUTCDay() ) ); //求出下个周日

   this.m = dayEaster.getUTCMonth();
   this.d = dayEaster.getUTCDate();

}

//====================== 中文日期===================================//
function cDay(d){
   var s;

   switch (d) {
      case 10:
         s = '初十'; break;
      case 20:
         s = '二十'; break;
         break;
      case 30:
         s = '三十'; break;
         break;
      default :
         s = nStr2[Math.floor(d/10)];
         s += nStr1[d%10];
   }
   return(s);
}
//===============================rili365.js========================//
var colors = ['#CC3333', '#DD4477', '#994499', '#6633CC', '#336699', '#3366CC',
		'#22AA99', '#329262', '#109618', '#66AA00', '#AAAA11', '#D6AE00',
		'#EE8800', '#DD5511', '#A87070', '#8C6D8C', '#627487', '#7083A8',
		'#5C8D87', '#898951', '#B08B59'];
var options = {
	maxDetailHeight : 350, // 弹出层显示的最大高度
	maxDetailItems : 20, // 弹出层默认显示的最大条目数
	firstDayOfWeek : 1, // 每周的第一天，默认周一
	schedulePageSize : 20
	// 日程列表每页显示条目数
};
var global = {
	currYear : -1, // 当前年
	currMonth : -1, // 当前月，0-11
	currDate : null, // 当前点选的日期
	uid : null,
	username : null,
	email : null,
	single : false
	// 是否为独立页调用，如果是值为日历id，使用时请注意对0的判断，使用 single !== false 或者 single === false
};
// 当前点击的日期
var day_num;
var monthView;
function MonthViewObj(year, month) {
	this.cld = null; // calendar的实例
	this.year = year;
	this.month = month;
	this.firstline = 0; // 当前月第一行能显示的日期数
	this.linecount = 0; // 当前月总共需要的日期行数
}
var dateSelection = {
	currYear : -1,
	currMonth : -1,

	minYear : 1901,
	maxYear : 2100,

	beginYear : 0,
	endYear : 0,

	tmpYear : -1,
	tmpMonth : -1,

	init : function(year, month) {
		if (typeof year == 'undefined' || typeof month == 'undefined') {
			year = global.currYear;
			month = global.currMonth;
		}
		
		this.setYear(year);
		this.setMonth(month);
		this.showYearContent();
		this.showMonthContent();
	},
	show : function() {
		$.dom('dateSelectionDiv').style.display = 'block';
	},
	hide : function() {
		this.rollback();
		$.dom('dateSelectionDiv').style.display = 'none';
	},
	today : function() {
		var today = new Date();
		var year = today.getFullYear();
		var month = today.getMonth();

		if (this.currYear != year || this.currMonth != month) {
			if (this.tmpYear == year && this.tmpMonth == month) {
				this.rollback();
			} else {
				this.init(year, month);
				this.commit();
			}
		}
	},
	go : function() {
		if (this.currYear == this.tmpYear && this.currMonth == this.tmpMonth) {
			this.rollback();
		} else {
			this.commit();
		}
		this.hide();
	},
	goToday : function() {
		this.today();
		this.hide();
	},
	goPrevMonth : function() {
		this.prevMonth();
		this.commit();
	},
	goNextMonth : function() {
		this.nextMonth();
		this.commit();
	},
	goPrevYear : function() {
		this.prevYear();
		this.commit();
	},
	goNextYear : function() {
		this.nextYear();
		this.commit();
	},
	changeView : function() {
		global.currYear = this.currYear;
		global.currMonth = this.currMonth;

		loadPage(global.currYear, global.currMonth);
	},
	commit : function() {
		if (this.tmpYear != -1 || this.tmpMonth != -1) {
			// 如果发生了变化
			if (this.currYear != this.tmpYear
					|| this.currMonth != this.tmpMonth) {
				// 执行某操作
				this.showYearContent();
				this.showMonthContent();
				this.changeView();
				/*
				if(global.currYear>2007&&global.currYear<2012) //杨
					if (eval("HuangLi.y" + global.currYear) == null) {
						var filename="js/huangli/"+global.currYear+".js";
						loadjscssfile(filename,"js");
					}
				*/
			}
			//add holiday
			thisYear = this.currYear;
			thisMonth = this.currMonth
			$.post("getHolidayOneMonth.action", {holidayType:holidayType,thisMonth:(this.currMonth + 1),thisYear:this.currYear}, function (data) {
				if(data != null && data != ''){
 					var dateMap = cacheMgr.getMonthCache(global.currYear, global.currMonth).dateMap;
					for (var dateNum in dateMap) {
						var dateCell = dateMap[dateNum];
						date = thisYear+ '-' + fillZeroBellowTwo(thisMonth + 1) + '-' + fillZeroBellowTwo(dateCell.date);
						var cell = $.dom('rg_cell_h' + dateCell.date);
						if(data.indexOf(date)>-1) {
 						    if(data.indexOf(date+'-add,')>-1){
						        initBackGroundAdd(cell.parentNode);
						    }else if(data.indexOf(date+'-remove,')>-1){
						        initBackGroundRemove(cell.parentNode);
						    }else if(data.indexOf(date+',')>-1){
								initBackGround(cell.parentNode);
						    }
						}
					}
				}
			});
			
			this.tmpYear = -1;
			this.tmpMonth = -1;
		}
	},
	rollback : function() {
		if (this.tmpYear != -1) {
			this.setYear(this.tmpYear);
		}
		if (this.tmpMonth != -1) {
			this.setMonth(this.tmpMonth);
		}
		this.tmpYear = -1;
		this.tmpMonth = -1;
		this.showYearContent();
		this.showMonthContent();
	},
	prevMonth : function() {
		var month = this.currMonth - 1;
		if (month == -1) {
			var year = this.currYear - 1;
			if (year >= this.minYear) {
				month = 11;
				this.setYear(year);
			} else {
				month = 0;
			}
		}
		this.setMonth(month);
	},
	nextMonth : function() {
		var month = this.currMonth + 1;
		if (month == 12) {
			var year = this.currYear + 1;
			if (year <= this.maxYear) {
				month = 0;
				this.setYear(year);
			} else {
				month = 11;
			}
		}
		this.setMonth(month);
	},
	prevYear : function() {
		var year = this.currYear - 1;
		if (year >= this.minYear) {
			this.setYear(year);
		}
	},
	nextYear : function() {
		var year = this.currYear + 1;
		if (year <= this.maxYear) {
			this.setYear(year);
		}
	},
	prevYearPage : function() {
		this.endYear = this.beginYear - 1;
		this.showYearContent(null, this.endYear);
	},
	nextYearPage : function() {
		this.beginYear = this.endYear + 1;
		this.showYearContent(this.beginYear, null);
	},
	selectYear : function(){//杨：select
		var selectY = $('select[@name="SY"] option[@selected]').text();
		this.setYear(selectY);
		this.commit();	
	},
	selectMonth : function(){
		var selectM = $('select[@name="SM"] option[@selected]').text();
		this.setMonth(selectM-1);
		this.commit();	
	},
	setYear : function(value) {
		if (this.tmpYear == -1 && this.currYear != -1) {
			this.tmpYear = this.currYear;
		}
		$('#SY' + this.currYear).removeClass('curr');
		this.currYear = value;
		$('#SY' + this.currYear).addClass('curr');
	},
	setMonth : function(value) {
		if (this.tmpMonth == -1 && this.currMonth != -1) {
			this.tmpMonth = this.currMonth;
		}
		$('#SM' + this.currMonth).removeClass('curr');
		this.currMonth = value;
		$('#SM' + this.currMonth).addClass('curr');
	},
	showYearContent : function(beginYear, endYear) {
		if (!beginYear) {
			if (!endYear) {
				endYear = this.currYear + 1;
			}
			this.endYear = endYear;
			if (this.endYear > this.maxYear) {
				this.endYear = this.maxYear;
			}
			this.beginYear = this.endYear - 3;
			if (this.beginYear < this.minYear) {
				this.beginYear = this.minYear;
				this.endYear = this.beginYear + 3;
			}
		}
		if (!endYear) {
			if (!beginYear) {
				beginYear = this.currYear - 2;
			}
			this.beginYear = beginYear;
			if (this.beginYear < this.minYear) {
				this.beginYear = this.minYear;
			}
			this.endYear = this.beginYear + 3;
			if (this.endYear > this.maxYear) {
				this.endYear = this.maxYear;
				this.beginYear = this.endYear - 3;
			}
		}

		var s = '';
		for (var i = this.beginYear; i <= this.endYear; i++) {
			s += '<span id="SY' + i
					+ '" class="year" onclick="dateSelection.setYear(' + i
					+ ')">' + i + '</span>';
		}
		$.dom('yearListContent').innerHTML = s;
		$('#SY' + this.currYear).addClass('curr');
	},
	showMonthContent : function() {
		var s = '';
		for (var i = 0; i < 12; i++) {
			s += '<span id="SM' + i
					+ '" class="month" onclick="dateSelection.setMonth(' + i
					+ ')">' + (i + 1).toString() + '</span>';
		}
		$.dom('monthListContent').innerHTML = s;
		$('#SM' + this.currMonth).addClass('curr');
	}
};
var utils = {
	numToWeek : function(num) {
		switch (num) {
			case 1 :
				return '一';
			case 2 :
				return '二';
			case 3 :
				return '三';
			case 4 :
				return '四';
			case 5 :
				return '五';
			case 6 :
				return '六';
			case 0 :
				return '日';
		}
	},
	getEvent : function(ev) {
		return window.event ? window.event : (ev ? ev : null);
	},
	getMousePosition : function(ev) {
		var evt = this.getEvent(ev);
		if (evt.pageX || evt.pageY) {
			return {
				x : evt.pageX,
				y : evt.pageY
			};
		}
		return {
			x : evt.clientX + document.documentElement.scrollLeft
					- document.documentElement.clientLeft,
			y : evt.clientY + document.documentElement.scrollTop
					- document.documentElement.clientTop
		};
	},
	getClientWidth : function() {
		return $.browser.msie ? ieBody.clientWidth : window.innerWidth;
	},
	getClientHeight : function() {
		return $.browser.msie ? ieBody.clientHeight : window.innerHeight;
	},
	getOffsetXY : function(obj, parentId) {
		/*
		 * 
		 * getOffsetXY 获取相对坐标
		 * 
		 * @param obj id或者dom对象 @param parentId 父级id，如果不提供则为body
		 * 
		 * @return 坐标对象，x、y
		 * 
		 */
		var element;
		if (typeof obj == 'object') {
			element = obj;
		} else {
			element = document.getElementById(obj);
		}
		var element_X = element.offsetLeft;
		var element_Y = element.offsetTop;
		while (true) {
			if ((!element.offsetParent) || (!element.offsetParent.style)
					|| (!!parentId && element.offsetParent.id == parentId)) {
				break;
			}
			element_X += element.offsetParent.offsetLeft;
			element_Y += element.offsetParent.offsetTop;
			element = element.offsetParent;
		}
		element_X = element_X - document.body.scrollLeft;
		element_Y = element_Y - document.body.scrollTop;

		return {
			x : element_X,
			y : element_Y
		};
	},
	getChinaNum : function(num){
		var cNum = "";
		switch(num){
			case 1 : cNum = "一" ; break;
			case 2 : cNum = "二" ; break;
			case 3 : cNum = "三" ; break;
			case 4 : cNum = "四" ; break;
			case 5 : cNum = "五" ; break;
			case 6 : cNum = "六" ; break;
			case 7 : cNum = "七" ; break;
			case 8 : cNum = "八" ; break;
			case 9 : cNum = "九" ; break;
			case 10 : cNum = "十" ; break;
			case 11 : cNum = "十一" ; break;
			case 12 : cNum = "腊" ; break;
			
		}
		return cNum;
	},
	getMonthKey : function(year, month) { // 传入的month为0-11的数值
		return year.toString() + (month + 1).toString().leftpad(2) // 返回yyyyMM格式的字符串
	}
};

var tplMgr = {
	tplMap : {},
	getInstance : function(id) {
		var instance = this.tplMap[id];
		if (!instance) {
			instance = new jCT($.dom(id).value);
			instance.Build();
			this.tplMap[id] = instance;
		}
		return instance;
	}
};
var cacheMgr = {
	permissions : {}, // 保存日历的权限，以cid为key，value=permission
	cldCache : {}, // 注意！这里存的是calendarObj.js中定义的calendar对象，不是数据文件载入的cldObj
	getCld : function(year, month) {
		var key = utils.getMonthKey(year, month);
		var cld = this.cldCache[key];
		if (typeof cld == 'undefined') {
			cld = new calendar(year, month);
			this.cldCache[key] = cld;
		}
		return cld;
	},
	monthViewCache : {},
	getMonthView : function(year, month) {
		var key = utils.getMonthKey(year, month);
		var monthView = this.monthViewCache[key];
		if (typeof monthView == 'undefined') {
			var cld = this.getCld(year, month);
			var firstline = 7 - cld.firstWeek + options.firstDayOfWeek; // 计算第一行能显示的日期数
			firstline = firstline > 7 ? firstline - 7 : firstline;
			var linecount = Math.ceil((cld.length - firstline) / 7) + 1; // 计算总共需要的日期行数
			monthView = new MonthViewObj(year, month);
			monthView.cld = cld;
			monthView.firstline = firstline;
			monthView.linecount = linecount;
			this.monthViewCache[key] = monthView;
		}

		return monthView;
	},
	monthMap : {},
	getMonthCache : function(year, month) {// yang----
		var cache = this.monthMap[utils.getMonthKey(year, month)];
		if (typeof cache == 'undefined') {
			var view = this.getMonthView(year, month);

			var cld = view.cld;
			var firstline = view.firstline;
			var linecount = view.linecount;
			cache = new MonthCache(year, month);
			for (var i = 0; i < linecount; i++) {
				for (var j = 0; j < 7; j++) {
					var index = i * 7 + j;
					var dayInMonth = true;
					if ((index < (7 - firstline))
							|| (index >= (cld.length - firstline + 7))) {
						dayInMonth = false;
					}

					var dateNum = 0; // 本月的几号
					if (dayInMonth) {
						dateNum = index - 7 + firstline + 1;

						var cell = new DateCell(dateNum, index);
						cache.dateMap[dateNum] = cell;
					}
				}
			}
			this.monthMap[utils.getMonthKey(year, month)] = cache;
		}
		return cache;
	}
};
var tabMgr = {
	switchTab : function(tabId, selectedId) {
		$('#' + tabId + ' li.selected').each(function() {
					$(this).removeClass('selected');
					$('#con_' + $(this).attr('id')).hide();
				});
		$('#' + selectedId).addClass('selected');
		$('#con_' + selectedId).show();
	}
}

function changeBackGround(o){
	o.style.backgroundColor = '';
}
function initBackGroundAdd(o){
	o.style.backgroundColor = FIX_COLORADD;
	//o.style.color = 'red';
}
function initBackGroundRemove(o){
	o.style.backgroundColor = FIX_COLORREMORE;
	//o.style.color = 'red';
}
function initBackGround(o){
	o.style.backgroundColor = FIX_COLOR;
	//o.style.color = 'red';
}
function isBackGroundChanged(o){
	return o.style.backgroundColor == FIX_COLOR;
}
function isBackGroundChangedAdd(o){
	return o.style.backgroundColor == FIX_COLORADD;
}
function isBackGroundChangedRemove(o){
	return o.style.backgroundColor == FIX_COLORREMORE;
}
function DateCell(date, index) {
	this.date = date;
	this.index = index;
	this.scheduleMap = {}; // 当天的日程
	this.cldIds = {}; // 在当天有日程的日历ID，用于绘制小图标

	this.displayed = 0;
	this.count = 0;

	this.iconHTML = '';
	this.eventHTML = '';

	this.getClickAreaHTML = function() {
		// 计算日期格子顶点坐标
		var zero = utils.getOffsetXY('rg_rowy_h' + this.index, 'decowner');

		var el_rg_rowy = $.dom('rg_rowy_h' + this.index);
		var elCell = el_rg_rowy.parentNode;

		// 日期格子的大小
		var w = elCell.offsetWidth;
		var h = elCell.offsetHeight;

		// 创建可点击区域
		var instance = tplMgr.getInstance('detail_click_tpl');
		return instance.GetView({
					index : this.index,
					date : this.date,
					left : zero.x,
					top : zero.y -(elCell.offsetHeight - el_rg_rowy.offsetHeight),
					width : elCell.offsetWidth,
					height : elCell.offsetHeight
				});
	}
}

function MonthCache(year, month) {
	this.year = year;
	this.month = month;
	this.dateMap = {};
}
function loadPage(year, month) {
	// 加载主题
	var theme = new Theme();
	singleMgr.appendTheme(theme);
	// alert("loadPage");
	monthView = cacheMgr.getMonthView(year, month);

	$.dom('yearValue').innerHTML = year.toString();
	$.dom('monthValue').innerHTML = (month + 1).toString().leftpad(2);

	$.dom('lunarValue').innerHTML = '农历 '
			+ cyclical(global.currYear - 1900 + 36) + '年 【'
			+ Animals[(global.currYear - 4) % 12] + '年】';

	pageMgrRili.drawCalendar();
	pageMgrRili.adjustGridContainer();
}

function Theme() {
	this.bgColor = '#6b92d7';
	this.bgColor2 = '#ecf1ff';
	this.bgColor1 = '#c2cff1';//'#d9e1f4';
	this.bgColor3 = '#ffffff';
}

var indexMgr = {
	dateover : function(index,dataNum,event) {
		var dateTitle = $.dom('rg_rowy_h' + index);
		//var dateClick = $.dom('click_Num'+index);
		var dateCell = dateTitle.parentNode;
		if(dateCell.style.backgroundColor != FIX_COLOR){
			//dateTitle.style.backgroundColor = '#FBFFBC'; 
			//dateCell.style.backgroundColor = '#FBFFBC';
		}
		//dateClick.style.backgroundColor = '#FBFFBC';  //给图片添加hover效果
		//showDetailsRili(event,dataNum);
	},
	dateout : function(index) {
		var dateTitle = $.dom('rg_rowy_h' + index);
		//var dateClick = $.dom('click_Num'+index);
		var dateCell = dateTitle.parentNode;
		if(dateCell.style.backgroundColor != FIX_COLOR){
			//dateTitle.style.backgroundColor = '';
			//dateCell.style.backgroundColor = '';
		}
		//dateClick.style.backgroundColor = '';
		//dialogMgrRili.hide();
	},
	addHoilday: function(index){
		// add holidays
		var dateCell = $.dom('rg_cell_h' + index);
		var removeType = "remove";
		var thisDate = global.currYear+'-'+fillZeroBellowTwo(global.currMonth+1) + "-" + fillZeroBellowTwo(dateCell.innerText);
		if(isBackGroundChanged(dateCell.parentNode)||isBackGroundChangedAdd(dateCell.parentNode)||isBackGroundChangedRemove(dateCell.parentNode)){
		    if(isBackGroundChangedAdd(dateCell.parentNode)){
		        removeType = "removeAdd" ;
		    }else if(isBackGroundChangedRemove(dateCell.parentNode)){
		        removeType = "removeRemove" ;
		    }
			$.post("setThis2Holiday.action", {holidayType:holidayType,removeType:removeType,thisDate:thisDate,method:"remove"}, function (data) {
				if(data == 'ok'){
					changeBackGround(dateCell.parentNode);
				}else if(data == 'okAdd'){
				    initBackGroundAdd(dateCell.parentNode);
				}else if(data == 'okRemove'){
				    initBackGroundRemove(dateCell.parentNode);
				}else if(data=='removeRemove'){
				    initBackGround(dateCell.parentNode);
				}else if(data=='removeAdd'){
					changeBackGround(dateCell.parentNode);
				}else{
					alert(data);
				}
			});
		}else{
			$.post("setThis2Holiday.action", {holidayType:holidayType,thisDate:thisDate,method:"add"}, function (data) {
				if(data == 'ok'){
					initBackGround(dateCell.parentNode);
				}else if(data == 'okAdd'){
                    initBackGroundAdd(dateCell.parentNode);
                }else if(data == 'okRemove'){
                    initBackGroundRemove(dateCell.parentNode);
                }else{
                    alert(data);
                }
			});
		}
		
	},
	removeHoilday :function(index){
		var dateCell = $.dom('rg_cell_h' + index);
		dateCell.style.color='white';
	
	}
};

var singleMgr = {
	appendTheme : function(theme) {
		$('.themeBgColor').each(function() {
					$(this).css('background-color', theme.bgColor);
				});
		$('.themeBgColor1').each(function() {
					$(this).css('background-color', theme.bgColor1);
				});
		$('.themeBgColorRili').each(function() {
					$(this).css('background-color', theme.bgColor2);
				});
		$('.themeBgColorRili1').each(function() {
					$(this).css('background-color', theme.bgColor3);
				});
	}
};
function gotoWeb(cid, sid) {
	window.open("/link.jsp?cid=" + cid + "&sid=" + sid + "&refer="
			+ escape(window.location));
}
function initRiliIndex() {
	var dateObj = new Date();
	global.currYear = dateObj.getFullYear();
	global.currMonth = dateObj.getMonth();
	loadPage(global.currYear, global.currMonth);
	dateSelection.init();
	hoverDialog();
	initHolidays(global.currYear, global.currMonth);
	jumpToEarlyMonth();
}
function jumpToEarlyMonth(){
	if(SHOW_HOLIDAY_MONTH){
		$.post("getMostEarlyMonth.action", {holidayType:holidayType,thisYear:global.currYear,thisMonth:global.currMonth}, function (data) {
			//earlyMonth = parseInt(data) > 0 ? parseInt(data) - 1 : global.currMonth;
			var array = data.split('-');
			dateSelection.setYear(parseInt(array[0]));
			dateSelection.setMonth(parseInt(array[1]));
			dateSelection.commit();
		});
	}
}
function initHolidays(thisYear, thisMonth){
	$.post("getHolidayOneMonth.action", {holidayType:holidayType,thisMonth:(thisMonth + 1),thisYear:thisYear}, function (data) {
		if(data != null && data != ''){
			var dateMap = cacheMgr.getMonthCache(global.currYear, global.currMonth).dateMap;
			for (var dateNum in dateMap) {
				var dateCell = dateMap[dateNum];
				date = thisYear+ '-' + fillZeroBellowTwo(thisMonth + 1) + '-' + fillZeroBellowTwo(dateCell.date);
				//var cell = $.dom('rg_cell_h' + dateCell.date);
				var el_rg_rowy = $.dom('rg_rowy_h' + dateCell.index);
				var cell = el_rg_rowy.parentNode;
				//if(data.indexOf(date + ',')>-1) {
				if(data.indexOf(date )>-1) {
				    //alert(data);
				    if(data.indexOf(date+'-add,')>-1){
				        initBackGroundAdd(cell);
				    }else if(data.indexOf(date+'-remove,')>-1){
				        initBackGroundRemove(cell);
				    }else if(data.indexOf(date+',')>-1){
						initBackGround(cell);
				    }
				}
			}
		}
	});
}
function fillZeroBellowTwo(a){
	if(a < 10){
		return '0' + a;
	}
	return a;
}
var dateDetailNum;
function showDetailsRili(event,dateNum) {
	var op = {
		width : 328
	};
	var cld = cacheMgr.getCld(global.currYear, global.currMonth);
	var cld_day = cld[dateNum - 1];
	op.title = cld_day.sYear + '年' + cld_day.sMonth + '月' + cld_day.sDay
			+ '日 星期' + cld_day.week;
	var key = 'd'+global.currYear+'-'+(global.currMonth+1)+'-'+dateNum;
	if(cellImage[key]){
		op.desc = cellImage[key].imageDesc;
	}
	else			//杨：节气
	{
		var s;
		s=cld_day.lunarFestival;
      	if(s.length>0) { //农历节日
      		op.desc=s;
      	}
      	else { //廿四节气
        	s=cld_day.solarTerms;
        	if(s.length>0){
        	op.desc=s;	
        }
        else { //公历节日
          s=cld_day.solarFestival;
          if(s.length>0) {
           op.desc=s;
          }
        }
      }
	}
	var detailInfo = {
		year : cld_day.sYear,
		month : cld_day.sMonth,
		dayOfMonth : dateNum
	};
	var evt = utils.getMousePosition(event);
    var	x = evt.x;
	var y = evt.y;
	detailInfo.lunar = '农历' + (cld_day.isLeap ? '闰 ' : '') + cld_day.lMonth
			+ '月' + cDay(cld_day.lDay) + '&nbsp;&nbsp;' + cld_day.cYear + '年 '
			+ cld_day.cMonth + '月 ' + cld_day.cDay + '日';
	detailInfo.dateNum = dateNum;
	detailInfo.dateDetail = op.title;
	try {
		var hl = eval('HuangLi.y'
				+ global.currYear
				+ '.d'
				+ (cld_day.sMonth < 10
						? ('0' + cld_day.sMonth)
						: cld_day.sMonth)
				+ (cld_day.sDay < 10 ? ('0' + cld_day.sDay) : cld_day.sDay));
		detailInfo.huangliY = hl.y;
		detailInfo.huangliJ = hl.j;
		if(indexDataSchedule[key])
		detailInfo.schedule = indexDataSchedule[key];
	} catch (e) {
	}
	var elDetail = $.dom('detail');
	
	elDetail.innerHTML = tplMgr.getInstance('detail_default_tpl').GetView(
			detailInfo);
	dialogMgrRili.show('detailDialog', op,x,y,event);
}
function showIframeRili(d)
{    
    if(typeof(READ_ONLY)!='undefined'&&READ_ONLY)
       return;
    if(document.getElementById("unAudit").value=='true'){
	    alert("此节假日未审核，无法修改。");
	    return;
    }
	//$("#calendar_container").slideDown("slow");
	dateDetailNum = d;
	var cld = cacheMgr.getCld(global.currYear, global.currMonth);
	var cld_day = cld[d - 1];
	var mth='';
	var day='';
	
	mth=cld_day.sMonth<10?'0'+cld_day.sMonth:''+cld_day.sMonth;
	day=d<10?'0'+d:''+d;
	//alert(cld_day.sYear + '年' + mth + '月' + day+ '日');
	indexMgr.addHoilday(d);
	
	return;
}
function showCalendars()
{
	$("#iframeContainer").fadeOut("fast",function(){
		$("#calendar_container").fadeIn("slow");
	});
}
var dialogMgrRili = {
	dialog: null,
	option: null,
	moving: false,
	pos: null,
	
	show: function(el, options,x,y,event){
		var op = {
			width: 328,
			title: '',
			draggable: true,		//默认对话框允许点击标题栏拖动
			desc:''
		};
		var options = options || {};
		for (var p in options) {
			op[p] = options[p];
		}
		
		if (this.dialog) {
			this.hide();
		}
		
		this.option = op;
		
		this.dialog = $.dom(el);
		var width = this.option.width;
		var w = utils.getClientWidth();
		var h = utils.getClientHeight();
		var deatilHeight = 180;//对话框的高度
		
		var left = 0;
		
		if (w > width) {
			left = (w - width) / 2;
		}

		left = (x-w/2)>0?(x-width-8):x;
		//left = (x-width/2>0)?(x-width/2):x;
		this.dialog.style.left = left + 'px';

		var top = (y-h/2>0)?(y-deatilHeight-28):(y+8);
		//var scrollTop = document.body.scrollTop || document.documentElement.scrollTop;
		//var top = y+10;
		this.dialog.style.top = top + 'px';
		
		var shadowHeight = $.dom('page').offsetHeight > utils.getClientHeight() ? $.dom('page').offsetHeight : utils.getClientHeight();
		var d = document;
		var pos={ x:x,y:y};
		d.onmousemove = function(ev){
					try{
					var p = utils.getMousePosition(ev);
					var left1 = parseInt(dialogMgrRili.dialog.style.left);
					var top1 = parseInt(dialogMgrRili.dialog.style.top);
					
					left1 += p.x - pos.x;
					top1 += p.y - pos.y;
					top = top>p.y?top:p.y+1;
					dialogMgrRili.dialog.style.left = left1 + 'px';
					dialogMgrRili.dialog.style.top = top1 + 'px';
					pos = p;
					}
					catch(e){}
				};
		$(this.dialog).find('.title').html(this.option.desc).end().show();
	},
	hide: function(){
		//hideQuickAdd();//kun:ugly代码
		if (this.dialog) {
			$(this.dialog).hide();
			if (this.option.hideCallback) {
				try {
					this.option.hideCallback();
				} 
				catch (ex) {
				}
			}
			this.option = null;
			this.dialog = null;
			this.moving = false;
			this.pos = null;
		}
	}
};
var pageMgrRili = {
	resizing : false,
	adjustGridContainer : function() {
		var elMainWrapper = $.dom('mainWrapper');
		var elMainNav = $.dom('mainNavRili');
		var elColheaders = $.dom('colheadersRili');
		var elGridContainer = $.dom('gridcontainerRili');

		elGridContainer.style.height = 300 + 'px';
		//elGridContainer.style.height = 300+'px';
		var pos = utils.getOffsetXY('dateSelectionRili', 'mainBody');

		var dateSelectionDiv = $.dom('dateSelectionDiv');
		dateSelectionDiv.style.left = pos.x + 'px';

		if (monthView && monthView.ready) {
			pageMgrRili.drawClickArea();
			//showSchedules();
		}
	},
	drawWeekLine : function() { // 绘制周标题
		var width = Math.round(1 / 7 * 1000000) / 10000;
		var buffer = new StringBuffer();
		for (var i = 0; i < 7; i++) { // draw weekline
			var weeknum = (options.firstDayOfWeek + i) % 7;
			var left = Math.round(i / 7 * 1000000) / 10000;
			if(utils.numToWeek(weeknum)=='六'||utils.numToWeek(weeknum)=='日')
			{
				buffer.append('<div style="width: ').append(width)
					.append('%; left: ').append(left)
					.append('%;color:red;" class="chead cheadNotTodayRili"><span id="chead')
					.append(i).append('">星期').append(utils.numToWeek(weeknum))
					.append('</span></div>');
			}
			else{
			buffer.append('<div style="width: ').append(width)
					.append('%; left: ').append(left)
					.append('%;" class="chead cheadNotTodayRili"><span id="chead')
					.append(i).append('">星期').append(utils.numToWeek(weeknum))
					.append('</span></div>');
			}
		}
		$.dom('colheadersRili').innerHTML = buffer.toString();
	},
	drawRowSpliter : function() { // 绘制行分隔线
		var linecount = monthView.linecount;
		var buffer = new StringBuffer();
		for (var i = 0; i < linecount; i++) {
			var top = Math.round(i / linecount * 1000000) / 10000;
			if (i > 0) {
				buffer.append('<div id="r').append(i)
						.append('" class="hrule hruleMonth" style="top: ')
						.append(top).append('%;"></div>');
			}
		}
		$.dom('rowowner').innerHTML = buffer.toString();
	},
	drawColSpliter : function() { // 绘制列间隔线
		var buffer = new StringBuffer();
		for (var i = 1; i < 7; i++) {
			var left = Math.round(i / 7 * 1000000) / 10000;
			buffer
					.append('<div id="c')
					.append(i)
					.append('" class="vrule nogutterRili" style="width: 1px; left: ')
					.append(left)
					.append('%; height: 100%;"></div>');
		}
		$.dom('colowner').innerHTML = buffer.toString();
	},
	drawClickArea : function() {
		var dateMap = cacheMgr.getMonthCache(global.currYear, global.currMonth).dateMap;

		var buffer = new StringBuffer();
		for (var dateNum in dateMap) {
			var dateCell = dateMap[dateNum];
			buffer.append(dateCell.getClickAreaHTML());
		}

		$.dom('clickowner').innerHTML = buffer.toString();
	},
	drawDateGrid : function() { // 绘制日期格子
		monthView.ready = false; // 日历月视图是否准备好

		var year = monthView.year;
		var month = monthView.month;
		var cld = monthView.cld;
		var firstline = monthView.firstline;
		var linecount = monthView.linecount;

		var width = Math.round(1 / 7 * 1000000) / 10000;

		var todayObj = new Date();
		var isThisMonth = todayObj.getFullYear() == year
				&& todayObj.getMonth() == month;
		var today = todayObj.getDate();

		var buffer = new StringBuffer();
		var height = Math.round(1 / linecount * 1000000) / 10000;

		var in_instance = tplMgr.getInstance('calendar_cell_in_tpl');
		var out_instance = tplMgr.getInstance('calendar_cell_out_tpl');
		var today_instance = tplMgr.getInstance('calendar_cell_today_tpl');
		for (var i = 0; i < linecount; i++) {
			var top = Math.round(i / linecount * 1000000) / 10000;
			for (var j = 0; j < 7; j++) {
				var index = i * 7 + j;
				var currW = '14.2857';
				var dayInMonth = true;
				if ((index < (7 - firstline))
						|| (index >= (cld.length - firstline + 7))) {
					dayInMonth = false;
				}

				var dateNum = 0; // 本月的几号
				var lunar = '';
				var lunarColor = '';
				if (dayInMonth) {
					dateNum = index - 7 + firstline + 1;
					var cld_day = cld[dateNum - 1];
					var s;
//					lunar = cld_day.solarTerms;
//					//alert(lunar);
//					if (lunar.length > 0) {
//						lunarColor = 'red';
//					} else {
//						lunar = cld_day.lDay == 1
//								? (cld_day.isLeap ? '闰' : '')
//										+ cld_day.lMonth
//										+ '月'
//										+ (monthDays(cld_day.lYear,
//												cld_day.lMonth) == 29
//												? '小'
//												: '大')
//								: cDay(cld_day.lDay);
//					}
	 if(cld_day.lDay==1) //显示农历月
        lunar = (cld_day.isLeap?'闰':'') + cld_day.lMonth + '月' + (monthDays(cld_day.lYear,cld_day.lMonth)==29?'小':'大');
      else //显示农历日
        lunar = cDay(cld_day.lDay);

      s=cld_day.lunarFestival;
      if(s.length>0) { //农历节日
        if(s.length>6) s = s.substr(0, 4)+'...';
        lunarColor = "#32CD32";
      }
      else { //廿四节气
        s=cld_day.solarTerms;
        if(s.length>0){
          lunarColor = "#32CD32";             
          if((s =='清明')||(s =='芒种')||(s =='夏至')||(s =='冬至')){
            lunarColor = "#32CD32";
            if(s =='清明') s = '清明节';
          }             
        }
        else { //公历节日
          s=cld_day.solarFestival;
          if(s.length>0) {
            if(s.length>6) s = s.substr(0, 4)+'...';
            lunarColor = "#46BAEC";
          }
        }
      }
      if(s.length>0) lunar = s;
				}

				var left = Math.round(j / 7 * 1000000) / 10000;

				var isToday = isThisMonth
						&& (today == index - 7 + firstline + 1);
				
				var tpl_data = {
					index : index,
					dateNum : dateNum,
					isToday : isToday,
					lunar : lunar,
					lunarColor : lunarColor,
					left : left,
					top : top,
					width : currW,
					height : height
				}
				if (isToday) {
					buffer.append(today_instance.GetView(tpl_data));
				}
				if (dayInMonth) {
					buffer.append(in_instance.GetView(tpl_data));
				} else {
					buffer.append(out_instance.GetView(tpl_data));
				}
			}
		}

		$.dom('decowner').innerHTML = buffer.toString();
		monthView.ready = true; // 月视图准备完成
	},
		addHoverBtn:function(){
		var config = {
							sensitivity : 3, // number = sensitivity
							interval : 10, // number = milliseconds for
							// onMouseOver polling interval
							over : this.overBtn, // function
							timeout : 10, // number = milliseconds delay
							// before onMouseOut
							out : this.outBtn
							// function = onMouseOut callback (REQUIRED)
						};
						
			$('#mainNav .dateNavBtnWrapperRili')
								.hoverIntent(config);
	},
	overBtn:function(){
		$(this).find('.t2').removeClass('themeBgColorRili1').css('background-color', '#6b92d7');
		$(this).find('.getBtn').removeClass('themeBgColorRili1').removeClass('dateNavBtnRili').addClass('dateNavBtnRili1');
		$(this).find('.getBtn').css('background-color','#6b92d7');

	},
	outBtn:function(){
		$(this).find('.t2').removeClass('themeBgColor').css('background-color', '#ffffff');
		$(this).find('.getBtn').removeClass('themeBgColor').removeClass('dateNavBtnRili1').addClass('dateNavBtnRili');
		$(this).find('.getBtn').css('background-color','#ffffff');
	},
	drawCalendar : function() { // 绘制整个日历
		this.drawWeekLine();
		this.drawColSpliter();
		this.drawRowSpliter();
		this.drawDateGrid();
		this.addHoverBtn();
	}
};
function hoverDialog()
{
		$(".title_class").hover(
		 function () {
    	$(this).css("color","red");
  		},
  		function () {
    		$(this).css("color","#000000");
  		}
	);
	$(".leftImg").hover(
		function(){
			$("#play_prev").attr("src","../img/leftImg2.gif");
		},
		function(){
			$("#play_prev").attr("src","../img/leftImg1.gif");
		}
	);
	$(".rightImg").hover(
		function(){
			$("#play_next").attr("src","../img/rightImg1.gif");
		},
		function(){
			$("#play_next").attr("src","../img/rightImg2.gif");
		}
	);
}
//======================================jquery.scrollTo-min.js============//
/**
 * jQuery.ScrollTo - Easy element scrolling using jQuery.
 * Copyright (c) 2007-2008 Ariel Flesler - aflesler(at)gmail(dot)com | http://flesler.blogspot.com
 * Dual licensed under MIT and GPL.
 * Date: 2/19/2008
 * @author Ariel Flesler
 * @version 1.3.3
 *
 * http://flesler.blogspot.com/2007/10/jqueryscrollto.html
 */
;(function($){var o=$.scrollTo=function(a,b,c){o.window().scrollTo(a,b,c)};o.defaults={axis:'y',duration:1};o.window=function(){return $($.browser.safari?'body':'html')};$.fn.scrollTo=function(l,m,n){if(typeof m=='object'){n=m;m=0}n=$.extend({},o.defaults,n);m=m||n.speed||n.duration;n.queue=n.queue&&n.axis.length>1;if(n.queue)m/=2;n.offset=j(n.offset);n.over=j(n.over);return this.each(function(){var a=this,b=$(a),t=l,c,d={},w=b.is('html,body');switch(typeof t){case'number':case'string':if(/^([+-]=)?\d+(px)?$/.test(t)){t=j(t);break}t=$(t,this);case'object':if(t.is||t.style)c=(t=$(t)).offset()}$.each(n.axis.split(''),function(i,f){var P=f=='x'?'Left':'Top',p=P.toLowerCase(),k='scroll'+P,e=a[k],D=f=='x'?'Width':'Height';if(c){d[k]=c[p]+(w?0:e-b.offset()[p]);if(n.margin){d[k]-=parseInt(t.css('margin'+P))||0;d[k]-=parseInt(t.css('border'+P+'Width'))||0}d[k]+=n.offset[p]||0;if(n.over[p])d[k]+=t[D.toLowerCase()]()*n.over[p]}else d[k]=t[p];if(/^\d+$/.test(d[k]))d[k]=d[k]<=0?0:Math.min(d[k],h(D));if(!i&&n.queue){if(e!=d[k])g(n.onAfterFirst);delete d[k]}});g(n.onAfter);function g(a){b.animate(d,m,n.easing,a&&function(){a.call(this,l)})};function h(D){var b=w?$.browser.opera?document.body:document.documentElement:a;return b['scroll'+D]-b['client'+D]}})};function j(a){return typeof a=='object'?a:{top:a,left:a}}})(jQuery);
//==========================================jquery.serialScroll-min.js=================//
/**
 * jQuery[a] - Animated scrolling of series
 * Copyright (c) 2007-2008 Ariel Flesler - aflesler(at)gmail(dot)com | http://flesler.blogspot.com
 * Dual licensed under MIT and GPL.
 * Date: 3/20/2008
 * @author Ariel Flesler
 * @version 1.2.1
 *
 * http://flesler.blogspot.com/2008/02/jqueryserialscroll.html
 */
;(function($){var a='serialScroll',b='.'+a,c='bind',C=$[a]=function(b){$.scrollTo.window()[a](b)};C.defaults={duration:1e3,axis:'x',event:'click',start:0,step:1,lock:1,cycle:1,constant:1};$.fn[a]=function(y){y=$.extend({},C.defaults,y);var z=y.event,A=y.step,B=y.lazy;return this.each(function(){var j=y.target?this:document,k=$(y.target||this,j),l=k[0],m=y.items,o=y.start,p=y.interval,q=y.navigation,r;if(!B)m=w();if(y.force)t({},o);$(y.prev||[],j)[c](z,-A,s);$(y.next||[],j)[c](z,A,s);if(!l.ssbound)k[c]('prev'+b,-A,s)[c]('next'+b,A,s)[c]('goto'+b,t);if(p)k[c]('start'+b,function(e){if(!p){v();p=1;u()}})[c]('stop'+b,function(){v();p=0});k[c]('notify'+b,function(e,a){var i=x(a);if(i>-1)o=i});l.ssbound=1;if(y.jump)(B?k:w())[c](z,function(e){t(e,x(e.target))});if(q)q=$(q,j)[c](z,function(e){e.data=Math.round(w().length/q.length)*q.index(this);t(e,this)});function s(e){e.data+=o;t(e,this)};function t(e,a){if(!isNaN(a)){e.data=a;a=l}var c=e.data,n,d=e.type,f=y.exclude?w().slice(0,-y.exclude):w(),g=f.length,h=f[c],i=y.duration;if(d)e.preventDefault();if(p){v();r=setTimeout(u,y.interval)}if(!h){n=c<0?0:n=g-1;if(o!=n)c=n;else if(!y.cycle)return;else c=g-n-1;h=f[c]}if(!h||d&&o==c||y.lock&&k.is(':animated')||d&&y.onBefore&&y.onBefore.call(a,e,h,k,w(),c)===!1)return;if(y.stop)k.queue('fx',[]).stop();if(y.constant)i=Math.abs(i/A*(o-c));k.scrollTo(h,i,y).trigger('notify'+b,[c])};function u(){k.trigger('next'+b)};function v(){clearTimeout(r)};function w(){return $(m,l)};function x(a){if(!isNaN(a))return a;var b=w(),i;while((i=b.index(a))==-1&&a!=l)a=a.parentNode;return i}})}})(jQuery);

//==================rili_index.js============================//
jQuery(function( $ ){
	$('#downLoad').serialScroll({
		target:'#imgDiv',
		items:'li', // Selector to the items ( relative to the matched elements, '#sections' in this case )
		prev:'#play_prev',// Selector to the 'prev' button (absolute!, meaning it's relative to the document)
		next:'#play_next',// Selector to the 'next' button (absolute too)
		axis:'xy',// The default is 'y' scroll on both ways
		duration:700,// Length of the animation (if you scroll 2 axes and use queue, then each axis take half this time)
		force:true, // Force a scroll to the element specified by 'start' (some browsers don't reset on refreshes)
		onBefore:function( e, elem, $pane, $items, pos ){
			e.preventDefault();
			if( this.blur )
				this.blur();
		},
		onAfter:function( elem ){
			//'this' is the element being scrolled ($pane) not jqueryfied
		}
	})
});
function createXmlhttp(){
  var xmlhttp=null;
  if(window.XMLHttpRequest){
	  xmlhttp = new XMLHttpRequest();
	  if (xmlhttp.overrideMimeType){
	    xmlhttp.overrideMimeType("text/xml");
	  }
  }
  else if(window.ActiveXObject){
	  try{
	    xmlhttp = new  ActiveXObject("Msxml2.XMLHTTP");
	  }catch(e){
	    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	  }
  }
  if(!xmlhttp){
    window.alert("Your broswer not support XMLHttpRequest!");
  }
  return xmlhttp;
}
//同步获取url内容
function getUrlContent(surl){
  //window.alert("get:"+surl);
	var xmlhttp = createXmlhttp();
	var rsContent = null;
	xmlhttp.open("GET", surl, false); 
	xmlhttp.setRequestHeader("If-Modified-Since","Last-Modified");
	xmlhttp.send(null);
  if(xmlhttp.readyState == 4){
    if(xmlhttp.status == 200 || xmlhttp.status == 304){
      rsContent = xmlhttp.responseText;  
      return rsContent;
    }
    else{
      return null;
    }
  }
}
//动态load js文件
function loadjscssfile(filename, filetype){
	if (filetype == "js") { //判断文件类型 
		var fileref = document.createElement('script');//创建标签 
		fileref.setAttribute("language", "javascript");
		fileref.setAttribute("type", "text/javascript");//定义属性type的值为text/javascript 
		//fileref.setAttribute("src", filename);//文件的地址 
		fileref.text = getUrlContent(filename);
	}
	else 
		if (filetype == "css") { //判断文件类型 
			var fileref = document.createElement("link");
			fileref.setAttribute("rel", "stylesheet");
			fileref.setAttribute("type", "text/css");
			fileref.setAttribute("href", filename);
		}
	if (typeof fileref != "undefined") 
		document.getElementsByTagName("head")[0].appendChild(fileref);
}