package com.ruoyi.preparation.service;

import java.util.List;
import com.ruoyi.preparation.domain.KyUserAction;

/**
 * 用户互动Service接口
 * 
 * @author ruoyi
 */
public interface IKyUserActionService 
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
     * 批量删除用户互动
     * 
     * @param ids 需要删除的用户互动主键集合
     * @return 结果
     */
    public int deleteKyUserActionByIds(Long[] ids);

    /**
     * 删除用户互动信息
     * 
     * @param id 用户互动主键
     * @return 结果
     */
    public int deleteKyUserActionById(Long id);

    /**
     * 执行互动（点赞/收藏/下载）
     * 如果已存在则取消，不存在则新增
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param actionType 动作类型
     * @return 1表示新增成功，-1表示取消成功
     */
    public int doAction(Long userId, Long targetId, String targetType, String actionType);

    /**
     * 检查用户是否已执行某个互动
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param actionType 动作类型
     * @return 是否已执行
     */
    public boolean checkAction(Long userId, Long targetId, String targetType, String actionType);
}
