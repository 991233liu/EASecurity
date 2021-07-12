<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>成本管理系统</title>
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
    <asset:link rel="stylesheet" href="plugins/iCheck/square/blue.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/morris/morris.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/jvectormap/jquery-jvectormap-1.2.2.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/datepicker/datepicker3.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/daterangepicker/daterangepicker.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.min.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/datatables/dataTables.bootstrap.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/treeTable/jquery.treetable.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/treeTable/jquery.treetable.theme.default.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/select2/select2.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/colorpicker/bootstrap-colorpicker.min.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/bootstrap-markdown/css/bootstrap-markdown.min.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/adminLTE/css/AdminLTE.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/adminLTE/css/skins/all-skins.min.css" type="text/css" />
    <asset:link rel="stylesheet" href="css/style.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/ionslider/ion.rangeSlider.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/ionslider/ion.rangeSlider.skinNice.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/bootstrap-slider/slider.css" type="text/css" />
    <asset:link rel="stylesheet" href="plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.css" type="text/css" />

    <!-- 平台应用css -->
    <asset:link rel="stylesheet" href="css/css_main.css" type="text/css" />


    <asset:javascript src="plugins/jQuery/jquery-2.2.3.min.js" />
    <asset:javascript src="plugins/jQueryUI/jquery-ui.min.js" />
    <script>
        $.widget.bridge('uibutton', $.ui.button);
    </script>
    <asset:javascript src="plugins/bootstrap/js/bootstrap.min.js" />
    <asset:javascript src="plugins/raphael/raphael-min.js" />
    <asset:javascript src="plugins/morris/morris.min.js" />
    <asset:javascript src="plugins/sparkline/jquery.sparkline.min.js" />
    <asset:javascript src="plugins/jvectormap/jquery-jvectormap-1.2.2.min.js" />
    <asset:javascript src="plugins/jvectormap/jquery-jvectormap-world-mill-en.js" />
    <asset:javascript src="plugins/knob/jquery.knob.js" />
    <asset:javascript src="plugins/daterangepicker/moment.min.js" />
    <asset:javascript src="plugins/daterangepicker/daterangepicker.js" />
    <asset:javascript src="plugins/daterangepicker/daterangepicker.zh-CN.js" />
    <asset:javascript src="plugins/datepicker/bootstrap-datepicker.js" />
    <asset:javascript src="plugins/datepicker/locales/bootstrap-datepicker.zh-CN.js" />
    <asset:javascript src="plugins/bootstrap-wysihtml5/bootstrap3-wysihtml5.all.min.js" />
    <asset:javascript src="plugins/slimScroll/jquery.slimscroll.min.js" />
    <asset:javascript src="plugins/fastclick/fastclick.js" />
    <asset:javascript src="plugins/iCheck/icheck.min.js" />
    <asset:javascript src="plugins/adminLTE/js/app.min.js" />
    <asset:javascript src="plugins/treeTable/jquery.treetable.js" />
    <asset:javascript src="plugins/select2/select2.full.min.js" />
    <asset:javascript src="plugins/colorpicker/bootstrap-colorpicker.min.js" />
    <asset:javascript src="plugins/bootstrap-wysihtml5/bootstrap-wysihtml5.zh-CN.js" />
    <asset:javascript src="plugins/bootstrap-markdown/js/bootstrap-markdown.js" />
    <asset:javascript src="plugins/bootstrap-markdown/locale/bootstrap-markdown.zh.js" />
    <asset:javascript src="plugins/bootstrap-markdown/js/markdown.js" />
    <asset:javascript src="plugins/bootstrap-markdown/js/to-markdown.js" />
    <asset:javascript src="plugins/ckeditor/ckeditor.js" />
    <asset:javascript src="plugins/input-mask/jquery.inputmask.js" />
    <asset:javascript src="plugins/input-mask/jquery.inputmask.date.extensions.js" />
    <asset:javascript src="plugins/input-mask/jquery.inputmask.extensions.js" />
    <asset:javascript src="plugins/datatables/jquery.dataTables.min.js" />
    <asset:javascript src="plugins/datatables/dataTables.bootstrap.min.js" />
    <asset:javascript src="plugins/chartjs/Chart.min.js" />
    <asset:javascript src="plugins/flot/jquery.flot.min.js" />
    <asset:javascript src="plugins/flot/jquery.flot.resize.min.js" />
    <asset:javascript src="plugins/flot/jquery.flot.pie.min.js" />
    <asset:javascript src="plugins/flot/jquery.flot.categories.min.js" />
    <asset:javascript src="plugins/ionslider/ion.rangeSlider.min.js" />
    <asset:javascript src="plugins/bootstrap-slider/bootstrap-slider.js" />
    <asset:javascript src="plugins/bootstrap-datetimepicker/bootstrap-datetimepicker.js" />
    <asset:javascript src="plugins/bootstrap-datetimepicker/locales/bootstrap-datetimepicker.zh-CN.js" />

    <!-- 平台应用js -->
    <asset:javascript src="js/js_main.js" />
