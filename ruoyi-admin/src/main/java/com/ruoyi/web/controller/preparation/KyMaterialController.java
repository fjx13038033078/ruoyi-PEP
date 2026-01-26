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
import com.ruoyi.preparation.domain.KyMaterial;
import com.ruoyi.preparation.service.IKyMaterialService;
import com.ruoyi.preparation.service.IKyBrowseLogService;

/**
 * 共享资料Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/preparation/material")
public class KyMaterialController extends BaseController
{
    @Autowired
    private IKyMaterialService kyMaterialService;

    @Autowired
    private IKyBrowseLogService kyBrowseLogService;

    /** 目标类型：资料 */
    private static final String TARGET_TYPE_MATERIAL = "1";

    /**
     * 查询共享资料列表
     */
    @GetMapping("/list")
    public TableDataInfo list(KyMaterial kyMaterial)
    {
        startPage();
        List<KyMaterial> list = kyMaterialService.selectKyMaterialList(kyMaterial);
        return getDataTable(list);
    }

    /**
     * 导出共享资料列表
     */
    @Log(title = "共享资料", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KyMaterial kyMaterial)
    {
        List<KyMaterial> list = kyMaterialService.selectKyMaterialList(kyMaterial);
        ExcelUtil<KyMaterial> util = new ExcelUtil<KyMaterial>(KyMaterial.class);
        util.exportExcel(response, list, "共享资料数据");
    }

    /**
     * 获取共享资料详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        // 更新浏览次数
        kyMaterialService.updateViewCount(id);
        // 记录浏览日志
        try
        {
            kyBrowseLogService.recordBrowse(getUserId(), id, TARGET_TYPE_MATERIAL);
        }
        catch (Exception e)
        {
            // 浏览记录失败不影响主流程
        }
        return success(kyMaterialService.selectKyMaterialById(id));
    }

    /**
     * 新增共享资料
     */
    @Log(title = "共享资料", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KyMaterial kyMaterial)
    {
        kyMaterial.setUserId(getUserId());
        return toAjax(kyMaterialService.insertKyMaterial(kyMaterial));
    }

    /**
     * 修改共享资料
     */
    @Log(title = "共享资料", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KyMaterial kyMaterial)
    {
        return toAjax(kyMaterialService.updateKyMaterial(kyMaterial));
    }

    /**
     * 删除共享资料
     */
    @Log(title = "共享资料", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(kyMaterialService.deleteKyMaterialByIds(ids));
    }

    /**
     * 下载资料（记录下载次数）
     */
    @PostMapping("/download/{id}")
    public AjaxResult download(@PathVariable("id") Long id)
    {
        kyMaterialService.updateDownCount(id);
        return success();
    }
}
