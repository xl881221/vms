

    // ====== 封装的AJAX库 ======

    // 定义名称空间，简单的可以理解为package
    var ajax = new Object();
    // 定义Ajax的状态
    ajax.READY_STATE_UNINITIALIZED = 0;
    ajax.READY_STATE_LOADING = 1;
    ajax.READY_STATE_LOAD = 2;
    ajax.READY_STATE_INTERACTIVE = 3;
    ajax.READY_STATE_COMPLATE = 4;
    ajax.HTTP_SUCCESS_CODE = 200;

    // ContentLoader工具类的构造方法
    /*
    * httpMethod    以get或者post方式
    * url            服务器端的出来URL
    * params        请求参数
    * callback        回调方法
    */
    ajax.ContentLoader = function(httpMethod, url, params, callback) {
        this.xmlhttp = null;
        this.sendRequest(httpMethod === 'undifined'? "post" : httpMethod , url, callback, params);
    }

    // 使用JavaScript中原型的机制定义对象结构
    ajax.ContentLoader.prototype = {
        // 核心的处理方法
        sendRequest : function(httpMethod, url, callback, params) {
            // 创建XMLHttpRequest对象
            if(window.XMLHttpRequest) {
                this.req = new XMLHttpRequest();
            } else if(window.ActiveXObject) {
                var activeNames = ["Microsoft.XMLHTTP", "MSXML2.XMLHTTP"];
                for(var i = 0; i < activeNames.length; i++) {
                    try {
                        this.req = new ActiveXObject(activeNames[i]);
                        break;
                    }catch(err) {
                        this.onerror.call(this);
                    }
                }
            }
            // 若XMLHttpRequest对象创建成功
            if(this.req) {
                var loader = this;
                try {
                    // 注册回调方法
                    this.req.onreadystatechange = function() {
                        callback.call(loader);
                    }
                    // 与服务器端建立连接
                    this.req.open(httpMethod,url,true);
                   // 对于不同的请求方法提供不同的处理方式
                    if(httpMethod == "post") {
                        this.req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
                        this.req.send(params);
                    } else {
                        this.req.send(null);
                    }
                }catch(err) {
                    // 若发生异常，则调用自己的错误处理机制
                    this.onerror.call(this);
                }
            }
        },
        // 自己定义的错误处理方式，自己可扩展
        onerror : function() {
            alert("error fetching data!" +
                "\n\nreadyState: " + this.req.readyState +
                "\nstatus: " + this.req.status +
                "\nheaders: " + this.req.getAllResponseHeaders());
        }
    }

    //====== 封装的AJAX库 ======
function sendJsonRequest(method,url,params) {
	var loader = new ajax.ContentLoader(method,url,params,function() {
	  var req = this.req;
	  if(req.readyState == ajax.READY_STATE_COMPLATE) {
	   if(req.status == ajax.HTTP_SUCCESS_CODE) {
	    alert(req.responseText);
	   }
	  }
	});
}