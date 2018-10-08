// JavaScript Document
// 2009-07-16 11:27 ShiCH 这种写法只能在IE下使用 重新编写
/*
function login(){
	with(document.forms(0)){
		if(userId.value==null || userId.value==""){
			alert("请输入用户名");
			userId.focus();
			return false;
		} 
		if(pwd.value==null || pwd.value==""){
			alert("请输入密码");
			pwd.focus();
			return false;
		}
		submit();
		return true;
	}
}
*/

$G = function(id){
	return document.getElementById(id);
}
function login(){
	var userNameIpt = $G("userId"),userPswIpt = $G("pwd");
	var reg = /(^\s*)|(\s*$)/gim;
	if(userNameIpt.value.replace(reg,"") == ""){
    var m = new MessageBox(userNameIpt);
    m.Show("请输入用户名");
		//alert("请输入用户名");
		userNameIpt.focus();
		return;
	}
	if(userPswIpt.value.replace(reg,"") == ""){
    var m = new MessageBox(userPswIpt);
    m.Show("请输入密码");
		//alert("请输入密码");
		userPswIpt.focus();
		return;
	}
	var form = $G("login");
	userNameIpt.value = userNameIpt.value.replace(reg,"");
    $G("passInfo").value=btoa(userNameIpt.value+"#@#@#"+PASSKEY+"#@#@#"+userPswIpt.value);
//	var mywidth = 800;
//	var myheight = 600;
//    window.open("login.action", "_blank", "top=0,left=0,toolbar=no,location=no,status=no,menubar=no,scrollbars=no,resizable=no,width=" + mywidth + ",height=" + myheight);
    form.submit();
}

function checkErrorLogin(){
	  var reg = /(^\s*)|(\s*$)/gim;
    var td = $G("msg"),tx = td.innerText || td.textContent;
    if(null != tx && tx.replace(reg,"") != ""){
        var m = new MessageBox($G("userId"));
        m.Show(tx,5000);
    }
}
//BASE64Decoder 加密
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

// 2009-07-16 11:25 ShiCH 添加默认光标聚焦
window.onload = function(){
	var userNameIpt = $G("userId");
	userNameIpt.focus();
  checkErrorLogin();
}
