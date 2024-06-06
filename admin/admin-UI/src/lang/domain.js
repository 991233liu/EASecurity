import enLocale from './en'
import zhLocale from './zh'
// import esLocale from './es'
// import jaLocale from './ja'
import userAdmin from './i18n-userAdmin'
import org from './i18n-org'
import orgUser from './i18n-orgUser'
import posts from './i18n-posts'
import user from './i18n-user'
import userInfo from './i18n-userInfo'
import role from './i18n-role'
import roleGroup from './i18n-roleGroup'
import roleUser from './i18n-roleUser'
import menu from './i18n-menu'
import uri from './i18n-uri'
import uriIp from './i18n-uriIp'
import uriOrg from './i18n-uriOrg'
import uriPosts from './i18n-uriPosts'
import uriRole from './i18n-uriRole'
import uriRoleGroup from './i18n-uriRoleGroup'
import uriUser from './i18n-uriUser'

const messages = {
  en: {
    ...enLocale
  },
  zh: {
    ...zhLocale
  }
//  es: {
//    ...esLocale
//  },
//  ja: {
//    ...jaLocale
//  }
}

const domain = {
  zh: messages.zh,
  en: messages.en
}

assignLanguage(userAdmin)
assignLanguage(org)
assignLanguage(orgUser)
assignLanguage(posts)
assignLanguage(user)
assignLanguage(userInfo)
assignLanguage(role)
assignLanguage(roleGroup)
assignLanguage(roleUser)
assignLanguage(menu)
assignLanguage(uri)
assignLanguage(uriIp)
assignLanguage(uriOrg)
assignLanguage(uriPosts)
assignLanguage(uriRole)
assignLanguage(uriRoleGroup)
assignLanguage(uriUser)

function assignLanguage(dm) {
  Object.assign(messages.en.route, dm.en.route)
  Object.assign(messages.en.domain, dm.en.domain)
  Object.assign(messages.zh.route, dm.zh.route)
  Object.assign(messages.zh.domain, dm.zh.domain)
}

export default domain
