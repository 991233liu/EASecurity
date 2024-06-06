import request from '@/utils/request'
import qs from 'qs'

export function login(data) {
  return request({
    url: '/admin/login/authenticate',
    method: 'post',
    data: qs.stringify(data)
  })
}

export function getInfo(token) {
  return request({
    url: '/admin/login/userInfo',
    method: 'get'
  })
}

export function changePassword(data) {
  return request({
    url: '/admin/changePassword/changeOwnerPassword',
    method: 'post',
    data: qs.stringify(data)
  })
}

export function logout() {
  return request({
    url: '/admin/logout',
    method: 'get'
  })
}

export function captchas() {
  return request({
    url: '/admin/login/gifCaptcha',
    method: 'get'
  })
}
