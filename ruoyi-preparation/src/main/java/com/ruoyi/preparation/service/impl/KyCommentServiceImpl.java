package com.ruoyi.preparation.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.preparation.mapper.KyCommentMapper;
import com.ruoyi.preparation.domain.KyComment;
import com.ruoyi.preparation.service.IKyCommentService;

/**
 * 通用评论Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class KyCommentServiceImpl implements IKyCommentService 
{
    @Autowired
    private KyCommentMapper kyCommentMapper;

    /**
     * 查询通用评论
     * 
     * @param id 通用评论主键
     * @return 通用评论
     */
    @Override
    public KyComment selectKyCommentById(Long id)
    {
        return kyCommentMapper.selectKyCommentById(id);
    }

    /**
     * 查询通用评论列表
     * 
     * @param kyComment 通用评论
     * @return 通用评论
     */
    @Override
    public List<KyComment> selectKyCommentList(KyComment kyComment)
    {
        return kyCommentMapper.selectKyCommentList(kyComment);
    }

    /**
     * 查询目标的评论列表（树形结构）
     * 
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 评论列表
     */
    @Override
    public List<KyComment> selectCommentTree(Long targetId, String targetType)
    {
        List<KyComment> allComments = kyCommentMapper.selectCommentsByTarget(targetId, targetType);
        // 构建树形结构
        List<KyComment> rootComments = allComments.stream()
                .filter(c -> c.getParentId() == null || c.getParentId() == 0L)
                .collect(Collectors.toList());
        
        for (KyComment root : rootComments)
        {
            root.setChildren(getChildren(root.getId(), allComments));
        }
        return rootComments;
    }

    /**
     * 递归获取子评论
     */
    private List<KyComment> getChildren(Long parentId, List<KyComment> allComments)
    {
        List<KyComment> children = allComments.stream()
                .filter(c -> parentId.equals(c.getParentId()))
                .collect(Collectors.toList());
        
        for (KyComment child : children)
        {
            child.setChildren(getChildren(child.getId(), allComments));
        }
        return children.isEmpty() ? null : children;
    }

    /**
     * 新增通用评论
     * 
     * @param kyComment 通用评论
     * @return 结果
     */
    @Override
    public int insertKyComment(KyComment kyComment)
    {
        return kyCommentMapper.insertKyComment(kyComment);
    }

    /**
     * 修改通用评论
     * 
     * @param kyComment 通用评论
     * @return 结果
     */
    @Override
    public int updateKyComment(KyComment kyComment)
    {
        return kyCommentMapper.updateKyComment(kyComment);
    }

    /**
     * 批量删除通用评论
     * 
     * @param ids 需要删除的通用评论主键
     * @return 结果
     */
    @Override
    public int deleteKyCommentByIds(Long[] ids)
    {
        return kyCommentMapper.deleteKyCommentByIds(ids);
    }

    /**
     * 删除通用评论信息
     * 
     * @param id 通用评论主键
     * @return 结果
     */
    @Override
    public int deleteKyCommentById(Long id)
    {
        return kyCommentMapper.deleteKyCommentById(id);
    }
}
