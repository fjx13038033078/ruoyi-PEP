<template>
  <div class="post-detail">
    <div class="page-content" v-loading="loading">
      <el-row :gutter="20" v-if="post.id">
        <el-col :span="17">
          <el-card class="main-card">
            <!-- 帖子头部 -->
            <div class="post-header">
              <div class="post-user">
                <el-avatar :size="50" :src="post.userAvatar">
                  <span>{{ post.userName ? post.userName.substring(0, 1) : 'U' }}</span>
                </el-avatar>
                <div class="user-info">
                  <span class="user-name">{{ post.userName }}</span>
                  <span class="post-time">{{ parseTime(post.createTime) }}</span>
                </div>
              </div>
              <el-tag v-if="post.isTop === '1'" type="danger">置顶</el-tag>
              <el-tag v-if="post.category" :type="getCategoryTagType(post.category)" style="margin-left: 8px;">
                {{ getCategoryLabel(post.category) }}
              </el-tag>
            </div>

            <h1 class="post-title">{{ post.title }}</h1>

            <!-- 帖子内容 -->
            <div class="post-content" v-html="post.content"></div>

            <!-- 帖子操作 -->
            <div class="post-actions">
              <el-button :type="isLiked ? 'primary' : 'default'" size="small" @click="handleLike">
                <i class="el-icon-thumb"></i> {{ isLiked ? '已点赞' : '点赞' }} ({{ post.likeNum || 0 }})
              </el-button>
              <el-button :type="isCollected ? 'warning' : 'default'" size="small" @click="handleCollect">
                <i class="el-icon-star-off"></i> {{ isCollected ? '已收藏' : '收藏' }} ({{ post.collectNum || 0 }})
              </el-button>
              <el-button
                v-if="post.userId === userId"
                type="danger"
                size="small"
                @click="handleDeletePost"
              >
                <i class="el-icon-delete"></i> 删除帖子
              </el-button>
            </div>

            <el-divider></el-divider>

            <!-- 评论区 -->
            <div class="comment-section">
              <h3><i class="el-icon-chat-dot-round"></i> 评论 ({{ comments.length }})</h3>
              
              <div class="comment-input">
                <el-input
                  v-model="commentContent"
                  type="textarea"
                  :rows="3"
                  placeholder="写下你的评论..."
                ></el-input>
                <el-button type="primary" size="small" @click="submitComment" style="margin-top: 10px;">
                  发表评论
                </el-button>
              </div>

              <div class="comment-list">
                <div v-for="item in comments" :key="item.id" class="comment-item">
                  <el-avatar :size="40" :src="item.userAvatar">
                    <span>{{ item.userName ? item.userName.substring(0, 1) : 'U' }}</span>
                  </el-avatar>
                  <div class="comment-body">
                    <div class="comment-header">
                      <span class="comment-user">{{ item.userName }}</span>
                      <span class="comment-time">{{ parseTime(item.createTime) }}</span>
                      <el-button
                        v-if="item.userId === userId"
                        type="text"
                        size="mini"
                        @click="deleteComment(item.id)"
                        style="color: #f56c6c;"
                      >删除</el-button>
                    </div>
                    <p class="comment-text">{{ item.content }}</p>
                    <!-- 子评论 -->
                    <div v-if="item.children && item.children.length > 0" class="child-comments">
                      <div v-for="child in item.children" :key="child.id" class="child-comment-item">
                        <el-avatar :size="30" :src="child.userAvatar">
                          <span>{{ child.userName ? child.userName.substring(0, 1) : 'U' }}</span>
                        </el-avatar>
                        <div class="child-comment-body">
                          <div class="comment-header">
                            <span class="comment-user">{{ child.userName }}</span>
                            <span class="comment-time">{{ parseTime(child.createTime) }}</span>
                            <el-button
                              v-if="child.userId === userId"
                              type="text"
                              size="mini"
                              @click="deleteComment(child.id)"
                              style="color: #f56c6c;"
                            >删除</el-button>
                          </div>
                          <p class="comment-text">{{ child.content }}</p>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <el-empty v-if="comments.length === 0" description="暂无评论，快来抢沙发~"></el-empty>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="7">
          <el-card class="info-card">
            <div slot="header">
              <i class="el-icon-data-analysis"></i> 帖子信息
            </div>
            <div class="info-item" v-if="post.category">
              <span class="label">帖子分类</span>
              <el-tag :type="getCategoryTagType(post.category)" size="small">
                {{ getCategoryLabel(post.category) }}
              </el-tag>
            </div>
            <div class="info-item">
              <span class="label">浏览量</span>
              <span class="value">{{ post.viewNum || 0 }}</span>
            </div>
            <div class="info-item">
              <span class="label">点赞数</span>
              <span class="value">{{ post.likeNum || 0 }}</span>
            </div>
            <div class="info-item">
              <span class="label">收藏数</span>
              <span class="value">{{ post.collectNum || 0 }}</span>
            </div>
          </el-card>

          <el-card class="back-card" style="margin-top: 20px;">
            <el-button @click="goBack" style="width: 100%;">
              <i class="el-icon-arrow-left"></i> 返回论坛
            </el-button>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-else-if="!loading" description="帖子不存在"></el-empty>
    </div>
  </div>
</template>

<script>
import { getPost, delPost } from "@/api/preparation/post";
import { getCommentTree, addComment, delComment } from "@/api/preparation/comment";
import { getDicts } from "@/api/system/dict/data";
import request from '@/utils/request'

