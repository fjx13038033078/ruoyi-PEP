package com.ruoyi.preparation.recommend;

/**
 * 推荐算法配置
 *
 * @author ruoyi
 */
public class RecommendConfig
{
    /** 浏览行为权重 */
    public static final double WEIGHT_BROWSE = 1.0;

    /** 点赞行为权重 */
    public static final double WEIGHT_LIKE = 3.0;

    /** 收藏行为权重 */
    public static final double WEIGHT_COLLECT = 5.0;

    /** 下载行为权重（用于热门排序） */
    public static final double WEIGHT_DOWNLOAD = 5.0;

    /** 相似用户数量 */
    public static final int SIMILAR_USER_COUNT = 20;

    /** 冷启动阈值（用户行为数少于此值视为冷启动） */
    public static final int COLD_START_THRESHOLD = 3;

    /** 行为类型常量 */
    public static final String BEHAVIOR_BROWSE = "browse";
    public static final String BEHAVIOR_LIKE = "like";
    public static final String BEHAVIOR_COLLECT = "collect";

    /**
     * 根据行为类型获取权重
     */
    public static double getWeight(String behaviorType)
    {
        switch (behaviorType)
        {
            case BEHAVIOR_BROWSE:
                return WEIGHT_BROWSE;
            case BEHAVIOR_LIKE:
                return WEIGHT_LIKE;
            case BEHAVIOR_COLLECT:
                return WEIGHT_COLLECT;
            default:
                return 1.0;
        }
    }
}
