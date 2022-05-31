/*! application.js
 * ================
 * 应用系统js，所有项目组写的js，都可以放这里
 *
 */


/**
 * 点击列表中批量删除按钮
 */
function deleteAll(obj) {
    var ids = []
    $.each($('input:checkbox[name="ids"]').get(), function (index, it) {
        if (it.checked) ids.push(it.value)
    });
    obj.form.action = obj.form.action + "/" + ids
}

/**
 * 刷新图片验证码（登录）
 */
function flushLoginCaptcha() {
    $.ajax({
        url: '/SecurityCentre/auth/gifCaptcha',
        method: "GET",
        asyn: true,
        dataType: "json",
        success: (response) => {
            $('#gifCaptcha').attr('value', response.key)
            $('#gifCaptchaImg').attr('src', response.image)
        }
    });
}

/**
 * 加密密码（bcrypt算法）
 */
function encodePassword() {
//    var salt = bcrypt.genSaltSync(10);
	var salt = $("#salt").val();
	if(!salt||salt.length<28)
		throw Error("salt 为非法值，请联系管理员申请: " + salt);
	var hash = bcrypt.hashSync($("#password").val(), salt);
	$("#password").val(hash);
}

/**
 * 登录
 */
function onLogin() {
	encodePassword();
	$('#loginForm').submit();
}

/**
 * Ajax登录
 */
function onLoginAjax() {
	if($("#password").val().length<50) encodePassword();
	$.ajax({
        url: $('#loginForm').attr('action'),
        method: "POST",
        asyn: true,
        dataType: "json",
//		data: {"username":$("#username").val(),"password":$("#password").val(),"gifCaptchaValue":$("#gifCaptchaValue").val()},
		data: $('#loginForm').serialize(),
        success: (response) => {
//            alert(response);
			var srchref = $("#srchref").val();
			if(srchref)$(location).prop('href', srchref)
        },
		error : function(XMLHttpRequest, ajaxOptions, thrownError) {
			if(XMLHttpRequest.responseJSON){
				var response = XMLHttpRequest.responseJSON;
				$('#errormessage').text(response.message);
				$('#message').css('display','block'); 
				flushLoginCaptcha();
			} else {
				alert("服务器异常！" + XMLHttpRequest.status + ":" + XMLHttpRequest.responseText);
			}
		}
    });
}

/**
 * 获取URL中参数
 */
function GetRequest(name){
	var url = window.location.search;
	if(url.indexOf("?")!= -1){
		var str = url.substr(1);
		var strs = str.split("&");
		for (var i = 0; i < strs.length; i++){
			var theRequest = strs[i].split("=");
			if(theRequest[0]==name)
				return theRequest[1];
		}
	}
	return null;
}

/**
 * string转Byte
 */
function stringToByte(str) {
    var bytes = new Array();
    var len, c;
    len = str.length;
    for (var i = 0; i < len; i++) {
        c = str.charCodeAt(i);
        if (c >= 0x010000 && c <= 0x10FFFF) {
            bytes.push(((c >> 18) & 0x07) | 0xF0);
            bytes.push(((c >> 12) & 0x3F) | 0x80);
            bytes.push(((c >> 6) & 0x3F) | 0x80);
            bytes.push((c & 0x3F) | 0x80);
        } else if (c >= 0x000800 && c <= 0x00FFFF) {
            bytes.push(((c >> 12) & 0x0F) | 0xE0);
            bytes.push(((c >> 6) & 0x3F) | 0x80);
            bytes.push((c & 0x3F) | 0x80);
        } else if (c >= 0x000080 && c <= 0x0007FF) {
            bytes.push(((c >> 6) & 0x1F) | 0xC0);
            bytes.push((c & 0x3F) | 0x80);
        } else {
            bytes.push(c & 0xFF);
        }
    }
    return bytes;
}

/**
 * Byte转string
 */
function byteToString(arr) {
    if(typeof arr === 'string') {
        return arr;
    }
    var str = '',
        _arr = arr;
    for(var i = 0; i < _arr.length; i++) {
        var one = _arr[i].toString(2),
            v = one.match(/^1+?(?=0)/);
        if(v && one.length == 8) {
            var bytesLength = v[0].length;
            var store = _arr[i].toString(2).slice(7 - bytesLength);
            for(var st = 1; st < bytesLength; st++) {
                store += _arr[st + i].toString(2).slice(2);
            }
            str += String.fromCharCode(parseInt(store, 2));
            i += bytesLength - 1;
        } else {
            str += String.fromCharCode(_arr[i]);
        }
    }
    return str;
}

/**
 * 加密（Blowfish算法）
 */
function encodeBlowfish(key, plaintext) {
    blowfish.setBoxes(newBoxes);
    return blowfish.encrypt(base64.encode(stringToByte(plaintext)), key, {cipherMode: 0, outputType: 1});
}

/**
 * 解密（Blowfish算法）
 */
function decryptBlowfish(key, ciphertext) {
    blowfish.setBoxes(newBoxes);
    return byteToString(base64.decode(blowfish.decrypt(ciphertext, key, {cipherMode: 0, outputType: 1})));
}

