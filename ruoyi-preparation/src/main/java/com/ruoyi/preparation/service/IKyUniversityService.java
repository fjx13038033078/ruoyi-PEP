package com.ruoyi.preparation.service;

import java.util.List;
import com.ruoyi.preparation.domain.KyUniversity;

/**
 * 院校信息Service接口
 * 
 * @author ruoyi
 */
public interface IKyUniversityService 
{
    /**
     * 查询院校信息
     * 
     * @param id 院校信息主键
     * @return 院校信息
     */
    public KyUniversity selectKyUniversityById(Long id);

    /**
     * 查询院校信息列表
     * 
     * @param kyUniversity 院校信息
     * @return 院校信息集合
     */
    public List<KyUniversity> selectKyUniversityList(KyUniversity kyUniversity);

    /**
     * 新增院校信息
     * 
     * @param kyUniversity 院校信息
     * @return 结果
     */
    public int insertKyUniversity(KyUniversity kyUniversity);

    /**
     * 修改院校信息
     * 
     * @param kyUniversity 院校信息
     * @return 结果
     */
    public int updateKyUniversity(KyUniversity kyUniversity);

    /**
     * 批量删除院校信息
     * 
     * @param ids 需要删除的院校信息主键集合
     * @return 结果
     */
    public int deleteKyUniversityByIds(Long[] ids);

    /**
     * 删除院校信息信息
     * 
     * @param id 院校信息主键
     * @return 结果
     */
    public int deleteKyUniversityById(Long id);
}
