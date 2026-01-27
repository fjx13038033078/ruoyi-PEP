package com.ruoyi.preparation.recommend;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用户相似度
 *
 * @author ruoyi
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSimilarity implements Comparable<UserSimilarity>
{
    /** 用户ID */
    private Long userId;

    /** 相似度值 */
    private double similarity;

    @Override
    public int compareTo(UserSimilarity other)
    {
        // 降序排列
        return Double.compare(other.similarity, this.similarity);
    }
}
