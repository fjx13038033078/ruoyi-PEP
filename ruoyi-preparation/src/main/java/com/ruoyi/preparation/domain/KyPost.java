package com.ruoyi.preparation.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 论坛帖子对象 ky_post
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KyPost extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 帖子ID */
    private Long id;

    /** 发帖人ID */
    @Excel(name = "发帖人ID")
    private Long userId;

    /** 帖子标题 */
    @Excel(name = "帖子标题")
    private String title;

    /** 帖子内容(富文本) */
    @Excel(name = "帖子内容")
    private String content;

    /** 帖子分类（字典：ky_post_category） */
    @Excel(name = "帖子分类", dictType = "ky_post_category")
    private String category;

    /** 浏览量 */
    @Excel(name = "浏览量")
    private Integer viewNum;

    /** 点赞数 */
    @Excel(name = "点赞数")
    private Integer likeNum;

    /** 收藏数 */
    @Excel(name = "收藏数")
    private Integer collectNum;

    /** 是否置顶 */
    @Excel(name = "是否置顶", readConverterExp = "0=否,1=是")
    private String isTop;

    /** 删除标志 */
    private String delFlag;

    /** 发帖人名称（非数据库字段） */
    private String userName;

    /** 发帖人头像（非数据库字段） */
    private String userAvatar;
}
