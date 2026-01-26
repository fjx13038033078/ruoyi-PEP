<template>
  <div class="portal-home">
    <!-- Banner区域 -->
    <div class="banner-section">
      <div class="banner-content">
        <h1>考研互助交流平台</h1>
        <p>分享资料、交流经验、共同进步</p>
        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索院校、资料、帖子..."
            prefix-icon="el-icon-search"
            size="large"
            @keyup.enter.native="handleSearch"
          >
            <el-button slot="append" type="primary" @click="handleSearch">搜索</el-button>
          </el-input>
        </div>
      </div>
    </div>

    <div class="main-content">
      <!-- 快捷入口 -->
      <div class="quick-entry">
        <div class="entry-item" @click="$router.push('/portal/university')">
          <div class="entry-icon" style="background-color: #409EFF">
            <i class="el-icon-school"></i>
          </div>
          <span>院校信息</span>
        </div>
        <div class="entry-item" @click="$router.push('/portal/material')">
          <div class="entry-icon" style="background-color: #67C23A">
            <i class="el-icon-folder-opened"></i>
          </div>
          <span>资料下载</span>
        </div>
        <div class="entry-item" @click="$router.push('/portal/forum')">
          <div class="entry-icon" style="background-color: #E6A23C">
            <i class="el-icon-chat-dot-round"></i>
          </div>
          <span>考研论坛</span>
        </div>
        <div class="entry-item" @click="$router.push('/portal/profile')">
          <div class="entry-icon" style="background-color: #909399">
            <i class="el-icon-user"></i>
          </div>
          <span>个人中心</span>
        </div>
      </div>

      <!-- 猜你喜欢 -->
      <el-card class="section-card recommend-card">
        <div slot="header" class="section-header">
          <span><i class="el-icon-magic-stick"></i> 猜你喜欢</span>
          <el-button type="text" @click="refreshRecommend" :loading="recommendLoading">
            <i class="el-icon-refresh"></i> 换一批
          </el-button>
        </div>
        <div class="recommend-list" v-loading="recommendLoading">
          <div
            v-for="item in recommendMaterials"
            :key="item.id"
            class="recommend-item"
            @click="goMaterialDetail(item.id)"
          >
            <div class="recommend-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="recommend-info">
              <div class="recommend-title">{{ item.title }}</div>
              <div class="recommend-meta">
                <el-tag size="mini">{{ item.categoryName || '未分类' }}</el-tag>
                <span><i class="el-icon-view"></i> {{ item.viewCount || 0 }}</span>
                <span><i class="el-icon-download"></i> {{ item.downCount || 0 }}</span>
              </div>
            </div>
          </div>
          <el-empty v-if="recommendMaterials.length === 0 && !recommendLoading" description="暂无推荐"></el-empty>
        </div>
      </el-card>

      <el-row :gutter="20" style="margin-top: 20px;">
        <!-- 热门帖子 -->
        <el-col :span="16">
          <el-card class="section-card">
            <div slot="header" class="section-header">
              <span><i class="el-icon-s-flag"></i> 热门帖子</span>
              <el-button type="text" @click="$router.push('/portal/forum')">更多 <i class="el-icon-arrow-right"></i></el-button>
            </div>
            <div class="post-list">
              <div
                v-for="item in hotPosts"
                :key="item.id"
                class="post-item"
                @click="goPostDetail(item.id)"
              >
                <div class="post-info">
                  <el-tag v-if="item.isTop === '1'" type="danger" size="mini">置顶</el-tag>
                  <span class="post-title">{{ item.title }}</span>
                </div>
                <div class="post-meta">
                  <span><i class="el-icon-user"></i> {{ item.userName }}</span>
                  <span><i class="el-icon-view"></i> {{ item.viewNum || 0 }}</span>
                  <span><i class="el-icon-chat-dot-round"></i> {{ item.likeNum || 0 }}</span>
                  <span class="post-time">{{ parseTime(item.createTime, '{y}-{m}-{d}') }}</span>
                </div>
              </div>
              <el-empty v-if="hotPosts.length === 0" description="暂无帖子"></el-empty>
            </div>
          </el-card>
        </el-col>

        <!-- 右侧边栏 -->
        <el-col :span="8">
          <!-- 热门资料 -->
          <el-card class="section-card">
            <div slot="header" class="section-header">
              <span><i class="el-icon-document"></i> 热门资料</span>
              <el-button type="text" @click="$router.push('/portal/material')">更多 <i class="el-icon-arrow-right"></i></el-button>
            </div>
            <div class="material-list">
              <div
                v-for="item in hotMaterials"
                :key="item.id"
                class="material-item"
                @click="goMaterialDetail(item.id)"
              >
                <div class="material-title">{{ item.title }}</div>
                <div class="material-meta">
                  <el-tag size="mini">{{ item.categoryName }}</el-tag>
                  <span><i class="el-icon-download"></i> {{ item.downCount || 0 }}</span>
                </div>
              </div>
              <el-empty v-if="hotMaterials.length === 0" description="暂无资料"></el-empty>
            </div>
          </el-card>

          <!-- 热门院校 -->
          <el-card class="section-card" style="margin-top: 20px;">
            <div slot="header" class="section-header">
              <span><i class="el-icon-office-building"></i> 热门院校</span>
              <el-button type="text" @click="$router.push('/portal/university')">更多 <i class="el-icon-arrow-right"></i></el-button>
            </div>
            <div class="university-list">
              <div
                v-for="item in hotUniversities"
                :key="item.id"
                class="university-item"
                @click="goUniversityDetail(item.id)"
              >
                <el-avatar :size="40" :src="item.logoUrl">
                  <span>{{ item.name.substring(0, 1) }}</span>
                </el-avatar>
                <div class="university-info">
                  <div class="university-name">{{ item.name }}</div>
                  <div class="university-tags">
                    <el-tag :type="getLevelType(item.level)" size="mini">{{ item.level }}</el-tag>
                    <span class="university-region">{{ item.region }}</span>
                  </div>
                </div>
              </div>
              <el-empty v-if="hotUniversities.length === 0" description="暂无院校"></el-empty>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listPost } from "@/api/preparation/post";
