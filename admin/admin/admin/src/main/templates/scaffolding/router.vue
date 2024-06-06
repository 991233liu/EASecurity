      {
        path: '${className}',
        component: () => import('${mdView}/${propertyName}/index'),
        name: '${className}',
        meta: {
          title: '${propertyName}',
          icon: 'el-icon-d-arrow-right',
          roles: ['ROLE_GROUP_admin']
        }
      }