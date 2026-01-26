<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="资料标题" prop="title">
        <el-input
          v-model="queryParams.title"
          placeholder="请输入资料标题"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="分类" prop="categoryId">
        <el-select v-model="queryParams.categoryId" placeholder="请选择分类" clearable style="width: 200px">
          <el-option
            v-for="item in categoryOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable style="width: 200px">
          <el-option label="正常" value="0" />
          <el-option label="审核中" value="1" />
          <el-option label="拒绝" value="2" />
        </el-select>
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

    <el-table v-loading="loading" :data="materialList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="资料ID" align="center" prop="id" width="80" />
      <el-table-column label="资料标题" align="center" prop="title" :show-overflow-tooltip="true" min-width="200" />
      <el-table-column label="分类" align="center" prop="categoryName" width="100" />
      <el-table-column label="上传用户" align="center" prop="userName" width="100" />
      <el-table-column label="浏览" align="center" prop="viewCount" width="80">
        <template slot-scope="scope">
          <el-tag type="info" size="small">{{ scope.row.viewCount || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="下载" align="center" prop="downCount" width="80">
        <template slot-scope="scope">
          <el-tag type="success" size="small">{{ scope.row.downCount || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="点赞" align="center" prop="likeCount" width="80">
        <template slot-scope="scope">
          <el-tag type="warning" size="small">{{ scope.row.likeCount || 0 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ getStatusLabel(scope.row.status) }}</el-tag>
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

    <!-- 添加或修改资料对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="资料标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入资料标题" />
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="item in categoryOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="资料文件" prop="fileUrl">
          <file-upload v-model="form.fileUrl" :limit="1" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">审核中</el-radio>
            <el-radio label="2">拒绝</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="资料描述" prop="remark">
          <el-input v-model="form.remark" type="textarea" :rows="4" placeholder="请输入资料描述" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 资料详情对话框 -->
    <el-dialog title="资料详情" :visible.sync="viewOpen" width="600px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="资料标题" :span="2">{{ viewForm.title }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ viewForm.categoryName }}</el-descriptions-item>
        <el-descriptions-item label="上传用户">{{ viewForm.userName }}</el-descriptions-item>
        <el-descriptions-item label="浏览次数">{{ viewForm.viewCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="下载次数">{{ viewForm.downCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="点赞次数">{{ viewForm.likeCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="评论次数">{{ viewForm.commentCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusType(viewForm.status)">{{ getStatusLabel(viewForm.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ parseTime(viewForm.createTime) }}</el-descriptions-item>
        <el-descriptions-item label="资料文件" :span="2">
          <el-button v-if="viewForm.fileUrl" type="text" @click="handleDownload(viewForm)">点击下载</el-button>
          <span v-else>暂无文件</span>
        </el-descriptions-item>
        <el-descriptions-item label="资料描述" :span="2">{{ viewForm.remark || '暂无描述' }}</el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMaterial, getMaterial, delMaterial, addMaterial, updateMaterial } from "@/api/preparation/material";
import { listAllCategory } from "@/api/preparation/category";

export default {
  name: "Material",
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
      // 资料表格数据
      materialList: [],
      // 分类选项
      categoryOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示详情弹出层
      viewOpen: false,
      // 详情表单
      viewForm: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: undefined,
        categoryId: undefined,
        status: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [
          { required: true, message: "资料标题不能为空", trigger: "blur" }
        ],
        categoryId: [
          { required: true, message: "请选择分类", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getCategoryOptions();
  },
  methods: {
    /** 查询资料列表 */
    getList() {
      this.loading = true;
      listMaterial(this.queryParams).then(response => {
        this.materialList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 获取分类选项 */
    getCategoryOptions() {
      listAllCategory({ status: '0' }).then(response => {
        this.categoryOptions = response.data;
      });
    },
    // 获取状态标签类型
    getStatusType(status) {
      const typeMap = { '0': 'success', '1': 'warning', '2': 'danger' };
      return typeMap[status] || 'info';
    },
    // 获取状态标签文字
    getStatusLabel(status) {
      const labelMap = { '0': '正常', '1': '审核中', '2': '拒绝' };
      return labelMap[status] || '未知';
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
        categoryId: undefined,
        fileUrl: undefined,
        status: "0",
        remark: undefined
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
      this.title = "添加资料";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getMaterial(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改资料";
      });
    },
    /** 查看按钮操作 */
    handleView(row) {
      getMaterial(row.id).then(response => {
        this.viewForm = response.data;
        this.viewOpen = true;
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateMaterial(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addMaterial(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
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
      this.$modal.confirm('是否确认删除资料编号为"' + ids + '"的数据项？').then(() => {
        return delMaterial(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('preparation/material/export', {
        ...this.queryParams
      }, `material_${new Date().getTime()}.xlsx`);
    },
    /** 下载资料文件 */
    handleDownload(row) {
      if (row.fileUrl) {
        let fileUrl = row.fileUrl;
        // 如果是完整URL，提取路径部分
        if (fileUrl.startsWith('http://') || fileUrl.startsWith('https://')) {
          try {
            const url = new URL(fileUrl);
            fileUrl = url.pathname;
          } catch (e) {
            console.error('URL解析失败', e);
          }
        }
        this.$download.resource(fileUrl);
      } else {
        this.$modal.msgWarning("暂无可下载文件");
      }
    }
  }
};
</script>
