<!--劳动法普及搜索-->
<template>
  <div class="container">
    <!--      顶部搜索框+文字-->
    <div class="container_search">
      <div class="container_search_left" @click="back">
        <van-icon name="arrow-left" />
      </div>
      <div class="container_search_pos">
        <van-search
          shape="round"
          v-model="value"
          placeholder="请输入搜索关键词"
          @search="search"
        />
      </div>
      <div class="container_search_text" @click="search">搜索</div>
    </div>

    <div class="container_divider"></div>

    <div class="container_content">
      <!--    搜索历史-->
      <div class="container_content_history" v-if="!showList">
        <!--      标题+按钮-->
        <div class="container_content_history_top">
          <div class="container_content_history_top_title">搜索历史</div>
          <div class="container_content_history_top_btn">
            <img
              src="../../assets/images/del.png"
              alt=""
              v-if="!showDelHis && searchList.length > 0"
              @click="delBtn"
            />
            <!--全部删除和完成-->
            <div
              class="container_content_history_top_btn_text"
              v-if="showDelHis"
            >
              <div
                class="container_content_history_top_btn_text_left"
                @click="delAll"
              >
                全部删除
              </div>
              <div
                class="container_content_history_top_btn_text_right"
                @click="delFinish"
              >
                完成
              </div>
            </div>
          </div>
        </div>
        <!--搜索历史记录-->
        <div class="container_content_history_bottom">
          <div
            class="container_content_history_bottom_item"
            v-for="(item, index) in searchList"
            :key="index"
          >
            <div
              class="container_content_history_bottom_item_text"
              @click="clickSeachItem(item.content)"
            >
              {{ item.content }}
            </div>
            <div
              class="container_content_history_bottom_item_icon"
              v-if="showDelHis"
            >
              <van-icon name="cross" @click="delIteamBtn(item.id)" />
            </div>
          </div>
        </div>
      </div>
      <!--列表-->
      <div class="container_content_list" v-if="showList">
        <!--标题-->
        <div class="container_content_list_title">
          搜索到<span style="color: #1779ff">{{ total }}</span
          >条结果
        </div>
        <div class="container_content_list_content">
          <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
            <van-list
              v-if="!isempty"
              v-model:loading="loading"
              :finished="finished"
              finished-text="没有更多了"
              @load="onLoad"
              ref="listRef"
            >
              <div
                class="container_content_list_content_item"
                v-for="item in list"
                :key="item"
                :title="item"
                @click="toDetail(item.noticeId)"
              >
                <div class="container_content_list_content_item_box">
                  <div class="container_content_list_content_item_info">
                    <div class="container_content_list_content_item_info_title">
                      {{ item.noticeTitle }}
                    </div>
                    <div class="container_content_list_content_item_info_desc">
                      <span>{{ item.monthAndDay }}</span
                      ><span style="margin-left: 15px">
                        {{ item.readCount }}阅读</span
                      >
                    </div>
                  </div>
                  <div class="container_content_list_content_item_pic">
                    <img :src="item.imgUrl === null ? imgUrl : item.imgUrl" />
                  </div>
                </div>
                <van-divider />
              </div>
            </van-list>
          </van-pull-refresh>
          <van-empty description="暂无信息" v-if="isempty" />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup name="Search">
import { useRouter } from "vue-router";
import { reactive, ref, toRefs } from "@vue/reactivity";
import {
  getSearchListRequest,
  deleteAllRequest,
  addSearchRequest,
  deleteSearchByIdRequest,
} from "@/api/search";
import { getNoticeList } from "@/api/notice";
const router = useRouter();
const value = ref("");
//是否首次进入
const isFirst = ref(true);
//搜索记录list
const searchList = ref([]);
// 总数
const total = ref(0);
const isempty = ref(false);
const query = reactive({ queryParams: { pageSize: 10, pageNum: 1 } });
const { queryParams } = toRefs(query);
const back = () => {
  router.go(-1);
};
const list = ref([]);
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
const listRef = ref(null);
//是否显示列表
const showList = ref(false);
//是否显示删除搜索历史x
const showDelHis = ref(false);
listRef.value?.check();
const imgUrl = ref(require("../../assets/images/banner2.png"));
const onLoad = async () => {
  isFirst.value = false;
  if (refreshing.value) {
    queryParams.value.pageNum = 1;
    list.value = [];
    refreshing.value = false;
  }
  const data = {
    noticeTitle: value.value,
  };
  let result = await getNoticeList(queryParams.value, data);
  if (result?.code === 200) {
    loading.value = false;
    refreshing.value = false;
    if (result.rows.length > 0) {
      list.value = list.value.concat(result.rows);
      queryParams.value.pageNum++;
      finished.value = false;
    } else {
      // 数据全部加载完成
      finished.value = true;
      total.value = list.value.length;
    }
  } else {
    finished.value = true;
    loading.value = false;
  }
  if (list.value.length === 0) {
    isempty.value = true;
  } else {
    isempty.value = false;
  }
};
const onRefresh = () => {
  finished.value = false;
  // 重新加载数据
  // 将 loading 设置为 true，表示处于加载状态
  loading.value = true;
  onLoad();
};

