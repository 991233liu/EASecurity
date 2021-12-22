<!DOCTYPE html>
<html>
<head>
<title><g:message code="default.list.label" args="[entityName]" /></title>
<meta name="layout" content="adminLTE" />
</head>
<body>
	<g:render template="/themes/menuAll" model="['openMenu':'Home']"/>

	<!-- Content Wrapper. Contains page content -->
	<div class="content-wrapper">
		<!-- 内容头部 -->
		<section class="content-header">
			<h1>首页</h1>
			<ol class="breadcrumb">
				<i class="fa fa-dashboard"></i>
				<li>&nbsp <g:link controller="dashboard">
					<g:message code="home.label" default="Home" />
				</g:link></li>
			</ol>
		</section>
		<!-- 内容头部 /-->

		<!--信息提示栏-->
		<g:if test="${flash.message}">
			<div class="pad margin no-print">
				<div class="callout callout-info" style="margin-bottom: 0!important;">
					<h5><i class="fa fa-info"></i> Info!</h5>
					${flash.message}
				</div>
			</div>
		</g:if>
		<!-- 信息提示栏 /-->

		<!-- Main content -->
		<section class="content">
			<!-- row -->
			<div class="row">
				<div class="col-xs-6">
					<div class="box box-danger">
						<div class="box-header with-border">
%{--							<i class="fa fa-bar-chart-o"></i>--}%
							<h3 class="box-title">项目成本进度</h3>
							<div class="box-tools pull-right">
								<button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
								</button>
								<button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
							</div>
						</div>
						<!-- /.box-header -->
						<div class="box-body">
							<div class="row">
								<div class="col-xs-6 col-md-6 text-center">
									<input type="text" class="knob" value="30" data-width="90" data-height="90" data-fgcolor="#3c8dbc">
									<div class="knob-label">司库</div>
								</div>
								<!-- ./col -->
								<div class="col-xs-6 col-md-6 text-center">
									<input type="text" class="knob" value="70" data-width="90" data-height="90" data-fgcolor="#f56954">
									<div class="knob-label">核心</div>
								</div>
								<!-- ./col -->
							</div>
							<!-- /.row -->

							<div class="row">
								<div class="col-xs-6 col-md-12 text-center">
									<input type="text" class="knob" value="90" data-width="90" data-height="90" data-fgcolor="#932ab6">
									<div class="knob-label">实施</div>
								</div>
								<!-- ./col -->
							</div>
							<!-- /.row -->
						</div>
					</div>
				</div>
				<div class="col-xs-6">
					<!-- DONUT CHART -->
					<div class="box box-info">
						<div class="box-header with-border">
							<h3 class="box-title">jira子项目成本分布</h3>
							<div class="box-tools pull-right">
								<button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
								</button>
								<button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
							</div>
						</div>
						<div class="box-body">
							<canvas id="pieChart" style="height:250px"></canvas>
						</div>
						<!-- /.box-body -->
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-6">
					<!-- DONUT CHART -->
					<div class="box box-primary">
						<div class="box-header with-border">
							<h3 class="box-title">项目阶段成本分布</h3>
							<div class="box-tools pull-right">
								<button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
								</button>
								<button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
							</div>
						</div>
						<div class="box-body">
						</div>
						<!-- /.box-body -->
					</div>
				</div>
				<div class="col-xs-6">
					<!-- DONUT CHART -->
					<div class="box box-success">
						<div class="box-header with-border">
							<h3 class="box-title">项目过程域成本分布</h3>
							<div class="box-tools pull-right">
								<button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
								</button>
								<button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
							</div>
						</div>
						<div class="box-body">
						</div>
						<!-- /.box-body -->
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<!-- DONUT CHART -->
					<div class="box box-warning">
						<div class="box-header with-border">
							<h3 class="box-title">最费钱需求TOP10</h3>
							<div class="box-tools pull-right">
								<button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
								</button>
								<button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
							</div>
						</div>
						<div class="box-body">
						</div>
						<!-- /.box-body -->
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-xs-12">
					<!-- DONUT CHART -->
					<div class="box box-primary">
						<div class="box-header with-border">
							<h3 class="box-title">最费钱任务TOP10</h3>
							<div class="box-tools pull-right">
								<button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i>
								</button>
								<button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
							</div>
						</div>
						<div class="box-body">
						</div>
						<!-- /.box-body -->
					</div>
				</div>
			</div>
		</section>
		<!-- /.content -->
	</div>
	<!-- /.content-wrapper -->
