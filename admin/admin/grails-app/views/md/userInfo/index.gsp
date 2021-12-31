<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="adminLTE" />
        <g:set var="entityName"
               value="${message(code: 'userInfo.label', default: 'UserInfo')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="/themes/menuAll" model="['openMenu':'UserInfos', 'activeMenu':'UserInfoList']"/>

        <!-- 内容区域 -->
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">

            <!-- 内容头部 -->
            <section class="content-header">
                <h1>
                    <g:message code="userInfo.list.label" args="[]" default="UserInfos" />
                    <small>XXX<g:message code="default.list.label" args="[entityName]" /></small>
                </h1>
                <ol class="breadcrumb">
		    <i class="fa fa-dashboard"></i>
		    <li>&nbsp <g:link controller="dashboard">
			    <g:message code="home.label" default="Home" />
			</g:link></li>
			<li><g:message code="userInfos.label" default="UserInfos" /></li>
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

            <!-- 正文区域 -->
            <section class="content">

                <!-- .box-body -->
                <div class="box box-primary">
%{--                    <div class="box-header fa-border">--}%
%{--                        <div class="table-box">--}%
%{--                            <h3 class="box-title">列表</h3>--}%
%{--                        </div>--}%
%{--                    </div>--}%

                    <div class="box-body">
                        <div class="box-group" id="accordion">
                            <!-- 搜索条件面板 -->
                            <g:form name="search" controller="userInfo" action="index" method="PUT">
                                <div class="">
    %{--                                <div class="box-header with-border">--}%
    %{--                                    <h3 class="box-title">列表</h3>--}%
    %{--                                </div>--}%
                                    <div class="box-body">
                                        <div class="table-box">
                                            <div class="col-sm-11 pull-left">
                                                <div class="form-group col-sm-6">
                                                    <label class="col-sm-3 control-label"><div class="pull-right">
                                                        <g:message code="com.easecurity.admin.core.b.UserInfo.name" default="Name" /></div></label>
                                                    <div class="col-sm-9 left"><div class="col-lg-pull-12">
                                                        <g:field type="search" name="search.name" value="${params.search?.name}" class="form-control"/>
                                                    </div></div>
                                                </div>
                                                <div class="form-group col-sm-6">
                                                    <label class="col-sm-3 control-label"><div class="pull-right">
                                                        <g:message code="com.easecurity.admin.core.b.UserInfo.code" default="Code" /></div></label>
                                                    <div class="col-sm-9 left"><div class="col-lg-pull-12">
                                                        <g:field type="search" name="search.code" value="${params.search?.code}" class="form-control"/>
                                                    </div></div>
                                                </div>
                                            </div>
                                            <div class="pull-right">
                                                <div class="has-feedback">
                                                    <input class="btn btn-default" type="submit" value="&nbsp;&nbsp;"/>
%{--                                                    <input type="text" class="form-control input-sm" placeholder="搜索">--}%
                                                    <span class="glyphicon glyphicon-search form-control-feedback"></span>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </g:form>
                            <!-- 搜索条件面板 /-->

                            <!-- 查询结果面板 -->
                            <div class="box">
    %{--                            <div class="box-header with-border">--}%
    %{--                                <h3 class="box-title">列表</h3>--}%
    %{--                            </div>--}%
                                <div class="box-body">
                                    <!-- 数据表格 -->
                                    <div class="table-box">
                                        <!--工具栏-->
                                        <div class="pull-left">
                                            <div class="form-group form-inline">
                                                <div class="btn-group">
                                                    <g:form name="userInfoDelete" controller="userInfo" action="delete" method="DELETE">

                                                        <g:link class="btn btn-default" action="create">
                                                            <g:message code="default.button.create.label" default="Create" />
                                                        </g:link>
                                                        <input class="btn btn-danger" type="submit"
                                                               value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                                               onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')?deleteAll(this):false" />
                                                        <g:link class="btn btn-default" controller="userInfo">
                                                            <g:message code="default.button.reload.label" default="Reload" />
                                                        </g:link>
                                                    </g:form>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="box-tools pull-right">
                                            <div class="has-feedback">
%{--                                                <input type="text" class="form-control input-sm" placeholder="搜索">--}%
%{--                                                <span class="glyphicon glyphicon-search form-control-feedback"></span>--}%
                                            </div>
                                        </div>
                                        <!--工具栏/-->

                                        <!--数据列表-->
                                        <f:table collection="${userInfoList}" except="id" />
                                        <!--数据列表/-->
                                    </div>
                                    <!-- 数据表格 /-->
                                </div>
                            </div>
                            <!-- 查询结果面板 /-->
                        </div>
                    </div>
                    <!-- /.box-body -->

                    <!-- .box-footer-->
                    <div class="box-footer">
                        <div class="pull-left">
                        </div>

                        <div class="box-tools pull-right">
                            <ul class="pagination">
                                <alte:paginate total="${userInfoCount ?: 0}" />
                            </ul>
                        </div>

                    </div>
                    <!-- /.box-footer-->
                </div>

            </section>
            <!-- 正文区域 /-->

        </div>
        <!-- /.content-wrapper -->
        <!-- 内容区域 /-->
    </body>
</html>