//点击了搜索
const search = () => {
  showList.value = true;
  if (value.value) {
    if (!isFirst.value) {
      queryParams.value.pageNum = 1;
      list.value = [];
      onLoad();
    }
    addSearch(value.value);
  } else {
    showList.value = false;
  }
};
//添加搜索记录
const addSearch = async (content) => {
  if (content) {
    const params = {
      type: "1",
      content: content,
    };
    let result = await addSearchRequest(params);
    if (result != null && result.code === 200) {
      getSearchList();
    }
  }
};

//点击了删除
const delBtn = () => {
  showDelHis.value = true;
};

//点击了每一项后面的x
const delIteamBtn = async (id) => {
  let result = await deleteSearchByIdRequest(id);
  if (result != null && result.code === 200) {
    getSearchList();
  }
};

//删除了全部
const delAll = async () => {
  if (searchList.value.length > 0) {
    const params = {
      type: "1",
    };
    let result = await deleteAllRequest(params);
    if (result != null && result.code === 200) {
      searchList.value = [];
    }
  }
};

//点击了完成
const delFinish = () => {
  showDelHis.value = false;
};

//获取了所有的搜索记录
const getSearchList = async () => {
  const params = {
    type: "1",
  };
  let result = await getSearchListRequest(params);
  if (result != null && result.code === 200) {
    searchList.value = result.rows;
  } else {
    searchList.value = [];
  }
};
getSearchList();

//点击了搜索项
const clickSeachItem = (content) => {
  queryParams.value.pageNum = "1";
  list.value = [];
  showList.value = true;
  value.value = content;
  addSearch(content);
};

const toDetail = (id) => {
  router.push({
    path: "/lawContent",
    query: {
      id,
    },
  });
};
</script>
<style scoped lang="scss">
.container {
  &_search {
    position: fixed;
    width: 100%;
    display: flex;
    justify-content: space-between;
    &_left {
      padding: 40px 0px 0px 15px;
    }
    &_pos {
      flex: 2;
      padding: 20px 0px 0px 5px;
    }
    &_text {
      font-size: 16px;
      color: #1779ff;
      padding: 40px 15px 0px 0px;
    }
  }

  &_content {
    width: 100%;
    position: absolute;
    margin-top: 90px;
    &_history {
      &_top {
        display: flex;
        justify-content: space-between;
        padding: 10px 15px 0 15px;
        &_title {
          color: #a3a3a3;
          font-size: 12px;
        }
        &_btn {
          img {
            width: 15px;
            height: 15px;
          }

          &_text {
            display: flex;
            justify-content: start;
            &_left {
              color: #757575;
              font-size: 12px;
            }
            &_right {
              margin-left: 15px;
              color: #757575;
              font-size: 12px;
            }
          }
        }
      }

      &_bottom {
        padding: 18px 15px 0 15px;
        display: flex;
        flex-wrap: wrap;
        &_item {
          display: flex;
          justify-content: start;
          width: 48%;
          margin-bottom: 15px;
          padding-right: 4px;
          &_text {
            width: 100%;
            text-overflow: ellipsis;
            overflow: hidden;
            white-space: nowrap;
          }
          &_icon {
            .van-icon {
              margin-left: 15px;
              width: 15px;
              height: 15px;
              color: #bdbdbd;
            }
          }
        }
      }
    }

    &_list {
      height: 85vh;
      overflow-y: scroll;
      padding: 10px 15px 0 15px;
      &_title {
        color: #a3a3a3;
        font-size: 12px;
      }
      &_content {
        padding-top: 20px;
        &_item {
          border-radius: 2px;
          //background-color: rgba(250, 250, 250, 100);
          &_info {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            &_title {
              color: #212121;
              font-size: 15px;
            }
            &_desc {
              color: #bdbdbd;
              font-size: 12px;
            }
          }
          &_box {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
          }
          &_pic {
            img {
              width: 124px;
              height: 80px;
            }
          }
        }
      }
    }
  }

  &_divider {
    position: absolute;
    height: 1px;
    width: 100%;
    margin-top: 80px;
    background-color: #fafafa;
  }
}
</style>
