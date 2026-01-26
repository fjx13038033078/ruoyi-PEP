package com.ruoyi.preparation.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户浏览记录对象 ky_browse_log
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KyBrowseLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private Long userId;

    /** 目标ID(资料ID或帖子ID) */
    @Excel(name = "目标ID")
    private Long targetId;

    /** 类型(1资料 2帖子) */
    @Excel(name = "类型", readConverterExp = "1=资料,2=帖子")
    private String targetType;

    /** 浏览时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "浏览时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date browseTime;

    /** 目标标题（非数据库字段） */
    private String targetTitle;

    /** 用户名称（非数据库字段） */
    private String userName;
}
