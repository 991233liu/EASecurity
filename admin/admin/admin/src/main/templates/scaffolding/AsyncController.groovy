<%=packageName ? "package ${packageName}" : ''%>
<%
List  expand = []
expand.addAll(hasMany*.name)
expand.addAll(hasOne*.name)
String expandStr = expand.isEmpty() ? "" : '"'+expand.join('","')+'"'
%>
class ${className}Controller {
    static scaffold = ${className}
    static expand = [${expandStr}]

}
