
        <!-- 导航侧栏 -->
        <aside class="main-sidebar">
            <!-- sidebar: style can be found in sidebar.less -->
            <section class="sidebar">
                <!-- Sidebar user panel -->
                <div class="user-panel">
                    <div class="pull-left image">
			<asset:image src="img/user2-160x160.jpg" class="img-circle" alt="User Image" />
                    </div>
                    <div class="pull-left info">
                        <p>
			<sec:ifLoggedIn>
				<sec:username />
			</sec:ifLoggedIn>
			</p>
                        <a href="#"><i class="fa fa-circle text-success"></i> 在线</a>
                    </div>
                </div>
                <!-- search form -->
                <!--<form action="#" method="get" class="sidebar-form">
            <div class="input-group">
                <input type="text" name="q" class="form-control" placeholder="搜索...">
                <span class="input-group-btn">
                <button type="submit" name="search" id="search-btn" class="btn btn-flat"><i class="fa fa-search"></i>
                </button>
              </span>
            </div>
        </form>-->
                <!-- /.search form -->

                <!-- sidebar menu: : style can be found in sidebar.less -->
                <ul class="sidebar-menu">
                    <li class="header">菜单</li>

                    <li id="admin-index" class="${'Dashboard'==openMenu?'active':''}">
                        <g:link controller="dashboard">
                            <i class="fa fa-dashboard"></i>
                            <span><g:message code="home.label" default="Dashboard" /></span>
                        </g:link>
                    </li>

                    <sec:access expression="hasRole('ROLE_ADMIN2222')">
                        <li class="treeview ${'Animals'==openMenu?'active':''}">
                            <a href="#">
                                <i class="fa fa-cogs"></i>
                                <span><g:message code="animals.label" default="Animals" /></span>
                                <span class="pull-right-container">
                                    <i class="fa fa-angle-left pull-right"></i>
                                </span>
                            </a>
                            <ul class="treeview-menu">
                                <li>
                                    <g:link controller="animal">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="default.list.label" args="${['']}"
                                                   default="List" />
                                    </g:link>
                                </li>
                                <li>
                                    <g:link controller="animal" action="create">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="default.add.label" args="${['']}"
                                                   default="Create" />
                                    </g:link>
                                </li>

                            </ul>
                        </li>

                        <li class="treeview ${'DomainA1s'==openMenu?'active':''}">
                            <a href="#">
                                <i class="fa fa-edit"></i>
                                <span>
                                    <g:message code="animals.label" default="DomainA1s" />
                                </span>
                                <span class="pull-right-container">
                                    <i class="fa fa-angle-left pull-right"></i>
                                </span>
                            </a>
                            <ul class="treeview-menu">
                                <li class="${('DomainA1s'==openMenu&'List'==activeMenu)?'active':''}">
                                    <g:link controller="domainA1">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="default.list.label" args="${['']}"
                                                   default="List" />
                                    </g:link>
                                </li>
                                <li class="${('DomainA1s'==openMenu&'Create'==activeMenu)?'active':''}">
                                    <g:link controller="domainA1" action="create">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="default.add.label" args="${['']}"
                                                   default="Create" />
                                    </g:link>
                                </li>
                            </ul>
                        </li>

                        <li class="treeview ${'Users'==openMenu?'active':''}">
                            <a href="#">
                                <i class="fa fa-cogs"></i>
                                <span>
                                    <g:message code="users.label" default="Users" />
                                </span>
                                <span class="pull-right-container">
                                    <i class="fa fa-angle-left pull-right"></i>
                                </span>
                            </a>
                            <ul class="treeview-menu">
                                <li class="${('Users'==openMenu&'List'==activeMenu)?'active':''}">
                                    <g:link controller="user">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="user.list.label" args="${['']}"
                                                   default="List" />
                                    </g:link>
                                </li>
                                <li class="${('Users'==openMenu&'LdapUserList'==activeMenu)?'active':''}">
                                    <g:link controller="ldapUser">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="ldapUser.list.label" args="${['']}"
                                                   default="LdapUserList" />
                                    </g:link>
                                </li>
                            </ul>
                        </li>
                    </sec:access>

                    <sec:access expression="hasRole('ROLE_PMO')">
                        <li class="treeview ${'Staffs'==openMenu?'active':''}">
                            <a href="#">
                                <i class="fa fa-user"></i>
                                <span>
                                    <g:message code="staffs.label" default="Staffs" />
                                </span>
                                <span class="pull-right-container">
                                    <i class="fa fa-angle-left pull-right"></i>
                                </span>
                            </a>
                            <ul class="treeview-menu">
                                <li class="${('Staffs'==openMenu&'staffList'==activeMenu)?'active':''}">
                                    <g:link controller="staff">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="staff.list.label" args="${['']}"
                                                   default="List" />
                                    </g:link>
                                </li>
                                <li class="${('Staffs'==openMenu&'staffCostList'==activeMenu)?'active':''}">
                                    <g:link controller="staffCost">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="staffCost.list.label" args="${['']}"
                                                   default="StaffCost" />
                                    </g:link>
                                </li>
                            </ul>
                        </li>
                    </sec:access>

                    <sec:access expression="hasRole('ROLE_PMO')||hasRole('ROLE_PM')">
                        <li class="treeview ${'Projects'==openMenu?'active':''}">
                            <a href="#">
                                <i class="fa fa-anchor"></i>
                                <span>
                                    <g:message code="projects.label" default="Projects" />
                                </span>
                                <span class="pull-right-container">
                                    <i class="fa fa-angle-left pull-right"></i>
                                </span>
                            </a>
                            <ul class="treeview-menu">
                                <li class="${('Projects'==openMenu&'projectList'==activeMenu)?'active':''}">
                                    <g:link controller="project">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="project.list.label" args="${['']}"
                                                   default="List" />
                                    </g:link>
                                </li>
                                <li class="${('Projects'==openMenu&'projectJiraList'==activeMenu)?'active':''}">
                                    <g:link controller="projectJira">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="projectJira.list.label" args="${['']}"
                                                   default="ProjectJira" />
                                    </g:link>
                                </li>
                                <li class="${('Projects'==openMenu&'projectRoleList'==activeMenu)?'active':''}">
                                    <g:link controller="projectRole">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="projectRole.list.label" args="${['']}"
                                                   default="ProjectRole" />
                                    </g:link>
                                </li>
                                <li class="${('Projects'==openMenu&'staffProjectRoleList'==activeMenu)?'active':''}">
                                    <g:link controller="staffProjectRole">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="staffProjectRole.list.label" args="${['']}"
                                                   default="StaffProjectRole" />
                                    </g:link>
                                </li>
                            </ul>
                        </li>
                    </sec:access>

                    <li class="treeview ${'WeeklyReports'==openMenu?'active':''}">
                        <a href="#">
                            <i class="fa fa-files-o"></i>
                            <span>
                                <g:message code="weeklyReports.label" default="WeeklyReports" />
                            </span>
                            <span class="pull-right-container">
                                <i class="fa fa-angle-left pull-right"></i>
                            </span>
                        </a>
                        <ul class="treeview-menu">
                            <li class="${('WeeklyReports'==openMenu&'dailyReportList'==activeMenu)?'active':''}">
                                <g:link controller="dailyReport" >
                                    <i class="fa fa-circle-o"></i>
                                    <g:message code="dailyReport.list.label" args="${['']}"
                                               default="DailyReport" />
                                </g:link>
                            </li>
                            <li class="${('WeeklyReports'==openMenu&'weeklyReportList'==activeMenu)?'active':''}">
                                <g:link controller="weeklyReport">
                                    <i class="fa fa-circle-o"></i>
                                    <g:message code="weeklyReport.list.label" args="${['']}"
                                               default="WeeklyReport" />
                                </g:link>
                            </li>
                            <sec:access expression="hasRole('ROLE_PM')">
                                <li class="${('WeeklyReports'==openMenu&'weeklyReportVetting'==activeMenu)?'active':''}">
                                    <g:link controller="weeklyReport" action="vetting">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="weeklyReport.vetting.label" args="${['']}"
                                                   default="WeeklyReportVetting" />
                                    </g:link>
                                </li>
                            </sec:access>
                            <sec:access expression="hasRole('ROLE_PMO')">
                                <li class="${('WeeklyReports'==openMenu&'weeklyReportPMO'==activeMenu)?'active':''}">
                                    <g:link controller="weeklyReport" action="indexpmo">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="weeklyReport.pmo.label" args="${['']}"
                                                   default="WeeklyReport(PMO)" />
                                    </g:link>
                                </li>
                            </sec:access>
                        </ul>
                    </li>
                </ul>
            </section>
            <!-- /.sidebar -->
        </aside>
        <!-- 导航侧栏 /-->
