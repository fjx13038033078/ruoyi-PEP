package com.ruoyi.preparation.mapper;

import java.util.List;
import com.ruoyi.preparation.domain.KyMaterial;
import org.apache.ibatis.annotations.Param;

/**
 * 共享资料Mapper接口
 * 
 * @author ruoyi
 */
public interface KyMaterialMapper 
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
     * 删除共享资料
     * 
     * @param id 共享资料主键
     * @return 结果
     */
    public int deleteKyMaterialById(Long id);

    /**
     * 批量删除共享资料
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKyMaterialByIds(Long[] ids);

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

    /**
     * 更新点赞次数
     * 
     * @param id 资料ID
     * @param count 变化数量（正数增加，负数减少）
     * @return 结果
     */
    public int updateLikeCount(Long id);

    /**
     * 更新评论次数
     * 
     * @param id 资料ID
     * @param count 变化数量
     * @return 结果
     */
    public int updateCommentCount(Long id);

    /**
     * 根据ID列表查询资料
     * 
     * @param ids 资料ID列表
     * @return 资料列表
     */
    public List<KyMaterial> selectMaterialsByIds(@Param("ids") List<Long> ids);

    /**
     * 查询热门资料（按浏览、点赞、收藏加权排序）
     * 
     * @param limit 数量限制
     * @return 热门资料列表
     */
    public List<KyMaterial> selectHotMaterials(@Param("limit") int limit);
}
