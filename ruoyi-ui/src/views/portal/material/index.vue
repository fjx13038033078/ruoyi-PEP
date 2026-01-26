<template>
  <div class="material-page">
    <div class="page-header">
      <h2>资料中心</h2>
      <p>海量考研资料，助力上岸</p>
    </div>

    <div class="page-content">
      <el-row :gutter="20">
        <!-- 左侧分类 -->
        <el-col :span="5">
          <el-card class="category-card">
            <div slot="header">
              <i class="el-icon-menu"></i> 资料分类
            </div>
            <div class="category-list">
              <div
                :class="['category-item', { active: !queryParams.categoryId }]"
                @click="selectCategory(null)"
              >
                <i class="el-icon-files"></i> 全部资料
              </div>
              <div
                v-for="item in categoryList"
                :key="item.id"
                :class="['category-item', { active: queryParams.categoryId === item.id }]"
                @click="selectCategory(item.id)"
              >
                <i class="el-icon-folder"></i> {{ item.name }}
              </div>
            </div>
          </el-card>
        </el-col>

        <!-- 右侧资料列表 -->
        <el-col :span="19">
          <!-- 搜索 -->
          <el-card class="search-card">
            <el-input
              v-model="queryParams.title"
              placeholder="搜索资料..."
              prefix-icon="el-icon-search"
              clearable
              @keyup.enter.native="handleQuery"
            >
              <el-button slot="append" type="primary" @click="handleQuery">搜索</el-button>
            </el-input>
          </el-card>

          <!-- 资料列表 -->
          <div class="material-list" v-loading="loading">
            <el-card
              v-for="item in materialList"
              :key="item.id"
              class="material-card"
              shadow="hover"
            >
              <div class="material-content">
                <div class="material-icon">
                  <i class="el-icon-document"></i>
                </div>
                <div class="material-info">
                  <h3 class="material-title" @click="goDetail(item.id)">{{ item.title }}</h3>
                  <p class="material-desc">{{ item.remark || '暂无描述' }}</p>
                  <div class="material-meta">
                    <el-tag size="mini">{{ item.categoryName }}</el-tag>
                    <span><i class="el-icon-user"></i> {{ item.userName }}</span>
                    <span><i class="el-icon-view"></i> {{ item.viewCount || 0 }}</span>
                    <span><i class="el-icon-download"></i> {{ item.downCount || 0 }}</span>
                    <span><i class="el-icon-time"></i> {{ parseTime(item.createTime, '{y}-{m}-{d}') }}</span>
                  </div>
                </div>
                <div class="material-actions">
                  <el-button type="primary" size="small" @click="goDetail(item.id)">
                    <i class="el-icon-view"></i> 查看
                  </el-button>
                </div>
              </div>
            </el-card>
            <el-empty v-if="materialList.length === 0 && !loading" description="暂无资料"></el-empty>
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
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listMaterial } from "@/api/preparation/material";
import { listAllCategory } from "@/api/preparation/category";

export default {
  name: "PortalMaterial",
  data() {
    return {
      loading: false,
      materialList: [],
      categoryList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        categoryId: undefined,
        status: '0'
      }
    };
  },
  created() {
    this.getCategoryList();
    this.getList();
  },
  methods: {
    getCategoryList() {
      listAllCategory({ status: '0' }).then(res => {
        this.categoryList = res.data || [];
      });
    },
    getList() {
      this.loading = true;
      listMaterial(this.queryParams).then(res => {
        this.materialList = res.rows || [];
        this.total = res.total || 0;
        this.loading = false;
      });
    },
    selectCategory(categoryId) {
      this.queryParams.categoryId = categoryId;
      this.queryParams.pageNum = 1;
      this.getList();
    },
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    handlePageChange(page) {
      this.queryParams.pageNum = page;
      this.getList();
    },
    goDetail(id) {
      this.$router.push('/portal/material/' + id);
    }
  }
};
</script>

<style lang="scss" scoped>
.material-page {
  .page-header {
    background-color: #67C23A;
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

  .category-card {
    position: sticky;
    top: 80px;

    .category-list {
      .category-item {
        padding: 12px 15px;
        cursor: pointer;
        margin-bottom: 5px;

        i {
          margin-right: 8px;
        }

        &:hover {
          background-color: #f5f5f5;
          color: #409EFF;
        }

        &.active {
          background-color: #ecf5ff;
          color: #409EFF;
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;

    ::v-deep .el-input-group__append {
      background-color: #409EFF;
      border-color: #409EFF;
      color: #fff;
    }
  }

  .material-list {
    min-height: 400px;
  }

  .material-card {
    margin-bottom: 15px;

    .material-content {
      display: flex;
      align-items: center;

      .material-icon {
        width: 50px;
        height: 50px;
        background-color: #67C23A;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-shrink: 0;

        i {
          font-size: 24px;
          color: #fff;
        }
      }

      .material-info {
        flex: 1;
        margin-left: 20px;
        overflow: hidden;

        .material-title {
          font-size: 15px;
          color: #333;
          margin-bottom: 8px;
          cursor: pointer;

          &:hover {
            color: #409EFF;
          }
        }

        .material-desc {
          font-size: 13px;
          color: #999;
          margin-bottom: 8px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }

        .material-meta {
          font-size: 12px;
          color: #999;

          .el-tag {
            margin-right: 10px;
          }

          span {
            margin-right: 15px;

            i {
              margin-right: 3px;
            }
          }
        }
      }

      .material-actions {
        flex-shrink: 0;
        margin-left: 20px;
      }
    }
  }

  .pagination-wrap {
    text-align: center;
    padding: 20px 0;
  }
}
</style>
