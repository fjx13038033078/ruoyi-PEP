package com.ruoyi.preparation.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.preparation.mapper.KyMaterialMapper;
import com.ruoyi.preparation.domain.KyMaterial;
import com.ruoyi.preparation.service.IKyMaterialService;

/**
 * 共享资料Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class KyMaterialServiceImpl implements IKyMaterialService 
{
    @Autowired
    private KyMaterialMapper kyMaterialMapper;

    /**
     * 查询共享资料
     * 
     * @param id 共享资料主键
     * @return 共享资料
     */
    @Override
    public KyMaterial selectKyMaterialById(Long id)
    {
        return kyMaterialMapper.selectKyMaterialById(id);
    }

    /**
     * 查询共享资料列表
     * 
     * @param kyMaterial 共享资料
     * @return 共享资料
     */
    @Override
    public List<KyMaterial> selectKyMaterialList(KyMaterial kyMaterial)
    {
        return kyMaterialMapper.selectKyMaterialList(kyMaterial);
    }

    /**
     * 新增共享资料
     * 
     * @param kyMaterial 共享资料
     * @return 结果
     */
    @Override
    public int insertKyMaterial(KyMaterial kyMaterial)
    {
        return kyMaterialMapper.insertKyMaterial(kyMaterial);
    }

    /**
     * 修改共享资料
     * 
     * @param kyMaterial 共享资料
     * @return 结果
     */
    @Override
    public int updateKyMaterial(KyMaterial kyMaterial)
    {
        return kyMaterialMapper.updateKyMaterial(kyMaterial);
    }

    /**
     * 批量删除共享资料
     * 
     * @param ids 需要删除的共享资料主键
     * @return 结果
     */
    @Override
    public int deleteKyMaterialByIds(Long[] ids)
    {
        return kyMaterialMapper.deleteKyMaterialByIds(ids);
    }

    /**
     * 删除共享资料信息
     * 
     * @param id 共享资料主键
     * @return 结果
     */
    @Override
    public int deleteKyMaterialById(Long id)
    {
        return kyMaterialMapper.deleteKyMaterialById(id);
    }

    /**
     * 更新浏览次数
     * 
     * @param id 资料ID
     * @return 结果
     */
    @Override
    public int updateViewCount(Long id)
    {
        return kyMaterialMapper.updateViewCount(id);
    }

    /**
     * 更新下载次数
     * 
     * @param id 资料ID
     * @return 结果
     */
    @Override
    public int updateDownCount(Long id)
    {
        return kyMaterialMapper.updateDownCount(id);
    }
}
