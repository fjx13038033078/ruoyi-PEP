package com.ruoyi.preparation.mapper;

import java.util.List;
import com.ruoyi.preparation.domain.KyBrowseLog;

/**
 * 用户浏览记录Mapper接口
 * 
 * @author ruoyi
 */
public interface KyBrowseLogMapper 
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
     * 删除用户浏览记录
     * 
     * @param id 用户浏览记录主键
     * @return 结果
     */
    public int deleteKyBrowseLogById(Long id);

    /**
     * 批量删除用户浏览记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKyBrowseLogByIds(Long[] ids);

    /**
     * 查询用户对某个目标的浏览记录
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 浏览记录
     */
    public KyBrowseLog selectByUserAndTarget(Long userId, Long targetId, String targetType);
}
