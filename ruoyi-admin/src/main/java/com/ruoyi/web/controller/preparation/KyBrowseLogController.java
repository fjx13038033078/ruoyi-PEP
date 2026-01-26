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
import com.ruoyi.preparation.domain.KyBrowseLog;
import com.ruoyi.preparation.service.IKyBrowseLogService;

/**
 * 用户浏览记录Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/preparation/browse")
public class KyBrowseLogController extends BaseController
{
    @Autowired
    private IKyBrowseLogService kyBrowseLogService;

    /**
     * 查询用户浏览记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(KyBrowseLog kyBrowseLog)
    {
        startPage();
        List<KyBrowseLog> list = kyBrowseLogService.selectKyBrowseLogList(kyBrowseLog);
        return getDataTable(list);
    }

    /**
     * 查询当前用户的浏览记录
     */
    @GetMapping("/my")
    public TableDataInfo myList(KyBrowseLog kyBrowseLog)
    {
        startPage();
        kyBrowseLog.setUserId(getUserId());
        List<KyBrowseLog> list = kyBrowseLogService.selectKyBrowseLogList(kyBrowseLog);
        return getDataTable(list);
    }

    /**
     * 导出用户浏览记录列表
     */
    @Log(title = "用户浏览记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KyBrowseLog kyBrowseLog)
    {
        List<KyBrowseLog> list = kyBrowseLogService.selectKyBrowseLogList(kyBrowseLog);
        ExcelUtil<KyBrowseLog> util = new ExcelUtil<KyBrowseLog>(KyBrowseLog.class);
        util.exportExcel(response, list, "用户浏览记录数据");
    }

    /**
     * 获取用户浏览记录详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(kyBrowseLogService.selectKyBrowseLogById(id));
    }

    /**
     * 新增用户浏览记录
     */
    @Log(title = "用户浏览记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KyBrowseLog kyBrowseLog)
    {
        kyBrowseLog.setUserId(getUserId());
        return toAjax(kyBrowseLogService.insertKyBrowseLog(kyBrowseLog));
    }

    /**
     * 修改用户浏览记录
     */
    @Log(title = "用户浏览记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KyBrowseLog kyBrowseLog)
    {
        return toAjax(kyBrowseLogService.updateKyBrowseLog(kyBrowseLog));
    }

    /**
     * 删除用户浏览记录
     */
    @Log(title = "用户浏览记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(kyBrowseLogService.deleteKyBrowseLogByIds(ids));
    }
}
