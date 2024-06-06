/** When your routing table is too long, you can split it into small modules**/
import Layout from '@/layout'
import componentsRouter from './demo/components'
import chartsRouter from './demo/charts'
import tableRouter from './demo/table'
import nestedRouter from './demo/nested'

const demoRouter = {
  path: '/demo',
  component: Layout,
  redirect: 'noRedirect',
  name: 'demo',
  meta: {
    title: 'systemDemo',
    icon: 'component',
    roles: ['ROLE_GROUP_admin']
  },
  children: [
    componentsRouter,
    chartsRouter,
    nestedRouter,
    tableRouter,
    {
      path: '/documentation',
      component: { render: (e) => e('router-view') },
      meta: {
        roles: ['ROLE_GROUP_admin', 'editor'] // you can set roles in root nav
      },
      children: [
        {
          path: 'index',
          component: () => import('@/views/demo/documentation/index'),
          name: 'Documentation',
          meta: { title: 'documentation', icon: 'documentation', affix: true }
        }
      ]
    },
    {
      path: '/guide',
      component: { render: (e) => e('router-view') },
      redirect: '/guide/index',
      children: [
        {
          path: 'index',
          component: () => import('@/views/demo/guide/index'),
          name: 'Guide',
          meta: { title: 'guide', icon: 'guide', noCache: true }
        }
      ]
    },
    {
      path: '/permission',
      component: { render: (e) => e('router-view') },
      redirect: '/permission/page',
      alwaysShow: true, // will always show the root menu
      name: 'Permission',
      meta: {
        title: 'permission',
        icon: 'lock',
        roles: ['ROLE_GROUP_admin', 'editor'] // you can set roles in root nav
      },
      children: [
        {
          path: 'page',
          component: () => import('@/views/demo/permission/page'),
          name: 'PagePermission',
          meta: {
            title: 'pagePermission',
            roles: ['ROLE_GROUP_admin'] // or you can only set roles in sub nav
          }
        },
        {
          path: 'directive',
          component: () => import('@/views/demo/permission/directive'),
          name: 'DirectivePermission',
          meta: {
            title: 'directivePermission'
            // if do not set roles, means: this page does not require permission
          }
        },
        {
          path: 'role',
          component: () => import('@/views/demo/permission/role'),
          name: 'RolePermission',
          meta: {
            title: 'rolePermission',
            roles: ['ROLE_GROUP_admin']
          }
        }
      ]
    },

    {
      path: '/icon',
      component: { render: (e) => e('router-view') },
      children: [
        {
          path: 'index',
          component: () => import('@/views/demo/icons/index'),
          name: 'Icons',
          meta: { title: 'icons', icon: 'icon', noCache: true }
        }
      ]
    },
    {
      path: '/example',
      component: { render: (e) => e('router-view') },
      redirect: '/example/list',
      name: 'Example',
      meta: {
        title: 'example',
        icon: 'el-icon-s-help'
      },
      children: [
        {
          path: 'create',
          component: () => import('@/views/demo/example/create'),
          name: 'CreateArticle',
          meta: { title: 'createArticle', icon: 'edit' }
        },
        {
          path: 'edit/:id(\\d+)',
          component: () => import('@/views/demo/example/edit'),
          name: 'EditArticle',
          meta: { title: 'editArticle', noCache: true, activeMenu: '/example/list' },
          hidden: true
        },
        {
          path: 'list',
          component: () => import('@/views/demo/example/list'),
          name: 'ArticleList',
          meta: { title: 'articleList', icon: 'list' }
        }
      ]
    },

    {
      path: '/tab',
      component: { render: (e) => e('router-view') },
      children: [
        {
          path: 'index',
          component: () => import('@/views/demo/tab/index'),
          name: 'Tab',
          meta: { title: 'tab', icon: 'tab' }
        }
      ]
    },

    {
      path: '/error',
      component: { render: (e) => e('router-view') },
      redirect: 'noRedirect',
      name: 'ErrorPages',
      meta: {
        title: 'errorPages',
        icon: '404'
      },
      children: [
        {
          path: '401',
          component: () => import('@/views/error-page/401'),
          name: 'Page401',
          meta: { title: 'page401', noCache: true }
        },
        {
          path: '404',
          component: () => import('@/views/error-page/404'),
          name: 'Page404',
          meta: { title: 'page404', noCache: true }
        }
      ]
    },

    {
      path: '/error-log',
      component: { render: (e) => e('router-view') },
      children: [
        {
          path: 'log',
          component: () => import('@/views/error-log/index'),
          name: 'ErrorLog',
          meta: { title: 'errorLog', icon: 'bug' }
        }
      ]
    },

    {
      path: '/excel',
      component: { render: (e) => e('router-view') },
      redirect: '/excel/export-excel',
      name: 'Excel',
      meta: {
        title: 'excel',
        icon: 'excel'
      },
      children: [
        {
          path: 'export-excel',
          component: () => import('@/views/demo/excel/export-excel'),
          name: 'ExportExcel',
          meta: { title: 'exportExcel' }
        },
        {
          path: 'export-selected-excel',
          component: () => import('@/views/demo/excel/select-excel'),
          name: 'SelectExcel',
          meta: { title: 'selectExcel' }
        },
        {
          path: 'export-merge-header',
          component: () => import('@/views/demo/excel/merge-header'),
          name: 'MergeHeader',
          meta: { title: 'mergeHeader' }
        },
        {
          path: 'upload-excel',
          component: () => import('@/views/demo/excel/upload-excel'),
          name: 'UploadExcel',
          meta: { title: 'uploadExcel' }
        }
      ]
    },

    {
      path: '/zip',
      component: { render: (e) => e('router-view') },
      redirect: '/zip/download',
      alwaysShow: true,
      name: 'Zip',
      meta: { title: 'zip', icon: 'zip' },
      children: [
        {
          path: 'download',
          component: () => import('@/views/demo/zip/index'),
          name: 'ExportZip',
          meta: { title: 'exportZip' }
        }
      ]
    },

    {
      path: '/pdf',
      component: { render: (e) => e('router-view') },
      redirect: '/pdf/index',
      children: [
        {
          path: 'index',
          component: () => import('@/views/demo/pdf/index'),
          name: 'PDF',
          meta: { title: 'pdf', icon: 'pdf' }
        }
      ]
    },
    {
      path: '/pdf/download',
      component: () => import('@/views/demo/pdf/download'),
      hidden: true
    },

    {
      path: '/theme',
      component: { render: (e) => e('router-view') },
      children: [
        {
          path: 'index',
          component: () => import('@/views/demo/theme/index'),
          name: 'Theme',
          meta: { title: 'theme', icon: 'theme' }
        }
      ]
    },

    {
      path: '/clipboard',
      component: { render: (e) => e('router-view') },
      children: [
        {
          path: 'index',
          component: () => import('@/views/demo/clipboard/index'),
          name: 'ClipboardDemo',
          meta: { title: 'clipboardDemo', icon: 'clipboard' }
        }
      ]
    },

    {
      path: '/i18n',
      component: { render: (e) => e('router-view') },
      children: [
        {
          path: 'index',
          component: () => import('@/views/demo/i18n-demo/index'),
          name: 'I18n',
          meta: { title: 'i18n', icon: 'international' }
        }
      ]
    },

    {
      path: 'external-link',
      component: { render: (e) => e('router-view') },
      children: [
        {
          path: 'https://gitee.com/liulufeng/easecurity',
          meta: { title: 'externalLink', icon: 'link' }
        }
      ]
    }
  ]
}

export default demoRouter
