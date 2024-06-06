/** When your routing table is too long, you can split it into small modules**/

import Layout from '@/layout'

const basisRouter = [
  {
    path: '/b',
    component: Layout,
    redirect: 'noRedirect',
    name: 'b',
    meta: {
      title: 'b',
      icon: 'user2',
      roles: ['ROLE_GROUP_admin']
    },
    children: [
      {
        path: 'Org',
        component: () => import('@/views/md/org/index'),
        name: 'Org',
        meta: {
          title: 'org',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'OrgUser',
        component: () => import('@/views/md/orgUser/index'),
        name: 'OrgUser',
        meta: {
          title: 'orgUser',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'Posts',
        component: () => import('@/views/md/posts/index'),
        name: 'Posts',
        meta: {
          title: 'posts',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'User',
        component: () => import('@/views/md/user/index'),
        name: 'User',
        meta: {
          title: 'user',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'UserInfo',
        component: () => import('@/views/md/userInfo/index'),
        name: 'UserInfo',
        meta: {
          title: 'userInfo',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      }
    ]
  },
  {
    path: '/r',
    component: Layout,
    redirect: 'noRedirect',
    name: 'r',
    meta: {
      title: 'r',
      icon: 'role',
      roles: ['ROLE_GROUP_admin']
    },
    children: [
      {
        path: 'Role',
        component: () => import('@/views/md/role/index'),
        name: 'Role',
        meta: {
          title: 'role',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'RoleGroup',
        component: () => import('@/views/md/roleGroup/index'),
        name: 'RoleGroup',
        meta: {
          title: 'roleGroup',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'RoleUser',
        component: () => import('@/views/md/roleUser/index'),
        name: 'RoleUser',
        meta: {
          title: 'roleUser',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      }
    ]
  },
  {
    path: '/re',
    component: Layout,
    redirect: 'noRedirect',
    name: 're',
    meta: {
      title: 're',
      icon: 'resource',
      roles: ['ROLE_GROUP_admin']
    },
    children: [
      {
        path: 'Menu',
        component: () => import('@/views/md/menu/index'),
        name: 'Menu',
        meta: {
          title: 'menu',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'Uri',
        component: () => import('@/views/md/uri/index'),
        name: 'Uri',
        meta: {
          title: 'uri',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      }
    ]
  },
  {
    path: '/au',
    component: Layout,
    redirect: 'noRedirect',
    name: 'au',
    meta: {
      title: 'au',
      icon: 'authorization',
      roles: ['ROLE_GROUP_admin']
    },
    children: [
      {
        path: 'UriIp',
        component: () => import('@/views/md/uriIp/index'),
        name: 'UriIp',
        meta: {
          title: 'uriIp',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'UriOrg',
        component: () => import('@/views/md/uriOrg/index'),
        name: 'UriOrg',
        meta: {
          title: 'uriOrg',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'UriPosts',
        component: () => import('@/views/md/uriPosts/index'),
        name: 'UriPosts',
        meta: {
          title: 'uriPosts',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'UriRole',
        component: () => import('@/views/md/uriRole/index'),
        name: 'UriRole',
        meta: {
          title: 'uriRole',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'UriRoleGroup',
        component: () => import('@/views/md/uriRoleGroup/index'),
        name: 'UriRoleGroup',
        meta: {
          title: 'uriRoleGroup',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      },
      {
        path: 'UriUser',
        component: () => import('@/views/md/uriUser/index'),
        name: 'UriUser',
        meta: {
          title: 'uriUser',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      }
    ]
  }
]

export default basisRouter
