<!--企业调解建议书第一步(申请人信息)-->
<template>
  <div class="container">
    <!--标题-->
    <div class="container_title">劳动调解申请书（1/4）申请人信息</div>
    <!--form表单-->
    <div class="container_content">
      <van-form>
        <van-cell-group inset>
          <van-field
            name="企业名称"
            label="企业名称"
            v-model="companyName"
            readonly="true"
            placeholder="请输入(必填)"
            input-align="right"
            :rules="[{ required: true, message: '请填写企业名称' }]"
          />
          <van-field
            name="联系电话"
            label="联系电话"
            v-model="phone"
            placeholder="请输入(必填)"
            input-align="right"
            :rules="[{ required: true, message: '请填写联系电话' }]"
          />
          <van-field
            name="法定代表人"
            label="法定代表人"
            v-model="companyLar"
            placeholder="请输入(必填)"
            input-align="right"
            :rules="[{ required: true, message: '请填写法定代表人' }]"
          />
          <van-field
            name="职务"
            label="职务"
            v-model="job"
            placeholder="请输入(必填)"
            input-align="right"
            :rules="[{ required: true, message: '请填写职务' }]"
          />
          <van-field
            name="单位性质"
            label="单位性质"
            v-model="companyNature"
            placeholder="请输入(必填)"
            input-align="right"
            :rules="[{ required: true, message: '请填写单位性质' }]"
          />
          <van-field
            name="省市区"
            label="省市区"
            v-model="area"
            input-align="right"
            placeholder="请选择(必填)"
            readonly="true"
            right-icon="arrow-down"
            @click="showArea = true"
            :rules="[{ required: true, message: '请填写省市区' }]"
          />
          <van-field
            name="详细地址"
            label="详细地址"
            v-model="address"
            input-align="right"
            placeholder="请输入(必填)"
            :rules="[{ required: true, message: '请填写详细地址' }]"
          />
          <van-field
            name="邮政编码"
            label="邮政编码"
            v-model="postCode"
            input-align="right"
            placeholder="请输入(必填)"
            :rules="[{ required: true, message: '请填写邮政编码' }]"
          />
          <van-field
              name="纠纷类型"
              label="纠纷类型"
              v-model="typeName"
              input-align="right"
              placeholder="请选择(必填)"
              readonly="true"
              right-icon="arrow-down"
              @click="showTypePicer = true"
              :rules="[{ required: true, message: '请选择纠纷类型' }]"
          />
        </van-cell-group>
      </van-form>
    </div>
  </div>
  <!-- 底部按钮 -->
  <div class="container_btn">
    <van-button @click="sub">下一步</van-button>
  </div>
  <!--纠纷类型-->
  <van-popup v-model:show="showTypePicer" position="bottom">
    <van-picker
        :columns="typeColumns"
        @confirm="onTypeConfirm"
        @cancel="showTypePicer = false"
    />
  </van-popup>
  <!--省市区-->
  <van-popup v-model:show="showArea" position="bottom">
    <van-area
      :area-list="areaList"
      @confirm="onAreaConfirm"
      @cancel="showArea = false"
    />
  </van-popup>
</template>
<script setup name="CompanyApplyFirst">
import { ref } from "@vue/reactivity";
import { areaList } from "@vant/area-data";
import { useRouter } from "vue-router";
import { Toast } from "vant";
import {
  submitApplyToRedis,
  getCompanyDetailByRedis,
  getApplyType
} from "@/api/companyApply";
import { getUserInfo } from "@/api/user";
const companyName = ref("");
const phone = ref("");
const companyLar = ref("");
const area = ref("");
const job = ref("");
const companyNature = ref("");
const provinceId = ref("");
const cityId = ref("");
const areaId = ref("");
const address = ref("");
const postCode = ref("");
const showArea = ref(false);
const showTypePicer = ref(false);
//前端显示
const typeName = ref("");
//数据库存的值
const typeValue = ref("");
//此list只有vlue
const typeColumns = [];
//存所有的值
const typeDataList = [];
const router = useRouter();
const sub = async () => {
  if (companyName.value === "") {
    Toast("请填写企业名称");
    return;
  }
  if (phone.value === "") {
    Toast("请填写联系电话");
    return;
  }
  //匹配手机号或座机号
  var patrn = /^1[3456789]\d{9}$/; //手机电话
  var zjdh = /^((0\d{2,3})-)?(\d{7,8})$/; //座机电话
  if (patrn.test(phone.value) == false && zjdh.test(phone.value) == false) {
    Toast("请填写正确的联系电话");
    return;
  }
  if (companyLar.value === "") {
    Toast("请填写法定代表人");
    return;
  }
  if (job.value === "") {
    Toast("请填写职务");
    return;
  }
  if (companyNature.value === "") {
    Toast("请填写单位性质");
    return;
  }
  if (area.value === "") {
    Toast("请填写省市区");
    return;
  }
  if (address.value === "") {
    Toast("请填写详细地址");
    return;
  }
  if (postCode.value === "") {
    Toast("请填写邮政编码");
    return;
  }
  if (typeValue.value === "") {
    Toast("请选择纠纷类型");
    return;
  }
  const params = {
    phone: phone.value,
    companyLar: companyLar.value,
    job: job.value,
    companyNature: companyNature.value,
    address: address.value,
    postCode: postCode.value,
    provinceId: provinceId.value,
    cityId: cityId.value,
    areaId: areaId.value,
    type:typeValue.value,
    typeName:typeName.value,
  };
  let result = await submitApplyToRedis(params);
  if (result != null && result.code === 200) {
    router.push({
      path: "/companyApplySecond",
    });
  }
};

