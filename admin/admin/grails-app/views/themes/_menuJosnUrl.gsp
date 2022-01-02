<script>
    <%
      List menuTree = com.easecurity.admin.utils.ServletUtils.getSession()?.getAttribute('allMenuTree')?.get('children')
    %>
    <g:if test="${(menuTree!=null && !menuTree.isEmpty())}" >
        var menuJson = $.parseHTML("${menuTree as grails.converters.JSON}")[0];
        menuJson = $.parseJSON(menuJson.wholeText);
        menuObj.loadMenu(menuJson, '${activeMenu}');
    </g:if><g:else>
        menuObj.loadMenuUrl("/admin/login/allMenu", '${activeMenu}');
    </g:else>

</script>