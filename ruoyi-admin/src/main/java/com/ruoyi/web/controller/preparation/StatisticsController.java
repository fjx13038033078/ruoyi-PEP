package com.ruoyi.web.controller.preparation;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.R;
import com.ruoyi.preparation.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 统计Controller
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/preparation/statistics")
public class StatisticsController extends BaseController
{
    @Autowired
    private IStatisticsService statisticsService;

    /**
     * 获取资料分类统计（饼图）
     */
    @GetMapping("/materialCategory")
    public R<Map<String, Object>> getMaterialCategoryStats()
    {
        return R.ok(statisticsService.getMaterialCategoryStats());
    }

    /**
     * 获取近7天帖子发布趋势（折线图）
     */
    @GetMapping("/postTrend")
    public R<Map<String, Object>> getPostTrend()
    {
        return R.ok(statisticsService.getPostTrend());
    }

    /**
     * 获取院校层次分布（柱状图）
     */
    @GetMapping("/universityLevel")
    public R<Map<String, Object>> getUniversityLevelStats()
    {
        return R.ok(statisticsService.getUniversityLevelStats());
    }

    /**
     * 获取用户行为统计（环形图）
     */
    @GetMapping("/userAction")
    public R<Map<String, Object>> getUserActionStats()
    {
        return R.ok(statisticsService.getUserActionStats());
    }

    /**
     * 获取总体统计数据
     */
    @GetMapping("/overview")
    public R<Map<String, Object>> getOverview()
    {
        return R.ok(statisticsService.getOverview());
    }
}
