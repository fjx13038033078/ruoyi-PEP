package com.ruoyi.preparation.service;

import java.util.List;
import com.ruoyi.preparation.domain.KyBrowseLog;

/**
 * 用户浏览记录Service接口
 * 
 * @author ruoyi
 */
public interface IKyBrowseLogService 
{
    /**
     * 查询用户浏览记录
     * 
     * @param id 用户浏览记录主键
     * @return 用户浏览记录
     */
    public KyBrowseLog selectKyBrowseLogById(Long id);

    /**
     * 查询用户浏览记录列表
     * 
     * @param kyBrowseLog 用户浏览记录
     * @return 用户浏览记录集合
     */
    public List<KyBrowseLog> selectKyBrowseLogList(KyBrowseLog kyBrowseLog);

    /**
     * 新增用户浏览记录
     * 
     * @param kyBrowseLog 用户浏览记录
     * @return 结果
     */
    public int insertKyBrowseLog(KyBrowseLog kyBrowseLog);

    /**
     * 修改用户浏览记录
     * 
     * @param kyBrowseLog 用户浏览记录
     * @return 结果
     */
    public int updateKyBrowseLog(KyBrowseLog kyBrowseLog);

    /**
     * 批量删除用户浏览记录
     * 
     * @param ids 需要删除的用户浏览记录主键集合
     * @return 结果
     */
    public int deleteKyBrowseLogByIds(Long[] ids);

    /**
     * 删除用户浏览记录信息
     * 
     * @param id 用户浏览记录主键
     * @return 结果
     */
    public int deleteKyBrowseLogById(Long id);

    /**
     * 记录用户浏览（如果已存在则更新时间）
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 结果
     */
    public int recordBrowse(Long userId, Long targetId, String targetType);
}
