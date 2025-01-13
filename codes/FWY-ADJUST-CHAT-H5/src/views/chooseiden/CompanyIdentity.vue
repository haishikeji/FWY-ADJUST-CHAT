<template>
  <div class="container">
    <div class="container_tip">
      为了帮您更快捷的解决问题，请填写下面的基础信息
    </div>
    <div class="container_title">企业信息</div>
    <div class="container_form">
      <van-field
        v-model="name"
        name="企业名称"
        label="企业名称"
        placeholder="请输入企业名称"
        :rules="[{ required: true, message: '请填写企业名称' }]"
      />
      <van-field
        v-model="contactor"
        name="联系人"
        label="联系人"
        placeholder="请输入联系人姓名"
        :rules="[{ required: true, message: '请填写联系人姓名' }]"
      />
      <van-field
        v-model="phone"
        type="tel"
        name="电话"
        label="电话"
        placeholder="电话"
        :rules="[{ required: true, message: '请填写电话' }]"
      />
    </div>

    <div class="container_bottom">
      <van-button type="primary" size="large" @click="confirmSub"
        >确定</van-button
      >
    </div>
  </div>
</template>
<script setup name="CompanyIdentity">
import { ref } from "@vue/reactivity";
import { updateCompanyInfo } from "@/api/user";
import { addProgress } from "@/api/progress";
import { Toast } from "vant";
import { useRouter } from "vue-router";
const name = ref("");
const phone = ref("");
const contactor = ref("");
const router = useRouter();
const confirmSub = async () => {
  if (name.value === "") {
    Toast("请填写企业名称");
    return;
  }
  if (contactor.value === "") {
    Toast("请填写联系人姓名");
    return;
  }
  if (phone.value === "") {
    Toast("请填写电话");
    return;
  }
  //匹配手机号或座机号
  var patrn = /^1[3456789]\d{9}$/; //手机电话
  var zjdh = /^((0\d{2,3})-)?(\d{7,8})$/; //座机电话
  if (patrn.test(phone.value) == false && zjdh.test(phone.value) == false) {
    Toast("请填写正确的电话");
    return;
  }
  const params = {
    companyName: name.value,
    trueName: contactor.value,
    phone: phone.value,
  };
  let result = await updateCompanyInfo(params);
  if (result != null && result.code === 200) {
    //更新步骤
    const progressParams = { progress: "1" };
    let progressResult = await addProgress(progressParams);
    if (progressResult != null && progressResult.code === 200) {
      router.push({ name: "Home" });
    }
  }
};
</script>
<style lang="scss" scoped>
.container {
  background: white;
  padding: 0 20px 0 20px;
  position: relative;
  &_tip {
    margin-top: 33px;
    font-size: 14px;
    color: #9e9e9e;
    text-align: left;
  }
  &_title {
    margin-top: 30px;
    margin-bottom: 20px;
    font-size: 16px;
    color: #212121;
    font-weight: bold;
  }
  &_bottom {
    position: fixed;
    bottom: 20px;
    left: 15px;
    right: 15px;
  }
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

// .van-cell:after {
//   margin-left: 0px;
//   margin-right: 0px;
//   width: 350px;
// }
</style>
