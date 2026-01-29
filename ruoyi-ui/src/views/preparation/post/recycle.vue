<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="帖子标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入帖子标题"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="帖子分类" prop="category">
        <el-select v-model="queryParams.category" placeholder="请选择分类" clearable style="width: 200px">
          <el-option
            v-for="dict in dict.type.ky_post_category"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-refresh-left" size="mini" :disabled="multiple" @click="handleRestore">还原</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDestroy">彻底删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-delete" size="mini" @click="handleClearAll">清空回收站</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="postList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="帖子ID" align="center" prop="id" width="80" />
      <el-table-column label="帖子标题" align="center" prop="title" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="帖子分类" align="center" prop="category" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ky_post_category" :value="scope.row.category"/>
        </template>
      </el-table-column>
      <el-table-column label="发帖人" align="center" prop="userName" width="100" />
      <el-table-column label="浏览量" align="center" prop="viewNum" width="80">
        <template slot-scope="scope">
          <el-tag type="info" size="small">{{ scope.row.viewNum || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="点赞数" align="center" prop="likeNum" width="80">
        <template slot-scope="scope">
          <el-tag type="warning" size="small">{{ scope.row.likeNum || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="收藏数" align="center" prop="collectNum" width="80">
        <template slot-scope="scope">
          <el-tag type="success" size="small">{{ scope.row.collectNum || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="删除时间" align="center" prop="updateTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-refresh-left" @click="handleRestore(scope.row)">还原</el-button>
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" style="color: #f56c6c;" @click="handleDestroy(scope.row)">彻底删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 帖子详情对话框 -->
    <el-dialog title="帖子详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="帖子标题" :span="2">{{ viewForm.title }}</el-descriptions-item>
        <el-descriptions-item label="发帖人">{{ viewForm.userName }}</el-descriptions-item>
        <el-descriptions-item label="帖子分类">
          <dict-tag :options="dict.type.ky_post_category" :value="viewForm.category"/>
        </el-descriptions-item>
        <el-descriptions-item label="浏览量">{{ viewForm.viewNum || 0 }}</el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ viewForm.likeNum || 0 }}</el-descriptions-item>
        <el-descriptions-item label="收藏数">{{ viewForm.collectNum || 0 }}</el-descriptions-item>
        <el-descriptions-item label="发布时间">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="帖子内容" :span="2">
          <div v-html="viewForm.content" class="post-content"></div>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button type="success" @click="handleRestoreFromView">还 原</el-button>
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listRecyclePost, restorePost, destroyPost } from "@/api/preparation/post";

export default {
  name: "PostRecycle",
  dicts: ['ky_post_category'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 帖子表格数据
      postList: [],
      // 是否显示详情弹出层
      viewOpen: false,
      // 详情表单
      viewForm: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        category: undefined
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询回收站帖子列表 */
    getList() {
      this.loading = true;
      listRecyclePost(this.queryParams).then(response => {
        this.postList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.multiple = !selection.length;
    },
    /** 查看按钮操作 */
    handleView(row) {
      this.viewForm = row;
      this.viewOpen = true;
    },
    /** 还原按钮操作 */
    handleRestore(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认还原帖子编号为"' + ids + '"的数据项？').then(() => {
        return restorePost(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("还原成功");
      }).catch(() => {});
    },
    /** 从详情对话框还原 */
    handleRestoreFromView() {
      restorePost(this.viewForm.id).then(() => {
        this.viewOpen = false;
        this.getList();
        this.$modal.msgSuccess("还原成功");
      });
    },
    /** 彻底删除按钮操作 */
    handleDestroy(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认彻底删除帖子编号为"' + ids + '"的数据项？\n此操作不可恢复！').then(() => {
        return destroyPost(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 清空回收站 */
    handleClearAll() {
      if (this.postList.length === 0) {
        this.$modal.msgWarning("回收站为空");
        return;
      }
      this.$modal.confirm('是否确认清空回收站？\n此操作将彻底删除所有回收站中的帖子，不可恢复！').then(() => {
        const allIds = this.postList.map(item => item.id);
        return destroyPost(allIds.join(','));
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("已清空回收站");
      }).catch(() => {});
    }
  }
};
</script>

<style scoped>
.post-content {
  max-height: 400px;
  overflow-y: auto;
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
}
</style>
