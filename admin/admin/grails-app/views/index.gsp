<!doctype html>
<html>
<head>
    <meta name="layout" content="blank"/>
    <title>Welcome to Animals</title>
</head>
<body>
<!-- 内容区域 -->
<!-- Content Wrapper. Contains page content -->
<div class="content">

    <div class="svg" role="presentation">
        <div class="grails-logo-container">
            <!-- <asset:image src="animals.svg" class="grails-logo"/>  -->
        </div>
    </div>

    <div id="content" role="main">
        <section class="row colset-2-its">
            %{
                def user  = com.easecurity.admin.utils.ServletUtils.getCurrentUser()
            }%
            <h1>Welcome to Animals -${user}-${user.username}-${user.password}-${grails.plugin.springsecurity.SpringSecurityUtils.getPrincipalAuthorities()}-</h1>
            <p>
            </p>

            <div id="controllers" role="navigation">
                <h2>Available Controllers:</h2>
                <ul>
                    <g:each var="c" in="${grailsApplication.controllerClasses.sort { it.fullName } }">
                        <li class="controller">
                            <g:link controller="${c.logicalPropertyName}">${c.fullName}</g:link>
                        </li>
                    </g:each>
                </ul>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">Artefacts <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="#">Controllers: ${grailsApplication.controllerClasses.size()}</a></li>
                        <li><a href="#">Domains: ${grailsApplication.domainClasses.size()}</a></li>
                        <li><a href="#">Services: ${grailsApplication.serviceClasses.size()}</a></li>
                        <li><a href="#">Tag Libraries: ${grailsApplication.tagLibClasses.size()}</a></li>
                    </ul>
                </li>
            </div>
        </section>
    </div>
</div>
</body>
</html>
