<template>
  <div class="mine">
     <van-nav-bar title="我的"   @click-left="changePrepage"/>
    <div class="info">
      <img class="photo"
        src="https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2018939532,1617516463&fm=26&gp=0.jpg"
      />
      <div class="text">
        <span>xxxx有限公司</span>
        <div class="name">用户</div>
      </div>
    </div>
    <div class="menu">
      <router-link to="/amendPsd" class="item" v-if="roleId=='3'">修改登录密码 <van-icon class="icon" name="arrow" /></router-link>
      <router-link to="/salesManage" class="item" v-if="roleId=='2'">业务员管理   <van-icon class="icon" name="arrow" /></router-link>
      <router-link to="/about" class="item">关于   <van-icon class="icon" name="arrow" /></router-link>
      <div class="item">
        联系客服
        <span>400-633-0707</span>
      </div>
    </div>
    <div class="logout">退出登录</div>
    <BaseHeader></BaseHeader>
  </div>
</template>
<script>
import {mapState} from 'vuex';
import BaseHeader from "@/components/baser-header.vue";
export default {
  name: "mine",
  components: {
   BaseHeader 
  },
   computed:{
        ...mapState(['roleId'])
    },
  created() {
    //this.$route.params.type
  },
  data() {
    return {};
  },
  methods: {
    changePrepage(){
         
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/mine.scss";
</style>