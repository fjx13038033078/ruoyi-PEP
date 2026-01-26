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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.preparation.domain.KyComment;
import com.ruoyi.preparation.service.IKyCommentService;

/**
 * 通用评论Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/preparation/comment")
public class KyCommentController extends BaseController
{
    @Autowired
    private IKyCommentService kyCommentService;

    /**
     * 查询通用评论列表
     */
    @GetMapping("/list")
    public TableDataInfo list(KyComment kyComment)
    {
        startPage();
        List<KyComment> list = kyCommentService.selectKyCommentList(kyComment);
        return getDataTable(list);
    }

    /**
     * 查询目标的评论列表（树形结构）
     */
    @GetMapping("/tree")
    public AjaxResult tree(@RequestParam("targetId") Long targetId, @RequestParam("targetType") String targetType)
    {
        List<KyComment> list = kyCommentService.selectCommentTree(targetId, targetType);
        return success(list);
    }

    /**
     * 导出通用评论列表
     */
    @Log(title = "通用评论", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KyComment kyComment)
    {
        List<KyComment> list = kyCommentService.selectKyCommentList(kyComment);
        ExcelUtil<KyComment> util = new ExcelUtil<KyComment>(KyComment.class);
        util.exportExcel(response, list, "通用评论数据");
    }

    /**
     * 获取通用评论详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(kyCommentService.selectKyCommentById(id));
    }

    /**
     * 新增通用评论
     */
    @Log(title = "通用评论", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KyComment kyComment)
    {
        kyComment.setUserId(getUserId());
        return toAjax(kyCommentService.insertKyComment(kyComment));
    }

    /**
     * 修改通用评论
     */
    @Log(title = "通用评论", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KyComment kyComment)
    {
        return toAjax(kyCommentService.updateKyComment(kyComment));
    }

    /**
     * 删除通用评论
     */
    @Log(title = "通用评论", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(kyCommentService.deleteKyCommentByIds(ids));
    }
}
