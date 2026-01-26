package com.ruoyi.preparation.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 统计Mapper接口
 * 
 * @author ruoyi
 */
public interface StatisticsMapper
{
    /**
     * 获取资料分类统计
     */
    List<Map<String, Object>> selectMaterialCategoryStats();

    /**
     * 获取近7天帖子发布趋势
     */
    List<Map<String, Object>> selectPostTrend();

    /**
     * 获取院校层次分布
     */
    List<Map<String, Object>> selectUniversityLevelStats();

    /**
     * 获取浏览总数
     */
    int selectBrowseCount();

    /**
     * 获取用户行为数量
     */
    int selectActionCount(@Param("actionType") String actionType);

    /**
     * 获取下载总数
     */
    int selectDownloadCount();

    /**
     * 获取院校总数
     */
    int selectUniversityCount();

    /**
     * 获取资料总数
     */
    int selectMaterialCount();

    /**
     * 获取帖子总数
     */
    int selectPostCount();

    /**
     * 获取用户总数
     */
    int selectUserCount();
}
