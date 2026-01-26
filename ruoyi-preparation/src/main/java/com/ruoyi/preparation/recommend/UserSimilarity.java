package com.ruoyi.preparation.recommend;

/**
 * 用户相似度
 *
 * @author ruoyi
 */
public class UserSimilarity implements Comparable<UserSimilarity>
{
    /** 用户ID */
    private Long userId;

    /** 相似度值 */
    private double similarity;

    public UserSimilarity()
    {
    }

    public UserSimilarity(Long userId, double similarity)
    {
        this.userId = userId;
        this.similarity = similarity;
    }

    public Long getUserId()
    {
        return userId;
    }

    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public double getSimilarity()
    {
        return similarity;
    }

    public void setSimilarity(double similarity)
    {
        this.similarity = similarity;
    }

    @Override
    public int compareTo(UserSimilarity other)
    {
        // 降序排列
        return Double.compare(other.similarity, this.similarity);
    }
}
