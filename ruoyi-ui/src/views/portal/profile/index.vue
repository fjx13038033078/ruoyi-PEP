<template>
  <div class="profile-page">
    <div class="page-content">
      <el-row :gutter="20">
        <!-- 左侧用户信息 -->
        <el-col :span="6">
          <el-card class="user-card">
            <div class="user-avatar">
              <el-avatar :size="80" :src="user.avatar">
                <span style="font-size: 30px;">{{ user.nickName ? user.nickName.substring(0, 1) : 'U' }}</span>
              </el-avatar>
            </div>
            <h3 class="user-name">{{ user.nickName }}</h3>
            <p class="user-account">{{ user.userName }}</p>
            <div class="user-stats">
              <div class="stat-item">
                <span class="stat-value">{{ myPostCount }}</span>
                <span class="stat-label">帖子</span>
              </div>
              <div class="stat-item">
                <span class="stat-value">{{ myCollectCount }}</span>
                <span class="stat-label">收藏</span>
              </div>
            </div>
          </el-card>

          <el-card class="menu-card" style="margin-top: 20px;">
            <el-menu :default-active="activeMenu" @select="handleMenuSelect">
              <el-menu-item index="post">
                <i class="el-icon-document"></i>
                <span>我的帖子</span>
              </el-menu-item>
              <el-menu-item index="collect">
                <i class="el-icon-star-off"></i>
                <span>我的收藏</span>
              </el-menu-item>
              <el-menu-item index="browse">
                <i class="el-icon-view"></i>
                <span>浏览记录</span>
              </el-menu-item>
            </el-menu>
          </el-card>
        </el-col>

        <!-- 右侧内容区 -->
        <el-col :span="18">
          <!-- 我的帖子 -->
          <el-card v-if="activeMenu === 'post'" class="content-card">
            <div slot="header">
              <span><i class="el-icon-document"></i> 我的帖子</span>
            </div>
            <div class="list-content" v-loading="loading">
              <div v-for="item in myPosts" :key="item.id" class="list-item">
                <div class="item-content" @click="goPostDetail(item.id)">
                  <h4>{{ item.title }}</h4>
                  <div class="item-meta">
                    <span><i class="el-icon-view"></i> {{ item.viewNum || 0 }}</span>
                    <span><i class="el-icon-thumb"></i> {{ item.likeNum || 0 }}</span>
                    <span><i class="el-icon-time"></i> {{ parseTime(item.createTime, '{y}-{m}-{d}') }}</span>
                  </div>
                </div>
                <el-button type="danger" size="mini" @click="deletePost(item.id)">删除</el-button>
              </div>
              <el-empty v-if="myPosts.length === 0 && !loading" description="暂无帖子"></el-empty>
            </div>
            <div class="pagination-wrap" v-if="postTotal > 0">
              <el-pagination
                small
                background
                @current-change="handlePostPageChange"
                :current-page="postQuery.pageNum"
                :page-size="postQuery.pageSize"
                layout="prev, pager, next"
                :total="postTotal"
              ></el-pagination>
            </div>
          </el-card>

          <!-- 我的收藏 -->
          <el-card v-if="activeMenu === 'collect'" class="content-card">
            <div slot="header">
              <span><i class="el-icon-star-off"></i> 我的收藏</span>
            </div>
            <div class="list-content" v-loading="loading">
              <div v-for="item in myCollects" :key="item.id" class="list-item">
                <div class="item-content" @click="goPostDetail(item.targetId)">
                  <h4>{{ item.targetTitle || '帖子ID: ' + item.targetId }}</h4>
                  <div class="item-meta">
                    <el-tag size="mini">{{ item.targetType === '1' ? '资料' : '帖子' }}</el-tag>
                    <span><i class="el-icon-time"></i> {{ parseTime(item.createTime, '{y}-{m}-{d}') }}</span>
                  </div>
                </div>
                <el-button type="warning" size="mini" @click="cancelCollect(item)">取消收藏</el-button>
              </div>
              <el-empty v-if="myCollects.length === 0 && !loading" description="暂无收藏"></el-empty>
            </div>
            <div class="pagination-wrap" v-if="collectTotal > 0">
              <el-pagination
                small
                background
                @current-change="handleCollectPageChange"
                :current-page="collectQuery.pageNum"
                :page-size="collectQuery.pageSize"
                layout="prev, pager, next"
                :total="collectTotal"
              ></el-pagination>
            </div>
          </el-card>

          <!-- 浏览记录 -->
          <el-card v-if="activeMenu === 'browse'" class="content-card">
            <div slot="header">
              <span><i class="el-icon-view"></i> 浏览记录</span>
            </div>
            <div class="list-content" v-loading="loading">
              <div v-for="item in browseList" :key="item.id" class="list-item">
                <div class="item-content" @click="goBrowseDetail(item)">
                  <h4>{{ item.targetTitle || (item.targetType === '1' ? '资料' : '帖子') + ' ID: ' + item.targetId }}</h4>
                  <div class="item-meta">
                    <el-tag size="mini">{{ item.targetType === '1' ? '资料' : '帖子' }}</el-tag>
                    <span><i class="el-icon-time"></i> {{ parseTime(item.browseTime) }}</span>
                  </div>
                </div>
              </div>
              <el-empty v-if="browseList.length === 0 && !loading" description="暂无浏览记录"></el-empty>
            </div>
            <div class="pagination-wrap" v-if="browseTotal > 0">
              <el-pagination
                small
                background
                @current-change="handleBrowsePageChange"
                :current-page="browseQuery.pageNum"
                :page-size="browseQuery.pageSize"
                layout="prev, pager, next"
                :total="browseTotal"
              ></el-pagination>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { listPost, delPost } from "@/api/preparation/post";
