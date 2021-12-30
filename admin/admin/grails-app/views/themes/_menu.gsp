
                    <li class="header">菜单</li>

                    <li id="Home">
                        <g:link controller="home">
                            <i class="fa fa-dashboard"></i>
                            <span><g:message code="home.label" default="Home" /></span>
                        </g:link>
                    </li>

                    <sec:access expression="hasRole('ROLE_root#admin')">
%{--                        <li class="treeview ${'Users'==openMenu?'active':''}">--}%
                        <li id="Users" class="treeview">
                            <a href="#">
                                <i class="fa fa-cogs"></i>
                                <span><g:message code="users.label" default="Users" /></span>
                                <span class="pull-right-container">
                                    <i class="fa fa-angle-left pull-right"></i>
                                </span>
                            </a>
                            <ul class="treeview-menu">
                                <li id="UserList">
                                    <g:link controller="user">
                                        <i class="fa fa-circle-o"></i>
                                        <g:message code="default.list.label" args="${['']}"
                                                   default="List" />
                                    </g:link>
                                </li>
                            </ul>
                        </li>
                    </sec:access>

                    <li id="WeeklyReports" class="treeview">
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
                            <li id="DailyReport">
                                <g:link controller="dailyReport" >
                                    <i class="fa fa-circle-o"></i>
                                    <g:message code="dailyReport.list.label" args="${['']}"
                                               default="DailyReport" />
                                </g:link>
                            </li>
                        </ul>
                    </li>
<script>
    menuObj.openActiveMenu('${openMenu}','${activeMenu}');
</script>