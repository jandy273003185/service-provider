<template>
  <div>
    <BaseHeader></BaseHeader>
    <div class="salesman">
      <div class="serachBox">
        <div class="search" @click="searchshop">
          <img class="searchIcon" src="../assets/images/home/search.png" alt="">
          <div>搜索商户名称</div>
        </div>
      </div>
      <div class="incomingBox"  @touchstart="toIncoming">
        <div class="buttonAdd" >
          <img src="../assets/images/home/add.png" alt="添加">
          <p>商户进件</p>
        </div>
      </div>
      <timeSelect v-if="islogin"></timeSelect>   <!--引入时间选择组件-->
      <div class="situation">
        <div class="titleBox">
          <div class="line"></div>
          <p class="titleBar">商户进件情况</p>
          <div class="line"></div>
        </div>

        <ul>
          <li v-for="(item, index) in statesList" :key="index" @click="toDetail(item.state,item.custId)">
            <div>
              <span class="shopName">{{item.custName }}</span>
              <span class="time">{{ item.createTime }}</span>
            </div>
            <span v-if="item.state=='00'" class=" state state_0">审核通过</span>
            <span v-if="item.state=='01'"  class=" state state_1">待审核</span>
            <span v-if="item.state=='04'" class=" state state_4">审核失败</span>
            <span v-if="item.state=='05'" class=" state state_5">待完善</span>
          </li>
        </ul>
      </div>
    </div>
  </div>

</template>

<script>
  import timeSelect from "../components/time-select";
  import BaseHeader from "../components/baser-header.vue";
  import {shopAuditInfo,login} from "../assets/api/interface";
  import {mapMutations} from 'vuex';
  import storage from '../assets/modeljs/storage.js';
  import axios from "axios";
  import http from "../assets/api/http";

