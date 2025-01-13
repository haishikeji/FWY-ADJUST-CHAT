<template>
  <van-tabbar v-model="actives" :fixed="true">
    <van-tabbar-item
      v-for="(item, index) in tabbars"
      :key="index"
      replace
      :to="item.name"
    >
      <span :class="currIndex == index ? actives : ''">{{ item.title }}</span>
      <template v-slot:icon="props">
        <img :src="props.active ? item.active : item.normal" />
      </template>
    </van-tabbar-item>
  </van-tabbar>
</template>

<script>
import { Tabbar, TabbarItem } from "vant";
// import { getButtomTabList } from "@/api/bottomtabbar";

export default {
  name: "Bottomtabbar",
  computed: {
    getClosed() {
      return this.$store.state.closed;
    },
  },
  watch: {
    $route(to) {
      this.activeTab(to.path);
    },
  },
  data() {
    return {
      closed: false,
      currIndex: 0,
      actives: 0,
      // tabbars: [],
      tabbars: [
        {
          name: "/home",
          title: "劳动关系调解",
          normal: require("../assets/images/home_n.png"),
          active: require("../assets/images/home.png"),
        },
        {
          name: "law",
          title: "劳动法普及",
          normal: require("../assets/images/law_n.png"),
          active: require("../assets/images/law_n1.png"),
        },
        {
          name: "fawu",
          title: "法务培训",
          normal: require("../assets/images/fawu_n.png"),
          active: require("../assets/images/fawu.png"),
        },
        {
          name: "person",
          title: "个人中心",
          normal: require("../assets/images/person_n.png"),
          active: require("../assets/images/person.png"),
        },
      ],
    };
  },
  components: {
    [Tabbar.name]: Tabbar,
    [TabbarItem.name]: TabbarItem,
  },
  mounted() {},
  methods: {
    activeTab(path) {
      var index = this.tabbars.map((item) => item.name).indexOf(path);
      if (index !== -1) {
        this.actives = index;
      }
    },
    //从接口获取数据
    // async getButtomTab() {
    //   let response = await getButtomTabList();
    //   if (response.rows.length > 0) {
    //     for (var i = 0; i < response.rows.length; i++) {
    //       var data = response.rows[i];
    //       var item = {};
    //       item.name = data.router;
    //       item.title = data.title;
    //       item.normal = data.normal;
    //       item.active = data.active;
    //       this.tabbars.push(item);
    //     }
    //   }
    // },
  },
  created() {
    //this.getButtomTab();
    const routerPath = this.$route.path;
    if (routerPath === "/") {
      this.actives = 0;
    } else if (routerPath === "/law") {
      this.actives = 1;
    } else if (routerPath === "/fawu") {
      this.actives = 2;
    } else if (routerPath === "/person") {
      this.actives = 3;
    } else {
      this.actives = 0;
    }
    //this.tab(0, 'Videolist')
    //this.getButtomTab();
    // const routerPath = this.$route.path
    // if (routerPath === '/home') {
    //   for(var i = 0; i < this.tabbars.length;i++){
    //     if(this.tabbars.length[i].name === 'home'){
    //       this.actives = this.tabbars.length[i].sort;
    //     }
    //   }
    //   //this.actives = 0
    // } else if (routerPath === '/activity') {
    //   for(var j = 0; j < this.tabbars.length;j++){
    //     if(this.tabbars.length[j].name === 'activity'){
    //       this.actives = this.tabbars.length[j].sort;
    //     }
    //   }
    //   //this.actives = 1
    // } else if (routerPath === '/shop') {
    //   for(var k = 0; k < this.tabbars.length;k++){
    //     if(this.tabbars.length[k].name === 'shop'){
    //       this.actives = this.tabbars.length[k].sort;
    //     }
    //   }
    //   //this.actives = 2
    // } else if (routerPath === '/center') {
    //   for(var h = 0; h < this.tabbars.length;h++){
    //     if(this.tabbars.length[h].name === 'center'){
    //       this.actives = this.tabbars.length[h].sort;
    //     }
    //   }
    //   //this.actives = 3
    // }else {
    //   this.actives = 0
    // }
  },
};
</script>

<style scoped>
.container {
  font-size: 14px;
  padding: 0;
  background: #f7f7f7;
  padding-top: 0px;
  /* min-height: 84vh; */
}
.van-tabbar-item--active {
  color: #1779ff;
}
</style>
