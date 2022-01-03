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
    alert(obj.form.action)
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