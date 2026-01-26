import request from '@/utils/request'

// 查询资料分类列表
export function listCategory(query) {
  return request({
    url: '/preparation/category/list',
    method: 'get',
    params: query
  })
}

// 查询资料分类列表（不分页）
export function listAllCategory(query) {
  return request({
    url: '/preparation/category/listAll',
    method: 'get',
    params: query
  })
}

// 查询资料分类详细
export function getCategory(id) {
  return request({
    url: '/preparation/category/' + id,
    method: 'get'
  })
}

// 新增资料分类
export function addCategory(data) {
  return request({
    url: '/preparation/category',
    method: 'post',
    data: data
  })
}

// 修改资料分类
export function updateCategory(data) {
  return request({
    url: '/preparation/category',
    method: 'put',
    data: data
  })
}

// 删除资料分类
export function delCategory(id) {
  return request({
    url: '/preparation/category/' + id,
    method: 'delete'
  })
}
