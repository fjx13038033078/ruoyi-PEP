package com.ruoyi.preparation.recommend;

/**
 * 用户行为数据
 *
 * @author ruoyi
 */
public class UserBehavior
{
    /** 用户ID */
    private Long userId;

    /** 目标ID（资料ID） */
    private Long targetId;

    /** 行为类型：browse-浏览, like-点赞, collect-收藏 */
    private String behaviorType;

    /** 行为次数 */
    private int count;

    public UserBehavior()
    {
    }

    public UserBehavior(Long userId, Long targetId, String behaviorType, int count)
    {
        this.userId = userId;
        this.targetId = targetId;
        this.behaviorType = behaviorType;
        this.count = count;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getTargetId()
    {
        return targetId;
    }

    public void setTargetId(Long targetId)
    {
        this.targetId = targetId;
    }

    public String getBehaviorType()
    {
        return behaviorType;
    }

    public void setBehaviorType(String behaviorType)
    {
        this.behaviorType = behaviorType;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }
}
