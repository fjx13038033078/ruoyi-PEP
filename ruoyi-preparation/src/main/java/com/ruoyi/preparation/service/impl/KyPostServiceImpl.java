package com.ruoyi.preparation.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.preparation.mapper.KyPostMapper;
import com.ruoyi.preparation.domain.KyPost;
import com.ruoyi.preparation.service.IKyPostService;

/**
 * 论坛帖子Service业务层处理
 * 
 * @author ruoyi
 */
@Service
public class KyPostServiceImpl implements IKyPostService 
{
    @Autowired
    private KyPostMapper kyPostMapper;

    /**
     * 查询论坛帖子
     * 
     * @param id 论坛帖子主键
     * @return 论坛帖子
     */
    @Override
    public KyPost selectKyPostById(Long id)
    {
        return kyPostMapper.selectKyPostById(id);
    }

    /**
     * 查询论坛帖子列表
     * 
     * @param kyPost 论坛帖子
     * @return 论坛帖子
     */
    @Override
    public List<KyPost> selectKyPostList(KyPost kyPost)
    {
        return kyPostMapper.selectKyPostList(kyPost);
    }

    /**
     * 新增论坛帖子
     * 
     * @param kyPost 论坛帖子
     * @return 结果
     */
    @Override
    public int insertKyPost(KyPost kyPost)
    {
        return kyPostMapper.insertKyPost(kyPost);
    }

    /**
     * 修改论坛帖子
     * 
     * @param kyPost 论坛帖子
     * @return 结果
     */
    @Override
    public int updateKyPost(KyPost kyPost)
    {
        return kyPostMapper.updateKyPost(kyPost);
    }

    /**
     * 批量删除论坛帖子
     * 
     * @param ids 需要删除的论坛帖子主键
     * @return 结果
     */
    @Override
    public int deleteKyPostByIds(Long[] ids)
    {
        return kyPostMapper.deleteKyPostByIds(ids);
    }

    /**
     * 删除论坛帖子信息
     * 
     * @param id 论坛帖子主键
     * @return 结果
     */
    @Override
    public int deleteKyPostById(Long id)
    {
        return kyPostMapper.deleteKyPostById(id);
    }

    /**
     * 更新浏览量
     * 
     * @param id 帖子ID
     * @return 结果
     */
    @Override
    public int updateViewNum(Long id)
    {
        return kyPostMapper.updateViewNum(id);
    }

    /**
     * 查询回收站帖子列表
     * 
     * @param kyPost 论坛帖子
     * @return 论坛帖子
     */
    @Override
    public List<KyPost> selectDeletedPostList(KyPost kyPost)
    {
        return kyPostMapper.selectDeletedPostList(kyPost);
    }

    /**
     * 还原帖子
     * 
     * @param ids 需要还原的数据主键集合
     * @return 结果
     */
    @Override
    public int restoreKyPostByIds(Long[] ids)
    {
        return kyPostMapper.restoreKyPostByIds(ids);
    }

    /**
     * 彻底删除帖子
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    @Override
    public int destroyKyPostByIds(Long[] ids)
    {
        return kyPostMapper.destroyKyPostByIds(ids);
    }
}
