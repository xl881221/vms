
function CloseWindow(needReload){ 
		if(typeof(needReload) == 'undefined'){
			needReload 	= false;
		}		
		window.returnValue = needReload;
		
	     var ua = navigator.userAgent; 
	     var ie = navigator.appName=="Microsoft Internet Explorer" ? true : false; 
	     if(ie){
			 var IEversion = parseFloat(ua.substring(ua.indexOf("MSIE ")+5, ua.indexOf(";",ua.indexOf("MSIE ")))); 
			 if( IEversion< 5.5){
					 var str = '';
					 document.body.insertAdjacentHTML("beforeEnd", str);
					 document.all.noTipClose.Click(); 
			    } else {
			     window.opener =null; window.close();
			    }
		   }else{ 
		   		window.close() 
		   }
}

function OpenModalWindow(newURL,width,height,needReload) {
	try {
		var retData = false;

		if(typeof(width) == 'undefined'){
			width 	= screen.width * 0.9;
		}
		if(typeof(height) == 'undefined'){
			height 	= screen.height * 0.9;
		}
		if(typeof(needReload) == 'undefined'){
			needReload 	= false;
		}

		retData = showModalDialog(newURL, 
				  window, 
				  "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;");

		if(needReload && retData){
		    var url=window.location.href;
		    var reg="";
			if(url.indexOf("&RESULT_MESSAGE=")!=-1){
			   reg="&RESULT_MESSAGE=";
			}else if(url.indexOf("?RESULT_MESSAGE=")!=-1){
			   reg="?RESULT_MESSAGE=";
			}
			if(reg!=""){
				 var url1=url.substr(0,url.indexOf(reg));
				 var url2=url.substr(url.indexOf(reg)+reg.length);
				 if(url2.indexOf("&")!=-1)
					  url2=url2.substr(url2.indexOf("&"));
				 else
				    url2="";
				 url=url1+url2;
			}
			window.location.href = url;
		}
		return retData;
	} catch (err) {
	}
}

function OpenWindow(newURL,left,top,width,height) {

		var recdata = false;
		if(typeof(left) == 'undefined'){
			top 	= screen.height * 0.05;
		}
		if(typeof(top) == 'undefined'){
			top = screen.height * 0.05;
		}
		if(typeof(width) == 'undefined'){
			width 	= screen.width * 0.9;
		}
		if(typeof(height) == 'undefined'){
			height 	= screen.height * 0.9;
		}
		
		ContentWindow=window.open(newURL,
				  "_blank",
				  "width="+ width + ",height="+height+"," 
				+ "left="+ left + ",top="+top+"," 
				+ "directories=no"
				+ "scrollbars=no, resizable=no,status=no,toolbar=no,menubar=no,location=no,titlebar=no")
}

function OpenSubSystemWindow(url,needReload){
	OpenModalWindow(url,630,450,needReload);
}

function OpenAdministratorWindow(url){
	OpenModalWindow(url,630,500);
}
function OpenWin(newURL,left,top,width,height) {
		var recdata = false;
		if(typeof(left) == 'undefined' || left == null){
			left = (screen.width - width) /2;
		}
		if(typeof(top) == 'undefined' || top == null){
			top = (screen.height - height) /2;
		}
		ContentWindow=window.open(newURL,
				  "_blank",
				  "width="+ width + ",height="+height+"," 
				+ "left="+ left + ",top="+top+"," 
				+ "directories=no,status=no,"
				+ "menubar=no, scrollbars=no, resizable=no, location=no")
}
function OpenModalWindowReload(newURL,width,height,needReload) {
	try {
		var retData = false;

		if(typeof(width) == 'undefined'){
			width 	= screen.width * 0.9;
		}
		if(typeof(height) == 'undefined'){
			height 	= screen.height * 0.9;
		}
		if(typeof(needReload) == 'undefined'){
			needReload 	= false;
		}
		retData = showModalDialog(newURL, 
				  window, 
				  "dialogWidth:" + width
				+ "px;dialogHeight:" + height
				+ "px;center=1;scroll=1;help=0;status=0;");

		if(needReload && retData){
			window.location.reload();
		}
	} catch (err) {
	}
}

