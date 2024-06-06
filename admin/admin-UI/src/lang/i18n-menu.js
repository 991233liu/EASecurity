// import menu from './i18n-menu'
// assignLanguage(menu)
const menu = {
  zh: {
    route: {
      menu: 're_menu表：菜单'
    },
    domain: {
      menu: {
        pageTitle: 're_menu表：菜单',
        name: '菜单名称',
        code: '菜单编码',
        level: '菜单级别',
        sortNumber: '显示顺序',
        openUrl: '打开链接URL',
        icon: '图标',
        parentId: '上级菜单ID',
        parent: '上级菜单ID',
        fullName: '全路径名称，如：默认菜单/系统管理/用户管理/',
        status: '状态，0启用，1禁用',
        displayStatus: '显示状态，0始终显示、1始终隐藏、2禁用隐藏、3、无权限隐藏',
        disablePrompt: '禁用提示',
        noPermissionsPrompt: '无权限提示',
        accessType: '访问类型，0匿名访问、1登录用户访问、2授权访问'
      }
    }
  },
  en: {
    route: {
      menu: 'Menu'
    },
    domain: {
      menu: {
        pageTitle: 'Menu',
        name: 'Name',
        code: 'Code',
        level: 'Level',
        sortNumber: 'SortNumber',
        openUrl: 'OpenUrl',
        icon: 'Icon',
        parentId: 'ParentId',
        parent: 'Parent',
        fullName: 'FullName',
        status: 'Status',
        displayStatus: 'DisplayStatus',
        disablePrompt: 'DisablePrompt',
        noPermissionsPrompt: 'NoPermissionsPrompt',
        accessType: 'AccessType'
      }
    }
  }
}
export default menu
