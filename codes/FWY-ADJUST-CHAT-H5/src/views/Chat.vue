<template>
  <div class="container" id="myby">
    <!-- <div > -->
    <div class="chat-list" ref="chats">
      <div class="chat-sender">
        <div>
          <van-image
            width="42"
            height="42"
            round
            :src="require('../assets/images/logo.png')"
          />
        </div>
        <div class="chat-content">
          <div class="chat-left_triangle"></div>
          <div class="chat-text" style="padding: 20px">
            <div>请输入您的手机号：（用于接收回复提醒）</div>
            <div style="margin: 10px 0 20px">
              <van-field
                v-model="phone"
                placeholder="请输入手机号"
                :readonly="!editPhone"
                style="background: #fafafa"
              />
            </div>
            <div class="sub-phone" @click="getCode" v-if="editPhone">提交</div>
            <div class="unsub-phone" v-if="!editPhone">提交</div>
          </div>
        </div>
      </div>
      <div v-if="!editPhone" style="text-align: center" @click="changePhone">
        <div class="tishi">手机号填写错误？<span>点击重新填写</span></div>
      </div>
      <div v-if="showCode" class="chat-sender">
        <div>
          <van-image
            width="42"
            height="42"
            round
            :src="require('../assets/images/logo.png')"
          />
        </div>
        <div class="chat-content">
          <div class="chat-left_triangle"></div>
          <div class="chat-text" style="padding: 20px">
            <div>请输入收到的验证码，以确保手机号填写正确</div>
            <div style="margin: 10px 0 0">
              <van-password-input
                :value="code"
                :mask="false"
                :length="6"
                :error-info="errorInfo"
                :focused="showKeyboard"
                @focus="showKeyboard = editCode"
              />
              <van-number-keyboard
                v-model="code"
                :show="showKeyboard"
                @blur="showKeyboard = false"
              />
            </div>
          </div>
        </div>
      </div>
      <div v-for="(item, i) in list" :key="i" style="margin-bottom: 10px">
        <div v-if="!item.myself" class="chat-sender">
          <div>
            <van-image
              width="42"
              height="42"
              round
              :src="require('../assets/images/logo.png')"
            />
          </div>
          <div v-if="item.type == 2" class="chat-content">
            <div class="chat-left_triangle"></div>
            <div class="chat-text">{{ item.msg }}</div>
          </div>
        </div>
        <div v-if="item.myself" class="chat-receiver">
          <div v-if="item.type == 2" class="chat-content">
            <div class="chat-right_triangle"></div>
            <div class="chat-text">{{ item.msg }}</div>
          </div>
          <div>
            <van-image
              width="42"
              height="42"
              round
              :src="require('../assets/images/avatar.png')"
            />
          </div>
        </div>
      </div>
    </div>
    <div v-if="loading" style="text-align: center">
      <div class="tishi">{{ loadingText }}</div>
    </div>

    <!-- </div> -->
    <div class="endbtn" @click="toWxLogin">结束咨询</div>
    <div class="chat-footer">
      <div class="footer-top flex-box">
        <van-field
          ref="inputText"
          v-model="value"
          placeholder="输入你想发送的信息"
          style="line-height: 20px; background: #f7f7f7"
          @keyup.enter="submit(1)"
          @click="inputClick"
        />
        <van-icon
          name="smile-o"
          size="30"
          color="#333333"
          class="footer-top-icon"
          @click="footerSend('emoji')"
        />
        <!-- <van-icon
          name="add-o"
          size="30"
          color="#333333"
          class="footer-top-icon"
          @click="footerSend('poto')"
        /> -->
        <van-icon
          name="guide-o"
          size="30"
          color="#333333"
          class="footer-top-icon"
          @click="submit(1)"
        />
      </div>
      <div class="footer-bottom">
        <div class="emojis" v-if="footerActive === 0">
          <Picker
            :data="emojiIndex"
            :showSearch="false"
            set="twitter"
            @select="showEmoji"
          />
        </div>
        <div v-if="footerActive === 1">
          <div class="flex-box2">
            <div class="flex-box2-item">
              <van-uploader
                ref="avatarUploader"
                class="avatarUploader"
                :before-read="beforeRead"
                :after-read="afterRead"
                :max-count="1"
                :deletable="true"
              >
                <img src="../assets/images/photos.png" />
              </van-uploader>
              <div>图片</div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup name="Chat">
import { getChatList, getUserZiXunPayStatus } from "@/api/chat";
import { getSmsCode, login } from "@/api/user";
import url from "@/utils/URL";
import { ref } from "@vue/reactivity";
import { Toast } from "vant";
import { watch } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import {
  getPhone,
  getToken,
  getUserId,
  removePhone,
  removeToken,
  removeUserId,
  setPhone,
  setToken,
  setUserId,
  setUserType,
} from "../utils/auth";

