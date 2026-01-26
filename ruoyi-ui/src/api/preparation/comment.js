import request from '@/utils/request'

// 查询评论列表
export function listComment(query) {
  return request({
    url: '/preparation/comment/list',
    method: 'get',
    params: query
  })
}

// 查询评论树形结构
export function getCommentTree(targetId, targetType) {
  return request({
    url: '/preparation/comment/tree',
    method: 'get',
    params: { targetId, targetType }
  })
}

// 查询评论详细
export function getComment(id) {
  return request({
    url: '/preparation/comment/' + id,
    method: 'get'
  })
}

// 新增评论
export function addComment(data) {
  return request({
    url: '/preparation/comment',
    method: 'post',
    data: data
  })
}

// 修改评论
export function updateComment(data) {
  return request({
    url: '/preparation/comment',
    method: 'put',
    data: data
  })
}

// 删除评论
export function delComment(id) {
  return request({
    url: '/preparation/comment/' + id,
    method: 'delete'
  })
}
