package com.ruoyi.preparation.recommend;

import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 基于用户的协同过滤推荐算法
 * 
 * 算法流程：
 * 1. 构建用户-物品评分矩阵（基于浏览、点赞、收藏行为加权）
 * 2. 计算用户之间的相似度（余弦相似度）
 * 3. 找到与目标用户最相似的K个用户
 * 4. 基于相似用户的偏好预测目标用户的偏好
 * 5. 排除用户已交互的物品，返回推荐列表
 *
 * @author ruoyi
 */
@Component
public class CollaborativeFilteringAlgorithm
{
    /**
     * 执行协同过滤推荐
     *
     * @param targetUserId 目标用户ID
     * @param allBehaviors 所有用户的行为数据
     * @param allUserIds 所有用户ID列表
     * @param limit 推荐数量
     * @return 推荐的物品ID列表（按推荐分数降序）
     */
    public List<Long> recommend(Long targetUserId, List<UserBehavior> allBehaviors, 
                                List<Long> allUserIds, int limit)
    {
        // 1. 构建用户行为向量
        Map<Long, Map<Long, Double>> userVectors = buildUserVectors(allBehaviors);

        // 2. 获取目标用户的行为向量
        Map<Long, Double> targetUserVector = userVectors.getOrDefault(targetUserId, new HashMap<>());

        // 3. 冷启动检测
        if (isColdStart(targetUserVector))
        {
            return Collections.emptyList();
        }

        // 4. 计算与其他用户的相似度
        List<UserSimilarity> similarities = calculateSimilarities(targetUserId, targetUserVector, 
                                                                  userVectors, allUserIds);

        // 5. 获取最相似的K个用户
        List<UserSimilarity> topSimilarUsers = getTopSimilarUsers(similarities);

        if (topSimilarUsers.isEmpty())
        {
            return Collections.emptyList();
        }

        // 6. 基于相似用户计算推荐分数
        Map<Long, Double> recommendScores = calculateRecommendScores(targetUserVector, 
                                                                     topSimilarUsers, userVectors);

        // 7. 排序并返回推荐结果
        return getTopRecommendations(recommendScores, limit);
    }

    /**
     * 构建所有用户的行为向量
     * 
     * @param allBehaviors 所有行为数据
     * @return 用户ID -> (物品ID -> 加权评分)
     */
    private Map<Long, Map<Long, Double>> buildUserVectors(List<UserBehavior> allBehaviors)
    {
        Map<Long, Map<Long, Double>> userVectors = new HashMap<>();

        for (UserBehavior behavior : allBehaviors)
        {
            Long userId = behavior.getUserId();
            Long targetId = behavior.getTargetId();
            double weight = RecommendConfig.getWeight(behavior.getBehaviorType());
            double score = weight * behavior.getCount();

            userVectors.computeIfAbsent(userId, k -> new HashMap<>())
                       .merge(targetId, score, Double::sum);
        }

        return userVectors;
    }

    /**
     * 检测是否为冷启动用户
     */
    private boolean isColdStart(Map<Long, Double> userVector)
    {
        return userVector.isEmpty() || userVector.size() < RecommendConfig.COLD_START_THRESHOLD;
    }

    /**
     * 计算目标用户与所有其他用户的相似度
     */
    private List<UserSimilarity> calculateSimilarities(Long targetUserId, 
                                                        Map<Long, Double> targetUserVector,
                                                        Map<Long, Map<Long, Double>> userVectors, 
                                                        List<Long> allUserIds)
    {
        List<UserSimilarity> similarities = new ArrayList<>();

        for (Long otherUserId : allUserIds)
        {
            if (!otherUserId.equals(targetUserId))
            {
                Map<Long, Double> otherUserVector = userVectors.get(otherUserId);
                if (otherUserVector != null && !otherUserVector.isEmpty())
                {
                    double similarity = calculateCosineSimilarity(targetUserVector, otherUserVector);
                    if (similarity > 0)
                    {
                        similarities.add(new UserSimilarity(otherUserId, similarity));
                    }
                }
            }
        }

        return similarities;
    }

    /**
     * 计算两个向量的余弦相似度
     * 
     * 公式：cos(A,B) = (A·B) / (|A| * |B|)
     */
    private double calculateCosineSimilarity(Map<Long, Double> vector1, Map<Long, Double> vector2)
    {
        // 找到两个向量的交集（共同评分的物品）
        Set<Long> commonKeys = new HashSet<>(vector1.keySet());
        commonKeys.retainAll(vector2.keySet());

        if (commonKeys.isEmpty())
        {
            return 0.0;
        }

        // 计算点积 A·B
        double dotProduct = 0.0;
        for (Long key : commonKeys)
        {
            dotProduct += vector1.get(key) * vector2.get(key);
        }

        // 计算向量模长 |A| 和 |B|
        double norm1 = calculateNorm(vector1);
        double norm2 = calculateNorm(vector2);

        if (norm1 == 0 || norm2 == 0)
        {
            return 0.0;
        }

        return dotProduct / (norm1 * norm2);
    }

    /**
     * 计算向量的模长（L2范数）
     */
    private double calculateNorm(Map<Long, Double> vector)
    {
        double sumOfSquares = vector.values().stream()
                                    .mapToDouble(v -> v * v)
                                    .sum();
        return Math.sqrt(sumOfSquares);
    }

    /**
     * 获取最相似的K个用户
     */
    private List<UserSimilarity> getTopSimilarUsers(List<UserSimilarity> similarities)
    {
        Collections.sort(similarities);
        return similarities.stream()
                          .limit(RecommendConfig.SIMILAR_USER_COUNT)
                          .collect(Collectors.toList());
    }

    /**
     * 基于相似用户计算推荐分数
     * 
     * 推荐分数 = Σ(相似用户的评分 * 相似度)
     */
    private Map<Long, Double> calculateRecommendScores(Map<Long, Double> targetUserVector,
                                                        List<UserSimilarity> similarUsers,
                                                        Map<Long, Map<Long, Double>> userVectors)
    {
        Map<Long, Double> recommendScores = new HashMap<>();
        Set<Long> targetUserItems = targetUserVector.keySet();

        for (UserSimilarity similarUser : similarUsers)
        {
            Map<Long, Double> similarUserVector = userVectors.get(similarUser.getUserId());
            
            if (similarUserVector != null)
            {
                for (Map.Entry<Long, Double> entry : similarUserVector.entrySet())
                {
                    Long itemId = entry.getKey();
                    
                    // 排除目标用户已交互的物品
                    if (!targetUserItems.contains(itemId))
                    {
                        double score = entry.getValue() * similarUser.getSimilarity();
                        recommendScores.merge(itemId, score, Double::sum);
                    }
                }
            }
        }

        return recommendScores;
    }

    /**
     * 获取推荐分数最高的N个物品
     */
    private List<Long> getTopRecommendations(Map<Long, Double> recommendScores, int limit)
    {
        return recommendScores.entrySet().stream()
                             .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                             .limit(limit)
                             .map(Map.Entry::getKey)
                             .collect(Collectors.toList());
    }
}