export default {
  name: "PortalPostDetail",
  data() {
    return {
      loading: false,
      post: {},
      comments: [],
      commentContent: "",
      isLiked: false,
      isCollected: false,
      userId: null,
      // 分类字典
      categoryOptions: []
    };
  },
  created() {
    this.userId = this.$store.getters.userId;
    this.getDictData();
    const id = this.$route.params.id;
    if (id) {
      this.getDetail(id);
      this.getComments(id);
      this.checkActionStatus(id);
    }
  },
  methods: {
    /** 获取字典数据 */
    getDictData() {
      getDicts('ky_post_category').then(res => {
        this.categoryOptions = res.data || [];
      });
    },
    /** 获取分类标签 */
    getCategoryLabel(value) {
      const item = this.categoryOptions.find(d => d.dictValue === value);
      return item ? item.dictLabel : '';
    },
    /** 获取分类标签类型 */
    getCategoryTagType(value) {
      const typeMap = { '1': 'primary', '2': 'info', '3': 'warning' };
      return typeMap[value] || '';
    },
    getDetail(id) {
      this.loading = true;
      getPost(id).then(res => {
        this.post = res.data || {};
        this.loading = false;
      });
    },
    getComments(id) {
      getCommentTree(id, '2').then(res => {
        this.comments = res.data || [];
      });
    },
    checkActionStatus(id) {
      // 检查点赞状态
      request({
        url: '/preparation/action/check',
        method: 'get',
        params: { targetId: id, targetType: '2', actionType: '1' }
      }).then(res => {
        this.isLiked = res.data;
      });
      // 检查收藏状态
      request({
        url: '/preparation/action/check',
        method: 'get',
        params: { targetId: id, targetType: '2', actionType: '2' }
      }).then(res => {
        this.isCollected = res.data;
      });
    },
    submitComment() {
      if (!this.commentContent.trim()) {
        this.$modal.msgWarning("请输入评论内容");
        return;
      }
      addComment({
        targetId: this.post.id,
        targetType: '2',
        content: this.commentContent
      }).then(res => {
        this.$modal.msgSuccess("评论成功");
        this.commentContent = "";
        this.getComments(this.post.id);
      });
    },
    deleteComment(id) {
      this.$modal.confirm("确定删除该评论吗？").then(() => {
        delComment(id).then(res => {
          this.$modal.msgSuccess("删除成功");
          this.getComments(this.post.id);
        });
      }).catch(() => {});
    },
    handleLike() {
      request({
        url: '/preparation/action/do',
        method: 'post',
        params: { targetId: this.post.id, targetType: '2', actionType: '1' }
      }).then(res => {
        this.isLiked = !this.isLiked;
        if (this.isLiked) {
          this.post.likeNum = (this.post.likeNum || 0) + 1;
        } else {
          this.post.likeNum = Math.max(0, (this.post.likeNum || 0) - 1);
        }
      });
    },
    handleCollect() {
      request({
        url: '/preparation/action/do',
        method: 'post',
        params: { targetId: this.post.id, targetType: '2', actionType: '2' }
      }).then(res => {
        this.isCollected = !this.isCollected;
        if (this.isCollected) {
          this.post.collectNum = (this.post.collectNum || 0) + 1;
          this.$modal.msgSuccess("收藏成功");
        } else {
          this.post.collectNum = Math.max(0, (this.post.collectNum || 0) - 1);
          this.$modal.msgSuccess("已取消收藏");
        }
      });
    },
    handleDeletePost() {
      this.$modal.confirm("确定删除该帖子吗？删除后不可恢复！").then(() => {
        delPost(this.post.id).then(res => {
          this.$modal.msgSuccess("删除成功");
          this.$router.push('/portal/forum');
        });
      }).catch(() => {});
    },
    goBack() {
      this.$router.push('/portal/forum');
    }
  }
};
</script>

<style lang="scss" scoped>
.post-detail {
  .page-content {
    max-width: 1100px;
    margin: 0 auto;
    padding: 30px 20px;
  }

  .main-card {
    .post-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      .post-user {
        display: flex;
        align-items: center;

        .user-info {
          margin-left: 15px;

          .user-name {
            display: block;
            font-weight: 500;
            font-size: 15px;
            color: #333;
          }

          .post-time {
            font-size: 13px;
            color: #999;
          }
        }
      }
    }

    .post-title {
      font-size: 22px;
      color: #333;
      margin-bottom: 20px;
      line-height: 1.4;
    }

    .post-content {
      line-height: 1.8;
      color: #333;
      font-size: 15px;
      min-height: 200px;

      ::v-deep img {
        max-width: 100%;
      }
    }

    .post-actions {
      margin-top: 30px;
      padding-top: 20px;
      border-top: 1px solid #eee;
    }

    .comment-section {
      h3 {
        font-size: 16px;
        color: #333;
        margin-bottom: 20px;

        i {
          margin-right: 8px;
          color: #409EFF;
        }
      }

      .comment-input {
        margin-bottom: 25px;
      }

      .comment-item {
        display: flex;
        padding: 20px 0;
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        .comment-body {
          flex: 1;
          margin-left: 15px;

          .comment-header {
            margin-bottom: 8px;

            .comment-user {
              font-weight: 500;
              color: #333;
              margin-right: 15px;
            }

            .comment-time {
              font-size: 12px;
              color: #999;
            }
          }

          .comment-text {
            color: #666;
            line-height: 1.6;
            margin: 0;
          }

          .child-comments {
            margin-top: 15px;
            padding-left: 10px;
            border-left: 2px solid #eee;

            .child-comment-item {
              display: flex;
              padding: 10px 0;

              .child-comment-body {
                flex: 1;
                margin-left: 10px;
              }
            }
          }
        }
      }
    }
  }

  .info-card {
    .info-item {
      display: flex;
      justify-content: space-between;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;

      &:last-child {
        border-bottom: none;
      }

      .label {
        color: #666;
      }

      .value {
        font-weight: 600;
        color: #409EFF;
      }
    }
  }
}
</style>