import request from '@/utils/request'

export default {
  name: "PortalProfile",
  data() {
    return {
      loading: false,
      activeMenu: 'post',
      user: {},
      myPosts: [],
      myCollects: [],
      browseList: [],
      myPostCount: 0,
      myCollectCount: 0,
      postTotal: 0,
      collectTotal: 0,
      browseTotal: 0,
      postQuery: { pageNum: 1, pageSize: 10 },
      collectQuery: { pageNum: 1, pageSize: 10 },
      browseQuery: { pageNum: 1, pageSize: 10 }
    };
  },
  created() {
    this.user = this.$store.getters.userInfo || {};
    // 根据路由参数决定显示哪个tab
    const tab = this.$route.params.tab;
    if (tab) {
      this.activeMenu = tab;
    }
    this.loadData();
  },
  watch: {
    '$route.params.tab'(newTab) {
      if (newTab) {
        this.activeMenu = newTab;
        this.loadData();
      }
    }
  },
  methods: {
    loadData() {
      if (this.activeMenu === 'post') {
        this.getMyPosts();
      } else if (this.activeMenu === 'collect') {
        this.getMyCollects();
      } else if (this.activeMenu === 'browse') {
        this.getBrowseList();
      }
      this.getStatistics();
    },
    getStatistics() {
      // 获取我的帖子数量
      listPost({ pageNum: 1, pageSize: 1, userId: this.$store.getters.userId }).then(res => {
        this.myPostCount = res.total || 0;
      });
      // 获取我的收藏数量
      request({
        url: '/preparation/action/my',
        method: 'get',
        params: { pageNum: 1, pageSize: 1, actionType: '2' }
      }).then(res => {
        this.myCollectCount = res.total || 0;
      });
    },
    getMyPosts() {
      this.loading = true;
      listPost({ ...this.postQuery, userId: this.$store.getters.userId }).then(res => {
        this.myPosts = res.rows || [];
        this.postTotal = res.total || 0;
        this.loading = false;
      });
    },
    getMyCollects() {
      this.loading = true;
      request({
        url: '/preparation/action/my',
        method: 'get',
        params: { ...this.collectQuery, actionType: '2' }
      }).then(res => {
        this.myCollects = res.rows || [];
        this.collectTotal = res.total || 0;
        this.loading = false;
      });
    },
    getBrowseList() {
      this.loading = true;
      request({
        url: '/preparation/browse/my',
        method: 'get',
        params: this.browseQuery
      }).then(res => {
        this.browseList = res.rows || [];
        this.browseTotal = res.total || 0;
        this.loading = false;
      });
    },
    handleMenuSelect(index) {
      this.activeMenu = index;
      this.loadData();
    },
    handlePostPageChange(page) {
      this.postQuery.pageNum = page;
      this.getMyPosts();
    },
    handleCollectPageChange(page) {
      this.collectQuery.pageNum = page;
      this.getMyCollects();
    },
    handleBrowsePageChange(page) {
      this.browseQuery.pageNum = page;
      this.getBrowseList();
    },
    goPostDetail(id) {
      this.$router.push('/portal/post/' + id);
    },
    goBrowseDetail(item) {
      if (item.targetType === '1') {
        this.$router.push('/portal/material/' + item.targetId);
      } else {
        this.$router.push('/portal/post/' + item.targetId);
      }
    },
    deletePost(id) {
      this.$modal.confirm("确定删除该帖子吗？").then(() => {
        delPost(id).then(res => {
          this.$modal.msgSuccess("删除成功");
          this.getMyPosts();
          this.getStatistics();
        });
      }).catch(() => {});
    },
    cancelCollect(item) {
      this.$modal.confirm("确定取消收藏吗？").then(() => {
        request({
          url: '/preparation/action/do',
          method: 'post',
          params: { targetId: item.targetId, targetType: item.targetType, actionType: '2' }
        }).then(res => {
          this.$modal.msgSuccess("已取消收藏");
          this.getMyCollects();
          this.getStatistics();
        });
      }).catch(() => {});
    }
  }
};
</script>

