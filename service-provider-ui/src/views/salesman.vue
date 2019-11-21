<template>
  <div v-show="logined">
    <BaseHeader></BaseHeader>
    <div class="salesman">
      <div class="serachBox">
        <div class="search" @click="searchshop">
          <img class="searchIcon" src="../assets/images/home/search.png" alt />
          <div>搜索商户名称</div>
        </div>
      </div>
      <div class="incomingBox" @touchstart="toIncoming">
        <div class="buttonAdd">
          <img src="../assets/images/home/add.png" alt="添加" />
          <p>商户进件</p>
        </div>
      </div>
      <timeSelect v-if="islogin"></timeSelect>
      <!--引入时间选择组件-->
      <div class="situation">
        <div class="titleBox">
          <div class="line"></div>
          <p class="titleBar">商户进件情况</p>
          <div class="line"></div>
        </div>

        <ul>
          <li
            v-for="(item, index) in statesList"
            :key="index"
            @click="toDetail(item.state,item.custId)"
          >
            <div>
              <span class="shopName">{{item.custName }}</span>
              <span class="time">{{ item.createTime }}</span>
            </div>
            <span v-if="item.state=='00'" class="state state_0">审核通过</span>
            <span v-if="item.state=='01'" class="state state_1">待审核</span>
            <span v-if="item.state=='04'" class="state state_4">审核失败</span>
            <span v-if="item.state=='05'" class="state state_5">待完善</span>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import timeSelect from "../components/time-select";
import BaseHeader from "../components/baser-header.vue";
import { shopAuditInfo, login } from "../assets/api/interface";
import { mapMutations } from "vuex";
import storage from "../assets/modeljs/storage.js";
import axios from "axios";
export default {
  name: "salesman",
  components: { timeSelect, BaseHeader },
  data() {
    return {
      logined:false,//控制页面已经在登录状态
      islogin: false,//组件事件选择控制
      openId: "",
      roleId: "salesman",
      statesList: [],
      userId: ""
    };
  },
  created() {
    this.$store.commit("setincoming", {});
    this.$store.commit("setPhotos", []);
    this.$store.commit("setCheckedState", ""); //snsapi_base
    console.log("createdCode");
    console.log(this.$store.state.code);
    if (!this.$store.state.code) {
      var code = this.getUrlParam("code");
      if (!code) {
        //  let REDIRECT_URI = encodeURIComponent("https://sp.qifenqian.com/wx/index.html/#/salesman");
        let REDIRECT_URI = encodeURIComponent(
          "https://sp.qifenqian.com/wx/index.html#salesman"
        );
        window.location.href =
          "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxce65746e62998dce&redirect_uri=" +
          REDIRECT_URI +
          "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
      } else {
        console.log("业务员主页获取code");
        this.setCode(code);
        this.getOpenId(code);
      }
    } else {
      console.log(this.$route.params);
      if (this.$route.params.fname && this.$route.params.fname == "login") {
        console.log("登录页进入");
        this.firstLogin({
          openId: this.$store.state.openId,
          roleId: this.roleId
        });
      } else {
        console.log("其他页返回");
        this.logined=true;
        this.islogin = true;
        this.salesShopNew();
      }
    }
  },
  mounted() {},

  methods: {
    async getOpenId(code) {
      //获取openid
      let res = await login.getOpenId({
        code: code
      });
      let openId = res.data.resultMsg;
      console.log(openId);
      if (openId) {
        this.openId = openId;
        this.setOpenID(this.openId);
        this.setRole(this.roleId);
        this.setRoleId("3");
        const params = {
          openId: this.openId,
          roleId: this.roleId
        };
        console.log(params);
        this.firstLogin({
          openId: this.openId,
          roleId: this.roleId
        });
      }
    },
    getUrlParam(name) {
      //获取code
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      }

      return null;
    },
    toIncoming() {
      //路由跳转到商户进件页面
      console.log("商户进件");
      this.$router.push("baseInfo");
    },
    searchshop() {
      this.$router.push("searchShop");
    },
    async firstLogin(params) {
      //初次进入主页，传OpenId到后台，判断是否有绑定过账户
      const { data } = await login.firstLogin(params); //获取绑定状态
      console.log(data);
      if (data.resultCode == "0") {
        //未绑定
        this.$router.push("login");
      }
      if (data.resultCode == "1") {
        //已绑定
        this.logined=true;
        localStorage.setItem("token", data.resultMsg.token);
        axios.defaults.headers.common["token"] = data.resultMsg.token;
        this.setToken(data.resultMsg.token);
        this.setUserId(data.resultMsg.userId);
        storage.set("userId", data.resultMsg.userId);
        console.log(storage.get("userId"));
        this.islogin = true;
        this.salesShopNew();
      }
      if (data.resultCode == "2") {
        //业务员进入了管理员入口
        const _this = this;
        this.$toast({
          message:data.resultMsg,
          onClose:function(){
            _this.$router.push("Administrator");
          }
        });
      }
    },
    //业务员主页下部分商户进件最新十条信息
    async salesShopNew() {
      let listInfo = await shopAuditInfo.shopAuditInfo({
        userId: storage.get("userId"),
        pageSize: "10",
        pageNum: "1",
        roleId: "3"
      });
      console.log(storage.get("userId"));
      console.log(listInfo);
      this.statesList = listInfo.data.resultMsg.data;
      /*let total=listInfo.data.resultMsg.total;*/
    },
    //查看审核失败信息和审核成功信息
    toDetail(state, custId) {
      if (state == "04") {
        this.$router.push("whyFailed");
      }
      if (state == "00") {
        this.$router.push("/audit/pass");
      }
      if (state == "05") {
        this.$router.push({
          name: "baseInfo",
          params: { type: "corvidae", custId: custId }
        });
      }
      this.setCustId(custId);
    },

    ...mapMutations([
      "setRole",
      "setRoleId",
      "setUserId",
      "setToken",
      "setCustId",
      "setOpenID",
      "setCode"
    ])
  }
};
</script>
<style scoped lang="stylus" ref="stylesheet/stylus">
@import '../style/common/base.styl';

