/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout'

const systemSettingsRouter = {
  path: '/SystemSettings',
  component: Layout,
  redirect: 'noRedirect',
  name: 'SystemSettings',
  alwaysShow: true, // will always show the root menu
  meta: {
    title: 'systemSettings',
    icon: 'setting',
    roles: ['ROLE_GROUP_admin']
  },
  children: [
    {
      path: 'UserAdmin',
      component: () => import('@/views/md/userAdmin/index'),
      name: 'UserAdmin',
      meta: {
        title: 'userAdmin',
        icon: 'el-icon-d-arrow-right',
        roles: ['ROLE_GROUP_admin']
      }
    }
  ]
}

export default systemSettingsRouter
