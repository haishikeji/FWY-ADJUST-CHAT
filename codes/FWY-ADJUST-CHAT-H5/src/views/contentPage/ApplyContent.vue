<template>
  <!--劳动者申请详情-->
  <div class="container">
    <div class="container_top">
      <div class="container_top_left">
        <span>{{ yearAndMonthAndDay }}</span>
        <span style="margin-left: 20px">{{ time }}</span>
      </div>
      <div class="container_top_right">
        <span style="color: red">{{ status }}</span>
      </div>
    </div>
    <van-divider />
    <div
      class="container_info"
      v-for="(item, index) in respondentList"
      :key="index"
    >
      <div class="container_info_row">
        <div
          class="container_info_row_left"
          style="color: #000000; font-size: 16px"
        >
          被申请人信息{{ index + 1 }}
        </div>
        <div class="container_info_row_right">
          <img src="../../assets/images/collapse.png" alt="" />
        </div>
      </div>
      <div class="container_info_row">
        <div class="container_info_row_left">企业名称</div>
        <div class="container_info_row_right">{{ item.companyName }}</div>
      </div>
      <div class="container_info_row">
        <div class="container_info_row_left">联系电话</div>
        <div class="container_info_row_right">{{ item.phone }}</div>
      </div>
      <div class="container_info_row">
        <div class="container_info_row_left">法定代表人</div>
        <div class="container_info_row_right">{{ item.companyLar }}</div>
      </div>
      <div class="container_info_row">
        <div class="container_info_row_left">职务</div>
        <div class="container_info_row_right">{{ item.job }}</div>
      </div>
      <div class="container_info_row">
        <div class="container_info_row_left">单位性质</div>
        <div class="container_info_row_right">{{ item.companyNature }}</div>
      </div>
      <div class="container_info_row">
        <div class="container_info_row_left">省市区</div>
        <div class="container_info_row_right">
          {{ item.provinceAndCityAndArea }}
        </div>
      </div>
      <div class="container_info_row">
        <div class="container_info_row_left">详细地址</div>
        <div class="container_info_row_right">{{ item.address }}</div>
      </div>
      <div class="container_info_row">
        <div class="container_info_row_left">邮政编码</div>
        <div class="container_info_row_right">{{ item.postCode }}</div>
      </div>
    </div>
    <div
      class="container_demand"
      v-for="(item, index) in requestList"
      :key="index"
    >
      <div class="container_demand_title">请求事项{{ index + 1 }}：</div>
      <div class="container_demand_desc">
        {{ item.request }}
      </div>
    </div>
    <div class="container_demand">
      <div class="container_demand_title">事实和理由：</div>
      <div class="container_demand_desc">
        {{ reason }}
      </div>
    </div>

    <div class="container_demand" v-if="url != null && url !== ''">
      <div class="container_demand_title">调解协议书:</div>
      <div class="container_demand_desc">
        <van-button type="primary" @click="downLoad">点击下载</van-button>
      </div>
    </div>
    <div  class="container_footer">
      <div class="container_delete" @click="del">删除</div>
      <div class="container_pay" @click="toPay">付款</div>
    </div>
    
  </div>
</template>
<script setup name="ApplyContent">
import { delWorkerApplyDetail, getWorkerDetail } from "@/api/workerApply";
import { ref } from "@vue/reactivity";
import { Dialog, Toast } from "vant";
import { useRoute, useRouter } from "vue-router";
const route = useRoute();
const router = useRouter();
const id = route.query.id;
//被申请人list
const respondentList = ref([]);
//请求事项List
const requestList = ref([]);
//事实和理由
const reason = ref("");
//时间
const time = ref("");
//月份
const yearAndMonthAndDay = ref("");
//状态
const status = ref("");
//调解协议书地址
const url = ref("");
//获取详情
const getDetail = async () => {
  let result = await getWorkerDetail(id);
  if (result != null && result.code === 200) {
    if (result.data != null) {
      if (result.data.respondentList != null) {
        respondentList.value = result.data.respondentList;
      }
      if (result.data.requestList != null) {
        requestList.value = result.data.requestList;
      }
      if (result.data.createTime != null) {
        let createTime = result.data.createTime;
        yearAndMonthAndDay.value = createTime.substring(
          0,
          createTime.indexOf(" ")
        );
        time.value = createTime.substring(createTime.indexOf(" "));
      }
      if (result.data.reason != null) {
        reason.value = result.data.reason;
      }
      if (result.data.status != null) {
        if (result.data.status === "0") {
          status.value = result.data.isPay === '0'?"已提交申请未支付":'已支付';
        } else if (result.data.status === "1") {
          status.value = "调解书已上传";
        } else if (result.data.status === "2") {
          status.value = "已结束";
        }
      }
      if (result.data.adjustUrl != null) {
        url.value = result.data.adjustUrl;
      }
    }
  }
};
getDetail();

//删除
const del = async () => {
  Dialog.confirm({
    title: "提示",
    message: "您确定要删除此申请吗?",
  })
    .then(async () => {
      let result = await delWorkerApplyDetail(id);
      if (result.code === 200) {
        Toast("删除成功");
        router.go(-1);
      }
    })
    .catch(async () => {});
};

//点击了下载
const downLoad = () => {
  if (isWeiXin())
  {
    window.location.href = url.value;
  }else{
    window.open(url.value);
  }
};
function toPay(){
  router.push({path: "/paymoney",query: {id}});
}
const isWeiXin  = () => {
  var ua = window.navigator.userAgent.toLowerCase();
  if (ua.match(/MicroMessenger/i) == 'micromessenger') {
    return true;
  } else {
    return false;
  }
};


</script>
<style lang="scss" scoped>
.container {
  padding-bottom: 60px;
  height: 90vh;
  overflow-y: scroll;

  &_top {
    padding: 30px 15px 0px 15px;
    display: flex;
    justify-content: space-between;
    &_right {
    }
  }
  &_info {
    margin: 5px 15px 20px 15px;
    border-radius: 5px;
    background-color: rgba(250, 250, 250, 100);
    padding: 30px 20px 10px 20px;
    &_row {
      display: flex;
      flex-direction: row;
      justify-content: space-between;
      margin-bottom: 25px;
      &_left {
        font-size: 14px;
        color: #9e9e9e;
      }
      &_right {
        img {
          height: 16px;
          vertical-align: middle;
        }
        font-size: 14px;
        color: #272727;
      }
    }
  }
  &_demand {
    margin-top: 15px;
    padding: 15px 15px;
    &_title {
      color: #212121;
      font-size: 16px;
    }
    &_desc {
      margin-top: 20px;
      color: #757575;
      font-size: 15px;
    }
  }
  &_footer{
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    display: flex;
    text-align: center;
    font-size: 16px;
    line-height: 50px;
    z-index: 2;
  }
    &_delete {
      width: 50%;
      color: #ff3a2f;
      background: #fafafa;
    }
  &_pay{
   width: 50%;
      color: #fff;
      background: #417FFF;
  }
}

.van-button {
  height: 30px;
}

</style>
