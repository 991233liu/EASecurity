// import ${propertyName} from './i18n-${propertyName}'
// assignLanguage(${propertyName})
const ${propertyName} = {
  zh: {
    route: {
      ${propertyName}: '${i18n.pageTitle}'
    },
    domain: {
      ${propertyName}: {
<%
        i18n.eachWithIndex { p, i ->
%>        ${p.key}: '${p.value}'${i<i18n.size()-1?',':''}
<%
        }
%>      }
    }
  },
  en: {
    route: {
      ${propertyName}: '${className}'
    },
    domain: {
      ${propertyName}: {
<%
        i18n.eachWithIndex { p, i ->
          if (p.key.equals('pageTitle')) {
%>        ${p.key}: '${className}',
<%
          } else {
%>        ${p.key}: '${org.apache.commons.lang3.StringUtils.capitalize(p.key)}'${i<i18n.size()-1?',':''}
<%
          }
        }
%>      }
    }
  }
}
export default ${propertyName}
