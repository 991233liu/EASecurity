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
