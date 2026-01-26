package com.ruoyi.preparation.service;

import java.util.List;
import com.ruoyi.preparation.domain.KyPost;

/**
 * 论坛帖子Service接口
 * 
 * @author ruoyi
 */
public interface IKyPostService 
{
    /**
     * 查询论坛帖子
     * 
     * @param id 论坛帖子主键
     * @return 论坛帖子
     */
    public KyPost selectKyPostById(Long id);

    /**
     * 查询论坛帖子列表
     * 
     * @param kyPost 论坛帖子
     * @return 论坛帖子集合
     */
    public List<KyPost> selectKyPostList(KyPost kyPost);

    /**
     * 新增论坛帖子
     * 
     * @param kyPost 论坛帖子
     * @return 结果
     */
    public int insertKyPost(KyPost kyPost);

    /**
     * 修改论坛帖子
     * 
     * @param kyPost 论坛帖子
     * @return 结果
     */
    public int updateKyPost(KyPost kyPost);

    /**
     * 批量删除论坛帖子
     * 
     * @param ids 需要删除的论坛帖子主键集合
     * @return 结果
     */
    public int deleteKyPostByIds(Long[] ids);

    /**
     * 删除论坛帖子信息
     * 
     * @param id 论坛帖子主键
     * @return 结果
     */
    public int deleteKyPostById(Long id);

    /**
     * 更新浏览量
     * 
     * @param id 帖子ID
     * @return 结果
     */
    public int updateViewNum(Long id);
}
