package com.ruoyi.preparation.domain;

import java.util.List;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 通用评论对象 ky_comment
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KyComment extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 评论ID */
    private Long id;

    /** 目标ID(资料ID或帖子ID) */
    @Excel(name = "目标ID")
    private Long targetId;

    /** 目标类型(1资料 2帖子) */
    @Excel(name = "目标类型", readConverterExp = "1=资料,2=帖子")
    private String targetType;

    /** 父评论ID */
    @Excel(name = "父评论ID")
    private Long parentId;

    /** 评论人ID */
    @Excel(name = "评论人ID")
    private Long userId;

    /** 评论内容 */
    @Excel(name = "评论内容")
    private String content;

    /** 删除标志 */
    private String delFlag;

    /** 评论人名称（非数据库字段） */
    private String userName;

    /** 评论人头像（非数据库字段） */
    private String userAvatar;

    /** 子评论列表（非数据库字段） */
    private List<KyComment> children;
}
