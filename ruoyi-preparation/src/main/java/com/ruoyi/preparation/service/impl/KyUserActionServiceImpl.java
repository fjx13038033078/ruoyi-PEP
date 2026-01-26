package com.ruoyi.preparation.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.preparation.mapper.KyUserActionMapper;
import com.ruoyi.preparation.mapper.KyMaterialMapper;
import com.ruoyi.preparation.mapper.KyPostMapper;
import com.ruoyi.preparation.domain.KyUserAction;
import com.ruoyi.preparation.service.IKyUserActionService;

/**
 * 用户互动Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class KyUserActionServiceImpl implements IKyUserActionService 
{
    @Autowired
    private KyUserActionMapper kyUserActionMapper;

    @Autowired
    private KyMaterialMapper kyMaterialMapper;

    @Autowired
    private KyPostMapper kyPostMapper;

    /** 目标类型：资料 */
    private static final String TARGET_TYPE_MATERIAL = "1";
    /** 目标类型：帖子 */
    private static final String TARGET_TYPE_POST = "2";
    /** 动作类型：点赞 */
    private static final String ACTION_TYPE_LIKE = "1";
    /** 动作类型：收藏 */
    private static final String ACTION_TYPE_COLLECT = "2";
    /** 动作类型：下载 */
    private static final String ACTION_TYPE_DOWNLOAD = "3";

    /**
     * 查询用户互动
     * 
     * @param id 用户互动主键
     * @return 用户互动
     */
    @Override
    public KyUserAction selectKyUserActionById(Long id)
    {
        return kyUserActionMapper.selectKyUserActionById(id);
    }

    /**
     * 查询用户互动列表
     * 
     * @param kyUserAction 用户互动
     * @return 用户互动
     */
    @Override
    public List<KyUserAction> selectKyUserActionList(KyUserAction kyUserAction)
    {
        return kyUserActionMapper.selectKyUserActionList(kyUserAction);
    }

    /**
     * 新增用户互动
     * 
     * @param kyUserAction 用户互动
     * @return 结果
     */
    @Override
    public int insertKyUserAction(KyUserAction kyUserAction)
    {
        return kyUserActionMapper.insertKyUserAction(kyUserAction);
    }

    /**
     * 修改用户互动
     * 
     * @param kyUserAction 用户互动
     * @return 结果
     */
    @Override
    public int updateKyUserAction(KyUserAction kyUserAction)
    {
        return kyUserActionMapper.updateKyUserAction(kyUserAction);
    }

    /**
     * 批量删除用户互动
     * 
     * @param ids 需要删除的用户互动主键
     * @return 结果
     */
    @Override
    public int deleteKyUserActionByIds(Long[] ids)
    {
        return kyUserActionMapper.deleteKyUserActionByIds(ids);
    }

    /**
     * 删除用户互动信息
     * 
     * @param id 用户互动主键
     * @return 结果
     */
    @Override
    public int deleteKyUserActionById(Long id)
    {
        return kyUserActionMapper.deleteKyUserActionById(id);
    }

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
    @Override
    @Transactional
    public int doAction(Long userId, Long targetId, String targetType, String actionType)
    {
        KyUserAction existAction = kyUserActionMapper.selectByUserAction(userId, targetId, targetType, actionType);
        if (existAction != null)
        {
            // 已存在，取消互动
            kyUserActionMapper.deleteByUserAction(userId, targetId, targetType, actionType);
            // 更新目标统计数（需要根据类型处理）
            updateTargetCount(targetId, targetType, actionType, false);
            return -1;
        }
        else
        {
            // 不存在，新增互动
            KyUserAction action = new KyUserAction();
            action.setUserId(userId);
            action.setTargetId(targetId);
            action.setTargetType(targetType);
            action.setActionType(actionType);
            kyUserActionMapper.insertKyUserAction(action);
            // 更新目标统计数
            updateTargetCount(targetId, targetType, actionType, true);
            return 1;
        }
    }

    /**
     * 更新目标的统计数
     */
    private void updateTargetCount(Long targetId, String targetType, String actionType, boolean isAdd)
    {
        if (TARGET_TYPE_MATERIAL.equals(targetType))
        {
            // 资料
            if (ACTION_TYPE_LIKE.equals(actionType))
            {
                kyMaterialMapper.updateLikeCount(targetId);
            }
            else if (ACTION_TYPE_DOWNLOAD.equals(actionType) && isAdd)
            {
                kyMaterialMapper.updateDownCount(targetId);
            }
        }
        else if (TARGET_TYPE_POST.equals(targetType))
        {
            // 帖子
            if (ACTION_TYPE_LIKE.equals(actionType))
            {
                kyPostMapper.updateLikeNum(targetId);
            }
            else if (ACTION_TYPE_COLLECT.equals(actionType))
            {
                kyPostMapper.updateCollectNum(targetId);
            }
        }
    }

    /**
     * 检查用户是否已执行某个互动
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @param actionType 动作类型
     * @return 是否已执行
     */
    @Override
    public boolean checkAction(Long userId, Long targetId, String targetType, String actionType)
    {
        KyUserAction action = kyUserActionMapper.selectByUserAction(userId, targetId, targetType, actionType);
        return action != null;
    }
}
