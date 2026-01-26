import request from '@/utils/request'

// 查询论坛帖子列表
export function listPost(query) {
  return request({
    url: '/preparation/post/list',
    method: 'get',
    params: query
  })
}

// 查询论坛帖子详细
export function getPost(id) {
  return request({
    url: '/preparation/post/' + id,
    method: 'get'
  })
}

// 新增论坛帖子
export function addPost(data) {
  return request({
    url: '/preparation/post',
    method: 'post',
    data: data
  })
}

// 修改论坛帖子
export function updatePost(data) {
  return request({
    url: '/preparation/post',
    method: 'put',
    data: data
  })
}

// 删除论坛帖子
export function delPost(id) {
  return request({
    url: '/preparation/post/' + id,
    method: 'delete'
  })
}
