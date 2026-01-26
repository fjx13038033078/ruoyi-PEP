import request from '@/utils/request'

// 查询院校信息列表
export function listUniversity(query) {
  return request({
    url: '/preparation/university/list',
    method: 'get',
    params: query
  })
}

// 查询院校信息详细
export function getUniversity(id) {
  return request({
    url: '/preparation/university/' + id,
    method: 'get'
  })
}

// 新增院校信息
export function addUniversity(data) {
  return request({
    url: '/preparation/university',
    method: 'post',
    data: data
  })
}

// 修改院校信息
export function updateUniversity(data) {
  return request({
    url: '/preparation/university',
    method: 'put',
    data: data
  })
}

// 删除院校信息
export function delUniversity(id) {
  return request({
    url: '/preparation/university/' + id,
    method: 'delete'
  })
}
