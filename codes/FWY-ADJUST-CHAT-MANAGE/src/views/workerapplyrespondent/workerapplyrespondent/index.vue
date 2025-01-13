<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="aj_worker_apply表id" prop="workApplyId">
        <el-input
          v-model="queryParams.workApplyId"
          placeholder="请输入aj_worker_apply表id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="企业名称" prop="companyName">
        <el-input
          v-model="queryParams.companyName"
          placeholder="请输入企业名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input
          v-model="queryParams.phone"
          placeholder="请输入联系电话"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="法定代表人" prop="companyLar">
        <el-input
          v-model="queryParams.companyLar"
          placeholder="请输入法定代表人"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="职务" prop="job">
        <el-input
          v-model="queryParams.job"
          placeholder="请输入职务"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="单位性质" prop="companyNature">
        <el-input
          v-model="queryParams.companyNature"
          placeholder="请输入单位性质"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="省级id" prop="provinceId">
        <el-input
          v-model="queryParams.provinceId"
          placeholder="请输入省级id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="市级id" prop="cityId">
        <el-input
          v-model="queryParams.cityId"
          placeholder="请输入市级id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="区级id" prop="areaId">
        <el-input
          v-model="queryParams.areaId"
          placeholder="请输入区级id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="详细地址" prop="address">
        <el-input
          v-model="queryParams.address"
          placeholder="请输入详细地址"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮政编码" prop="postCode">
        <el-input
          v-model="queryParams.postCode"
          placeholder="请输入邮政编码"
          clearable
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
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['workerapplyrespondent:workerapplyrespondent:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['workerapplyrespondent:workerapplyrespondent:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['workerapplyrespondent:workerapplyrespondent:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['workerapplyrespondent:workerapplyrespondent:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workerapplyrespondentList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="${comment}" align="center" prop="id" />
      <el-table-column label="aj_worker_apply表id" align="center" prop="workApplyId" />
      <el-table-column label="企业名称" align="center" prop="companyName" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="法定代表人" align="center" prop="companyLar" />
      <el-table-column label="职务" align="center" prop="job" />
      <el-table-column label="单位性质" align="center" prop="companyNature" />
      <el-table-column label="省级id" align="center" prop="provinceId" />
      <el-table-column label="市级id" align="center" prop="cityId" />
      <el-table-column label="区级id" align="center" prop="areaId" />
      <el-table-column label="详细地址" align="center" prop="address" />
      <el-table-column label="邮政编码" align="center" prop="postCode" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['workerapplyrespondent:workerapplyrespondent:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['workerapplyrespondent:workerapplyrespondent:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改劳动者提交的申请（被申请人信息）对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="aj_worker_apply表id" prop="workApplyId">
          <el-input v-model="form.workApplyId" placeholder="请输入aj_worker_apply表id" />
        </el-form-item>
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="form.companyName" placeholder="请输入企业名称" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="法定代表人" prop="companyLar">
          <el-input v-model="form.companyLar" placeholder="请输入法定代表人" />
        </el-form-item>
        <el-form-item label="职务" prop="job">
          <el-input v-model="form.job" placeholder="请输入职务" />
        </el-form-item>
        <el-form-item label="单位性质" prop="companyNature">
          <el-input v-model="form.companyNature" placeholder="请输入单位性质" />
        </el-form-item>
        <el-form-item label="省级id" prop="provinceId">
          <el-input v-model="form.provinceId" placeholder="请输入省级id" />
        </el-form-item>
        <el-form-item label="市级id" prop="cityId">
          <el-input v-model="form.cityId" placeholder="请输入市级id" />
        </el-form-item>
        <el-form-item label="区级id" prop="areaId">
          <el-input v-model="form.areaId" placeholder="请输入区级id" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="邮政编码" prop="postCode">
          <el-input v-model="form.postCode" placeholder="请输入邮政编码" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listWorkerapplyrespondent, getWorkerapplyrespondent, delWorkerapplyrespondent, addWorkerapplyrespondent, updateWorkerapplyrespondent } from "@/api/workerapplyrespondent/workerapplyrespondent";

export default {
  name: "Workerapplyrespondent",
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
      // 劳动者提交的申请（被申请人信息）表格数据
      workerapplyrespondentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        workApplyId: null,
        companyName: null,
        phone: null,
        companyLar: null,
        job: null,
        companyNature: null,
        provinceId: null,
        cityId: null,
        areaId: null,
        address: null,
        postCode: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询劳动者提交的申请（被申请人信息）列表 */
    getList() {
      this.loading = true;
      listWorkerapplyrespondent(this.queryParams).then(response => {
        this.workerapplyrespondentList = response.rows;
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
        id: null,
        workApplyId: null,
        companyName: null,
        phone: null,
        companyLar: null,
        job: null,
        companyNature: null,
        provinceId: null,
        cityId: null,
        areaId: null,
        address: null,
        postCode: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remark: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加劳动者提交的申请（被申请人信息）";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getWorkerapplyrespondent(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改劳动者提交的申请（被申请人信息）";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateWorkerapplyrespondent(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addWorkerapplyrespondent(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除劳动者提交的申请（被申请人信息）编号为"' + ids + '"的数据项？').then(function() {
        return delWorkerapplyrespondent(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('workerapplyrespondent/workerapplyrespondent/export', {
        ...this.queryParams
      }, `workerapplyrespondent_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
