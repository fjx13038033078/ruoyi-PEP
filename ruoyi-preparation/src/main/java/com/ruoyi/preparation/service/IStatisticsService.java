package com.ruoyi.preparation.service;

import java.util.Map;

/**
 * 统计服务接口
 *
 * @author ruoyi
 */
public interface IStatisticsService
{
    /**
     * 获取资料分类统计
     */
    Map<String, Object> getMaterialCategoryStats();

    /**
     * 获取近7天帖子发布趋势
     */
    Map<String, Object> getPostTrend();

    /**
     * 获取院校层次分布
     */
    Map<String, Object> getUniversityLevelStats();

    /**
     * 获取用户行为统计
     */
    Map<String, Object> getUserActionStats();

    /**
     * 获取总体统计数据
     */
    Map<String, Object> getOverview();
}
