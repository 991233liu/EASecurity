import axios from 'axios'
import { MessageBox, Message, Notification } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // url = base url + request url
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 15000 // request timeout
})

// request interceptor
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      // let each request carry token
      // ['X-Token'] is a custom headers key
      // please modify it according to the actual situation
      config.headers['X-Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response interceptor
service.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
  */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  response => {
    const res = response.data
    const req = response.request

    const adminExp = /\/(?:admin+\/)/
    if (adminExp.test(req.responseURL)) { // admin工程返回值处理
      if (res.error) { // 服务器返回了错误信息
        Message({
          message: res.message || 'Error',
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(new Error(res.message || 'Error'))
      } else if (res.code && !(res.code === '200' || res.code === 200)) { // 服务器返回了状态码，但是状态码不是200
        Notification({
          title: 'Code: ' + res.code,
          message: res.message || 'Info',
          type: 'error',
          duration: 5 * 1000
        })
        return Promise.reject(new Error(res.message || 'Info'))
      }
      return res.data
    } else { // demo工程返回值处理
      // if the custom code is not 20000, it is judged as an error.
      if (res.code !== 20000) {
        Message({
          message: res.message || 'Error',
          type: 'error',
          duration: 5 * 1000
        })

        // 50008: Illegal token; 50012: Other clients logged in; 50014: Token expired;
        if (res.code === 50008 || res.code === 50012 || res.code === 50014) {
          // to re-login
          MessageBox.confirm('You have been logged out, you can cancel to stay on this page, or log in again', 'Confirm logout', {
            confirmButtonText: 'Re-Login',
            cancelButtonText: 'Cancel',
            type: 'warning'
          }).then(() => {
            store.dispatch('user/resetToken').then(() => {
              location.reload()
            })
          })
        }
        return Promise.reject(new Error(res.message || 'Error'))
      } else {
        return res
      }
    }
  },
  error => {
    console.log('err' + error) // for debug
    if (error.response.status === 422) {
      if (error.response.data.total) {
        error.response.data.errors.forEach(item => {
          setTimeout(() => {
            Notification({
              title: 'Code: 422',
              message: item.message || 'Error',
              type: 'error',
              duration: 5 * 1000
            })
          }, 0)
        })
      } else {
        Notification({
          title: 'Code: 422',
          message: error.response.data.message || error.message,
          type: 'error',
          duration: 5 * 1000
        })
      }
    } else {
      Message({
        message: error.response.data.message || error.message,
        type: 'error',
        duration: 5 * 1000
      })
    }
    return Promise.reject(error)
  }
)

export default service
