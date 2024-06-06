// import orgUser from './i18n-orgUser'
// assignLanguage(orgUser)
const orgUser = {
  zh: {
    route: {
      orgUser: 'b_org_user表：组织用户关系表，含职务。'
    },
    domain: {
      orgUser: {
        pageTitle: 'b_org_user表：组织用户关系表，含职务。',
        userId: '用户ID',
        user: '用户ID',
        account: '登录账号（冗余）',
        orgId: '组织ID',
        org: '组织ID',
        postsId: '职务ID',
        posts: '职务ID',
        masterPosts: '是否主职'
      }
    }
  },
  en: {
    route: {
      orgUser: 'OrgUser'
    },
    domain: {
      orgUser: {
        pageTitle: 'OrgUser',
        userId: 'UserId',
        user: 'User',
        account: 'Account',
        orgId: 'OrgId',
        org: 'Org',
        postsId: 'PostsId',
        posts: 'Posts',
        masterPosts: 'MasterPosts'
      }
    }
  }
}
export default orgUser
