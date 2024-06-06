// import uriRoleGroup from './i18n-uriRoleGroup'
// assignLanguage(uriRoleGroup)
const uriRoleGroup = {
  zh: {
    route: {
      uriRoleGroup: 'au_uri_role_group表：基于大角色的接口（服务）授权'
    },
    domain: {
      uriRoleGroup: {
        pageTitle: 'au_uri_role_group表：基于大角色的接口（服务）授权',
        uriId: 'URI资源ID',
        uri: 'URI资源ID',
        roleGroupId: '大角色ID',
        annotation: '注解配置信息。如果此配置是来源于FromTo.SOURCECODE，则此值不为空',
        group1: '分组。同一组内多个条件，满足任一条件则此组为true；多组条件，任一组是false，则最终结果为false。 值的范围1~99',
        fromTo: '来源，0人工、1程序源代码。如果是来着程序源代码，则只能禁用，不能删除',
        status: '状态，0启用、1禁用'
      }
    }
  },
  en: {
    route: {
      uriRoleGroup: 'UriRoleGroup'
    },
    domain: {
      uriRoleGroup: {
        pageTitle: 'UriRoleGroup',
        uriId: 'UriId',
        uri: 'Uri',
        roleGroupId: 'RoleGroupId',
        annotation: 'Annotation',
        group1: 'Group1',
        fromTo: 'FromTo',
        status: 'Status'
      }
    }
  }
}
export default uriRoleGroup
