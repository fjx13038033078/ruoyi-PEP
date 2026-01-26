package com.ruoyi.preparation.service;

import java.util.List;
import com.ruoyi.preparation.domain.KyMaterialCategory;

/**
 * 资料分类Service接口
 * 
 * @author ruoyi
 */
public interface IKyMaterialCategoryService 
{
    /**
     * 查询资料分类
     * 
     * @param id 资料分类主键
     * @return 资料分类
     */
    public KyMaterialCategory selectKyMaterialCategoryById(Long id);

    /**
     * 查询资料分类列表
     * 
     * @param kyMaterialCategory 资料分类
     * @return 资料分类集合
     */
    public List<KyMaterialCategory> selectKyMaterialCategoryList(KyMaterialCategory kyMaterialCategory);

    /**
     * 新增资料分类
     * 
     * @param kyMaterialCategory 资料分类
     * @return 结果
     */
    public int insertKyMaterialCategory(KyMaterialCategory kyMaterialCategory);

    /**
     * 修改资料分类
     * 
     * @param kyMaterialCategory 资料分类
     * @return 结果
     */
    public int updateKyMaterialCategory(KyMaterialCategory kyMaterialCategory);

    /**
     * 批量删除资料分类
     * 
     * @param ids 需要删除的资料分类主键集合
     * @return 结果
     */
    public int deleteKyMaterialCategoryByIds(Long[] ids);

    /**
     * 删除资料分类信息
     * 
     * @param id 资料分类主键
     * @return 结果
     */
    public int deleteKyMaterialCategoryById(Long id);
}
