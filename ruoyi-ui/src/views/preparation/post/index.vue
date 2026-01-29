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
      <el-form-item label="是否置顶" prop="isTop">
        <el-select v-model="queryParams.isTop" placeholder="请选择" clearable style="width: 200px">
          <el-option label="是" value="1" />
          <el-option label="否" value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="dateRange"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport">导出</el-button>
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
      <el-table-column label="置顶" align="center" prop="isTop" width="80">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.isTop"
            active-value="1"
            inactive-value="0"
            @change="handleTopChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
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

    <!-- 添加或修改帖子对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="帖子标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入帖子标题" />
        </el-form-item>
        <el-form-item label="帖子分类" prop="category">
          <el-select v-model="form.category" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="dict in dict.type.ky_post_category"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="是否置顶" prop="isTop">
          <el-radio-group v-model="form.isTop">
            <el-radio label="1">是</el-radio>
            <el-radio label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="帖子内容" prop="content">
          <editor v-model="form.content" :min-height="300" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 帖子详情对话框 -->
    <el-dialog title="帖子详情" :visible.sync="viewOpen" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="帖子标题" :span="2">{{ viewForm.title }}</el-descriptions-item>
        <el-descriptions-item label="发帖人">{{ viewForm.userName }}</el-descriptions-item>
        <el-descriptions-item label="帖子分类">
          <dict-tag :options="dict.type.ky_post_category" :value="viewForm.category"/>
        </el-descriptions-item>
        <el-descriptions-item label="是否置顶">
          <el-tag :type="viewForm.isTop === '1' ? 'success' : 'info'">{{ viewForm.isTop === '1' ? '是' : '否' }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="浏览量">{{ viewForm.viewNum || 0 }}</el-descriptions-item>
        <el-descriptions-item label="点赞数">{{ viewForm.likeNum || 0 }}</el-descriptions-item>
        <el-descriptions-item label="收藏数">{{ viewForm.collectNum || 0 }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="帖子内容" :span="2">
          <div v-html="viewForm.content" class="post-content"></div>
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listPost, getPost, delPost, addPost, updatePost } from "@/api/preparation/post";

export default {
  name: "Post",
  dicts: ['ky_post_category'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 帖子表格数据
      postList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      viewOpen: false,
      // 详情表单
      viewForm: {},
      // 日期范围
      dateRange: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        category: undefined,
        isTop: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "帖子标题不能为空", trigger: "blur" }
        ],
        category: [
          { required: true, message: "请选择帖子分类", trigger: "change" }
        ],
        content: [
          { required: true, message: "帖子内容不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询帖子列表 */
    getList() {
      this.loading = true;
      listPost(this.addDateRange(this.queryParams, this.dateRange)).then(response => {
        this.postList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: undefined,
        title: undefined,
        content: undefined,
        category: undefined,
        isTop: "0"
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "发布帖子";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getPost(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改帖子";
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      getPost(row.id).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 置顶状态修改 */
    handleTopChange(row) {
      updatePost({ id: row.id, isTop: row.isTop }).then(response => {
        this.$modal.msgSuccess(row.isTop === '1' ? '已置顶' : '已取消置顶');
      }).catch(() => {
        row.isTop = row.isTop === '1' ? '0' : '1';
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updatePost(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPost(this.form).then(response => {
              this.$modal.msgSuccess("发布成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除帖子编号为"' + ids + '"的数据项？').then(() => {
        return delPost(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('preparation/post/export', {
        ...this.queryParams
      }, `post_${new Date().getTime()}.xlsx`);
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
