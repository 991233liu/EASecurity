// import uri from './i18n-uri'
// assignLanguage(uri)
const uri = {
  zh: {
    route: {
      uri: 're_uri表：接口（服务）'
    },
    domain: {
      uri: {
        pageTitle: 're_uri表：接口（服务）',
        uri: '接口（服务）',
        classFullName: '类的全称',
        methodName: '方法名称',
        methodSignature: '方法的Signature',
        easType: '授权（控制）方式',
        fromTo: '来源，0人工、1程序源代码。如果是来着程序源代码，则只能禁用，不能删除',
        status: '状态，0启用、1禁用'
      }
    }
  },
  en: {
    route: {
      uri: 'Uri'
    },
    domain: {
      uri: {
        pageTitle: 'Uri',
        uri: 'Uri',
        classFullName: 'ClassFullName',
        methodName: 'MethodName',
        methodSignature: 'MethodSignature',
        easType: 'EasType',
        fromTo: 'FromTo',
        status: 'Status'
      }
    }
  }
}
export default uri