.salesman {
  width: 100%;
  height: 100%;
  background-color: #EEEEEE;

  .serachBox {
    width: 100%;
    height: vw(92);
    background-color: #699dd7;
    display: flex;
    justifu-content: center;
    align-items: center;

    .search {
      width: vw(690);
      height: vw(58);
      background-color: #eeeeee;
      display: flex;
      align-items: center;
      justifu-content: center;
      border-radius: vw(10);

      .searchIcon {
        width: vw(50);
        height: vw(50);
      }

      div {
        height: vw(50);
        font-size: vw(32);
        color: #999999;
      }
    }
  }

  .incomingBox {
    width: vw(690);
    height: vw(80);
    margin: vw(36) auto;
    border: vw(1) dashed #699dd7;

    .buttonAdd {
      width: vw(220);
      height: vw(40);
      margin: vw(20) auto;
      /* border: 1px solid red; */
      display: flex;
      align-items: center;
      justify-content: space-between;

      img {
        width: vw(40);
        height: vw(40);
      }

      p {
        height: vw(40);
        line-height: vw(40);
        font-size: vw(40);
        color: #699dd7;
      }
    }
  }

  .situation {
    width: 100%;
    height: vw(600);
    box-sizing: border-box;

    .titleBox {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .line {
        height: vw(2);
        width: vw(100);
        background-color: #c0c0c0;
        flex-grow: 1;
      }

      .titleBar {
        height: vw(30);
        line-height: vw(30);
        text-align: center;
        color: #333333;
        font-size: vw(30);
      }
    }

    ul {
      width: 100%;
      margin: auto;
      background-color: #fff;

      li {
        width: vw(680);
        height: vw(94);
        margin: 0 auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
        border-bottom: vw(1) solid #EEEEEE;

        div {
          color: #333333;
          height: 100%;
          margin: 0;
          display: flex;
          flex-direction: column;
          justify-content: space-around;
          align-items: flex-start;

          .shopName {
            color: #030303;
            font-size: vw(32);
          }

          .time {
            color: #969696;
            font-size: vw(22);
          }
        }

        .state {
          font-size: vw(32);
        }

        .state_0 {
          color: #3fd016
        }

        .state_1 {
          color: #aeaeae;
        }

        .state_4 {
          color: #ff495d;
        }

        .state_5 {
          color: #3f9aff;
        }
      }
    }
  }
}
</style>