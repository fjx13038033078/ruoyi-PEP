package com.ruoyi.preparation.service;

import java.util.List;
import com.ruoyi.preparation.domain.KyMaterial;

/**
 * 共享资料Service接口
 * 
 * @author ruoyi
 */
public interface IKyMaterialService 
{
    /**
     * 查询共享资料
     * 
     * @param id 共享资料主键
     * @return 共享资料
     */
    public KyMaterial selectKyMaterialById(Long id);

    /**
     * 查询共享资料列表
     * 
     * @param kyMaterial 共享资料
     * @return 共享资料集合
     */
    public List<KyMaterial> selectKyMaterialList(KyMaterial kyMaterial);

    /**
     * 新增共享资料
     * 
     * @param kyMaterial 共享资料
     * @return 结果
     */
    public int insertKyMaterial(KyMaterial kyMaterial);

    /**
     * 修改共享资料
     * 
     * @param kyMaterial 共享资料
     * @return 结果
     */
    public int updateKyMaterial(KyMaterial kyMaterial);

    /**
     * 批量删除共享资料
     * 
     * @param ids 需要删除的共享资料主键集合
     * @return 结果
     */
    public int deleteKyMaterialByIds(Long[] ids);

    /**
     * 删除共享资料信息
     * 
     * @param id 共享资料主键
     * @return 结果
     */
    public int deleteKyMaterialById(Long id);

    /**
     * 更新浏览次数
     * 
     * @param id 资料ID
     * @return 结果
     */
    public int updateViewCount(Long id);

    /**
     * 更新下载次数
     * 
     * @param id 资料ID
     * @return 结果
     */
    public int updateDownCount(Long id);
}
