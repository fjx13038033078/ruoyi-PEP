import request from '@/utils/request'

/**
 * 获取推荐资料（猜你喜欢）
 * @param {number} limit 数量限制
 */
export function getRecommendMaterials(limit) {
  return request({
    url: '/preparation/recommend/materials',
    method: 'get',
    params: { limit }
  })
}

/**
 * 获取热门资料
 * @param {number} limit 数量限制
 */
export function getHotMaterials(limit) {
  return request({
    url: '/preparation/recommend/hot',
    method: 'get',
    params: { limit }
  })
}
