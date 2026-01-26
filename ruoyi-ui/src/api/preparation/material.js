import request from '@/utils/request'

// 查询共享资料列表
export function listMaterial(query) {
  return request({
    url: '/preparation/material/list',
    method: 'get',
    params: query
  })
}

// 查询共享资料详细
export function getMaterial(id) {
  return request({
    url: '/preparation/material/' + id,
    method: 'get'
  })
}

// 新增共享资料
export function addMaterial(data) {
  return request({
    url: '/preparation/material',
    method: 'post',
    data: data
  })
}

// 修改共享资料
export function updateMaterial(data) {
  return request({
    url: '/preparation/material',
    method: 'put',
    data: data
  })
}

// 删除共享资料
export function delMaterial(id) {
  return request({
    url: '/preparation/material/' + id,
    method: 'delete'
  })
}

// 记录下载
export function downloadMaterial(id) {
  return request({
    url: '/preparation/material/download/' + id,
    method: 'post'
  })
}