//获取用户信息
const getUser = async () => {
  let result = await getUserInfo();
  if (result != null && result.code === 200) {
    if (result.data != null) {
      if (result.data.companyInfo != null) {
        //企业名称
        if (result.data.companyInfo.companyName !== null) {
          companyName.value = result.data.companyInfo.companyName;
        }
        //联系电话
        if (result.data.companyInfo.phone != null) {
          phone.value = result.data.companyInfo.phone;
        }
      }
    }
  }
};
getUser();

const onAreaConfirm = (value) => {
  console.error(value);
  showArea.value = false;
  area.value = value
    .filter((item) => !!item)
    .map((item) => item.name)
    .join("-");
  provinceId.value = value[0].code;
  cityId.value = value[1].code;
  areaId.value = value[2].code;
};

//获取详情
const getApplyDetail = async () => {
  let result = await getCompanyDetailByRedis();
  if (result != null && result.code === 200) {
    if (result.data != null) {
      if (result.data.phone != null) {
        phone.value = result.data.phone;
      }
      if (result.data.companyLar != null) {
        companyLar.value = result.data.companyLar;
      }
      if (result.data.job != null) {
        job.value = result.data.job;
      }
      if (result.data.companyNature != null) {
        companyNature.value = result.data.companyNature;
      }
      if (result.data.provinceAndCityAndArea != null) {
        area.value = result.data.provinceAndCityAndArea;
      }
      if (result.data.provinceId != null) {
        provinceId.value = result.data.provinceId;
      }
      if (result.data.cityId != null) {
        cityId.value = result.data.cityId;
      }
      if (result.data.areaId != null) {
        areaId.value = result.data.areaId;
      }
      if (result.data.address != null) {
        address.value = result.data.address;
      }
      if (result.data.postCode != null) {
        postCode.value = result.data.postCode;
      }
      if(result.data.type != null){
        typeValue.value = result.data.type;
      }
      if(result.data.typeName != null){
        typeName.value = result.data.typeName;
      }
    }
  }
};

getApplyDetail();
//获取分类
const getApplyTypeList = async () =>{
  let result = await getApplyType();
  if(result != null && result.code === 200){
    if(result.data != null){
      typeDataList.value = result.data;
      for(let item of result.data){
        typeColumns.push(item.dictLabel);
      }
    }
  }
}
getApplyTypeList();

//分类
const onTypeConfirm = (value) => {
  //名字
  typeName.value = value;
  showTypePicer.value = false;
  for(let item of typeDataList.value){
    if(item.dictLabel === value){
      //值
      typeValue.value = item.dictValue;
    }
  }
};
</script>
<style scoped lang="scss">
.container {
  position: relative;
  height: calc(100vh - 120px);
  overflow-y: scroll;
  &_title {
    font-weight: bold;
    margin-top: 20px;
    font-size: 18px;
    text-align: center;
    color: #212121;
  }
  &_content {
    font-size: 16px;
    color: #757575;
    margin-top: 50px;
    padding-left: 15px;
    padding-right: 15px;
  }
}
.container_btn {
  position: fixed;
  bottom: 0;
  width: 100%;
  height: 64px;
  line-height: 20px;
  background-color: rgba(255, 255, 255, 100);
  color: rgba(16, 16, 16, 100);
  font-size: 14px;
  text-align: center;
  box-shadow: 0px -1px 1px 0px rgba(238, 238, 238, 100);
  font-family: Arial;

  .van-button {
    margin: 10px;
    width: 355px;
    height: 44px;
    border-radius: 6px;
    background-color: #1779ff;
    text-align: center;
    color: rgba(255, 255, 255, 100);
    font-size: 16px;
    font-family: PingFangSC-semiBold;
    font-weight: bolder;
  }
}
.van-field {
  color: #212121;
}
.van-cell {
  padding-left: 0;
  padding-right: 0;
}
.van-cell::after {
  width: 100%;
  display: inline-block;
  left: 0;
}
</style>
