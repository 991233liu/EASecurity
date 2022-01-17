<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title><g:message code="default.app.title" default="default.app.title" /></title>

    <!-- Tell the browser to be responsive to screen width -->
    <meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">
    <!-- Bootstrap 3.3.6 -->
    <!-- Font Awesome -->
    <!-- Ionicons -->
    <!-- Theme style -->
    <!-- iCheck -->
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
  <script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
  <![endif]-->
    <asset:link rel="stylesheet" href="plugins/bootstrap/css/bootstrap.min.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/font-awesome/css/font-awesome.min.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/ionicons/css/ionicons.min.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/adminLTE/css/AdminLTE.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/iCheck/square/blue.css" type="text/css" />
</head>

<body class="hold-transition login-page">
    <div class="login-box">
	<!-- 加密用盐值，每个系统唯一，需要联系管理员获取 -->
	<input type="hidden" name="salt" id="salt" value="$2a$10$ayeUQHCANiGOhpO3uBTllu"/>
	<!-- 加密用盐值，每个系统唯一，需要联系管理员获取 /-->
        <div class="login-logo">
            <a href="pages/all-admin-index.html"><b><g:message code="default.app.title" default="default.app.title" /></b></a>
        </div>
        <!-- /.login-logo -->
        <div class="login-box-body">
            <p class="login-box-msg">重置用户密码</p>
            <g:if test='${flash.message}'>
                <div class="login_message">${flash.message}</div>
            </g:if>
            <form action='/admin/changePassword/changePassword' method='POST' id='changePasswordForm' class='cssform' autocomplete='off'>
                <div class="form-group has-feedback">
                    <g:textField name="username" value="${username}"
                                 class="form-control"
                                 placeholder="${message(code: 'user.username.label', default: 'Username')}" />
                    <span class="glyphicon glyphicon-envelope form-control-feedback"></span>
                </div>
                <div class="form-group has-feedback">
                    <g:passwordField name="newPassword" class="form-control"
                                     placeholder="${message(code: 'user.password.label', default: 'NewPassword')}" />
                    <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                </div>
                <div class="form-group has-feedback">
                    <g:passwordField name="new2Password" class="form-control"
                                     placeholder="${message(code: 'user.password.label', default: 'NewPassword')}" />
                    <span class="glyphicon glyphicon-lock form-control-feedback"></span>
                </div>
                <g:if test='${gifCaptcha}'>
                    <div class="form-group has-feedback">
                        <p>
                            <label for="gifCaptchaValue"><img id="gifCaptchaImg" src="${gifCaptcha.get("image")}" onclick="flushLoginCaptcha()"/>
                            <input type="hidden" name="gifCaptcha" id="gifCaptcha" value="${gifCaptcha.get("key")}"/>
                            <input type="text" style="width: 120px;" class="text_" name="gifCaptchaValue" id="gifCaptchaValue"/>
                            <a href="javascript:;" class="btn btn-default" onclick="flushLoginCaptcha()">
                                <g:message code="login.flush.label" default="刷新" />
                            </a></label>
                        </p>
                    </div>
                </g:if>
                <div class="row">
                    <button type="submit" class="btn btn-primary btn-block btn-flat" onclick="return onChangePasswordByUser()">
                        <g:message code="login.signin.label" default="修改密码" />
                    </button>
                </div>
            </form>
        </div>
        <!-- /.login-box-body -->
    </div>
    <!-- /.login-box -->

    <!-- jQuery 2.2.3 -->
    <!-- Bootstrap 3.3.6 -->
    <!-- iCheck -->
    <asset:javascript src="plugins/jQuery/jquery-2.2.3.min.js" />
    <asset:javascript src="plugins/bootstrap/js/bootstrap.min.js" />
    <asset:javascript src="plugins/iCheck/icheck.min.js" />
    <asset:javascript src="js/application.js" />
    <asset:javascript src="js/bcrypt/prng/accum.js" />
    <asset:javascript src="js/bcrypt/prng/isaac.js" />
    <asset:javascript src="js/bcrypt/util/base64.js" />
    <asset:javascript src="js/bcrypt/util/utf8.js" />
    <asset:javascript src="js/bcrypt/impl.js" />
    <asset:javascript src="js/bcrypt/util.js" />
    <asset:javascript src="js/bcrypt/bcrypt.js" />
</body>

</html>