import data from "emoji-mart-vue-fast/data/all.json";
// Note: component needs to be imported from /src subfolder:
import "emoji-mart-vue-fast/css/emoji-mart.css";
import { EmojiIndex, Picker } from "emoji-mart-vue-fast/src";
const emojiIndex = new EmojiIndex(data);
const emojisOutput = ref("");
const editPhone = ref(true); //是否可以输入手机号
const phone = ref("");
const loginCodeText = ref("获取验证码");
const showCode = ref(false);
const loading = ref(false);
const loadingText = ref("");
const code = ref("");
const editCode = ref(true);
const errorInfo = ref("");
const showKeyboard = ref(false);
const value = ref("");
const footerActive = ref(2);
const list = ref([]);
const payStatus = ref(false);
function showLoading() {
  var intervalId;
  if (loading.value === true) {
    let dots = 1;
    loadingText.value = "对方正在输入.";
    intervalId = setInterval(function () {
      if (dots < 3) {
        dots++;
        loadingText.value += ".";
      } else {
        dots = 1;
        loadingText.value = "对方正在输入.";
      }
    }, 500);
  } else {
    clearInterval(intervalId);
  }
}
var socket;
function onConnect() {
  var chatMsg = {};
  chatMsg.senderId = getUserId();
  chatMsg.msg = "";
  chatMsg.type = 1;
  console.log("onConnect", chatMsg);
  socket.send(JSON.stringify(chatMsg));
}
function keepalive() {
  var chatMsg = {};
  chatMsg.type = 4;
  chatMsg.senderId = getUserId();
  socket.send(JSON.stringify(chatMsg));
}
const socketFun = function () {
  var senderId = getUserId();
  var timer;
  if (window.WebSocket && senderId) {
    socket = new WebSocket(url.socketUrl);
    socket.onopen = function () {
      console.log("连接成功");
      onConnect();
    };
    socket.onclose = function () {
      clearInterval(timer);
      console.log("连接关闭");
    };
    socket.onerror = function () {
      clearInterval(timer);
      console.log("连接出错");
    };
    socket.onmessage = function (e) {
      timer = setInterval(function () {
        keepalive();
      }, 10000);
      console.log("eeee========", e);
      let data = JSON.parse(e.data);
      if (data.type === 2) {
        loading.value = false;
        showLoading();
        let obj = { myself: false, type: 2, msg: data.msg };
        list.value.push(obj);
        setTimeout(function () {
          var divscll = document.getElementById("myby");
          divscll.scrollTop = divscll.scrollHeight;
        }, 50);
        getPayStatus();
      }
    };
  }
};
const getPayStatus = async () => {
  let result = await getUserZiXunPayStatus();
  if (result != null && result.code === 200) {
    payStatus.value = result.msg === "1" ? true : false;
    if (result.msg === "0") {
      router.push({ name: "WxLogin" });
    }
  }
};
function changePhone() {
  editPhone.value = true;
  removeToken();
  removePhone();
  removeUserId();
  removeUserType();
  socket.close();
}
const getCode = async () => {
  if (phone.value === "") {
    Toast("请输入手机号");
    return;
  }
  if (phone.value.length > 11) {
    Toast("请输入正确的手机号");
    return;
  }
  //正则手机号匹配
  var patt = /^[1][3-9][\d]{9}/;
  if (!patt.test(phone.value)) {
    Toast("请输入正确的手机号");
    return;
  }

  if (loginCodeText.value !== "获取验证码") {
    Toast("验证码已发送，请注意查收");
    return;
  }
  const params = { phone: phone.value };
  let result = await getSmsCode(params);
  if (result.code === 200) {
    showCode.value = true;
    editPhone.value = false;
    loginCodeText.value = "验证码已发送";
  } else {
    editPhone.value = true;
    loginCodeText.value = "获取验证码";
  }
};

watch(code, (newVal) => {
  if (newVal.length === 6) {
    showKeyboard.value = false;
    handleLogin();
  }
});
watch(list, (newVal) => {
  console.log("newVal.length", newVal.length);
  if (newVal.length === 10) {
    router.push({ name: "WxLogin" });
  }
});
const handleLogin = async () => {
  const data = { phone: phone.value, code: code.value };
  let result = await login(data);
  if (result != null && result.code === 200) {
    editCode.value = false;
    setToken(result.data.token);
    setUserType(result.data.user.type);
    setPhone(result.data.user.userName);
    setUserId(result.data.user.id);
    socketFun();
  } else {
    editCode.value = true;
  }
};
const getChatLists = async () => {
  let result = await getChatList();
  if (result != null && result.code === 200) {
    if (result.data && result.data.length >= 10) {
      getPayStatus();
    }
    for (let item of result.data) {
      if (item.senderId === parseInt(getUserId())) {
        item.myself = true;
      } else {
        item.myself = false;
      }
    }
    list.value = result.data;
    setTimeout(function () {
      var divscll = document.getElementById("myby");
      divscll.scrollTop = divscll.scrollHeight;
      console.log(divscll.scrollHeight);
    }, 50);
  } else {
    list.value = [];
  }
};

