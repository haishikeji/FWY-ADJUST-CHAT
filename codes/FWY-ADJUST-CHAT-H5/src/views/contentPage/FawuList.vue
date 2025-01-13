<template>
  <div class="container">
    <div class="container_top">
      <div class="container_top_left">
        <div class="container_top_left_title">{{ name }}</div>
        <div class="container_top_left_info">官方课程</div>
        <div class="container_top_left_num">{{ num }}人学过</div>
      </div>
      <div class="container_top_right">
        <div class="container_top_right_title">共{{ classNum }}节课程</div>
      </div>
    </div>
    <div class="container_divider"></div>
    <div class="container_content">
      <div class="container_content_tabs">
        <van-tabs
          v-model:active="active"
          color="#1779FF"
          title-active-color="#1779FF"
          line-width="55px"
        >
          <van-tab title="课程目录">
            <van-pull-refresh v-model="refreshing" @refresh="onRefresh">
              <van-list
                v-if="!isempty"
                v-model:loading="loading"
                :finished="finished"
                finished-text="没有更多了"
                @load="onLoad"
              >
                <van-cell
                  v-for="(item, index) in list"
                  :key="index"
                  :title="item"
                  @click="toDetail(item.id)"
                >
                  <template #icon>
                    <van-tag type="primary">图文</van-tag>
                  </template>
                  <template #title>
                    <span class="custom-title">{{ item.name }}</span>
                  </template>
                </van-cell>
              </van-list>
            </van-pull-refresh>
            <van-empty description="暂无信息" v-if="isempty" />
          </van-tab>
          <van-tab title="课程介绍">
            <div class="container_content_desc">
              {{ introduce }}
            </div></van-tab
          >
        </van-tabs>
      </div>
    </div>
  </div>
</template>
<script setup name="FawuList">
import { reactive, ref, toRefs } from "@vue/reactivity";
import { useRoute, useRouter } from "vue-router";
import { getSubjectListByClassId } from "@/api/lawtraining";
const isempty = ref(false);
const active = ref(0);
const list = ref([]);
const loading = ref(false);
const finished = ref(false);
const refreshing = ref(false);
const router = useRouter();
const route = useRoute();
const listRef = ref(null);
listRef.value?.check();
const query = reactive({ queryParams: { pageSize: 10, pageNum: 1 } });
const { queryParams } = toRefs(query);
//分类id
const id = route.query.id;
//学过人数
const num = route.query.num;
//分类名称
const name = route.query.name;
//课时
const classNum = route.query.classNum;
//课程介绍
const introduce = route.query.introduce;
const onLoad = async () => {
  if (refreshing.value) {
    queryParams.value.pageNum = 1;
    list.value = [];
    refreshing.value = false;
  }
  const data = {
    classId: id,
  };
  let result = await getSubjectListByClassId(queryParams.value, data);
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
  // 清空列表数据
  finished.value = false;

  // 重新加载数据
  // 将 loading 设置为 true，表示处于加载状态
  loading.value = true;
  onLoad();
};
const toDetail = (id) => {
  router.push({
    path: "/fawuContent",
    query: {
      id: id,
    },
  });
};
</script>
<style lang="scss" scoped>
.container {
  height: calc(100vh - 50px);
  overflow-y: scroll;
  padding: 10px 15px 15px 15px;
  &_top {
    display: flex;
    justify-content: space-between;
    &_left {
      &_title {
        font-size: 18px;
        color: #212121;
        font-weight: bold;
      }
      &_info {
        margin-top: 10px;
        font-size: 14px;
        color: #757575;
      }
      &_num {
        color: #bdbdbd;
        font-size: 12px;
        margin-top: 10px;
      }
    }
    &_right {
      align-self: center;
      &_title {
        font-size: 12px;
        color: #9e9e9e;
      }
    }
  }
  &_divider {
    margin-top: 20px;
    margin-bottom: 15px;
    position: absolute;
    left: 0;
    top: 120px;
    height: 10px;
    width: 100%;
    background-color: #fafafa;
  }
  &_content {
    margin-top: 30px;
    &_desc {
      margin-top: 20px;
    }
  }
}
.custom-title {
  margin-left: 20px;
  margin-right: 4px;
  float: left;
  vertical-align:middle;

}
</style>