</head>

<body class="hold-transition skin-purple sidebar-mini">

    <div class="wrapper">

        <!-- 页面头部 -->
        <header class="main-header">
            <!-- Logo -->
            <a href="all-admin-index.html" class="logo">
                <!-- mini logo for sidebar mini 50x50 pixels -->
                <span class="logo-mini"><b>成本</b></span>
                <!-- logo for regular state and mobile devices -->
                <span class="logo-lg">成本管理</span>
            </a>


            <!-- Header Navbar: style can be found in header.less -->
            <nav class="navbar navbar-static-top">
                <!-- Sidebar toggle button-->
                <a href="#" class="sidebar-toggle" data-toggle="offcanvas" role="button">
            <span class="sr-only">Toggle navigation</span>
        </a>

                <div class="navbar-custom-menu">
                    <ul class="nav navbar-nav">
                        <!-- Messages: style can be found in dropdown.less-->
                        <li class="dropdown messages-menu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-envelope-o"></i>
                        <span class="label label-success">4</span>
                    </a>
                            <ul class="dropdown-menu">
                                <li class="header">你有4个邮件</li>
                                <li>
                                    <!-- inner menu: contains the actual data -->
                                    <ul class="menu">
                                        <li>
                                            <!-- start message -->
                                            <a href="#">
%{--                                                <div class="pull-left">--}%
%{--                                                    <img src="../img/user2-160x160.jpg" class="img-circle" alt="User Image">--}%
%{--                                                </div>--}%
                                                <h4>
                                                    系统消息
                                                    <small><i class="fa fa-clock-o"></i> 5 分钟前</small>
                                                </h4>
                                                <p>欢迎登录系统?</p>
                                            </a>
                                        </li>
                                        <!-- end message -->
                                        <li>
                                            <a href="#">
%{--                                                <div class="pull-left">--}%
%{--                                                    <img src="../img/user3-128x128.jpg" class="img-circle" alt="User Image">--}%
%{--                                                </div>--}%
                                                <h4>
                                                    团队消息
                                                    <small><i class="fa fa-clock-o"></i> 2 小时前</small>
                                                </h4>
                                                <p>你有新的任务了</p>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
%{--                                                <div class="pull-left">--}%
%{--                                                    <img src="../img/user4-128x128.jpg" class="img-circle" alt="User Image">--}%
%{--                                                </div>--}%
                                                <h4>
                                                    Developers
                                                    <small><i class="fa fa-clock-o"></i> Today</small>
                                                </h4>
                                                <p>Why not buy a new awesome theme?</p>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
%{--                                                <div class="pull-left">--}%
%{--                                                    <img src="../img/user3-128x128.jpg" class="img-circle" alt="User Image">--}%
%{--                                                </div>--}%
                                                <h4>
                                                    Sales Department
                                                    <small><i class="fa fa-clock-o"></i> Yesterday</small>
                                                </h4>
                                                <p>Why not buy a new awesome theme?</p>
                                            </a>
                                        </li>
                                        <li>
                                            <a href="#">
