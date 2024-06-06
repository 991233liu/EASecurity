const Mock = require('mockjs')

const List = []
const count = 100
const getRestfulId = (path) => {
  let strs  = path.split('/')
  return strs[strs.length - 1]
}

const baseContent = '<p>I am testing data, I am testing data.</p><p><img src="https://wpimg.wallstcn.com/4c69009c-0fd4-4153-b112-6cb53d1cf943"></p>'
const image_uri = 'https://wpimg.wallstcn.com/e4558086-631c-425c-9430-56ffb46e70b3'

for (let i = 0; i < count; i++) {
  List.push(Mock.mock({
    id: '@increment',
    gkey: '@guid',
    gvalue: '@string(5)',
    validTime: +Mock.Random.date('T'),
    sessionId: '@guid'
  }))
}

module.exports = [
  {
    url: '/admin/gifCaptcha/',
    type: 'get',
    response: config => {
      const id = getRestfulId(config.path)
      for (const article of List) {
        if (article.id == id) {
          return article
        }
      }
    }
  },

  {
    url: '/admin/gifCaptcha',
    type: 'post',
    response: config => {
      console.log(config.body)
      const temp = config.body
      temp.id = parseInt(Math.random() * 100) + 1024 // mock a id
      List.push(temp)
      return {
        code: 200,
        data: 'success'
      }
    }
  },

  {
    url: '/admin/gifCaptcha/',
    type: 'put',
    response: config => {
      console.log(config.body)
      const temp = config.body
      const id = getRestfulId(config.path)
      const index = List.findIndex(v => v.id == id)
      if (index && index > -1) {
        List.splice(index, 1)
        List.push(temp)
        return {
          code: 200,
          data: 'success'
        }
      }
      return {
        code: 300,
        data: '更新失败，未找到ID'
      }
    }
  },

  {
    url: '/admin/gifCaptcha/deleteAll',
    type: 'delete',
    response: config => {
      const { ids } = config.body
      for (const id of ids) {
        const index = List.findIndex(v => v.id == id)
        if (index && index > -1) {
          List.splice(index, 1)
        }
      }

      return {
        code: 200,
        data: 'success'
      }
    }
  },

  {
    url: '/admin/gifCaptcha/',
    type: 'delete',
    response: config => {
      const id = getRestfulId(config.path)
      const index = List.findIndex(v => v.id == id)
      if (index && index > -1) {
        List.splice(index, 1)
        return {
          code: 200,
          data: 'success'
        }
      }

      return {
        code: 300,
        data: '删除失败，未找到ID'
      }
    }
  },

  {
    url: '/admin/gifCaptcha',
    type: 'get',
    response: config => {
      const { search, offset = 1, max = 20, sort, order } = config.query
      console.log(search)

      let mockList = []
      if (search) {
        const data = JSON.parse(search)
        const gvalue = data.gvalue
        const importance = data.importance
        const type = data.type
        mockList = List.filter(item => {
//          if (importance && item.importance !== +importance) return false
//          if (type && item.type !== type) return false
          if (gvalue && item.gvalue.indexOf(gvalue) < 0) return false
          return true
        })
      }

      if (sort == 'id' && order == 'desc') {
        mockList = mockList.sort((a, b) => b.id - a.id)
      } else if (sort == 'id') {
        mockList = mockList.sort((a, b) => a.id - b.id)
      }

      const offsetList = mockList.filter((item, index) => {
        return (index < (max*1 + offset * 1) && index >= offset*1)
      })

      return {
        total: mockList.length,
        items: offsetList
      }
    }
  }

]

