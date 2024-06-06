import { login, logout, getInfo, captchas, changePassword } from '@/api/user'
import { getToken, setToken, removeToken } from '@/utils/auth'
import router, { resetRouter } from '@/router'
import bcrypt from 'bcryptjs'

const state = {
  token: getToken(),
  username: '',
  fullName: '',
  avatar: '',
  email: '',
  lastLoginTime: '',
  introduction: '',
  roles: []
}

const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_INTRODUCTION: (state, introduction) => {
    state.introduction = introduction
  },
  SET_USERNAME: (state, username) => {
    state.username = username
  },
  SET_FULLNAME: (state, fullName) => {
    state.fullName = fullName
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_EMAIL: (state, email) => {
    state.email = email
  },
  SET_LASTLOGINTIME: (state, lastLoginTime) => {
    state.lastLoginTime = lastLoginTime
  },
  SET_ROLES: (state, roles) => {
    state.roles = roles
  }
}

const actions = {
  // user login
  login({ commit }, userInfo) {
    const { username, password, captcha } = userInfo
    return new Promise((resolve, reject) => {
      login({ username: username.trim(), password: bcrypt.hashSync(password, '$2a$10$ayeUQHCANiGOhpO3uBTllu'), captcha: captcha.trim() }).then(response => {
        // const data = response
        commit('SET_TOKEN', 'admin-token')
        setToken('admin-token')
        // commit('SET_TOKEN', data.token)
        // setToken(data.token)
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // get user info
  getInfo({ commit, state }) {
    return new Promise((resolve, reject) => {
      getInfo(state.token).then(response => {
        const data = response

        if (!data) {
          reject('Verification failed, please Login again.')
        }

        const { roles, username, fullName, avatar, email, lastLoginTime, introduction } = data

        // roles must be a non-empty array
        if (!roles || roles.length <= 0) {
          reject('getInfo: roles must be a non-null array!')
        }

        commit('SET_ROLES', roles)
        commit('SET_USERNAME', username)
        commit('SET_FULLNAME', fullName)
        commit('SET_AVATAR', avatar)
        commit('SET_EMAIL', email)
        commit('SET_LASTLOGINTIME', lastLoginTime)
        commit('SET_INTRODUCTION', introduction)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // user changePassword
  changePassword({ commit }, form) {
    return new Promise((resolve, reject) => {
      changePassword({ oldPassword: bcrypt.hashSync(form.oldPassword, '$2a$10$ayeUQHCANiGOhpO3uBTllu'), newPassword: bcrypt.hashSync(form.newPassword, '$2a$10$ayeUQHCANiGOhpO3uBTllu'), captcha: form.captcha }).then(response => {
        resolve(response)
      }).catch(error => {
        console.log(error)
        reject(error)
      })
    })
  },

  // user logout
  logout({ commit, state, dispatch }) {
    return new Promise((resolve, reject) => {
      logout(state.token).then(() => {
        commit('SET_TOKEN', '')
        commit('SET_ROLES', [])
        removeToken()
        resetRouter()

        // reset visited views and cached views
        // to fixed https://github.com/PanJiaChen/vue-element-admin/issues/2485
        dispatch('tagsView/delAllViews', null, { root: true })

        resolve()
      }).catch(error => {
        console.log(error)
        reject(error)
      })
    })
  },

  // user captchas
  captchas({ commit }) {
    return new Promise((resolve, reject) => {
      captchas().then(response => {
        resolve(response)
      }).catch(error => {
        console.log(error)
        reject(error)
      })
    })
  },

  // remove token
  resetToken({ commit }) {
    return new Promise(resolve => {
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      removeToken()
      resolve()
    })
  },

  // dynamically modify permissions
  async changeRoles({ commit, dispatch }, role) {
    const token = role + '-token'

    commit('SET_TOKEN', token)
    setToken(token)

    const { roles } = await dispatch('getInfo')

    resetRouter()

    // generate accessible routes map based on roles
    const accessRoutes = await dispatch('permission/generateRoutes', roles, { root: true })
    // dynamically add accessible routes
    router.addRoutes(accessRoutes)

    // reset visited views and cached views
    dispatch('tagsView/delAllViews', null, { root: true })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
