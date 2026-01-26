package com.ruoyi.preparation.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.preparation.mapper.KyUniversityMapper;
import com.ruoyi.preparation.domain.KyUniversity;
import com.ruoyi.preparation.service.IKyUniversityService;

/**
 * 院校信息Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class KyUniversityServiceImpl implements IKyUniversityService 
{
    @Autowired
    private KyUniversityMapper kyUniversityMapper;

    /**
     * 查询院校信息
     * 
     * @param id 院校信息主键
     * @return 院校信息
     */
    @Override
    public KyUniversity selectKyUniversityById(Long id)
    {
        return kyUniversityMapper.selectKyUniversityById(id);
    }

    /**
     * 查询院校信息列表
     * 
     * @param kyUniversity 院校信息
     * @return 院校信息
     */
    @Override
    public List<KyUniversity> selectKyUniversityList(KyUniversity kyUniversity)
    {
        return kyUniversityMapper.selectKyUniversityList(kyUniversity);
    }

    /**
     * 新增院校信息
     * 
     * @param kyUniversity 院校信息
     * @return 结果
     */
    @Override
    public int insertKyUniversity(KyUniversity kyUniversity)
    {
        return kyUniversityMapper.insertKyUniversity(kyUniversity);
    }

    /**
     * 修改院校信息
     * 
     * @param kyUniversity 院校信息
     * @return 结果
     */
    @Override
    public int updateKyUniversity(KyUniversity kyUniversity)
    {
        return kyUniversityMapper.updateKyUniversity(kyUniversity);
    }

    /**
     * 批量删除院校信息
     * 
     * @param ids 需要删除的院校信息主键
     * @return 结果
     */
    @Override
    public int deleteKyUniversityByIds(Long[] ids)
    {
        return kyUniversityMapper.deleteKyUniversityByIds(ids);
    }

    /**
     * 删除院校信息信息
     * 
     * @param id 院校信息主键
     * @return 结果
     */
    @Override
    public int deleteKyUniversityById(Long id)
    {
        return kyUniversityMapper.deleteKyUniversityById(id);
    }
}
