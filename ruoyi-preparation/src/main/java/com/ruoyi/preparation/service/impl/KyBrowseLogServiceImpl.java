package com.ruoyi.preparation.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.preparation.mapper.KyBrowseLogMapper;
import com.ruoyi.preparation.domain.KyBrowseLog;
import com.ruoyi.preparation.service.IKyBrowseLogService;

/**
 * 用户浏览记录Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class KyBrowseLogServiceImpl implements IKyBrowseLogService 
{
    @Autowired
    private KyBrowseLogMapper kyBrowseLogMapper;

    /**
     * 查询用户浏览记录
     * 
     * @param id 用户浏览记录主键
     * @return 用户浏览记录
     */
    @Override
    public KyBrowseLog selectKyBrowseLogById(Long id)
    {
        return kyBrowseLogMapper.selectKyBrowseLogById(id);
    }

    /**
     * 查询用户浏览记录列表
     * 
     * @param kyBrowseLog 用户浏览记录
     * @return 用户浏览记录
     */
    @Override
    public List<KyBrowseLog> selectKyBrowseLogList(KyBrowseLog kyBrowseLog)
    {
        return kyBrowseLogMapper.selectKyBrowseLogList(kyBrowseLog);
    }

    /**
     * 新增用户浏览记录
     * 
     * @param kyBrowseLog 用户浏览记录
     * @return 结果
     */
    @Override
    public int insertKyBrowseLog(KyBrowseLog kyBrowseLog)
    {
        return kyBrowseLogMapper.insertKyBrowseLog(kyBrowseLog);
    }

    /**
     * 修改用户浏览记录
     * 
     * @param kyBrowseLog 用户浏览记录
     * @return 结果
     */
    @Override
    public int updateKyBrowseLog(KyBrowseLog kyBrowseLog)
    {
        return kyBrowseLogMapper.updateKyBrowseLog(kyBrowseLog);
    }

    /**
     * 批量删除用户浏览记录
     * 
     * @param ids 需要删除的用户浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteKyBrowseLogByIds(Long[] ids)
    {
        return kyBrowseLogMapper.deleteKyBrowseLogByIds(ids);
    }

    /**
     * 删除用户浏览记录信息
     * 
     * @param id 用户浏览记录主键
     * @return 结果
     */
    @Override
    public int deleteKyBrowseLogById(Long id)
    {
        return kyBrowseLogMapper.deleteKyBrowseLogById(id);
    }

    /**
     * 记录用户浏览（如果已存在则更新时间）
     * 
     * @param userId 用户ID
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 结果
     */
    @Override
    public int recordBrowse(Long userId, Long targetId, String targetType)
    {
        KyBrowseLog existLog = kyBrowseLogMapper.selectByUserAndTarget(userId, targetId, targetType);
        if (existLog != null)
        {
            // 已存在，更新浏览时间
            return kyBrowseLogMapper.updateKyBrowseLog(existLog);
        }
        else
        {
            // 不存在，新增记录
            KyBrowseLog browseLog = new KyBrowseLog();
            browseLog.setUserId(userId);
            browseLog.setTargetId(targetId);
            browseLog.setTargetType(targetType);
            return kyBrowseLogMapper.insertKyBrowseLog(browseLog);
        }
    }
}
