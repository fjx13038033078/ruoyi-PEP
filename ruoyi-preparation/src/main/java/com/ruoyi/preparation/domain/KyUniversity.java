package com.ruoyi.preparation.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 院校信息对象 ky_university
 * 
 * @author ruoyi
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KyUniversity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 院校ID */
    private Long id;

    /** 院校名称 */
    @Excel(name = "院校名称")
    private String name;

    /** 院校代码 */
    @Excel(name = "院校代码")
    private String code;

    /** 层次(985/211/双一流/普通) */
    @Excel(name = "层次", readConverterExp = "985=985,211=211,双一流=双一流,普通=普通")
    private String level;

    /** 所在地区 */
    @Excel(name = "所在地区")
    private String region;

    /** 官网地址 */
    @Excel(name = "官网地址")
    private String website;

    /** 院校简介 */
    @Excel(name = "院校简介")
    private String intro;

    /** 校徽图片 */
    @Excel(name = "校徽图片")
    private String logoUrl;

    /** 删除标志（0代表存在 1代表删除） */
    private String delFlag;
}
