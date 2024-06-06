/* 后台系统中不会修改的enum类。此类可极大减轻前后台系统压力 */
const enumClazz = {
  'com.bmo.admin.AdminUserEnum': [
    {
      'code': 'ENABLED',
      'title': '启用'
    },
    {
      'code': 'DISABLED',
      'title': '禁用'
    },
    {
      'code': 'DEREGISTRATION',
      'title': '注销'
    },
    {
      'code': 'DEREGISTRATION',
      'title': '删除'
    }
  ]
}

export default enumClazz
