<template>
  <div class="dashboard-container">
    <!-- 页面标题 -->
    <div class="page-title">
      <h1>考研互助交流平台</h1>
      <p>数据统计概览</p>
    </div>

    <!-- 统计概览卡片 -->
    <el-row :gutter="20" class="overview-row">
      <el-col :span="6">
        <div class="overview-card">
          <div class="overview-icon" style="background-color: #409EFF;">
            <i class="el-icon-school"></i>
          </div>
          <div class="overview-info">
            <p class="overview-value">{{ overview.universityCount || 0 }}</p>
            <p class="overview-label">院校数量</p>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="overview-card">
          <div class="overview-icon" style="background-color: #67C23A;">
            <i class="el-icon-folder-opened"></i>
          </div>
          <div class="overview-info">
            <p class="overview-value">{{ overview.materialCount || 0 }}</p>
            <p class="overview-label">资料数量</p>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="overview-card">
          <div class="overview-icon" style="background-color: #E6A23C;">
            <i class="el-icon-document"></i>
          </div>
          <div class="overview-info">
            <p class="overview-value">{{ overview.postCount || 0 }}</p>
            <p class="overview-label">帖子数量</p>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="overview-card">
          <div class="overview-icon" style="background-color: #909399;">
            <i class="el-icon-user"></i>
          </div>
          <div class="overview-info">
            <p class="overview-value">{{ overview.userCount || 0 }}</p>
            <p class="overview-label">用户数量</p>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- 图表区域 -->
    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 资料分类统计 - 饼图 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>资料分类统计</span>
          </div>
          <div ref="pieChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <!-- 用户行为统计 - 环形图 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>用户行为统计</span>
          </div>
          <div ref="ringChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" style="margin-top: 20px;">
      <!-- 近7天帖子趋势 - 折线图 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>近7天帖子发布趋势</span>
          </div>
          <div ref="lineChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <!-- 院校层次分布 - 柱状图 -->
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="chart-header">
            <span>院校层次分布</span>
          </div>
          <div ref="barChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getMaterialCategoryStats, getPostTrend, getUniversityLevelStats, getUserActionStats, getOverview } from '@/api/preparation/statistics'

export default {
  name: "Dashboard",
  data() {
    return {
      overview: {},
      pieChart: null,
      ringChart: null,
      lineChart: null,
      barChart: null
    }
  },
  mounted() {
    this.loadData()
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.pieChart) this.pieChart.dispose()
    if (this.ringChart) this.ringChart.dispose()
    if (this.lineChart) this.lineChart.dispose()
    if (this.barChart) this.barChart.dispose()
  },
  methods: {
    loadData() {
      // 加载总体统计
      getOverview().then(res => {
        this.overview = res.data || {}
      })
      // 加载图表数据
      this.initPieChart()
      this.initRingChart()
      this.initLineChart()
      this.initBarChart()
    },
    handleResize() {
      if (this.pieChart) this.pieChart.resize()
      if (this.ringChart) this.ringChart.resize()
      if (this.lineChart) this.lineChart.resize()
      if (this.barChart) this.barChart.resize()
    },
    // 饼图 - 资料分类统计
    initPieChart() {
      this.pieChart = echarts.init(this.$refs.pieChart)
      getMaterialCategoryStats().then(res => {
        const data = res.data.data || []
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            right: '5%',
            top: 'center'
          },
          color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#5470c6'],
          series: [{
            type: 'pie',
            radius: '65%',
            center: ['40%', '50%'],
            data: data,
            label: {
              show: true,
              formatter: '{b}'
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }]
        }
        this.pieChart.setOption(option)
      })
    },
    // 环形图 - 用户行为统计
    initRingChart() {
      this.ringChart = echarts.init(this.$refs.ringChart)
      getUserActionStats().then(res => {
        const data = res.data.data || []
        const option = {
          tooltip: {
            trigger: 'item',
            formatter: '{b}: {c} ({d}%)'
          },
          legend: {
            orient: 'vertical',
            right: '5%',
            top: 'center'
          },
          color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C'],
          series: [{
            type: 'pie',
            radius: ['40%', '65%'],
            center: ['40%', '50%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              formatter: '{b}'
            },
            data: data
          }]
        }
        this.ringChart.setOption(option)
      })
    },
    // 折线图 - 帖子发布趋势
    initLineChart() {
      this.lineChart = echarts.init(this.$refs.lineChart)
      getPostTrend().then(res => {
        const dates = res.data.dates || []
        const counts = res.data.counts || []
        const option = {
          tooltip: {
            trigger: 'axis'
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: dates,
            axisLine: {
              lineStyle: { color: '#999' }
            }
          },
          yAxis: {
            type: 'value',
            minInterval: 1,
            axisLine: {
              lineStyle: { color: '#999' }
            }
          },
          series: [{
            name: '发帖数',
            type: 'line',
            smooth: true,
            data: counts,
            itemStyle: { color: '#409EFF' },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0, y: 0, x2: 0, y2: 1,
                colorStops: [
                  { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                  { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
                ]
              }
            }
          }]
        }
        this.lineChart.setOption(option)
      })
    },
    // 柱状图 - 院校层次分布
    initBarChart() {
      this.barChart = echarts.init(this.$refs.barChart)
      getUniversityLevelStats().then(res => {
        const levels = res.data.levels || []
        const counts = res.data.counts || []
        const option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: { type: 'shadow' }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: levels,
            axisLine: {
              lineStyle: { color: '#999' }
            },
            axisLabel: {
              interval: 0
            }
          },
          yAxis: {
            type: 'value',
            minInterval: 1,
            axisLine: {
              lineStyle: { color: '#999' }
            }
          },
          series: [{
            name: '院校数量',
            type: 'bar',
            barWidth: '50%',
            data: counts,
            itemStyle: {
              color: function(params) {
                const colors = ['#F56C6C', '#E6A23C', '#67C23A', '#909399']
                return colors[params.dataIndex % colors.length]
              }
            }
          }]
        }
        this.barChart.setOption(option)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-title {
  text-align: center;
  padding: 20px 0 30px;
  border-bottom: 2px solid #409EFF;
  margin-bottom: 20px;
  background-color: #fff;

  h1 {
    font-size: 28px;
    color: #333;
    margin: 0 0 10px 0;
    font-weight: 600;
    letter-spacing: 2px;
  }

  p {
    font-size: 14px;
    color: #999;
    margin: 0;
  }
}

.overview-row {
  .overview-card {
    background-color: #fff;
    padding: 20px;
    display: flex;
    align-items: center;
    border: 1px solid #e8e8e8;

    .overview-icon {
      width: 60px;
      height: 60px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 15px;

      i {
        font-size: 28px;
        color: #fff;
      }
    }

    .overview-info {
      flex: 1;

      .overview-value {
        font-size: 28px;
        font-weight: 600;
        color: #333;
        margin: 0 0 5px 0;
      }

      .overview-label {
        font-size: 14px;
        color: #999;
        margin: 0;
      }
    }
  }
}

.chart-card {
  ::v-deep .el-card__header {
    padding: 15px 20px;
    border-bottom: 1px solid #e8e8e8;
  }

  .chart-header {
    font-size: 16px;
    font-weight: 600;
    color: #333;
  }

  .chart-container {
    height: 300px;
  }
}
</style>
