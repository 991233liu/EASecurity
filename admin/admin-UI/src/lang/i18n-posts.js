// import posts from './i18n-posts'
// assignLanguage(posts)
const posts = {
  zh: {
    route: {
      posts: 'b_posts表：职务信息表。'
    },
    domain: {
      posts: {
        pageTitle: 'b_posts表：职务信息表。',
        name: '职务名称',
        code: '职务编码',
        ranking: '职级1~99',
        type: '职务类别，0 领导，1 职员'
      }
    }
  },
  en: {
    route: {
      posts: 'Posts'
    },
    domain: {
      posts: {
        pageTitle: 'Posts',
        name: 'Name',
        code: 'Code',
        ranking: 'Ranking',
        type: 'Type'
      }
    }
  }
}
export default posts
