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
import com.ruoyi.preparation.domain.KyUserAction;
import com.ruoyi.preparation.service.IKyUserActionService;

/**
 * 用户互动Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/preparation/action")
public class KyUserActionController extends BaseController
{
    @Autowired
    private IKyUserActionService kyUserActionService;

    /**
     * 查询用户互动列表
     */
    @GetMapping("/list")
    public TableDataInfo list(KyUserAction kyUserAction)
    {
        startPage();
        List<KyUserAction> list = kyUserActionService.selectKyUserActionList(kyUserAction);
        return getDataTable(list);
    }

    /**
     * 查询当前用户的互动列表（收藏、点赞等）
     */
    @GetMapping("/my")
    public TableDataInfo myList(KyUserAction kyUserAction)
    {
        startPage();
        kyUserAction.setUserId(getUserId());
        List<KyUserAction> list = kyUserActionService.selectKyUserActionList(kyUserAction);
        return getDataTable(list);
    }

    /**
     * 导出用户互动列表
     */
    @Log(title = "用户互动", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KyUserAction kyUserAction)
    {
        List<KyUserAction> list = kyUserActionService.selectKyUserActionList(kyUserAction);
        ExcelUtil<KyUserAction> util = new ExcelUtil<KyUserAction>(KyUserAction.class);
        util.exportExcel(response, list, "用户互动数据");
    }

    /**
     * 获取用户互动详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(kyUserActionService.selectKyUserActionById(id));
    }

    /**
     * 执行互动（点赞/收藏/下载）
     * 如果已存在则取消，不存在则新增
     */
    @PostMapping("/do")
    public AjaxResult doAction(@RequestParam("targetId") Long targetId, 
                               @RequestParam("targetType") String targetType,
                               @RequestParam("actionType") String actionType)
    {
        int result = kyUserActionService.doAction(getUserId(), targetId, targetType, actionType);
        if (result > 0)
        {
            return success("操作成功");
        }
        else
        {
            return success("已取消");
        }
    }

    /**
     * 检查用户是否已执行某个互动
     */
    @GetMapping("/check")
    public AjaxResult checkAction(@RequestParam("targetId") Long targetId, 
                                  @RequestParam("targetType") String targetType,
                                  @RequestParam("actionType") String actionType)
    {
        boolean hasAction = kyUserActionService.checkAction(getUserId(), targetId, targetType, actionType);
        return success(hasAction);
    }

    /**
     * 新增用户互动
     */
    @Log(title = "用户互动", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KyUserAction kyUserAction)
    {
        kyUserAction.setUserId(getUserId());
        return toAjax(kyUserActionService.insertKyUserAction(kyUserAction));
    }

    /**
     * 修改用户互动
     */
    @Log(title = "用户互动", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KyUserAction kyUserAction)
    {
        return toAjax(kyUserActionService.updateKyUserAction(kyUserAction));
    }

    /**
     * 删除用户互动
     */
    @Log(title = "用户互动", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(kyUserActionService.deleteKyUserActionByIds(ids));
    }
}
