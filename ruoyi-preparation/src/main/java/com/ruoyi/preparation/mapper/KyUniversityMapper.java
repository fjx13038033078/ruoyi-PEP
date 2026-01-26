package com.ruoyi.preparation.mapper;

import java.util.List;
import com.ruoyi.preparation.domain.KyUniversity;

/**
 * 院校信息Mapper接口
 * 
 * @author ruoyi
 */
public interface KyUniversityMapper 
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
     * 删除院校信息
     * 
     * @param id 院校信息主键
     * @return 结果
     */
    public int deleteKyUniversityById(Long id);

    /**
     * 批量删除院校信息
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKyUniversityByIds(Long[] ids);
}
