<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="adminLTE" />
        <g:set var="entityName"
               value="${message(code: 'org.label', default: 'Org')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="/themes/menuAll" model="['openMenu':'Orgs', 'activeMenu':'OrgList']"/>

        <!-- 内容区域 -->
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">

            <!-- 内容头部 -->
            <section class="content-header">
                <h1>
                    <g:message code="org.list.label" args="[]" default="Orgs" />
                    <small>XXX<g:message code="default.list.label" args="[entityName]" /></small>
                </h1>
                <ol class="breadcrumb">
		    <i class="fa fa-dashboard"></i>
		    <li>&nbsp <g:link controller="dashboard">
			    <g:message code="home.label" default="Home" />
			</g:link></li>
			<li><g:message code="orgs.label" default="Orgs" /></li>
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

                        <div class="box-group" id="accordion">
                            <!-- 搜索条件面板 -->
                            <g:form name="search" controller="org" action="index" method="PUT" class="form-horizontal">
                                <div class="box box-primary">
                                    <div class="box-header with-border">
                                        <h3 class="box-title"><g:message code="default.label.query.criteria" default="Query Criteria" /></h3>
                                        <div class="box-tools pull-right">
                                            <button type="button" class="btn btn-box-tool" data-widget="collapse"><i class="fa fa-minus"></i></button>
                                            <button type="button" class="btn btn-box-tool" data-widget="remove"><i class="fa fa-times"></i></button>
                                        </div>
                                    </div>
                                    <div class="box-body">
                                        <div class="table-box">
                                            <div class="col-sm-11 pull-left">
                                                <div class="form-group col-sm-6">
                                                    <label class="col-sm-3 control-label"><div class="pull-right">
                                                        <g:message code="com.easecurity.admin.core.b.Org.name" default="Name" /></div></label>
                                                    <div class="col-sm-9 left"><div class="col-lg-pull-12">
                                                        <g:field type="search" name="search.name" value="${params.search?.name}" class="form-control"/>
                                                    </div></div>
                                                </div>
                                                <div class="form-group col-sm-6">
                                                    <label class="col-sm-3 control-label"><div class="pull-right">
                                                        <g:message code="com.easecurity.admin.core.b.Org.code" default="Code" /></div></label>
                                                    <div class="col-sm-9 left"><div class="col-lg-pull-12">
                                                        <g:field type="search" name="search.code" value="${params.search?.code}" class="form-control"/>
                                                    </div></div>
                                                </div>
                                            </div>
                                            <div class="pull-right">
                                                <div class="has-feedback">
                                                    <a class="btn btn-default" onclick="return $g.searchListByLnk(this)"><span class="glyphicon glyphicon-search has-feedback"></span></a>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </g:form>
                            <!-- 搜索条件面板 /-->

                            <!-- 查询结果面板 -->
                            <div class="box">
                                <div class="box-header">
                                    <div class="pull-left"><h3 class="box-title"><g:message code="default.label.query.results" default="Query Results" /></h3></div>
                                    <!-- 工具栏 -->
                                    <div class="box-tools pull-right">
                                        <div class="form-group form-inline">
                                            <div class="btn-group">
                                                <g:form name="orgDelete" controller="org" action="delete" method="DELETE">

                                                    <g:link class="btn btn-default" action="create">
                                                        <g:message code="default.button.create.label" default="Create" />
                                                    </g:link>
                                                    <input class="btn btn-danger" type="submit"
                                                           value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                                           onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}')?deleteAll(this):false" />
                                                    <g:link class="btn btn-default" controller="org">
                                                        <g:message code="default.button.reload.label" default="Reload" />
                                                    </g:link>
                                                </g:form>
                                            </div>
                                        </div>
                                    </div>
                                    <!-- 工具栏 /-->
                                </div>
                                <div class="box-body">
                                    <!-- 数据表格 -->
                                    <div class="table-box">
                                        <!--数据列表-->
                                        <f:table collection="${orgList}" except="id" />
                                        <!--数据列表/-->
                                    </div>
                                    <!-- 数据表格 /-->
                                </div>

                                <!-- .box-footer-->
                                <div class="box-footer">
                                    <div class="pull-left">
                                    </div>

                                    <div class="box-tools pull-right">
                                        <ul class="pagination">
                                            <s:paginate total="${orgCount ?: 0}" mapping='["onclick":"return \$g.searchListByLnk(this)"]'/>
                                        </ul>
                                    </div>

                                </div>
                                <!-- /.box-footer-->
                            </div>
                            <!-- 查询结果面板 /-->
                        </div>

            </section>
            <!-- 正文区域 /-->

        </div>
        <!-- /.content-wrapper -->
        <!-- 内容区域 /-->
    </body>
</html>