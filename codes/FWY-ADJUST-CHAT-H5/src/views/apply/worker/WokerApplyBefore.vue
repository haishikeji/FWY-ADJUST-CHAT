<!--应得提交的材料-->
<template>
  <div class="container">
    <!--标题-->
    <div class="container_title">
      劳动调解申请应当提交的材料
    </div>
    <!--内容-->
    <div class="container_content">
      &emsp;1. 申请书一式三份(或按被申请人人数提供,详细陈述申请理由和请求,本人签名)；<br>
      &emsp;2. 申请人证件明原件及复印件一份(核对后返还原件)；<br>
      &emsp;3.有委托代理人的,需当面签订并提交 《授权委托书》,注明委托事项,同时提交受委托人的证件明复印件。如委托人的代理人是执业律师，应提供执业律师的证件复印件;如委托人的代理人是公民的，应提供与委托人签订的不收费代理协议书，以及代理人和委托人之间关系的法律资料;<br>
      &emsp;4.被申请人工商注册信息资料；<br>
      &emsp;5. 申请人与被申请人存在劳动关系的证明材料,包括但不限于：劳动合同、暂住证、工作证、厂牌、工卡、工资表(单）、入职登记表、押金收据、以及被处罚凭证和被开除,除名、辞退、解除(或终止)劳动关系通知或证明书等；<br>
      上述证明材料,申请人能提供的应尽量提供,如果一份都没有,则立案庭不予受理、申请人提交证明材料时，应附原件及复印件一式两份,审核后退回原件；<br>
      &emsp;6.《提交证据材料清单》一式两份。
    </div>
  </div>
  <!-- 底部按钮 -->
  <div class="container_btn">
    <van-button @click="toWorkerApply">下一步</van-button>
  </div>
</template>
<script setup name="workerApplyBefore">
import { useRouter } from "vue-router";

const router = useRouter();
const toWorkerApply = () => {
  router.push({
    path: "/workerApply",
  });
};
</script>
<style scoped lang="scss">
  .container {
    position: relative;
    height:  calc(100vh - 120px);
    overflow-y: scroll;
    &_title{
      font-weight: bold;
      margin-top: 20px;
      font-size:18px;
      text-align: center;
      color:#212121;
    }
    &_content{
      font-size:15px;
      color:#757575;
      margin-top:20px;
      padding-left: 30px;
      padding-right:30px;
      line-height: 40px
    }
    &_bottom{
      margin-top:30px;
      display: flex;
      flex-direction: row;
      justify-content: center;
      bottom: 0;
      &_check {
        margin-right: 6px;
      }
      &_check img {
        width: 14px;
      }
      &_info {
        color: #9da6c3;
        font-size: 12px;
      }
    }
  }
  .container_btn{
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
      background-color: #1779FF;
      text-align: center;
      color: rgba(255, 255, 255, 100);
      font-size: 16px;
      font-family: PingFangSC-semiBold;
      font-weight: bolder;
    }
  }
</style>
