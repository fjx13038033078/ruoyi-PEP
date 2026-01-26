package com.ruoyi.preparation.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 共享资料对象 ky_material
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KyMaterial extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 资料ID */
    private Long id;

    /** 分类ID */
    @Excel(name = "分类ID")
    private Long categoryId;

    /** 上传用户ID */
    @Excel(name = "上传用户ID")
    private Long userId;

    /** 资料标题 */
    @Excel(name = "资料标题")
    private String title;

    /** 文件路径 */
    @Excel(name = "文件路径")
    private String fileUrl;

    /** 下载次数(仅做统计展示) */
    @Excel(name = "下载次数")
    private Integer downCount;

    /** 浏览次数 */
    @Excel(name = "浏览次数")
    private Integer viewCount;

    /** 点赞次数 */
    @Excel(name = "点赞次数")
    private Integer likeCount;

    /** 评论次数 */
    @Excel(name = "评论次数")
    private Integer commentCount;

    /** 状态（0正常 1审核中 2拒绝） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=审核中,2=拒绝")
    private String status;

    /** 删除标志 */
    private String delFlag;

    /** 分类名称（非数据库字段） */
    private String categoryName;

    /** 上传用户名称（非数据库字段） */
    private String userName;
}
