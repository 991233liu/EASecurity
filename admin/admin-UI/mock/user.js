
const tokens = {
  admin: {
    token: 'admin-token'
  },
  editor: {
    token: 'editor-token'
  }
}

const users = {
  'admin-token': {
    roles: ['ROLE_ADMIN'],
    introduction: 'I am a super administrator',
    avatar: '/images/user.gif',
    name: 'Super Admin'
  },
  'editor-token': {
    roles: ['editor'],
    introduction: 'I am an editor',
    avatar: '/images/user.gif',
    name: 'Normal Editor'
  }
}

module.exports = [
  // user login
  {
    url: '/admin/login/authenticate',
    type: 'post',
    response: config => {
      const { username } = config.body
      const token = tokens[username]
      console.log(config.body)

      return {
        code: '200',
        data: token
      }
    }
  },

  // get user info
  {
    url: '/admin/login/userInfo\.*',
    type: 'get',
    response: config => {
//      const { token } = config.query
//      const info = users[token]
//      console.log(info)
//
//      // mock error
//      if (!info) {
//        return {
//          code: 50008,
//          message: 'Login failed, unable to get user details.'
//        }
//      }

      return {
		roles: ['ROLE_ADMIN'],
        "username": "admin",
        "fullName": "Super Admin",
        introduction: 'I am a super administrator',
        "avatar": "/images/user.gif",
        "email": '123@163.com',
        "lastLoginTime": "2023-09-09T14:05:57Z"
      }
    }
  },

  // user changePassword
  {
    url: '/admin/changePassword/changeOwnerPassword',
    type: 'post',
    response: _ => {
      return {
        code: '200',
        data: 'success'
      }
    }
  },

  // user logout
  {
    url: '/admin/logout',
    type: 'get',
    response: _ => {
      return {
        code: 200,
        data: 'success'
      }
    }
  },

  // user captchas
  {
    url: '/admin/login/gifCaptcha',
    type: 'get',
    response: _ => {
      return {
		"image": "data:image/gif;base64,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",
		"key": "1af6e72c-04aa-4608-ae31-31f25e78dca1"
      }
    }
  }
]