<script>
	$(document).ready(function() {
		/* jQueryKnob */

		$(".knob").knob({
			/*change : function (value) {
             //console.log("change : " + value);
             },
             release : function (value) {
             console.log("release : " + value);
             },
             cancel : function () {
             console.log("cancel : " + this.value);
             },*/
			draw: function() {

				// "tron" case
				if (this.$.data('skin') == 'tron') {

					var a = this.angle(this.cv) // Angle
							,
							sa = this.startAngle // Previous start angle
							,
							sat = this.startAngle // Start angle
							,
							ea // Previous end angle
							, eat = sat + a // End angle
							,
							r = true;

					this.g.lineWidth = this.lineWidth;

					this.o.cursor &&
					(sat = eat - 0.3) &&
					(eat = eat + 0.3);

					if (this.o.displayPrevious) {
						ea = this.startAngle + this.angle(this.value);
						this.o.cursor &&
						(sa = ea - 0.3) &&
						(ea = ea + 0.3);
						this.g.beginPath();
						this.g.strokeStyle = this.previousColor;
						this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, sa, ea, false);
						this.g.stroke();
					}

					this.g.beginPath();
					this.g.strokeStyle = r ? this.o.fgColor : this.fgColor;
					this.g.arc(this.xy, this.xy, this.radius - this.lineWidth, sat, eat, false);
					this.g.stroke();

					this.g.lineWidth = 2;
					this.g.beginPath();
					this.g.strokeStyle = this.o.fgColor;
					this.g.arc(this.xy, this.xy, this.radius - this.lineWidth + 1 + this.lineWidth * 2 / 3, 0, 2 * Math.PI, false);
					this.g.stroke();

					return false;
				}
			}
		});
		/* END JQUERY KNOB */

		var pieChartCanvas = $("#pieChart").get(0).getContext("2d");
		var pieChart = new Chart(pieChartCanvas);
		var PieData = [{
			value: 700,
			color: "#f56954",
			highlight: "#f56954",
			label: "需求"
			},
			{
				value: 500,
				color: "#00a65a",
				highlight: "#00a65a",
				label: "开发"
			},
			{
				value: 400,
				color: "#f39c12",
				highlight: "#f39c12",
				label: "测试"
			},
			{
				value: 600,
				color: "#00c0ef",
				highlight: "#00c0ef",
				label: "售后"
			}
		];
		var pieOptions = {
			//Boolean - Whether we should show a stroke on each segment
			segmentShowStroke: true,
			//String - The colour of each segment stroke
			segmentStrokeColor: "#fff",
			//Number - The width of each segment stroke
			segmentStrokeWidth: 2,
			//Number - The percentage of the chart that we cut out of the middle
			percentageInnerCutout: 50, // This is 0 for Pie charts
			//Number - Amount of animation steps
			animationSteps: 100,
			//String - Animation easing effect
			animationEasing: "easeOutBounce",
			//Boolean - Whether we animate the rotation of the Doughnut
			animateRotate: true,
			//Boolean - Whether we animate scaling the Doughnut from the centre
			animateScale: false,
			//Boolean - whether to make the chart responsive to window resizing
			responsive: true,
			// Boolean - whether to maintain the starting aspect ratio or not when responsive, if set to false, will take up entire container
			maintainAspectRatio: true,
			//String - A legend template
			%{--legendTemplate: "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<segments.length; i++){%><li><span style=\"background-color:<%=segments[i].fillColor%>\"></span><%if(segments[i].label){%><%=segments[i].label%><%}%></li><%}%></ul>"--}%
		};
		//Create pie or douhnut chart
		// You can switch between pie and douhnut using the method below.
		pieChart.Doughnut(PieData, pieOptions);
	});
</script>
</body>
</html>
