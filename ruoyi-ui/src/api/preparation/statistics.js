import request from '@/utils/request'

/**
 * 获取资料分类统计
 */
export function getMaterialCategoryStats() {
  return request({
    url: '/preparation/statistics/materialCategory',
    method: 'get'
  })
}

/**
 * 获取近7天帖子发布趋势
 */
export function getPostTrend() {
  return request({
    url: '/preparation/statistics/postTrend',
    method: 'get'
  })
}

/**
 * 获取院校层次分布
 */
export function getUniversityLevelStats() {
  return request({
    url: '/preparation/statistics/universityLevel',
    method: 'get'
  })
}

/**
 * 获取用户行为统计
 */
export function getUserActionStats() {
  return request({
    url: '/preparation/statistics/userAction',
    method: 'get'
  })
}

/**
 * 获取总体统计数据
 */
export function getOverview() {
  return request({
    url: '/preparation/statistics/overview',
    method: 'get'
  })
}
