<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="目标类型" prop="targetType">
        <el-select v-model="queryParams.targetType" placeholder="请选择目标类型" clearable style="width: 200px">
          <el-option label="资料" value="1" />
          <el-option label="帖子" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="目标ID" prop="targetId">
        <el-input
          v-model="queryParams.targetId"
          placeholder="请输入目标ID"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="评论内容" prop="content">
        <el-input
          v-model="queryParams.content"
          placeholder="请输入评论内容"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="commentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="评论ID" align="center" prop="id" width="80" />
      <el-table-column label="目标类型" align="center" prop="targetType" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.targetType === '1' ? 'success' : 'primary'">
            {{ scope.row.targetType === '1' ? '资料' : '帖子' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="目标ID" align="center" prop="targetId" width="80" />
      <el-table-column label="评论人" align="center" width="100">
        <template slot-scope="scope">
          <div class="user-info">
            <el-avatar v-if="scope.row.userAvatar" :src="scope.row.userAvatar" :size="24"></el-avatar>
            <span style="margin-left: 5px;">{{ scope.row.userName }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="评论内容" align="center" prop="content" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="父评论ID" align="center" prop="parentId" width="100">
        <template slot-scope="scope">
          <span>{{ scope.row.parentId || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="评论时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="120">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
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

    <!-- 评论详情对话框 -->
    <el-dialog title="评论详情" :visible.sync="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="评论ID">{{ viewForm.id }}</el-descriptions-item>
        <el-descriptions-item label="目标类型">
          <el-tag :type="viewForm.targetType === '1' ? 'success' : 'primary'">
            {{ viewForm.targetType === '1' ? '资料' : '帖子' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="目标ID">{{ viewForm.targetId }}</el-descriptions-item>
        <el-descriptions-item label="父评论ID">{{ viewForm.parentId || '无（顶级评论）' }}</el-descriptions-item>
        <el-descriptions-item label="评论人">
          <div class="user-info">
            <el-avatar v-if="viewForm.userAvatar" :src="viewForm.userAvatar" :size="24"></el-avatar>
            <span style="margin-left: 5px;">{{ viewForm.userName }}</span>
          </div>
        </el-descriptions-item>
        <el-descriptions-item label="评论时间">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="评论内容" :span="2">
          <div class="comment-content">{{ viewForm.content }}</div>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button type="danger" @click="handleDeleteFromView">删 除</el-button>
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listComment, getComment, delComment } from "@/api/preparation/comment";

export default {
  name: "Comment",
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
      // 评论表格数据
      commentList: [],
      // 是否显示详情弹出层
      viewOpen: false,
      // 详情表单
      viewForm: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        targetType: undefined,
        targetId: undefined,
        content: undefined
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询评论列表 */
    getList() {
      this.loading = true;
      listComment(this.queryParams).then(response => {
        this.commentList = response.rows;
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
      getComment(row.id).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 从详情页删除 */
    handleDeleteFromView() {
      this.$modal.confirm('是否确认删除该评论？').then(() => {
        return delComment(this.viewForm.id);
      }).then(() => {
        this.viewOpen = false;
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除评论编号为"' + ids + '"的数据项？').then(() => {
        return delComment(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('preparation/comment/export', {
        ...this.queryParams
      }, `comment_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>

<style scoped>
.user-info {
  display: flex;
  align-items: center;
  justify-content: center;
}
.comment-content {
  padding: 10px;
  background-color: #f5f5f5;
  border-radius: 4px;
  line-height: 1.6;
}
</style>
