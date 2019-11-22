<template>
  <div v-show="logined">
    <BaseHeader></BaseHeader>
    <div class="admin">
      <div class="serachBox">
        <div class="search" @click="searchshop">
          <img class="searchIcon" src="../assets/images/home/search.png" alt />
          <div>搜索业务员名字{{openId}}</div>
        </div>
      </div>
      <div class="incomingBox" @click="toIncoming">
        <div class="buttonAdd">
          <img src="../assets/images/home/add.png" alt="添加" />
          <p>商户进件</p>
        </div>
      </div>
      <timeSelect v-if="islogin" @getDataRange="getDataRange"></timeSelect>
      <!--引入时间选择组件-->
      <div class="salesmanInfo">
        <van-tabs
          v-model="sortType"
          title-active-color="#699dd7"
          color="#699dd7"
          swipeable
          @change="channgeTab"
        >
          <van-tab title="交易额排名">
            <ul>
              <li class="flex_r" v-for="(item, index) in sumList" :key="index">
                <div>
                  <span class="num">NO.{{ index+1 }}</span>
                  <span class="name">{{ item.userName }}</span>
                </div>
                <div>
                  <span
                    :style="'width:'+(parseFloat(item.effectiveNum)*100/sumMax)+'%'"
                    class="process"
                  ></span>
                  <span class="sum">{{ item.effectiveNum }}元</span>
                </div>
              </li>
            </ul>
          </van-tab>
          <van-tab title="进件数排名">
            <ul>
              <li v-for="(item, index) in numList" :key="index">
                <div>
                  <span class="num">NO.{{ index+1 }}</span>
                  <span class="name">{{ item.name }}</span>
                </div>
                <div>
                  <span :style="'width:'+(parseFloat(item.sum)*100/numMax)+'%'" class="process"></span>
                  <span class="sum">{{ item.sum }}件</span>
                </div>
              </li>
            </ul>
          </van-tab>
        </van-tabs>
      </div>
    </div>
  </div>
</template>

