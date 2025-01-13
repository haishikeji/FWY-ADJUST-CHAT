<template>
  <div class="app-container">

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['lawtrainingbanner:lawtrainingbanner:add']"
        >新增
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['lawtrainingbanner:lawtrainingbanner:remove']"
        >删除
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="lawtrainingbannerList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="id" align="center" prop="id"/>
      <el-table-column label="链接地址" align="center" prop="url">
        <template slot-scope="scope">
          <el-popover placement="top-start" title="" trigger="hover" v-if="scope.row.url!=null">
            <img :src="scope.row.url" alt="" style="height: 200px">
            <img slot="reference" :src="scope.row.url" style="width: 60px;height: 60px">
          </el-popover>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="状态" align="center" key="status">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['lawtrainingbanner:lawtrainingbanner:remove']"
          >删除
          </el-button>
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

    <!-- 添加或修改法务培训班轮播图对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="链接地址" prop="url">
          <el-upload
            class="avatar-uploader"
            :action="uploadFileUrl"
            accept=".jpg,.png,.JPG,.PNG,.jpeg"
            :show-file-list="false"
            :headers="headers"
            :on-error="handleUploadError"
            :on-success="handleAvatarSuccess"
            :on-exceed="handleExceed"
            :before-upload="beforeAvatarUpload">
            <img v-if="form.url" :src="form.url" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
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
import {
  addLawtrainingbanner,
  changeBannerStatus,
  delLawtrainingbanner,
  getLawtrainingbanner,
  listLawtrainingbanner,
  updateLawtrainingbanner
} from "@/api/lawtrainingbanner/lawtrainingbanner";
import {getToken} from "@/utils/auth";

export default {
  name: "Lawtrainingbanner",
  data() {
    return {
      uploadFileUrl: process.env.VUE_APP_BASE_API + "/common/upload", // 上传的图片服务器地址
      headers: {
        Authorization: "Bearer " + getToken(),
      },
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
      // 法务培训班轮播图表格数据
      lawtrainingbannerList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        url: null,
        isAsc: "desc",
        orderByColumn: "id"
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        url: [
          {required: true, message: "图片不能为空", trigger: "blur"}
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 用户状态修改
    handleStatusChange(row) {
      let text = row.status === "1" ? "启用" : "停用";
      this.$modal.confirm('确认要' + text + '吗？').then(function () {
        return changeBannerStatus(row.id, row.status);
      }).then(() => {
        this.$modal.msgSuccess(text + "成功");
      }).catch(function () {
        row.status = row.status === "1" ? "0" : "1";
      });
    },
    // 图片上传失败
    handleUploadError(err) {
      this.$message.error("上传失败, 请重试");
    },
    // 图片上传成功回调
    handleAvatarSuccess(res, file) {
      this.$message.success("上传成功");
      this.form.url = res.url;
    },
    // 图片上传前校检格式和大小
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isLt2M;
    },
    // 图片文件个数超出
    handleExceed() {
      this.$message.error(`只允许上传单个文件`);
    },
    /** 查询法务培训班轮播图列表 */
    getList() {
      this.loading = true;
      listLawtrainingbanner(this.queryParams).then(response => {
        this.lawtrainingbannerList = response.rows;
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
        url: null,
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
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加法务培训班轮播图";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getLawtrainingbanner(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改法务培训班轮播图";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateLawtrainingbanner(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLawtrainingbanner(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除法务培训班轮播图编号为"' + ids + '"的数据项？').then(function () {
        return delLawtrainingbanner(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('lawtrainingbanner/lawtrainingbanner/export', {
        ...this.queryParams
      }, `lawtrainingbanner_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
<style>
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>
