<!--企业调解建议书-->
<template>
  <div class="container">
    <!--标题-->
    <div class="container_title">调解建议书</div>
    <!--内容-->
    <div class="container_content">
      &emsp;&emsp;为了方便您参加劳动调解,尽快地以最有效的方式解决您的烦恼，请认真阅读这份建议书。希望您在了解调解的有关规则、程序、优点以及注意事项等内容后，对调解解決纠纷的方式有充分的认识，并愿意尝试以调解的方式解决纠纷。<br />
      &emsp;&emsp;一 、调解的原则<br />
      &emsp;&emsp;1、双方当事人自愿；<br />
      &emsp;&emsp;2、双方当事人互谅互让；<br />
      &emsp;&emsp;3、调解员保持公正、公平、中立。<br />
      &emsp;&emsp; 二、调解的程序<br />
      &emsp;&emsp; 1、调解尊重案件事实，依法进行；<br />
      &emsp;&emsp;2、当事人和调解员有遵守保密的义务，调解过程中的陈述不得作为裁决和诉讼的证据使用,调解员在与当事人单独会谈中了解到的信息，未经当事人允许，不得向另一方当事人透露。<br />
      &emsp;&emsp;三、当事人应注意的事项<br />
      &emsp;&emsp;1、要避免冲动，冷静客观地看待问题;<br />
      &emsp;&emsp;2、要保持平常心，不应向对方提出过高的要求或对自己的请求期望值过高;<br />
      &emsp;&emsp;3、要换位思考，能从对方角度出发考虑问题。<br />
    </div>
    <!--同意对勾-->
    <div class="container_bottom">
      <van-checkbox v-model="checked"
        ><span style="color: #9da6c3; font-size: 13px"
          >我已阅读并且完全同意通过劳务争议调节平台中心进行调解</span
        ></van-checkbox
      >
    </div>
  </div>
  <!-- 底部按钮 -->
  <div class="container_btn">
    <van-button @click="toWorkerApplyFirst">下一步</van-button>
  </div>
</template>
<script setup name="CompanyApply">
import { useRouter } from "vue-router";
import { ref } from "@vue/reactivity";
import { Toast } from "vant";
const checked = ref(false);
const router = useRouter();
const toWorkerApplyFirst = () => {
  if (!checked.value) {
    Toast("请先阅读并且完全同意通过劳务争议调节平台中心进行调解");
    return;
  }
  router.push({
    path: "/companyApplyFirst",
  });
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
    font-size: 15px;
    color: #757575;
    margin-top: 20px;
    padding-left: 30px;
    padding-right: 30px;
    line-height: 40px;
  }
  &_bottom {
    margin-top: 30px;
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
</style>
