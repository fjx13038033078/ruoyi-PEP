package com.ruoyi.preparation.mapper;

import java.util.List;
import com.ruoyi.preparation.domain.KyComment;
import org.apache.ibatis.annotations.Param;

/**
 * 通用评论Mapper接口
 * 
 * @author ruoyi
 */
public interface KyCommentMapper 
{
    /**
     * 查询通用评论
     * 
     * @param id 通用评论主键
     * @return 通用评论
     */
    public KyComment selectKyCommentById(Long id);

    /**
     * 查询通用评论列表
     * 
     * @param kyComment 通用评论
     * @return 通用评论集合
     */
    public List<KyComment> selectKyCommentList(KyComment kyComment);

    /**
     * 查询目标的评论列表（树形结构）
     * 
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 评论列表
     */
    public List<KyComment> selectCommentsByTarget(@Param("targetId") Long targetId, @Param("targetType") String targetType);

    /**
     * 新增通用评论
     * 
     * @param kyComment 通用评论
     * @return 结果
     */
    public int insertKyComment(KyComment kyComment);

    /**
     * 修改通用评论
     * 
     * @param kyComment 通用评论
     * @return 结果
     */
    public int updateKyComment(KyComment kyComment);

    /**
     * 删除通用评论
     * 
     * @param id 通用评论主键
     * @return 结果
     */
    public int deleteKyCommentById(Long id);

    /**
     * 批量删除通用评论
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteKyCommentByIds(Long[] ids);

    /**
     * 统计目标的评论数量
     * 
     * @param targetId 目标ID
     * @param targetType 目标类型
     * @return 评论数量
     */
    public int countByTarget(@Param("targetId") Long targetId, @Param("targetType") String targetType);
}