const init = function () {
  const token = getToken();
  phone.value = getPhone() ? getPhone() : "";
  console.log("phone", phone.value);
  if (phone.value) {
    editPhone.value = false;
    showCode.value = false;
    socketFun();
  }
  getChatLists();
};
init();

const inputClick = function () {
  if (footerActive === 0 || footerActive === 1) {
    footerActive = 2;
  }
};
const footerSend = function (str) {
  if (str === "emoji") {
    footerActive.value = 0;
  } else {
    footerActive.value = 1;
  }
};
const showEmoji = function (emoji) {
  value.value += emojisOutput.value + emoji.native;
};
const handleSendImageClick = function () {
  this.$refs.imagePicker.click();
};
const setEmoji = function (emoji) {
  footerSend("emoji");
};
const submit = function () {
  // Toast("系统升级中，请稍后再试~");
  let token = getToken();
  let phone = getPhone();
  if (token && phone) {
    var chatMsg = {};
    chatMsg.type = 2;
    chatMsg.senderId = getUserId();
    chatMsg.msg = value.value;
    socket.send(JSON.stringify(chatMsg));
    let obj = { myself: true, type: 2, msg: chatMsg.msg };
    list.value.push(obj);
    console.log(list.value);
    value.value = "";
    setTimeout(function () {
      var divscll = document.getElementById("myby");
      divscll.scrollTop = divscll.scrollHeight + 100;
    }, 50);
    loading.value = true;
    showLoading();
  } else {
    Toast("请先输入手机号完成验证");
    return false;
  }
};
const store = useStore();
const router = useRouter();
function toWxLogin() {
  // router.push({ name: "WxLogin" });
  router.push({ name: "Home" });
}
</script>
<style>
.emoji-mart {
  border: none;
  width: 100% !important;
  background: #f7f7f7;
}
.emoji-mart-category-label {
  display: none;
}
.emoji-mart {
  width: 100% !important;
}
.emoji-mart-bar,
.emoji-mart-bar-preview,
.emoji-mart-preview {
  display: none !important;
}
</style>
<style scoped lang="scss">
.container {
  //   background: #fafafa;
  background: #f7f7f7;
  height: calc(100vh - 100px);
  overflow: auto;
  .chat-list {
    padding: 15px 15px 100px;
    clear: both;
    // height: calc(100vh - 300px);
    // overflow-y: scroll;
  }
  .chat-sender {
    display: flex;
    width: 85%;
  }
  .chat-receiver {
    float: right;
    display: flex;
    justify-content: flex-end;
    width: 85%;
  }
  .chat-sender .chat-content {
    position: relative;
    margin: 0 0 10px 10px;
    background-color: #ffffff;
    border-radius: 6px;
  }
  .chat-text {
    font-size: 14px;
    line-height: 24px;
    padding: 10px 10px 10px 10px;
    word-wrap: break-word;
  }

  .chat-receiver .chat-content {
    position: relative;
    margin: 0 10px 10px 0;
    background-color: #417fff;
    border-radius: 6px;
    color: #ffffff;
  }
  .chat-left_triangle {
    height: 0px;
    width: 0px;
    border-width: 6px;
    border-style: solid;
    border-color: transparent white transparent transparent;
    position: absolute;
    left: -12px;
    top: 16px;
  }
  .chat-right_triangle {
    height: 0px;
    width: 0px;
    border-width: 6px;
    border-style: solid;
    border-color: transparent transparent transparent #417fff;
    position: absolute;
    right: -12px;
    top: 16px;
  }
  .chat-footer {
    position: fixed;
    bottom: 0;
    left: 0;
    right: 0;
    background: #fff;
  }
  .footer-top {
    padding: 10px 15px;
    display: flex;
  }
  .footer-top-icon {
    margin: 6px 4px;
  }
  .van-cell::after {
    bottom: -1px;
  }
  .flex-box2 {
    display: flex;
    justify-content: flex-start;
    text-align: center;
    font-size: 12px;
    line-height: 16px;
    padding: 10px 15px;
    border-top: solid 1px #f7f7f7;
  }
  .flex-box2 img {
    width: 50px;
    height: 50px;
  }
  .flex-box2-item:first-child {
    margin-right: 10px;
  }
  .sub-phone {
    width: 100%;
    line-height: 40px;
    background: #417fff;
    text-align: center;
    border-radius: 20px;
    color: #fff;
  }
  .unsub-phone {
    width: 100%;
    line-height: 40px;
    background: rgba(65, 127, 255, 0.6);
    text-align: center;
    border-radius: 20px;
    color: #fff;
  }
  .tishi {
    display: inline-block;
    margin: 20px auto;
    line-height: 36px;
    border-radius: 20px;
    font-size: 14px;
    background: #f0f0f0;
    padding: 0 15px;
    span {
      color: #417fff;
    }
  }
  .endbtn {
    position: fixed;
    bottom: 70px;
    left: 15px;
    line-height: 36px;
    display: inline-block;
    padding: 0 15px;
    color: #417fff;
    background: #f0f0f0;
    border-radius: 20px;
  }
}
</style>
