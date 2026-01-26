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
import com.ruoyi.preparation.domain.KyMaterialCategory;
import com.ruoyi.preparation.service.IKyMaterialCategoryService;

/**
 * 资料分类Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/preparation/category")
public class KyMaterialCategoryController extends BaseController
{
    @Autowired
    private IKyMaterialCategoryService kyMaterialCategoryService;

    /**
     * 查询资料分类列表
     */
    @GetMapping("/list")
    public TableDataInfo list(KyMaterialCategory kyMaterialCategory)
    {
        startPage();
        List<KyMaterialCategory> list = kyMaterialCategoryService.selectKyMaterialCategoryList(kyMaterialCategory);
        return getDataTable(list);
    }

    /**
     * 查询资料分类列表（不分页，用于下拉选择）
     */
    @GetMapping("/listAll")
    public AjaxResult listAll(KyMaterialCategory kyMaterialCategory)
    {
        List<KyMaterialCategory> list = kyMaterialCategoryService.selectKyMaterialCategoryList(kyMaterialCategory);
        return success(list);
    }

    /**
     * 导出资料分类列表
     */
    @Log(title = "资料分类", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, KyMaterialCategory kyMaterialCategory)
    {
        List<KyMaterialCategory> list = kyMaterialCategoryService.selectKyMaterialCategoryList(kyMaterialCategory);
        ExcelUtil<KyMaterialCategory> util = new ExcelUtil<KyMaterialCategory>(KyMaterialCategory.class);
        util.exportExcel(response, list, "资料分类数据");
    }

    /**
     * 获取资料分类详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(kyMaterialCategoryService.selectKyMaterialCategoryById(id));
    }

    /**
     * 新增资料分类
     */
    @Log(title = "资料分类", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody KyMaterialCategory kyMaterialCategory)
    {
        kyMaterialCategory.setCreateBy(getUsername());
        return toAjax(kyMaterialCategoryService.insertKyMaterialCategory(kyMaterialCategory));
    }

    /**
     * 修改资料分类
     */
    @Log(title = "资料分类", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody KyMaterialCategory kyMaterialCategory)
    {
        kyMaterialCategory.setUpdateBy(getUsername());
        return toAjax(kyMaterialCategoryService.updateKyMaterialCategory(kyMaterialCategory));
    }

    /**
     * 删除资料分类
     */
    @Log(title = "资料分类", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(kyMaterialCategoryService.deleteKyMaterialCategoryByIds(ids));
    }
}
