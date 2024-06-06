// import user from './i18n-user'
// assignLanguage(user)
const user = {
  zh: {
    route: {
      user: 'b_user表，用户账号信息'
    },
    domain: {
      user: {
        pageTitle: 'b_user表，用户账号信息',
        account: '登录账号',
        pd: '密码',
        acStatus: '账号状态',
        pdStatus: '密码状态',
        identities: '身份串',
        lastLoginTtime: '最后登录时间',
        pdErrorTimes: '密码错误次数'
      }
    }
  },
  en: {
    route: {
      user: 'User'
    },
    domain: {
      user: {
        pageTitle: 'User',
        account: 'Account',
        pd: 'Pd',
        acStatus: 'AcStatus',
        pdStatus: 'PdStatus',
        identities: 'Identities',
        lastLoginTtime: 'LastLoginTtime',
        pdErrorTimes: 'PdErrorTimes'
      }
    }
  }
}
export default user