<script>
import timeSelect from "../components/time-select";
import BaseHeader from "../components/baser-header.vue";
import { login, adminIndex } from "../assets/api/interface";
/*   import { mapState } from 'vuex';*/
import { mapMutations } from "vuex";
import storage from "../assets/modeljs/storage.js";
import axios from "axios";
import util from "@/lib/util.js";
export default {
  name: "Administrator",
  components: { timeSelect, BaseHeader },
  data() {
    return {
      logined:false,//控制页面已经在登录状态
      sortType: 0,
      openId: "",
      roleId: "agent",
      islogin: false,
      /*timeselect:true,*/
      queryStartDate: util.fun_date(7).timeStart,
      queryEndDate: util.fun_date(7).timeEnd,
      sumMax: 1, //计算业绩百分比，由第一名的值决定，
      numMax: 1, //计算笔数百分比，由第一名的值决定
      sumList: [],
      numList: []
    };
  },
  created() {
    this.$store.commit("setincoming", {});
    this.$store.commit("setPhotos", []);
    this.$store.commit("setCheckedState", "");
    if (!this.$store.state.code) {
      var code = this.getUrlParam("code");
      if (!code) {
        //  let REDIRECT_URI = encodeURIComponent("https://sp.qifenqian.com/wx/index.html/#/salesman");
        let REDIRECT_URI = encodeURIComponent(
          "https://sp.qifenqian.com/wx/index.html#Administrator"
        );
        window.location.href =
          "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxce65746e62998dce&redirect_uri=" +
          REDIRECT_URI +
          "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
      } else {
        this.setCode(code);
        this.getOpenId(code);
      }
    } else {
      if (this.$route.params.fname&&this.$route.params.fname == "login") {
        this.firstLogin({
          openId: this.$store.state.openId,
          roleId: this.roleId
        });
      } else {
         this.logined=true;
        this.islogin = true;
        this.getSalesRanking("0");
      }
    }
  },
  mounted() {},
  computed: {},
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
        this.setRoleId("2");
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
      if (r != null) return unescape(r[2]);
      return null;
    },
    channgeTab(e) {
      this.getSalesRanking(e);
    },
    getDataRange(obj) {
      //获取组件时间
      this.queryStartDate = obj.timeStart;
      this.queryEndDate = obj.timeEnd;
      this.sortType = 0;
      this.getSalesRanking("0");
    },
    toIncoming() {
      //路由跳转到商户进件页面
      console.log("商户进件");
      this.setCustId('');
      this.$router.push("baseInfo");
    },
    searchshop() {
      //搜索商户
      this.$router.push("SearchSalesman");
    },
    async getSalesRanking(type) {
      //业务员排名
      const params = {
        sortType: type || this.sortType,
        userId: this.$store.state.userId,
        queryStartDate: this.queryStartDate,
        queryEndDate: this.queryEndDate,
        roleId: 2
      };
      let sales = await adminIndex.SalesRanking(params);
      let list = sales.data.resultMsg;
      if (type == "0" || this.sortType == "0") {
        if (list[0] && list[0].effectiveNum) {
          this.sumMax = parseFloat(list[0].effectiveNum) * 1.5;
        }
        this.sumList = list;
      }
      if (type == "1" || this.sortType == "1") {
        if (list[0] && list[0].effectiveNum) {
          this.numMax = parseFloat(list[0].effectiveNum) * 1.5;
        }

        this.numList = list;
      }
    },
    async firstLogin(params) {//初次进入主页，传OpenId到后台，判断是否有绑定过账户
     
      const userData = await login.firstLogin(params);
      console.log(userData);
      if (userData.data.resultCode == "0") {
        this.$router.push("login");
      }
      if (userData.data.resultCode == "1") {
         this.logined=true;
        console.log(userData.data.resultMsg.token);
        this.setToken(userData.data.resultMsg.token);
        this.setUserId(userData.data.resultMsg.userId);
        localStorage.setItem("token", userData.data.resultMsg.token);
        axios.defaults.headers.common["token"] = userData.data.resultMsg.token;
        storage.set("userId", userData.data.resultMsg.userId);
        console.log(storage.get("userId"));
        this.islogin = true;
        this.getSalesRanking("0");
      }
      if (userData.data.resultCode == "2") {
        //管理员进入了业务员入口
        const _this = this;
        this.$toast({
          message:userData.data.resultMsg,
          onClose:function(){
            _this.$router.push("salesman");
          }
        });
      }
    },
    ...mapMutations([
      "setRole",
      "setToken",
      "setUserId",
      "setRoleId",
      "setOpenID",
      "setCode",
      "setCustId",
    ])
  }
};
</script>
<style scoped lang="stylus" ref="stylesheet/stylus">
@import '../style/common/base.styl';

.admin {
  width: 100%;
  background-color: #EEEEEE;

  .serachBox {
    width: 100%;
    height: vw(92);
    background-color: #699dd7;
    display: flex;
    align-items: center;

    .search {
      width: vw(580);
      height: vw(60);
      margin-left: vw(34);
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
        line-height: vw(50);
        font-size: vw(26);
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

  .salesmanInfo {
    width: vw(690);
    margin: vw(20) auto;
    background-color: #fff;

    ul {
      width: 100%;
      padding: 0 vw(20);

      li {
        height: vw(110);
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: flex-start;

        div {
          width: 100%;

          .process {
            display: inline-block;
            background: #b0b0b0;
            height: vw(20);
            border-radius: vw(6);
            line-height: vw(60);
          }

          .num {
            height: vw(20);
            font-size: vw(20);
            color: #929292;
          }

          .name {
            font-size: vw(20);
            margin-left: vw(10);
          }

          .sum {
            height: vw(20);
            font-size: vw(20);
            color: #8d8d8d;
            margin-left: vw(10);
          }
        }
      }

      li:nth-child(1) {
        .num {
          color: #557fae;
        }

        .process {
          background: #557fae;
        }
      }

      li:nth-child(2) {
        .num {
          color: #6698d1;
        }

        .process {
          background: #6698d1;
        }
      }

      li:nth-child(3) {
        .num {
          color: #7ab7fa;
        }

        .process {
          background: #7ab7fa;
        }
      }
    }
  }
}
</style>