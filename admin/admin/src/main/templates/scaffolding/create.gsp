<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="adminLTE" />
        <g:set var="entityName"
               value="\${message(code: '${propertyName}.label', default: '${className}')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <g:render template="/themes/menuAll" model="['openMenu':'${className}s', 'activeMenu':'List']"/>

        <!-- 内容区域 -->
        <!-- Content Wrapper. Contains page content -->
        <div class="content-wrapper">

            <!-- 内容头部 -->
            <section class="content-header">
                <h1>
                    <g:message code="${propertyName}.list.label" args="${['']}" default="${className}s" />
                    <small>XXX<g:message code="default.create.label" args="[entityName]" /></small>
                </h1>
                <ol class="breadcrumb">
                    <i class="fa fa-dashboard"></i>
                    <li>&nbsp <g:link controller="dashboard">
                        <g:message code="home.label" default="Home" />
                    </g:link></li>
                    <li class="breadcrumb-item"><g:link controller="${propertyName}">
                        <g:message code="${propertyName}s.label" default="${className}s" />
                    </g:link></li>
                    <li class="breadcrumb-item active"><g:message
                            code="${propertyName}.label" default="${className}" /></li>
                </ol>
            </section>
            <!-- 内容头部 /-->

            <!--信息提示栏-->
            <g:if test="\${flash.message || this.${propertyName}.hasErrors()}"><div class="pad margin no-print">
                <g:if test="\${flash.message}">
                    <div class="callout callout-info" style="margin-bottom: 0!important;">
                        <h5><i class="fa fa-info"></i> Info!</h5>
                        \${flash.message}
                    </div>
                </g:if>
                <g:hasErrors bean="\${this.${propertyName}}">
                    <div class="callout callout-danger" style="margin-bottom: 0!important;">
                        <g:eachError bean="\${this.${propertyName}}" var="error">
                            <li <g:if
                                        test="\${error in org.springframework.validation.FieldError}">data-field-id="\${error.field}"</g:if>>
                                <g:message error="\${error}" /></li>
                        </g:eachError>
                    </div>
                </g:hasErrors>
            </div></g:if>
            <!-- 信息提示栏 /-->

            <!-- 正文区域 -->
            <section class="content"><g:form resource="\${this.${propertyName}}" method="POST">
                <div class="box box-primary">
                    <div class="box-body">

                        <!--tab内容-->
                        <div class="tab-content">

                            <!--数据展示区-->
                            <div class="tab-pane active" id="tab-form">
                                <div class="row data-type">
                                    <!--两列展示-->
                                    <f:all bean="${propertyName}" />
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
                                    <g:submitButton name="create" class="btn bg-blue"
                                                    value="\${message(code: 'default.button.create.label', default: 'Create')}" />
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- /.box-footer-->
                </div>
            </g:form></section>
            <!-- 正文区域 /-->

        </div>
        <!-- /.content-wrapper -->
        <!-- 内容区域 /-->
    </body>
</html>