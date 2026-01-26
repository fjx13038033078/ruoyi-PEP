<template>
  <div class="forum-page">
    <div class="page-header">
      <h2>考研论坛</h2>
      <p>交流经验、分享心得、互助成长</p>
    </div>

    <div class="page-content">
      <el-row :gutter="20">
        <el-col :span="17">
          <!-- 搜索和发帖 -->
          <el-card class="search-card">
            <el-row :gutter="15">
              <el-col :span="18">
                <el-input
                  v-model="queryParams.title"
                  placeholder="搜索帖子..."
                  prefix-icon="el-icon-search"
                  clearable
                  @keyup.enter.native="handleQuery"
                />
              </el-col>
              <el-col :span="6">
                <el-button type="primary" @click="openPostDialog" style="width: 100%;">
                  <i class="el-icon-edit"></i> 发布帖子
                </el-button>
              </el-col>
            </el-row>
          </el-card>

          <!-- 帖子列表 -->
          <div class="post-list" v-loading="loading">
            <el-card
              v-for="item in postList"
              :key="item.id"
              class="post-card"
              shadow="hover"
            >
              <div class="post-content" @click="goDetail(item.id)">
                <div class="post-header">
                  <el-avatar :size="45" :src="item.userAvatar">
                    <span>{{ item.userName ? item.userName.substring(0, 1) : 'U' }}</span>
                  </el-avatar>
                  <div class="post-user-info">
                    <span class="user-name">{{ item.userName }}</span>
                    <span class="post-time">{{ parseTime(item.createTime, '{y}-{m}-{d} {h}:{i}') }}</span>
                  </div>
                  <el-tag v-if="item.isTop === '1'" type="danger" size="mini" class="top-tag">置顶</el-tag>
                </div>
                <h3 class="post-title">{{ item.title }}</h3>
                <div class="post-preview" v-html="getPreviewContent(item.content)"></div>
                <div class="post-stats">
                  <span><i class="el-icon-view"></i> {{ item.viewNum || 0 }}</span>
                  <span><i class="el-icon-thumb"></i> {{ item.likeNum || 0 }}</span>
                  <span><i class="el-icon-star-off"></i> {{ item.collectNum || 0 }}</span>
                </div>
              </div>
            </el-card>
            <el-empty v-if="postList.length === 0 && !loading" description="暂无帖子"></el-empty>
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

        <!-- 右侧边栏 -->
        <el-col :span="7">
          <el-card class="sidebar-card">
            <div slot="header">
              <i class="el-icon-data-line"></i> 论坛统计
            </div>
            <div class="stat-item">
              <span class="stat-label">帖子总数</span>
              <span class="stat-value">{{ total }}</span>
            </div>
          </el-card>

          <el-card class="sidebar-card" style="margin-top: 20px;">
            <div slot="header">
              <i class="el-icon-s-flag"></i> 发帖须知
            </div>
            <ul class="notice-list">
              <li>请文明发言，禁止发布违法违规内容</li>
              <li>尊重他人，禁止人身攻击</li>
              <li>禁止发布广告、垃圾信息</li>
              <li>分享有价值的考研经验和资料</li>
            </ul>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 发帖对话框 -->
    <el-dialog title="发布帖子" :visible.sync="postDialogVisible" width="700px" append-to-body>
      <el-form ref="postForm" :model="postForm" :rules="postRules" label-width="80px">
        <el-form-item label="帖子标题" prop="title">
          <el-input v-model="postForm.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="帖子内容" prop="content">
          <editor v-model="postForm.content" :min-height="250" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="postDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="submitPost">发 布</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPost, addPost } from "@/api/preparation/post";

export default {
  name: "PortalForum",
  data() {
    return {
      loading: false,
      postList: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined
      },
      postDialogVisible: false,
      postForm: {
        title: '',
        content: ''
      },
      postRules: {
        title: [{ required: true, message: '请输入帖子标题', trigger: 'blur' }],
        content: [{ required: true, message: '请输入帖子内容', trigger: 'blur' }]
      }
    };
  },
  created() {
    // 从首页搜索跳转过来
    if (this.$route.query.keyword) {
      this.queryParams.title = this.$route.query.keyword;
    }
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listPost(this.queryParams).then(res => {
        this.postList = res.rows || [];
        this.total = res.total || 0;
        this.loading = false;
      });
    },
    getPreviewContent(content) {
      if (!content) return '';
      // 去除HTML标签，截取前150个字符
      const text = content.replace(/<[^>]+>/g, '');
      return text.length > 150 ? text.substring(0, 150) + '...' : text;
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
      this.$router.push('/portal/post/' + id);
    },
    openPostDialog() {
      this.postForm = { title: '', content: '' };
      this.postDialogVisible = true;
    },
    submitPost() {
      this.$refs.postForm.validate(valid => {
        if (valid) {
          addPost(this.postForm).then(res => {
            this.$modal.msgSuccess("发布成功");
            this.postDialogVisible = false;
            this.getList();
          });
        }
      });
    }
  }
};
</script>

<style lang="scss" scoped>
.forum-page {
  .page-header {
    background-color: #E6A23C;
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

  .search-card {
    margin-bottom: 20px;
  }

  .post-list {
    min-height: 400px;
  }

  .post-card {
    margin-bottom: 15px;
    cursor: pointer;

    &:hover {
      border-color: #409EFF;
    }

    .post-content {
      .post-header {
        display: flex;
        align-items: center;
        margin-bottom: 15px;

        .post-user-info {
          margin-left: 12px;
          flex: 1;

          .user-name {
            display: block;
            font-weight: 500;
            color: #333;
          }

          .post-time {
            font-size: 12px;
            color: #999;
          }
        }

        .top-tag {
          flex-shrink: 0;
        }
      }

      .post-title {
        font-size: 16px;
        color: #333;
        margin-bottom: 10px;

        &:hover {
          color: #409EFF;
        }
      }

      .post-preview {
        font-size: 14px;
        color: #666;
        line-height: 1.6;
        margin-bottom: 15px;
      }

      .post-stats {
        font-size: 13px;
        color: #999;

        span {
          margin-right: 20px;

          i {
            margin-right: 5px;
          }
        }
      }
    }
  }

  .sidebar-card {
    .stat-item {
      display: flex;
      justify-content: space-between;
      padding: 10px 0;

      .stat-label {
        color: #666;
      }

      .stat-value {
        font-weight: 600;
        color: #409EFF;
      }
    }

    .notice-list {
      padding-left: 20px;
      margin: 0;

      li {
        color: #666;
        line-height: 2;
        font-size: 13px;
      }
    }
  }

  .pagination-wrap {
    text-align: center;
    padding: 20px 0;
  }
}
</style>