export default {
  name: "salesman",
  components: { timeSelect,BaseHeader },
  data() {
    return {
      islogin:false,
      getOpenId:'666666',
      roleId:'salesman',
      statesList:[],
      userCode:'',
      userId:'',
      redirect_uri:'https://sp.qifenqian.com/wx/index.html#/salesman'//https://sp.qifenqian.com'
    }
  },
  created(){
<<<<<<< HEAD
    /*this.getUserOpenId();//执行获取用户openID的函数*/
=======
    this.getUserOpenId();//执行获取用户openID的函数
>>>>>>> 54ddc67c8e0c567cc5f636baf4bd4a5d1c67ed35

    this.$store.commit("setincoming", {});
    this.$store.commit("setPhotos", []);
    this.$store.commit("setCheckedState", "");
    this.firstLogin();
    this.setOpenID(this.getOpenId);
    this.setRole(this.roleId);
    this.setRoleId('3');

  },
  mounted() {


  },

  methods:{

    urlencode (str) {//redirect_uri转换格式urlEncode
    str = (str + '').toString();
    return encodeURIComponent(str).replace(/!/g, '%21').replace(/'/g, '%27').replace(/\(/g, '%28').replace(/\)/g, '%29').replace(/\*/g, '%2A').replace(/%20/g, '+');
  },


  toIncoming(){//路由跳转到商户进件页面
      console.log("商户进件");
      this.$router.push('baseInfo');
    },
    searchshop(){
      this.$router.push('searchShop');
    },


    //实际环境中获取用户的openId
    async getUserOpenId(){
      const  encodeUrl = this.urlencode(this.redirect_uri);
      //第一步：获取code
      const getCodeUrl= 'https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxce65746e62998dce&redirect_uri=' +  encodeUrl +'&response_type=code&scope=snsapi_base&state=123#wechat_redirect';
      console.log(encodeUrl);
     this.userCode = await http.get(getCodeUrl);
        console.log(this.userCode);
      //第二步：使用code换取access_token和OpenId

       /* const getCode = '';
        const  getOpenIdUrl = 'https://api.weixin.qq.com/sns/oauth2/access_token?appid=wxce65746e62998dce&secret=67c3aebf0e3e397df1fce595b837353b
        &code='+ getCode +'&grant_type=authorization_code'
        const userOpenId = await http.get(getOpenIdUrl);
        console.log(userOpenId);*/

     /* //将openid传给后端做是否绑定的判断
        const params = {
          openId:userOpenId.openid,
          roleId:this.roleId
        };
        const {data} = await login.firstLogin(params);
        console.log(data);
        if(data.resultCode=='0'){//未绑定此公众号
          this.$router.push('login');
        }
        if(data.resultCode=='1'){//已绑定过此公众号
          localStorage.setItem('token',data.resultMsg.token);
          axios.defaults.headers.common['token']= data.resultMsg.token;//设置axios请求头中加入token
          this.setToken(data.resultMsg.token);
          this.setUserId(data.resultMsg.userId);
          storage.set("userId", data.resultMsg.userId);
          console.log(storage.get('userId'));
          this.islogin=true;
          this.salesShopNew();
        }*/

    },

    async firstLogin(){//初次进入主页，传OpenId到后台，判断是否有绑定过账户
      const params = {
        openId:this.getOpenId,
        roleId:this.roleId
      };
      const {data} = await login.firstLogin(params);
      console.log(data);
      if(data.resultCode=='0'){
        this.$router.push('login');
      }
      if(data.resultCode=='1'){
        localStorage.setItem('token',data.resultMsg.token);
        axios.defaults.headers.common['token']= data.resultMsg.token;
        this.setToken(data.resultMsg.token);
        this.setUserId(data.resultMsg.userId);
        storage.set("userId", data.resultMsg.userId);
        console.log(storage.get('userId'));
        this.islogin=true;
        this.salesShopNew();
      }
    },
    //业务员主页下部分商户进件最新十条信息

    async salesShopNew(){
      let listInfo= await shopAuditInfo.shopAuditInfo({
        userId:storage.get("userId"),
        pageSize:'10',
        pageNum:'1',
        roleId:'3'
      });
      console.log(storage.get("userId"));
      console.log(listInfo);
      this.statesList = listInfo.data.resultMsg.data;
      /*let total=listInfo.data.resultMsg.total;*/
      
    },
    //查看审核失败信息和审核成功信息
  toDetail(state,custId){
    if(state=='04'){
      this.$router.push('whyFailed');
    }
    if(state=='00'){
      this.$router.push('/audit/pass');
    }
    if (state == "05") {
      this.$router.push({
        name: "baseInfo",
        params: { type: "corvidae", custId: custId }
      });
    }
    this.setCustId(custId);
  },

      ...mapMutations(['setRole','setRoleId','setUserId','setToken','setCustId','setOpenID'])
  }
};
</script>
<style scoped lang="stylus" ref="stylesheet/stylus">
  @import "../style/common/base.styl";
  .salesman{
    width: 100%;
    height: 100%;
    background-color:#EEEEEE;

    .serachBox{
      width: 100%;
      height: vw(92);
      background-color: #699dd7;
      display:flex;
      align-items:center;

      .search{
        width: vw(580);
        height: vw(58);
        margin-left: vw(34) ;
        background-color:#eeeeee
        display:flex;
        align-items:center;
        justifu-content:center;
        border-radius:vw(10);

        .searchIcon{
          width: vw(50);
          height: vw(50);
        }

        div{
          height: vw(50);
          font-size: vw(32);
          color: #999999;
        }
      }
    }
   .incomingBox{
     width: vw(690);
     height: vw(80);
     margin: vw(36) auto;
     border:vw(1) dashed #699dd7;

     .buttonAdd{
       width: vw(220);
       height: vw(40);
       margin: vw(20) auto;
       /*border: 1px solid red;*/
       display: flex;
       align-items: center;
       justify-content: space-between;

       img{
         width: vw(40);
         height: vw(40);
       }

       p{
         height: vw(40);
         line-height: vw(40);
         font-size: vw(40);
         color: #699dd7;
       }
     }
   }
   .situation{
     width: 100%;
     height: vw(600);
     box-sizing border-box;
      .titleBox{
        display: flex;
        justify-content: space-between;
        align-items: center;
        .line{
          height: vw(2);
          width: vw(100);
          background-color: #c0c0c0;
          flex-grow: 1;
        }
        .titleBar{
          height: vw(30);
          line-height: vw(30);
          text-align: center;
          color: #333333;
          font-size: vw(30);
        }
      }

  ul{
    width: 100%;
    margin:auto;
    background-color: #fff;

  li{
    width: vw(680);
    height: vw(94);
    margin: 0 auto;
    display: flex;
    justify-content:space-between;
    align-items: center;
    border-bottom: vw(1) solid #EEEEEE;

  div{
    color: #333333;
    height: 100%;
    margin: 0;
    display: flex;
    flex-direction: column;
    justify-content:space-around;
    align-items: flex-start;
  .shopName{
    color: #030303;
    font-size: vw(32);
  }
  .time{
    color: #969696;
    font-size: vw(22);
  }
  }
  .state{
    font-size: vw(32);
  }
  .state_0{color: #4dff1a;}
  .state_1{color: #aeaeae;}
  .state_4{color: #ff495d;}
  .state_5{color: #544eec;}
  }
  }

  }

  }
</style>