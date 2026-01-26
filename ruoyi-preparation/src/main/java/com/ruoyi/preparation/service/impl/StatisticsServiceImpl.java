package com.ruoyi.preparation.service.impl;

import com.ruoyi.preparation.mapper.StatisticsMapper;
import com.ruoyi.preparation.service.IStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 统计服务实现类
 *
 * @author ruoyi
 */
@Service
public class StatisticsServiceImpl implements IStatisticsService
{
    @Autowired
    private StatisticsMapper statisticsMapper;

    /**
     * 获取资料分类统计
     */
    @Override
    public Map<String, Object> getMaterialCategoryStats()
    {
        List<Map<String, Object>> data = statisticsMapper.selectMaterialCategoryStats();
        Map<String, Object> result = new HashMap<>();
        result.put("data", data);
        return result;
    }

    /**
     * 获取近7天帖子发布趋势
     */
    @Override
    public Map<String, Object> getPostTrend()
    {
        List<Map<String, Object>> data = statisticsMapper.selectPostTrend();
        
        List<String> dates = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        
        for (Map<String, Object> item : data)
        {
            dates.add((String) item.get("date"));
            counts.add(((Number) item.get("count")).intValue());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("dates", dates);
        result.put("counts", counts);
        return result;
    }

    /**
     * 获取院校层次分布
     */
    @Override
    public Map<String, Object> getUniversityLevelStats()
    {
        List<Map<String, Object>> data = statisticsMapper.selectUniversityLevelStats();
        
        List<String> levels = new ArrayList<>();
        List<Integer> counts = new ArrayList<>();
        
        for (Map<String, Object> item : data)
        {
            String level = (String) item.get("level");
            levels.add(level != null ? level : "未知");
            counts.add(((Number) item.get("count")).intValue());
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("levels", levels);
        result.put("counts", counts);
        return result;
    }

    /**
     * 获取用户行为统计
     */
    @Override
    public Map<String, Object> getUserActionStats()
    {
        Map<String, Object> result = new HashMap<>();
        
        int browseCount = statisticsMapper.selectBrowseCount();
        int likeCount = statisticsMapper.selectActionCount("1");
        int collectCount = statisticsMapper.selectActionCount("2");
        int downloadCount = statisticsMapper.selectDownloadCount();
        
        List<Map<String, Object>> data = new ArrayList<>();
        data.add(createDataItem("浏览", browseCount));
        data.add(createDataItem("点赞", likeCount));
        data.add(createDataItem("收藏", collectCount));
        data.add(createDataItem("下载", downloadCount));
        
        result.put("data", data);
        return result;
    }

    /**
     * 获取总体统计数据
     */
    @Override
    public Map<String, Object> getOverview()
    {
        Map<String, Object> result = new HashMap<>();
        result.put("universityCount", statisticsMapper.selectUniversityCount());
        result.put("materialCount", statisticsMapper.selectMaterialCount());
        result.put("postCount", statisticsMapper.selectPostCount());
        result.put("userCount", statisticsMapper.selectUserCount());
        return result;
    }

    private Map<String, Object> createDataItem(String name, int value)
    {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }
}
