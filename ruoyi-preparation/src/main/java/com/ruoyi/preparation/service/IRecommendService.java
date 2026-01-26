package com.ruoyi.preparation.service;

import com.ruoyi.preparation.domain.KyMaterial;

import java.util.List;

/**
 * 推荐服务接口
 *
 * @author ruoyi
 */
public interface IRecommendService
{
    /**
     * 获取推荐资料列表（协同过滤）
     *
     * @param userId 用户ID
     * @param limit 推荐数量
     * @return 推荐的资料列表
     */
    List<KyMaterial> getRecommendMaterials(Long userId, int limit);

    /**
     * 获取热门资料（用于冷启动）
     *
     * @param limit 数量
     * @return 热门资料列表
     */
    List<KyMaterial> getHotMaterials(int limit);
}
