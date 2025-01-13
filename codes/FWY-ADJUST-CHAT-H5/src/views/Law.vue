<template>
  <div class="container">
    <div class="container_search">
      <div class="container_search_pos">
        <van-search
          shape="round"
          readonly="true"
          v-model="value"
          @click="toSearch"
          placeholder="请输入搜索关键词"
        />
      </div>
    </div>

    <div class="container_content">
      <div
        class="container_content_hot"
        @click="toDetail(topData.noticeId)"
        v-if="topData != null"
      >
        <div class="container_content_hot_icon">
          <img src="../assets/images/searchhot.png" alt="" />
        </div>
        <div class="container_content_hot_title">
          {{ topData.noticeTitle }}
        </div>
      </div>

      <div class="container_divider"></div>
      <div class="container_content_list">
        <van-pull-refresh
          v-model="refreshing"
          @refresh="onRefresh"
          v-if="!isempty"
        >
          <van-list
            v-model:loading="loading"
            :finished="finished"
            finished-text="没有更多了"
            @load="onLoad"
            ref="listRef"
          >
            <div
              class="container_content_list_item"
              v-for="item in list"
              :key="item"
              :title="item"
              @click="toDetail(item.noticeId)"
            >
              <div class="container_content_list_item_box">
                <div class="container_content_list_item_info">
                  <div class="container_content_list_item_info_title">
                    {{ item.noticeTitle }}
                  </div>
                  <div class="container_content_list_item_info_desc">
                    <span>{{ item.monthAndDay }}</span
                    ><span style="margin-left: 15px">
                      {{ item.readCount }}阅读</span
                    >
                  </div>
                </div>
                <div class="container_content_list_item_pic">
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
</template>
<script setup name="Law">
import { reactive, ref, toRefs } from "@vue/reactivity";
import { useRouter } from "vue-router";
import { getNoticeList, getTopNotice } from "@/api/notice";
const isempty = ref(false);
const list = ref([]);
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
const listRef = ref(null);
listRef.value?.check();
const query = reactive({ queryParams: { pageSize: 10, pageNum: 1 } });
const { queryParams } = toRefs(query);
const router = useRouter();
const imgUrl = ref(require("../assets/images/banner1.png"));
//搜索热榜
const topData = ref({});
const onLoad = async () => {
  if (refreshing.value) {
    queryParams.value.pageNum = 1;
    list.value = [];
    refreshing.value = false;
  }
  const data = {};
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
    }
  } else {
    finished.value = true;
    loading.value = false;
  }
  if (list.value.length === 0) {
    isempty.value = true;
  }
};
const onRefresh = () => {
  finished.value = false;

  // 重新加载数据
  // 将 loading 设置为 true，表示处于加载状态
  loading.value = true;
  onLoad();
};

const toDetail = (id) => {
  router.push({
    path: "/lawContent",
    query: {
      id,
    },
  });
};
const toSearch = () => {
  router.push({
    path: "/search",
  });
};

const getTop = async () => {
  let result = await getTopNotice();
  if (result != null && result.code === 200) {
    topData.value = result.data;
  }
};
getTop();
</script>
<style scoped lang="scss">
.container {
  &_search {
    width: 100%;
    position: fixed;
    &_pos {
      padding: 20px 5px 20px 5px;
    }
  }
  &_content {
    width: 100%;
    position: absolute;
    top: 80px;
    left: 0px;
    right: 0px;
    height: 80vh;
    overflow-y: scroll;
    &_hot {
      padding: 5px 15px 20px 15px;
      display: flex;
      flex-direction: row;
      align-items: center;
      &_icon {
        margin-right: 10px;
        img {
          width: 30px;
          height: 40px;
        }
      }
      &_title {
        color: #212121;
        font-size: 14px;
        line-height: 25px;
      }
    }
    &_list {
      padding-top: 15px;
      //padding: 15px 15px 15px 15px;
      &_item {
        border-radius: 2px;
        //background-color: rgba(250, 250, 250, 100);
        padding: 0px 15px 0 15px;
        &_info {
          display: flex;
          flex-direction: column;
          justify-content: space-between;
          &_title {
            color: #212121;
            font-size: 15px;
            line-height: 25px;
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
  &_divider {
    height: 10px;
    width: 100%;
    background-color: #fafafa;
  }
}
</style>
