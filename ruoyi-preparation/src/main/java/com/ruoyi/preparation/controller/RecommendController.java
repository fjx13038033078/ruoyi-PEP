package com.ruoyi.preparation.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.preparation.domain.KyMaterial;
import com.ruoyi.preparation.service.IRecommendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 推荐Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/preparation/recommend")
public class RecommendController extends BaseController
{
    @Autowired
    private IRecommendService recommendService;

    /**
     * 获取推荐资料（猜你喜欢）
     */
    @GetMapping("/materials")
    public R<List<KyMaterial>> getRecommendMaterials(@RequestParam(defaultValue = "10") Integer limit)
    {
        Long userId = SecurityUtils.getUserId();
        List<KyMaterial> materials = recommendService.getRecommendMaterials(userId, limit);
        return R.ok(materials);
    }

    /**
     * 获取热门资料
     */
    @GetMapping("/hot")
    public R<List<KyMaterial>> getHotMaterials(@RequestParam(defaultValue = "10") Integer limit)
    {
        List<KyMaterial> materials = recommendService.getHotMaterials(limit);
        return R.ok(materials);
    }
}
