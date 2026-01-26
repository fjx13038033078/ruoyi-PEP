<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="院校名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入院校名称"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="院校代码" prop="code">
        <el-input
          v-model="queryParams.code"
          placeholder="请输入院校代码"
          clearable
          style="width: 200px"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="层次" prop="level">
        <el-select v-model="queryParams.level" placeholder="请选择层次" clearable style="width: 200px">
          <el-option label="985" value="985" />
          <el-option label="211" value="211" />
          <el-option label="双一流" value="双一流" />
          <el-option label="普通" value="普通" />
        </el-select>
      </el-form-item>
      <el-form-item label="所在地区" prop="region">
        <el-input
          v-model="queryParams.region"
          placeholder="请输入所在地区"
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

    <el-table v-loading="loading" :data="universityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="院校ID" align="center" prop="id" width="80" />
      <el-table-column label="校徽" align="center" prop="logoUrl" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.logoUrl" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="院校名称" align="center" prop="name" :show-overflow-tooltip="true" />
      <el-table-column label="院校代码" align="center" prop="code" width="100" />
      <el-table-column label="层次" align="center" prop="level" width="100">
        <template slot-scope="scope">
          <el-tag :type="getLevelType(scope.row.level)">{{ scope.row.level }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="所在地区" align="center" prop="region" width="120" />
      <el-table-column label="官网地址" align="center" prop="website" :show-overflow-tooltip="true">
        <template slot-scope="scope">
          <el-link type="primary" :href="scope.row.website" target="_blank">{{ scope.row.website }}</el-link>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
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

    <!-- 添加或修改院校信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="院校名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入院校名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="院校代码" prop="code">
              <el-input v-model="form.code" placeholder="请输入院校代码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="层次" prop="level">
              <el-select v-model="form.level" placeholder="请选择层次" style="width: 100%">
                <el-option label="985" value="985" />
                <el-option label="211" value="211" />
                <el-option label="双一流" value="双一流" />
                <el-option label="普通" value="普通" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所在地区" prop="region">
              <el-input v-model="form.region" placeholder="请输入所在地区" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="官网地址" prop="website">
          <el-input v-model="form.website" placeholder="请输入官网地址" />
        </el-form-item>
        <el-form-item label="校徽图片" prop="logoUrl">
          <image-upload v-model="form.logoUrl" :limit="1" />
        </el-form-item>
        <el-form-item label="院校简介" prop="intro">
          <el-input v-model="form.intro" type="textarea" :rows="4" placeholder="请输入院校简介" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listUniversity, getUniversity, delUniversity, addUniversity, updateUniversity } from "@/api/preparation/university";

export default {
  name: "University",
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
      // 院校信息表格数据
      universityList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: undefined,
        code: undefined,
        level: undefined,
        region: undefined
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        name: [
          { required: true, message: "院校名称不能为空", trigger: "blur" }
        ],
        code: [
          { required: true, message: "院校代码不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询院校信息列表 */
    getList() {
      this.loading = true;
      listUniversity(this.queryParams).then(response => {
        this.universityList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 获取层次标签类型
    getLevelType(level) {
      const typeMap = {
        '985': 'danger',
        '211': 'warning',
        '双一流': 'success',
        '普通': 'info'
      };
      return typeMap[level] || 'info';
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
        name: undefined,
        code: undefined,
        level: undefined,
        region: undefined,
        website: undefined,
        intro: undefined,
        logoUrl: undefined,
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
      this.title = "添加院校信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getUniversity(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改院校信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateUniversity(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addUniversity(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除院校编号为"' + ids + '"的数据项？').then(() => {
        return delUniversity(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('preparation/university/export', {
        ...this.queryParams
      }, `university_${new Date().getTime()}.xlsx`);
    }
  }
};
</script>
