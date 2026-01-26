<template>
  <div class="material-detail">
    <div class="page-content" v-loading="loading">
      <el-row :gutter="20" v-if="material.id">
        <el-col :span="17">
          <el-card class="main-card">
            <div class="detail-header">
              <h1>{{ material.title }}</h1>
              <div class="detail-meta">
                <el-tag size="small">{{ material.categoryName }}</el-tag>
                <span><i class="el-icon-user"></i> {{ material.userName }}</span>
                <span><i class="el-icon-view"></i> {{ material.viewCount || 0 }} 浏览</span>
                <span><i class="el-icon-download"></i> {{ material.downCount || 0 }} 下载</span>
                <span><i class="el-icon-time"></i> {{ parseTime(material.createTime) }}</span>
              </div>
            </div>

            <el-divider></el-divider>

            <div class="detail-content">
              <h3>资料描述</h3>
              <p>{{ material.remark || '暂无描述' }}</p>
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
                  <div class="comment-content">
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
                  </div>
                </div>
                <el-empty v-if="comments.length === 0" description="暂无评论，快来抢沙发~"></el-empty>
              </div>
            </div>
          </el-card>
        </el-col>

        <el-col :span="7">
          <el-card class="action-card">
            <div class="file-info">
              <div class="file-icon">
                <i class="el-icon-document"></i>
              </div>
              <p>资料文件</p>
            </div>
            <el-button type="primary" size="medium" @click="handleDownload" style="width: 100%;">
              <i class="el-icon-download"></i> 下载资料
            </el-button>
            <div class="action-btns">
              <el-button :type="isLiked ? 'primary' : 'default'" @click="handleLike">
                <i class="el-icon-thumb"></i> {{ isLiked ? '已点赞' : '点赞' }} ({{ material.likeCount || 0 }})
              </el-button>
            </div>
          </el-card>

          <el-card class="back-card" style="margin-top: 20px;">
            <el-button @click="goBack" style="width: 100%;">
              <i class="el-icon-arrow-left"></i> 返回列表
            </el-button>
          </el-card>
        </el-col>
      </el-row>
      <el-empty v-else-if="!loading" description="资料不存在"></el-empty>
    </div>
  </div>
</template>

<script>
import { getMaterial, downloadMaterial } from "@/api/preparation/material";
import { getCommentTree, addComment, delComment } from "@/api/preparation/comment";
import request from '@/utils/request'

export default {
  name: "PortalMaterialDetail",
  data() {
    return {
      loading: false,
      material: {},
      comments: [],
      commentContent: "",
      isLiked: false,
      userId: null
    };
  },
  created() {
    this.userId = this.$store.getters.userId;
    const id = this.$route.params.id;
    if (id) {
      this.getDetail(id);
      this.getComments(id);
      this.checkLikeStatus(id);
    }
  },
  methods: {
    getDetail(id) {
      this.loading = true;
      getMaterial(id).then(res => {
        this.material = res.data || {};
        this.loading = false;
      });
    },
    getComments(id) {
      getCommentTree(id, '1').then(res => {
        this.comments = res.data || [];
      });
    },
    checkLikeStatus(id) {
      request({
        url: '/preparation/action/check',
        method: 'get',
        params: { targetId: id, targetType: '1', actionType: '1' }
      }).then(res => {
        this.isLiked = res.data;
      });
    },
    submitComment() {
      if (!this.commentContent.trim()) {
        this.$modal.msgWarning("请输入评论内容");
        return;
      }
      addComment({
        targetId: this.material.id,
        targetType: '1',
        content: this.commentContent
      }).then(res => {
        this.$modal.msgSuccess("评论成功");
        this.commentContent = "";
        this.getComments(this.material.id);
      });
    },
    deleteComment(id) {
      this.$modal.confirm("确定删除该评论吗？").then(() => {
        delComment(id).then(res => {
          this.$modal.msgSuccess("删除成功");
          this.getComments(this.material.id);
        });
      }).catch(() => {});
    },
    handleDownload() {
      if (this.material.fileUrl) {
        downloadMaterial(this.material.id).then(() => {
          window.open(this.material.fileUrl, '_blank');
        });
      } else {
        this.$modal.msgWarning("暂无可下载文件");
      }
    },
    handleLike() {
      request({
        url: '/preparation/action/do',
        method: 'post',
        params: { targetId: this.material.id, targetType: '1', actionType: '1' }
      }).then(res => {
        this.isLiked = !this.isLiked;
        if (this.isLiked) {
          this.material.likeCount = (this.material.likeCount || 0) + 1;
        } else {
          this.material.likeCount = Math.max(0, (this.material.likeCount || 0) - 1);
        }
      });
    },
    goBack() {
      this.$router.push('/portal/material');
    }
  }
};
</script>

<style lang="scss" scoped>
.material-detail {
  .page-content {
    max-width: 1100px;
    margin: 0 auto;
    padding: 30px 20px;
  }

  .main-card {
    .detail-header {
      h1 {
        font-size: 22px;
        color: #333;
        margin-bottom: 15px;
      }

      .detail-meta {
        font-size: 13px;
        color: #999;

        .el-tag {
          margin-right: 15px;
        }

        span {
          margin-right: 20px;

          i {
            margin-right: 5px;
          }
        }
      }
    }

    .detail-content {
      h3 {
        font-size: 15px;
        color: #333;
        margin-bottom: 15px;
      }

      p {
        line-height: 1.8;
        color: #666;
      }
    }

    .comment-section {
      h3 {
        font-size: 15px;
        color: #333;
        margin-bottom: 20px;

        i {
          margin-right: 8px;
          color: #409EFF;
        }
      }

      .comment-input {
        margin-bottom: 20px;
      }

      .comment-item {
        display: flex;
        padding: 15px 0;
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        .comment-content {
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
        }
      }
    }
  }

  .action-card {
    text-align: center;

    .file-info {
      margin-bottom: 20px;

      .file-icon {
        width: 70px;
        height: 70px;
        background-color: #67C23A;
        display: flex;
        align-items: center;
        justify-content: center;
        margin: 0 auto 15px;

        i {
          font-size: 32px;
          color: #fff;
        }
      }

      p {
        color: #666;
        margin: 0;
      }
    }

    .action-btns {
      margin-top: 15px;

      .el-button {
        width: 100%;
      }
    }
  }
}
</style>
