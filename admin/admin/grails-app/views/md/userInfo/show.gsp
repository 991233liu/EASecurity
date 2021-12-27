<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="adminLTE" />
        <g:set var="entityName"
               value="${message(code: 'userInfo.label', default: 'UserInfo')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="/themes/menuAll" model="['openMenu':'UserInfos', 'activeMenu':'List']"/>

        <!-- 内容区域 -->
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">

            <!-- 内容头部 -->
            <section class="content-header">
                <h1>
                    <g:message code="userInfo.list.label" args="[]" default="UserInfos" />
                    <small>XXX<g:message code="default.show.label" args="[entityName]" /></small>
                </h1>
                <ol class="breadcrumb">
                    <i class="fa fa-dashboard"></i>
                    <li>&nbsp <g:link controller="dashboard">
                        <g:message code="home.label" default="Home" />
                    </g:link></li>
                    <li class="breadcrumb-item"><g:link controller="userInfo">
                        <g:message code="userInfos.label" default="UserInfos" />
                    </g:link></li>
                    <li class="breadcrumb-item active"><g:message
                            code="userInfo.label" default="UserInfo" /></li>
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
                <div class="box box-primary">
                    <div class="box-body">

                        <!--tab内容-->
                        <div class="tab-content">

                            <!--数据展示区-->
                            <div class="tab-pane active" id="tab-form">
                                <div class="row data-type">
                                    <!--两列展示-->
                                    <f:display bean="userInfo" except="id" />
                                </div>
                            </div>
                            <!--数据展示区/-->

                        </div>
                        <!--tab内容/-->

                    </div>
                    <!-- .box-footer-->
                    <div class="box-footer">
                        <div class="pull-left">
                            <div class="form-group form-inline">
                                <div class="btn-group">
                                    <g:form resource="${this.userInfo}" method="DELETE">
                                        <g:link class="btn bg-blue" action="edit" resource="${this.userInfo}">
                                            <g:message code="default.button.edit.label" default="Edit" />
                                        </g:link>
                                        <input class="btn bg-maroon" type="submit"
                                               value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                               onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
                                    </g:form>
                                </div>
                            </div>
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
