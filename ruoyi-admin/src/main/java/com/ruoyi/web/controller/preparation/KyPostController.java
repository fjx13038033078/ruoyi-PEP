package com.ruoyi.web.controller.preparation;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.preparation.domain.KyPost;
import com.ruoyi.preparation.service.IKyPostService;
import com.ruoyi.preparation.service.IKyBrowseLogService;

/**
 * 论坛帖子Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/preparation/post")
public class KyPostController extends BaseController
{
    @Autowired
    private IKyPostService kyPostService;

    @Autowired
    private IKyBrowseLogService kyBrowseLogService;

    /** 目标类型：帖子 */
    private static final String TARGET_TYPE_POST = "2";

    /**
     * 查询论坛帖子列表
     */
    @GetMapping("/list")
    public TableDataInfo list(KyPost kyPost)
    {
        startPage();
        List<KyPost> list = kyPostService.selectKyPostList(kyPost);
        return getDataTable(list);
    }

    /**
     * 导出论坛帖子列表
     */
    @Log(title = "论坛帖子", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KyPost kyPost)
    {
        List<KyPost> list = kyPostService.selectKyPostList(kyPost);
        ExcelUtil<KyPost> util = new ExcelUtil<KyPost>(KyPost.class);
        util.exportExcel(response, list, "论坛帖子数据");
    }

    /**
     * 获取论坛帖子详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        // 更新浏览量
        kyPostService.updateViewNum(id);
        // 记录浏览日志
        try
        {
            kyBrowseLogService.recordBrowse(getUserId(), id, TARGET_TYPE_POST);
        }
        catch (Exception e)
        {
            // 浏览记录失败不影响主流程
        }
        return success(kyPostService.selectKyPostById(id));
    }

    /**
     * 新增论坛帖子
     */
    @Log(title = "论坛帖子", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KyPost kyPost)
    {
        kyPost.setUserId(getUserId());
        kyPost.setCreateBy(getUsername());
        return toAjax(kyPostService.insertKyPost(kyPost));
    }

    /**
     * 修改论坛帖子
     */
    @Log(title = "论坛帖子", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KyPost kyPost)
    {
        kyPost.setUpdateBy(getUsername());
        return toAjax(kyPostService.updateKyPost(kyPost));
    }

    /**
     * 删除论坛帖子
     */
    @Log(title = "论坛帖子", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(kyPostService.deleteKyPostByIds(ids));
    }

    /**
     * 查询回收站帖子列表
     */
    @GetMapping("/recycle/list")
    public TableDataInfo recycleList(KyPost kyPost)
    {
        startPage();
        List<KyPost> list = kyPostService.selectDeletedPostList(kyPost);
        return getDataTable(list);
    }

    /**
     * 还原帖子
     */
    @Log(title = "帖子回收站", businessType = BusinessType.UPDATE)
    @PutMapping("/recycle/restore/{ids}")
    public AjaxResult restore(@PathVariable Long[] ids)
    {
        return toAjax(kyPostService.restoreKyPostByIds(ids));
    }

    /**
     * 彻底删除帖子
     */
    @Log(title = "帖子回收站", businessType = BusinessType.DELETE)
    @DeleteMapping("/recycle/destroy/{ids}")
    public AjaxResult destroy(@PathVariable Long[] ids)
    {
        return toAjax(kyPostService.destroyKyPostByIds(ids));
    }
}
