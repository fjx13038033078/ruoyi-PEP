package com.ruoyi.preparation.recommend;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户行为数据
 *
 * @author ruoyi
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