import { listMaterial } from "@/api/preparation/material";
import { listUniversity } from "@/api/preparation/university";
import { getRecommendMaterials } from "@/api/preparation/recommend";

export default {
  name: "PortalHome",
  data() {
    return {
      searchKeyword: "",
      hotPosts: [],
      hotMaterials: [],
      hotUniversities: [],
      recommendMaterials: [],
      recommendLoading: false
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      // 加载推荐资料（猜你喜欢）
      this.loadRecommend();
      // 加载热门帖子
      listPost({ pageNum: 1, pageSize: 8 }).then(res => {
        this.hotPosts = res.rows || [];
      });
      // 加载热门资料
      listMaterial({ pageNum: 1, pageSize: 5, status: '0' }).then(res => {
        this.hotMaterials = res.rows || [];
      });
      // 加载热门院校
      listUniversity({ pageNum: 1, pageSize: 5 }).then(res => {
        this.hotUniversities = res.rows || [];
      });
    },
    loadRecommend() {
      this.recommendLoading = true;
      getRecommendMaterials(10).then(res => {
        this.recommendMaterials = res.data || [];
        this.recommendLoading = false;
      }).catch(() => {
        this.recommendLoading = false;
      });
    },
    refreshRecommend() {
      this.loadRecommend();
    },
    handleSearch() {
      if (this.searchKeyword) {
        this.$router.push({ path: '/portal/forum', query: { keyword: this.searchKeyword } });
      }
    },
    getLevelType(level) {
      const typeMap = { '985': 'danger', '211': 'warning', '双一流': 'success', '普通': 'info' };
      return typeMap[level] || 'info';
    },
    goPostDetail(id) {
      this.$router.push('/portal/post/' + id);
    },
    goMaterialDetail(id) {
      this.$router.push('/portal/material/' + id);
    },
    goUniversityDetail(id) {
      this.$router.push('/portal/university/' + id);
    }
  }
};
</script>

