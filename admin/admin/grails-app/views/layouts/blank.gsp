<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title><g:message code="default.app.title" default="default.app.title" /></title>
	<!-- Tell the browser to be responsive to screen width -->
%{--	<meta content="width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no" name="viewport">--}%
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
		<g:layoutBody />
	</body>
<script>
	$(document).ready(function() {
		// 选择框
		$(".select2").select2();

		// WYSIHTML5编辑器
		$(".textarea").wysihtml5({
			locale: 'zh-CN'
		});
	});
</script>
</html>