<style lang="scss" scoped>
.profile-page {
  background-color: #f5f5f5;
  min-height: calc(100vh - 120px);

  .page-content {
    max-width: 1100px;
    margin: 0 auto;
    padding: 30px 20px;
  }

  .user-card {
    text-align: center;
    padding: 20px 0;

    .user-avatar {
      margin-bottom: 15px;
    }

    .user-name {
      font-size: 16px;
      color: #333;
      margin-bottom: 5px;
    }

    .user-account {
      font-size: 13px;
      color: #999;
      margin-bottom: 20px;
    }

    .user-stats {
      display: flex;
      justify-content: center;
      border-top: 1px solid #eee;
      padding-top: 20px;

      .stat-item {
        padding: 0 30px;
        text-align: center;

        &:not(:last-child) {
          border-right: 1px solid #eee;
        }

        .stat-value {
          display: block;
          font-size: 18px;
          font-weight: 600;
          color: #409EFF;
        }

        .stat-label {
          font-size: 12px;
          color: #999;
        }
      }
    }
  }

  .menu-card {
    ::v-deep .el-card__body {
      padding: 0;
    }

    .el-menu {
      border-right: none;

      .el-menu-item {
        i {
          margin-right: 10px;
        }
      }
    }
  }

  .content-card {
    min-height: 500px;

    .list-content {
      min-height: 400px;
    }

    .list-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 15px;
      border-bottom: 1px solid #f0f0f0;

      &:hover {
        background-color: #fafafa;
      }

      &:last-child {
        border-bottom: none;
      }

      .item-content {
        flex: 1;
        cursor: pointer;
        overflow: hidden;

        h4 {
          font-size: 15px;
          color: #333;
          margin-bottom: 8px;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;

          &:hover {
            color: #409EFF;
          }
        }

        .item-meta {
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
    }

    .pagination-wrap {
      text-align: center;
      padding: 20px 0;
    }
  }
}
</style>
