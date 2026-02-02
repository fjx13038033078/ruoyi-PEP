package com.ruoyi.preparation.mapper;

import java.util.List;
import java.util.Map;
import com.ruoyi.preparation.domain.KyBrowseLog;
import org.apache.ibatis.annotations.Param;

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
    public KyBrowseLog selectByUserAndTarget(@Param("userId") Long userId, @Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 查询用户某类型的浏览记录（用于推荐）
     * 
     * @param userId 用户ID
     * @param targetType 目标类型
     * @return 浏览记录列表
     */
    public List<Map<String, Object>> selectUserBrowseByType(@Param("userId") Long userId, @Param("targetType") String targetType);

    /**
     * 查询所有有浏览记录的用户ID
     * 
     * @return 用户ID列表
     */
    public List<Long> selectDistinctUserIds();

    /**
     * 查询用户自己的浏览记录列表
     * 
     * @param kyBrowseLog 查询条件
     * @return 浏览记录列表
     */
    public List<KyBrowseLog> selectMyBrowseLogList(KyBrowseLog kyBrowseLog);

    /**
     * 查询所有用户的资料浏览记录（用于推荐算法）
     * 
     * @return 浏览记录汇总列表
     */
    public List<Map<String, Object>> selectAllBrowseForRecommend();
}
