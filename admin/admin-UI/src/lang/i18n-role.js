// import role from './i18n-role'
// assignLanguage(role)
const role = {
  zh: {
    route: {
      role: 'r_role表：小角色信息。'
    },
    domain: {
      role: {
        pageTitle: 'r_role表：小角色信息。',
        name: '角色名称',
        code: '小角色编码=组织编码+\'#\'+大角色编码',
        fullName: '小角色全称=组织名称+角色名称',
        roleGroupId: '大角色ID',
        roleGroup: '大角色ID',
        orgId: '组织ID',
        org: '组织ID'
      }
    }
  },
  en: {
    route: {
      role: 'Role'
    },
    domain: {
      role: {
        pageTitle: 'Role',
        name: 'Name',
        code: 'Code',
        fullName: 'FullName',
        roleGroupId: 'RoleGroupId',
        roleGroup: 'RoleGroup',
        orgId: 'OrgId',
        org: 'Org'
      }
    }
  }
}
export default role
