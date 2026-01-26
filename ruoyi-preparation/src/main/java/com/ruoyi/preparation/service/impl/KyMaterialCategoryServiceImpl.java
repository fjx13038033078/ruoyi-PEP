package com.ruoyi.preparation.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.preparation.mapper.KyMaterialCategoryMapper;
import com.ruoyi.preparation.domain.KyMaterialCategory;
import com.ruoyi.preparation.service.IKyMaterialCategoryService;

/**
 * 资料分类Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class KyMaterialCategoryServiceImpl implements IKyMaterialCategoryService 
{
    @Autowired
    private KyMaterialCategoryMapper kyMaterialCategoryMapper;

    /**
     * 查询资料分类
     * 
     * @param id 资料分类主键
     * @return 资料分类
     */
    @Override
    public KyMaterialCategory selectKyMaterialCategoryById(Long id)
    {
        return kyMaterialCategoryMapper.selectKyMaterialCategoryById(id);
    }

    /**
     * 查询资料分类列表
     * 
     * @param kyMaterialCategory 资料分类
     * @return 资料分类
     */
    @Override
    public List<KyMaterialCategory> selectKyMaterialCategoryList(KyMaterialCategory kyMaterialCategory)
    {
        return kyMaterialCategoryMapper.selectKyMaterialCategoryList(kyMaterialCategory);
    }

    /**
     * 新增资料分类
     * 
     * @param kyMaterialCategory 资料分类
     * @return 结果
     */
    @Override
    public int insertKyMaterialCategory(KyMaterialCategory kyMaterialCategory)
    {
        return kyMaterialCategoryMapper.insertKyMaterialCategory(kyMaterialCategory);
    }

    /**
     * 修改资料分类
     * 
     * @param kyMaterialCategory 资料分类
     * @return 结果
     */
    @Override
    public int updateKyMaterialCategory(KyMaterialCategory kyMaterialCategory)
    {
        return kyMaterialCategoryMapper.updateKyMaterialCategory(kyMaterialCategory);
    }

    /**
     * 批量删除资料分类
     * 
     * @param ids 需要删除的资料分类主键
     * @return 结果
     */
    @Override
    public int deleteKyMaterialCategoryByIds(Long[] ids)
    {
        return kyMaterialCategoryMapper.deleteKyMaterialCategoryByIds(ids);
    }

    /**
     * 删除资料分类信息
     * 
     * @param id 资料分类主键
     * @return 结果
     */
    @Override
    public int deleteKyMaterialCategoryById(Long id)
    {
        return kyMaterialCategoryMapper.deleteKyMaterialCategoryById(id);
    }
}
