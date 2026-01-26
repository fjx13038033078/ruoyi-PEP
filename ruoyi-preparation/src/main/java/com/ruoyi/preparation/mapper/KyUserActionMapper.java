package com.ruoyi.preparation.mapper;

import java.util.List;
import com.ruoyi.preparation.domain.KyUserAction;
import org.apache.ibatis.annotations.Param;

/**
 * 用户互动Mapper接口
 * 
 * @author ruoyi
 */
public interface KyUserActionMapper 
{
    /**
     * 查询用户互动
     * 
     * @param id 用户互动主键
     * @return 用户互动
     */
    public KyUserAction selectKyUserActionById(Long id);

    /**
     * 查询用户互动列表
     * 
     * @param kyUserAction 用户互动
     * @return 用户互动集合
     */
    public List<KyUserAction> selectKyUserActionList(KyUserAction kyUserAction);

    /**
     * 新增用户互动
     * 
     * @param kyUserAction 用户互动
     * @return 结果
     */
    public int insertKyUserAction(KyUserAction kyUserAction);

    /**
     * 修改用户互动
     * 
     * @param kyUserAction 用户互动
     * @return 结果
     */
    public int updateKyUserAction(KyUserAction kyUserAction);

    /**
     * 删除用户互动
     * 
     * @param id 用户互动主键
     * @return 结果
     */
    public int deleteKyUserActionById(Long id);

    /**
     * 批量删除用户互动
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKyUserActionByIds(Long[] ids);

    /**
     * 查询用户对某个目标的某种互动
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param actionType 动作类型
     * @return 用户互动
     */
    public KyUserAction selectByUserAction(@Param("userId") Long userId, @Param("targetId") Long targetId, 
                                           @Param("targetType") String targetType, @Param("actionType") String actionType);

    /**
     * 删除用户对某个目标的某种互动
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param actionType 动作类型
     * @return 结果
     */
    public int deleteByUserAction(@Param("userId") Long userId, @Param("targetId") Long targetId, 
                                  @Param("targetType") String targetType, @Param("actionType") String actionType);

    /**
     * 统计某个目标的互动数量
     * 
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param actionType 动作类型
     * @return 互动数量
     */
    public int countByTargetAction(@Param("targetId") Long targetId, @Param("targetType") String targetType, 
                                   @Param("actionType") String actionType);
}
