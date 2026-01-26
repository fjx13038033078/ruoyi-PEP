package com.ruoyi.preparation.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户互动对象 ky_user_action
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KyUserAction extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 目标ID */
    @Excel(name = "目标ID")
    private Long targetId;

    /** 目标类型(1资料 2帖子) */
    @Excel(name = "目标类型", readConverterExp = "1=资料,2=帖子")
    private String targetType;

    /** 动作类型(1点赞 2收藏 3下载) */
    @Excel(name = "动作类型", readConverterExp = "1=点赞,2=收藏,3=下载")
    private String actionType;

    /** 目标标题（非数据库字段） */
    private String targetTitle;

    /** 用户名称（非数据库字段） */
    private String userName;
}
