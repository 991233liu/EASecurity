import request from '@/utils/request'
// import qs from 'qs'

// 获取列表
export function getList(domain, params) {
  return request({
    url: '/admin/' + domain,
    method: 'get',
    params: params
  })
}

// 获取单条数据
export function show(domain, id, params) {
  return request({
    url: '/admin/' + domain + '/show/' + id,
    method: 'get',
    params: params
  })
}

// 新增一条数据
export function save(domain, data) {
  return request({
    url: '/admin/' + domain + "/save",
    method: 'post',
    data: data
  })
}

// 修改一条数据
export function update(domain, data) {
  return request({
    url: '/admin/' + domain + '/update/' + data.id,
    method: 'put',
    data: data
  })
}

// 删除一条数据
export function deleteOne(domain, id) {
  return request({
    url: '/admin/' + domain + '/delete/' + id,
    method: 'delete'
  })
}

// 删除多条数据
export function deleteAll(domain, ids) {
  return request({
    url: '/admin/' + domain + '/deleteAll',
    method: 'delete',
    data: { ids: ids }
  })
}