<style lang="scss" scoped>
.portal-home {
  .banner-section {
    background-color: #409EFF;
    padding: 50px 20px;
    text-align: center;

    .banner-content {
      max-width: 600px;
      margin: 0 auto;

      h1 {
        color: #fff;
        font-size: 32px;
        margin-bottom: 10px;
      }

      p {
        color: rgba(255, 255, 255, 0.9);
        font-size: 15px;
        margin-bottom: 25px;
      }

      .search-box {
        max-width: 500px;
        margin: 0 auto;

        ::v-deep .el-input-group__append {
          background-color: #fff;
          border-color: #fff;
          color: #409EFF;
        }
      }
    }
  }

  .main-content {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }

  .quick-entry {
    display: flex;
    justify-content: center;
    gap: 30px;
    margin: 20px 0 30px;

    .entry-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      cursor: pointer;

      &:hover {
        .entry-icon {
          background-color: #409EFF;
        }
      }

      .entry-icon {
        width: 60px;
        height: 60px;
        background-color: #666;
        display: flex;
        align-items: center;
        justify-content: center;

        i {
          font-size: 26px;
          color: #fff;
        }
      }

      span {
        margin-top: 10px;
        font-size: 14px;
        color: #333;
      }
    }
  }

  .section-card {
    ::v-deep .el-card__header {
      padding: 15px 20px;
      border-bottom: 1px solid #ebeef5;
    }

    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;

      span {
        font-size: 16px;
        font-weight: 600;

        i {
          margin-right: 5px;
          color: #409EFF;
        }
      }
    }
  }

  .recommend-card {
    .recommend-list {
      display: flex;
      flex-wrap: wrap;
      gap: 15px;
      min-height: 100px;

      .recommend-item {
        width: calc(20% - 12px);
        display: flex;
        flex-direction: column;
        padding: 15px;
        border: 1px solid #eee;
        cursor: pointer;

        &:hover {
          border-color: #409EFF;

          .recommend-title {
            color: #409EFF;
          }
        }

        .recommend-icon {
          width: 40px;
          height: 40px;
          background-color: #67C23A;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-bottom: 10px;

          i {
            font-size: 20px;
            color: #fff;
          }
        }

        .recommend-info {
          flex: 1;

          .recommend-title {
            font-size: 14px;
            color: #333;
            margin-bottom: 8px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
          }

          .recommend-meta {
            font-size: 12px;
            color: #999;

            .el-tag {
              margin-right: 8px;
            }

            span {
              margin-right: 10px;

              i {
                margin-right: 2px;
              }
            }
          }
        }
      }
    }
  }

  .post-list {
    .post-item {
      padding: 15px 0;
      border-bottom: 1px solid #f0f0f0;
      cursor: pointer;

      &:hover {
        background-color: #fafafa;
      }

      &:last-child {
        border-bottom: none;
      }

      .post-info {
        display: flex;
        align-items: center;
        gap: 10px;

        .post-title {
          font-size: 15px;
          color: #333;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
      }

      .post-meta {
        margin-top: 8px;
        font-size: 12px;
        color: #999;

        span {
          margin-right: 15px;

          i {
            margin-right: 3px;
          }
        }

        .post-time {
          float: right;
        }
      }
    }
  }

  .material-list {
    .material-item {
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;
      cursor: pointer;

      &:hover {
        .material-title {
          color: #409EFF;
        }
      }

      &:last-child {
        border-bottom: none;
      }

      .material-title {
        font-size: 14px;
        color: #333;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }

      .material-meta {
        margin-top: 8px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        font-size: 12px;
        color: #999;
      }
    }
  }

  .university-list {
    .university-item {
      display: flex;
      align-items: center;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;
      cursor: pointer;

      &:hover {
        .university-name {
          color: #409EFF;
        }
      }

      &:last-child {
        border-bottom: none;
      }

      .university-info {
        margin-left: 12px;
        flex: 1;
        overflow: hidden;

        .university-name {
          font-size: 14px;
          color: #333;
        }

        .university-tags {
          margin-top: 5px;
          display: flex;
          align-items: center;
          gap: 8px;

          .university-region {
            font-size: 12px;
            color: #999;
          }
        }
      }
    }
  }
}
</style>
