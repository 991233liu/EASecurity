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
        url: '/admin/login/gifCaptcha',
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
function encodePassword(obj) {
//    var salt = bcrypt.genSaltSync(10);
    var salt = $("#salt").val();
    if (!salt || salt.length < 28)
        throw Error("salt 为非法值，请联系管理员申请: " + salt);
    var hash = bcrypt.hashSync(obj.val(), salt);
    obj.val(hash);
}

/**
 * 登录
 */
function onLogin() {
    encodePassword($("#password"));
    $('#loginForm').submit();
}

/**
 * 修改密码
 */
function onChangePassword() {
    if (!$("#oldPassword") || !$("#newPassword").val()) {
        alert("密码不能为空");
        return false
    }
    if ($("#newPassword").val() != $("#new2Password").val()) {
        alert("两次输入的密码不一致！");
        return false
    }
    encodePassword($("#oldPassword"));
    encodePassword($("#newPassword"));
    $("#new2Password").val('');
    $('#changePasswordForm').submit();
}