%{--                                                <div class="pull-left">--}%
%{--                                                    <img src="../img/user4-128x128.jpg" class="img-circle" alt="User Image">--}%
%{--                                                </div>--}%
                                                <h4>
                                                    Reviewers
                                                    <small><i class="fa fa-clock-o"></i> 2 days</small>
                                                </h4>
                                                <p>Why not buy a new awesome theme?</p>
                                            </a>
                                        </li>
                                    </ul>
                                </li>
                                <li class="footer"><a href="#">See All Messages</a></li>
                            </ul>
                        </li>
                        <!-- Notifications: style can be found in dropdown.less -->
                        <li class="dropdown notifications-menu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-bell-o"></i>
                        <span class="label label-warning">10</span>
                    </a>
                            <ul class="dropdown-menu">
                                <li class="header">你有10个新消息</li>
                                <li>
                                    <!-- inner menu: contains the actual data -->
                                    <ul class="menu">
                                        <li>
                                            <a href="#">
                                        <i class="fa fa-users text-aqua"></i> 5 new members joined today
                                    </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                        <i class="fa fa-warning text-yellow"></i> Very long description here that may not
                                        fit into the page and may cause design problems
                                    </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                        <i class="fa fa-users text-red"></i> 5 new members joined
                                    </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                        <i class="fa fa-shopping-cart text-green"></i> 25 sales made
                                    </a>
                                        </li>
                                        <li>
                                            <a href="#">
                                        <i class="fa fa-user text-red"></i> You changed your username
                                    </a>
                                        </li>
                                    </ul>
                                </li>
                                <li class="footer"><a href="#">View all</a></li>
                            </ul>
                        </li>
                        <!-- Tasks: style can be found in dropdown.less -->
                        <li class="dropdown tasks-menu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                        <i class="fa fa-flag-o"></i>
                        <span class="label label-danger">9</span>
                    </a>
                            <ul class="dropdown-menu">
                                <li class="header">你有9个新任务</li>
                                <li>
                                    <!-- inner menu: contains the actual data -->
                                    <ul class="menu">
                                        <li>
                                            <!-- Task item -->
                                            <a href="#">
                                                <h3>
                                                    Design some buttons
                                                    <small class="pull-right">20%</small>
                                                </h3>
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-aqua" style="width: 20%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">20% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                        <!-- end task item -->
                                        <li>
                                            <!-- Task item -->
                                            <a href="#">
                                                <h3>
                                                    Create a nice theme
                                                    <small class="pull-right">40%</small>
                                                </h3>
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-green" style="width: 40%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">40% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                        <!-- end task item -->
                                        <li>
                                            <!-- Task item -->
                                            <a href="#">
                                                <h3>
                                                    Some task I need to do
                                                    <small class="pull-right">60%</small>
                                                </h3>
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-red" style="width: 60%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">60% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                        <!-- end task item -->
                                        <li>
                                            <!-- Task item -->
                                            <a href="#">
                                                <h3>
                                                    Make beautiful transitions
                                                    <small class="pull-right">80%</small>
                                                </h3>
                                                <div class="progress xs">
                                                    <div class="progress-bar progress-bar-yellow" style="width: 80%" role="progressbar" aria-valuenow="20" aria-valuemin="0" aria-valuemax="100">
                                                        <span class="sr-only">80% Complete</span>
                                                    </div>
                                                </div>
                                            </a>
                                        </li>
                                        <!-- end task item -->
                                    </ul>
                                </li>
                                <li class="footer">
                                    <a href="#">View all tasks</a>
                                </li>
                            </ul>
                        </li>
                        <!-- User Account: style can be found in dropdown.less -->
                        <li class="dropdown user user-menu">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown">
			    <asset:image src="img/user2-160x160.jpg" class="user-image img-circle elevation-2" alt="User Image" />
			<sec:ifLoggedIn>
				<span class="hidden-xs"><sec:username /></span>
			</sec:ifLoggedIn> <sec:ifNotLoggedIn>
				<span class="hidden-xs"><g:message
						code="default.user.guest" default="Guest" /> </span>
			</sec:ifNotLoggedIn>
                    </a>
                            <ul class="dropdown-menu">
                                <!-- User image -->
				<li class="user-header bg-primary">
				    <asset:image src="img/user2-160x160.jpg" class="img-circle elevation-2" alt="User Image" />
					<p>
						<sec:ifLoggedIn>
							<sec:username />
%{--							张猿猿 - 数据管理员--}%
						</sec:ifLoggedIn>
						<sec:ifNotLoggedIn>
							<g:message code="default.user.guest" default="Guest" />
						</sec:ifNotLoggedIn>
						<small>最后登录 11:20AM</small>
					</p></li>
                                <!-- Menu Footer-->
                                <li class="user-footer">
				    <g:link class="btn btn-default btn-flat" controller="logout">
					<g:message code="logout.signout.label" default="注销" />
				    </g:link>
                                </li>
                            </ul>
                        </li>

                    </ul>
                </div>
            </nav>
        </header>
        <!-- 页面头部 /-->

        <!-- 页面区域 -->
	<g:layoutBody />

        <!-- 底部导航 -->
        <footer class="main-footer">
            <div class="pull-right hidden-xs">
                <b>Version</b> <g:meta name="info.app.version"/>
            </div>
            <strong>Copyright &copy; 2014-2037 刘路峰</a>.</strong> All rights reserved.
        </footer>
        <!-- 底部导航 /-->

    </div>
    <script>
        $(document).ready(function() {
            // 选择框
            $(".select2").select2();

            // WYSIHTML5编辑器
            $(".textarea").wysihtml5({
                locale: 'zh-CN'
            });
        });

        // // 设置激活菜单
        // function setSidebarActive(tagUri) {
        //     var liObj = $("#" + tagUri);
        //     if (liObj.length > 0) {
        //         liObj.parent().parent().addClass("active");
        //         liObj.addClass("active");
        //     }
        // }
        //
        //
        // $(document).ready(function() {
        //     // 激活导航位置
        //     setSidebarActive("admin-index");
        // });
    </script>
</body>

</html>
<!---->