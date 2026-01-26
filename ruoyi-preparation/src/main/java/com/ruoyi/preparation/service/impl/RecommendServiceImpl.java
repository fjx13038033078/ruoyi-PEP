package com.ruoyi.preparation.service.impl;

import com.ruoyi.preparation.domain.KyMaterial;
import com.ruoyi.preparation.mapper.KyBrowseLogMapper;
import com.ruoyi.preparation.mapper.KyMaterialMapper;
import com.ruoyi.preparation.mapper.KyUserActionMapper;
import com.ruoyi.preparation.recommend.CollaborativeFilteringAlgorithm;
import com.ruoyi.preparation.recommend.RecommendConfig;
import com.ruoyi.preparation.recommend.UserBehavior;
import com.ruoyi.preparation.service.IRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 推荐服务实现类
 *
 * @author ruoyi
 */
@Service
public class RecommendServiceImpl implements IRecommendService
{
    @Autowired
    private CollaborativeFilteringAlgorithm cfAlgorithm;

    @Autowired
    private KyBrowseLogMapper browseLogMapper;

    @Autowired
    private KyUserActionMapper userActionMapper;

    @Autowired
    private KyMaterialMapper materialMapper;

    /**
     * 获取推荐资料列表
     */
    @Override
    public List<KyMaterial> getRecommendMaterials(Long userId, int limit)
    {
        // 1. 获取所有用户行为数据
        List<UserBehavior> allBehaviors = loadAllUserBehaviors();

        // 2. 获取所有活跃用户ID
        List<Long> allUserIds = getAllActiveUserIds();

        // 3. 调用协同过滤算法
        List<Long> recommendIds = cfAlgorithm.recommend(userId, allBehaviors, allUserIds, limit);

        // 4. 如果推荐结果为空或不足，使用热门资料补充（冷启动处理）
        if (recommendIds.isEmpty())
        {
            return getHotMaterials(limit);
        }

        // 5. 查询推荐的资料详情
        List<KyMaterial> result = materialMapper.selectMaterialsByIds(recommendIds);

        // 6. 如果结果不足，用热门资料补充
        if (result.size() < limit)
        {
            List<KyMaterial> hotMaterials = getHotMaterials(limit);
            Set<Long> existingIds = new HashSet<>();
            for (KyMaterial m : result)
            {
                existingIds.add(m.getId());
            }
            for (KyMaterial hot : hotMaterials)
            {
                if (!existingIds.contains(hot.getId()))
                {
                    result.add(hot);
                    if (result.size() >= limit)
                    {
                        break;
                    }
                }
            }
        }

        return result;
    }

    /**
     * 获取热门资料（用于冷启动和补充推荐）
     */
    @Override
    public List<KyMaterial> getHotMaterials(int limit)
    {
        return materialMapper.selectHotMaterials(limit);
    }

    /**
     * 加载所有用户行为数据
     */
    private List<UserBehavior> loadAllUserBehaviors()
    {
        List<UserBehavior> behaviors = new ArrayList<>();

        // 加载浏览行为（targetType='1'表示资料）
        List<Map<String, Object>> browseRecords = browseLogMapper.selectAllBrowseForRecommend();
        for (Map<String, Object> record : browseRecords)
        {
            behaviors.add(new UserBehavior(
                ((Number) record.get("userId")).longValue(),
                ((Number) record.get("targetId")).longValue(),
                RecommendConfig.BEHAVIOR_BROWSE,
                ((Number) record.get("browseCount")).intValue()
            ));
        }

        // 加载点赞行为（actionType='1', targetType='1'）
        List<Map<String, Object>> likeRecords = userActionMapper.selectAllActionsForRecommend("1", "1");
        for (Map<String, Object> record : likeRecords)
        {
            behaviors.add(new UserBehavior(
                ((Number) record.get("userId")).longValue(),
                ((Number) record.get("targetId")).longValue(),
                RecommendConfig.BEHAVIOR_LIKE,
                ((Number) record.get("actionCount")).intValue()
            ));
        }

        // 加载收藏行为（actionType='2', targetType='1'）
        List<Map<String, Object>> collectRecords = userActionMapper.selectAllActionsForRecommend("1", "2");
        for (Map<String, Object> record : collectRecords)
        {
            behaviors.add(new UserBehavior(
                ((Number) record.get("userId")).longValue(),
                ((Number) record.get("targetId")).longValue(),
                RecommendConfig.BEHAVIOR_COLLECT,
                ((Number) record.get("actionCount")).intValue()
            ));
        }

        return behaviors;
    }

    /**
     * 获取所有有行为的用户ID
     */
    private List<Long> getAllActiveUserIds()
    {
        Set<Long> userIds = new HashSet<>();
        userIds.addAll(browseLogMapper.selectDistinctUserIds());
        userIds.addAll(userActionMapper.selectDistinctUserIds());
        return new ArrayList<>(userIds);
    }
}
