<template>
  <div class="university-detail">
    <div class="page-content" v-loading="loading">
      <el-card v-if="university.id">
        <div class="detail-header">
          <el-avatar :size="100" :src="university.logoUrl" class="university-logo">
            <span style="font-size: 36px;">{{ university.name ? university.name.substring(0, 1) : '' }}</span>
          </el-avatar>
          <div class="university-info">
            <h1>{{ university.name }}</h1>
            <div class="info-tags">
              <el-tag :type="getLevelType(university.level)" size="medium">{{ university.level }}</el-tag>
              <el-tag type="info" size="medium">{{ university.region }}</el-tag>
              <el-tag size="medium">院校代码：{{ university.code }}</el-tag>
            </div>
            <div class="info-link" v-if="university.website">
              <i class="el-icon-link"></i>
              <el-link type="primary" :href="university.website" target="_blank">{{ university.website }}</el-link>
            </div>
          </div>
        </div>

        <el-divider></el-divider>

        <div class="detail-section">
          <h3><i class="el-icon-document"></i> 院校简介</h3>
          <div class="intro-content">
            {{ university.intro || '暂无简介' }}
          </div>
        </div>

        <el-divider></el-divider>

        <div class="detail-section">
          <h3><i class="el-icon-info"></i> 其他信息</h3>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="创建时间">{{ parseTime(university.createTime) }}</el-descriptions-item>
            <el-descriptions-item label="更新时间">{{ parseTime(university.updateTime) }}</el-descriptions-item>
            <el-descriptions-item label="备注" :span="2">{{ university.remark || '暂无' }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <div class="detail-actions">
          <el-button @click="goBack"><i class="el-icon-arrow-left"></i> 返回列表</el-button>
        </div>
      </el-card>
      <el-empty v-else-if="!loading" description="院校信息不存在"></el-empty>
    </div>
  </div>
</template>

<script>
import { getUniversity } from "@/api/preparation/university";

export default {
  name: "PortalUniversityDetail",
  data() {
    return {
      loading: false,
      university: {}
    };
  },
  created() {
    const id = this.$route.params.id;
    if (id) {
      this.getDetail(id);
    }
  },
  methods: {
    getDetail(id) {
      this.loading = true;
      getUniversity(id).then(res => {
        this.university = res.data || {};
        this.loading = false;
      });
    },
    getLevelType(level) {
      const typeMap = { '985': 'danger', '211': 'warning', '双一流': 'success', '普通': 'info' };
      return typeMap[level] || 'info';
    },
    goBack() {
      this.$router.push('/portal/university');
    }
  }
};
</script>

<style lang="scss" scoped>
.university-detail {
  .page-content {
    max-width: 900px;
    margin: 0 auto;
    padding: 30px 20px;
  }

  .detail-header {
    display: flex;
    align-items: center;
    padding: 20px 0;

    .university-logo {
      background-color: #409EFF;
      color: #fff;
      flex-shrink: 0;
    }

    .university-info {
      margin-left: 30px;

      h1 {
        font-size: 24px;
        color: #333;
        margin-bottom: 15px;
      }

      .info-tags {
        margin-bottom: 10px;

        .el-tag {
          margin-right: 10px;
        }
      }

      .info-link {
        font-size: 14px;
        color: #666;

        i {
          margin-right: 5px;
        }
      }
    }
  }

  .detail-section {
    padding: 10px 0;

    h3 {
      font-size: 16px;
      color: #333;
      margin-bottom: 15px;

      i {
        margin-right: 8px;
        color: #409EFF;
      }
    }

    .intro-content {
      line-height: 1.8;
      color: #666;
      text-align: justify;
    }
  }

  .detail-actions {
    margin-top: 30px;
    text-align: center;
  }
}
</style>
