// import org from './i18n-org'
// assignLanguage(org)
const org = {
  zh: {
    route: {
      org: 'b_org表：组织。'
    },
    domain: {
      org: {
        pageTitle: 'b_org表：组织。',
        name: '组织名称',
        code: '组织编码（业务用）',
        type: '类型:0根、1机构、2公司、3办事处、4部门、5临时',
        status: '组织状态',
        parentId: '父节点ID',
        parent: '父节点ID',
        fullName: '全路径名称，如：/xx公司/xx部门/',
        fullPathid: '全路径ID，如：/0/123/456/',
        fullCode: '全路径组织编码，如：/0/abc/def/'
      }
    }
  },
  en: {
    route: {
      org: 'Org'
    },
    domain: {
      org: {
        pageTitle: 'Org',
        name: 'Name',
        code: 'Code',
        type: 'Type',
        status: 'Status',
        parentId: 'ParentId',
        parent: 'Parent',
        fullName: 'FullName',
        fullPathid: 'FullPathid',
        fullCode: 'FullCode'
      }
    }
  }
}
export default org
