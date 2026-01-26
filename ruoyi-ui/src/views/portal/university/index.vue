<template>
  <div class="university-page">
    <div class="page-header">
      <h2>院校信息</h2>
      <p>查看各大高校考研信息</p>
    </div>

    <div class="page-content">
      <!-- 搜索筛选 -->
      <el-card class="filter-card">
        <el-form :inline="true" :model="queryParams" size="small">
          <el-form-item label="院校名称">
            <el-input v-model="queryParams.name" placeholder="请输入院校名称" clearable @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="层次">
            <el-select v-model="queryParams.level" placeholder="全部层次" clearable>
              <el-option label="985" value="985" />
              <el-option label="211" value="211" />
              <el-option label="双一流" value="双一流" />
              <el-option label="普通" value="普通" />
            </el-select>
          </el-form-item>
          <el-form-item label="地区">
            <el-input v-model="queryParams.region" placeholder="请输入地区" clearable />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </el-card>

      <!-- 院校列表 -->
      <div class="university-list" v-loading="loading">
        <el-row :gutter="20">
          <el-col :span="6" v-for="item in universityList" :key="item.id">
            <el-card class="university-card" shadow="hover" @click.native="goDetail(item.id)">
              <div class="card-content">
                <el-avatar :size="80" :src="item.logoUrl" class="university-logo">
                  <span style="font-size: 24px;">{{ item.name.substring(0, 1) }}</span>
                </el-avatar>
                <h3 class="university-name">{{ item.name }}</h3>
                <div class="university-tags">
                  <el-tag :type="getLevelType(item.level)" size="small">{{ item.level }}</el-tag>
                  <el-tag type="info" size="small">{{ item.region }}</el-tag>
                </div>
                <p class="university-code">院校代码：{{ item.code }}</p>
              </div>
            </el-card>
          </el-col>
        </el-row>
        <el-empty v-if="universityList.length === 0 && !loading" description="暂无院校信息"></el-empty>
      </div>

      <!-- 分页 -->
      <div class="pagination-wrap">
        <el-pagination
          background
          @current-change="handlePageChange"
          :current-page="queryParams.pageNum"
          :page-size="queryParams.pageSize"
          layout="total, prev, pager, next"
          :total="total"
        ></el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
import { listUniversity } from "@/api/preparation/university";

export default {
  name: "PortalUniversity",
  data() {
    return {
      loading: false,
      universityList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 12,
        name: undefined,
        level: undefined,
        region: undefined
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listUniversity(this.queryParams).then(res => {
        this.universityList = res.rows || [];
        this.total = res.total || 0;
        this.loading = false;
      });
    },
    getLevelType(level) {
      const typeMap = { '985': 'danger', '211': 'warning', '双一流': 'success', '普通': 'info' };
      return typeMap[level] || 'info';
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 12,
        name: undefined,
        level: undefined,
        region: undefined
      };
      this.getList();
    },
    handlePageChange(page) {
      this.queryParams.pageNum = page;
      this.getList();
    },
    goDetail(id) {
      this.$router.push('/portal/university/' + id);
    }
  }
};
</script>

<style lang="scss" scoped>
.university-page {
  .page-header {
    background-color: #409EFF;
    padding: 35px 20px;
    text-align: center;
    color: #fff;

    h2 {
      font-size: 24px;
      margin-bottom: 8px;
    }

    p {
      font-size: 14px;
      opacity: 0.9;
    }
  }

  .page-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }

  .filter-card {
    margin-bottom: 20px;
  }

  .university-list {
    min-height: 400px;
  }

  .university-card {
    margin-bottom: 20px;
    cursor: pointer;

    &:hover {
      border-color: #409EFF;
    }

    .card-content {
      text-align: center;
      padding: 20px 0;

      .university-logo {
        margin-bottom: 15px;
        background-color: #409EFF;
        color: #fff;
      }

      .university-name {
        font-size: 16px;
        color: #333;
        margin-bottom: 10px;
      }

      .university-tags {
        margin-bottom: 10px;

        .el-tag {
          margin: 0 3px;
        }
      }

      .university-code {
        font-size: 12px;
        color: #999;
        margin: 0;
      }
    }
  }

  .pagination-wrap {
    text-align: center;
    padding: 20px 0;
  }
}
</style>
