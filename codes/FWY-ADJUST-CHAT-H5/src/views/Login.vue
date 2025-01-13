<template>
  <div class="container">
    <div class="container_top">
      <div class="container_top_logo">
        <img src="../assets/images/logo.png" alt="" />
      </div>
      <div class="container_top_title">欢迎使用 劳务争议调节平台</div>
    </div>
    <div class="container_body">
      <input
        v-model="username"
        class="container_body_input"
        placeholder="请输入手机号"
        name="username"
      />
      <div class="container_body_code">
        <input
          v-model="code"
          class="container_body_input"
          placeholder="请输入验证码"
          name="username"
        />
        <div class="container_body_code_getcode" @click="getCode">
          {{ loginCodeText }}
        </div>
      </div>
      <div class="container_body_btn">
        <van-button type="warning" size="large" @click="handleLogin"
          >登录/注册</van-button
        >
      </div>

      <div class="container_body_bottom">
        <!--        <li>
          <span><img src="../assets/images/choosed.png" alt="" /></span
          ><span style="color: #9da6c3; font-size: 12px"
            >登录/注册即代表您同意《用户协议》</span
          >
        </li>-->
        <van-checkbox v-model="checked"
          ><span style="color: #9da6c3; font-size: 13px"
            >登录/注册即代表您同意<span
              @click="showProtocol"
              style="color: #417fff"
              >《用户协议》</span
            ></span
          ></van-checkbox
        >
      </div>
    </div>
  </div>
</template>
<script setup name="Login">
import { getSmsCode, login } from "@/api/user";
import { ref } from "@vue/reactivity";
import { Toast } from "vant";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import url from "@/utils/URL";
import { setPhone, setToken, setUserId, setUserType } from "../utils/auth";
const store = useStore();
const username = ref("");
const code = ref("");
const loginCodeText = ref("获取验证码");
const router = useRouter();
const checked = ref(false);
const getCode = async () => {
  if (username.value === "") {
    Toast("请输入手机号");
    return;
  }
  if (username.value.length > 11) {
    Toast("请输入正确的手机号");
    return;
  }
  //正则手机号匹配
  var patt = /^[1][3-9][\d]{9}/;
  if (!patt.test(username.value)) {
    Toast("请输入正确的手机号");
    return;
  }

  if (loginCodeText.value !== "获取验证码") {
    Toast("验证码已发送，请注意查收");
    return;
  }

  var timer;
  const params = { phone: username.value };
  // var socket = new WebSocket(
  //   url.socketUrl + "appreq/user/sendsms?phone=" + username.value
  // );
  // console.log(socket, "socket");
  // return;
  let result = await getSmsCode(params);
  if (result.code === 200) {
    // Toast(result.data);
    //console.log(result)
    var ss = 59;
    timer = setInterval(function () {
      ss--;
      loginCodeText.value = "验证码(" + ss + "s)";
      if (ss <= 0) {
        clearInterval(timer);
        loginCodeText.value = "获取验证码";
      }
    }, 1000);
  } else {
    clearInterval(timer);
    loginCodeText.value = "获取验证码";
  }
};

const handleLogin = async () => {
  if (!checked.value) {
    Toast("请先同意用户协议");
    return;
  }
  if (username.value === "") {
    Toast("请输入手机号");
    return;
  }
  if (code.value === "") {
    Toast("请输入验证码");
    return;
  }
  const data = { phone: username.value, code: code.value };
  let result = await login(data);
  if (result != null && result.code === 200) {
    setToken(result.data.token);
    setUserType(result.data.user.type);
    setPhone(result.data.user.userName);
    setUserId(result.data.user.id);
    router.push({ name: "Chat" });
    // store.commit("setUserType", result.data.user.type);
    /*if (result.data.isFisrt == "false") {
      //说明是老用户，不用跳转到选择身份
      setToken(result.data.token);
      setUserType(result.data.user.type);
      router.push({ name: "Home" });
    } else {
      //说明是新用户，需要跳转到选择身份
      setToken(result.data.token);
      router.push({ name: "ChooseIdentity" });
    }*/
    //获取注册步骤
    // let resultProgress = await getCurrentProgress();
    // if (resultProgress != null && resultProgress.code === 200) {
    //   if (resultProgress.data != null) {
    //     if (resultProgress.data.progress != null) {
    //       if (resultProgress.data.progress === 0) {
    //         //跳转到完善信息
    //         if (result.data.user.type != null) {
    //           if (result.data.user.type === "1") {
    //             //个人
    //             router.push({ name: "PersonIdentity" });
    //           } else if (result.data.user.type === "2") {
    //             //企业
    //             router.push({ name: "CompanyIdentity" });
    //           }
    //         }
    //       } else if (resultProgress.data.progress === 1) {
    //         //完成了
    //         router.push({ name: "Home" });
    //       }
    //     } else {
    //       console.error("进来了1");
    //       //跳转到选择身份页
    //       router.push({ name: "ChooseIdentity" });
    //     }
    //   } else {
    //     console.error("进来了2");
    //     //跳转到选择身份页
    //     router.push({ name: "ChooseIdentity" });
    //   }
    // } else {
    //   console.error("进来了3");
    //   //跳转到选择身份页
    //   router.push({ name: "ChooseIdentity" });
    // }
  }
};

//展示用户协议
const showProtocol = async () => {
  router.push({ name: "Protocol" });
};
</script>
<style scoped lang="scss">
.container {
  background-color: white;
  min-height: 100vh;
  overflow-x: hidden;
  overflow-y: auto;
  &_top {
    width: 320px;
    height: 206px;

    background-repeat: no-repeat;
    padding: 58px 30px 0 30px;
    &_logo {
      width: 50px;
      height: 50px;
      /*  background-color: rgba(23, 121, 255, 100);*/
      border-radius: 30px;
      text-align: center;
      vertical-align: middle;
      line-height: 50px;
      img {
        width: 50px;
        height: 50px;
      }
      margin-bottom: 28px;
    }
    &_title {
      color: #101010;
      font-size: 24px;
      font-family: HarmonyOS_Sans_SC-medium;
    }
  }
  &_body {
    padding: 0 30px 0 30px;
    &_input {
      border-radius: 5px;
      background-color: rgba(250, 250, 250, 100);
      text-align: left;
      border: none;
      height: 60px;
      width: 100%;
      text-indent: 20px;
    }
    &_code {
      margin-top: 20px;
      position: relative;
      &_getcode {
        position: absolute;
        right: 20px;
        top: 25px;
        bottom: 20px;
        color: #417fff;
        font-size: 14px;
      }
    }
    &_btn {
      width: 100%;
      margin-top: 20px;
    }
    &_bottom {
      text-align: center;
      margin-top: 3rem;
      //position: fixed;
      //bottom: 20px;
      //left: 75px;
      li {
        list-style: none;
        img {
          height: 16px;
          vertical-align: middle;
          margin-right: 6px;
        }
      }
    }
  }
}
.van-button--large {
  height: 60px;
  background: #1779ff;
  border-radius: 5px;
  border-color: #1779ff;
  width: 320px;
}
.van-checkbox {
  justify-content: center;
}
</style>
