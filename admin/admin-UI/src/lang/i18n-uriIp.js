// import uriIp from './i18n-uriIp'
// assignLanguage(uriIp)
const uriIp = {
  zh: {
    route: {
      uriIp: 'au_uri_ip表：基于ip的接口（服务）授权'
    },
    domain: {
      uriIp: {
        pageTitle: 'au_uri_ip表：基于ip的接口（服务）授权',
        uriId: 'URI资源ID',
        uri: 'URI资源ID',
        ips: 'IP地址，多值","分割',
        group1: '分组。同一组内多个条件，满足任一条件则此组为true；多组条件，任一组是false，则最终结果为false。',
        fromTo: '来源，0人工、1程序源代码。如果是来着程序源代码，则只能禁用，不能删除',
        status: '状态，0启用、1禁用'
      }
    }
  },
  en: {
    route: {
      uriIp: 'UriIp'
    },
    domain: {
      uriIp: {
        pageTitle: 'UriIp',
        uriId: 'UriId',
        uri: 'Uri',
        ips: 'Ips',
        group1: 'Group1',
        fromTo: 'FromTo',
        status: 'Status'
      }
    }
  }
}
export default uriIp
