<template>
  <div class="login">
    <div class="box">
      <div class="title">
        <img src="../assets/images/login/login.png" alt />
        <span>七分钱进件系统</span>
      </div>
      <p v-if="selectLogin=='psd'" class="logintype">账号密码登录</p>
      <p v-if="selectLogin=='code'" class="logintype">验证码登录</p>
      <div v-if="selectLogin=='psd'" >//账号密码登录
        <div class="item userName">
          <input v-model.trim="userName" type="text" placeholder="请输入账号" />
          <van-icon @click="clearName" name="close" />
        </div>
        <div class="item">
          <input v-model.trim="password" type="password" placeholder="登录密码" />
          <van-icon @click="clearPsd" name="close" />
        </div>
      </div>
      <div v-if="selectLogin=='code'" >//手机号登录
        <div class="item userName">
          <input v-model.trim="userPhone" type="text" placeholder="输入手机号码" />
          <van-icon @click="clearName" name="close" />
        </div>
        <div class="item">
          <input v-model.trim="userCode" type="text" placeholder="输入验证码" />
         <!-- <van-icon @click="clearPsd" name="close" />-->
          <div class="getCode" @click="getCode">获取验证码</div>
        </div>
      </div>
      <div class="item edter">
        <button @click="submitLogin">登录</button>
      </div>
     <!-- <div v-if="selectLogin=='psd'" class="selectLogin" @click="selectInto('code')">短信验证码登录</div>
      <div v-if="selectLogin=='code'" class="selectLogin" @click="selectInto('psd')">账号密码登录</div>-->
    </div>
  </div>
</template>

<script >
import { mapState } from "vuex";
import { login } from "../assets/api/interface";
import { Dialog } from "vant"; //弹窗函数，可直接调用
export default {
  name: "Login",
  data() {
    return {
      userName: null,
      password: null,
      selectLogin:'psd',
      userCode:'',//手机号
      userPhone:''//短信验证码
    };
  },
  mounted() {
    console.log(this.$store.state.role);
    console.log(this.role);
  },
  computed: {
    ...mapState(["role", "openId"])
  },
  methods: {
    //选择登录方式
    selectInto(way){
      if(way=='code'){
        this.$toast({message: "验证码登录暂未开通，敬请期待",duration:1000 });
      }
     /* this.selectLogin=way;*/
    },
    //获取短信验证码
    getCode(){
      this.$toast({ message: "已发送短信验证码，请前往查看" });
    },

    //判断账号密码非空
    submitLogin() {
      let params={};
      if(this.selectLogin=='psd'){
        if (!this.userName) {
          this.$toast({message: "请填写用户名账号",duration:1000 });
          return;
        }
        if (!this.password) {
          this.$toast({message: "请填写密码",duration:1000 });
          return;
        }
       params = {
          userName: this.userName,
          password: this.password,
          openId: this.openId,
          roleCode: this.role
       };
        console.log(params);

      }else if(this.selectLogin=='code'){
        if (!this.userPhone) {
          Dialog({ message: "手机号码不能为空" });
          return;
        }
        if (!this.userCode) {
          Dialog({ message: "验证码不能为空" });
          return;
        }
        params = {
          userName: this.userPhone,
          password: this.userCode,
          openId: this.openId,
          roleCode: this.role
        };
        console.log(params);
      }

      this.loginPost(params); //登录请求
    },
    clearName() {
      this.userName = "";
    },
    clearPsd() {
      this.password = "";
    },
    async loginPost(params) {
      let loginData = await login.login(params);
      console.log(loginData);
      if (loginData.data.resultCode == 1) {
            if (this.role == "agent") {
              console.log(this.role);
              this.$router.replace({
                name: "Administrator",
                params: {
                  fname: "login"
                }
              });
            } else if(this.role == "salesman") {
              console.log(this.role);
              this.$router.replace({
                name: "salesman",
                params: {
                  fname: "login"
                }
              });
            }


      } else {
        Dialog({ message: loginData.data.resultMsg });
      }
    }
    /*...mapMutations(['setToken','setGrade'])*/
  }
};
</script>

<style scoped lang="stylus" ref="stylesheet/stylus">
@import '../style/common/base.styl';

.login {
  width: 100%;
  height: 100%;

  .box {
    width: 90%;
    height: auto;
    margin: auto;

    .title {
      font-size: vw(36);
      height: vw(500);
      font-weight: 500;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;

      span {
        font-size: vw(20);
        margin-top: vw(5);
      }

      img {
        width: vw(150);
        height: vw(150);
      }
    }

    .logintype {
      font-size: vw(50);
      font-weight: vw(20);
      height: vw(80);
      line-height: vw(80);
    }

    .item {
      width: 100%;
      height: vw(100);
      display: flex;
      align-items: center;
      justify-content: center;
      box-sizing: border-box;
      border-bottom: 1px solid #d9d9d9;

      input {
        height: 80%;
        width: 100%;
        font-size: vw(32);
        background-color: #f7f7f7;
        color: #222222;
        border: none;
        outline: none;
        text-indent: vw(20);
      }

      button {
        height: 100%;
        width: 100%;
        background-color: #689ED7;
        border-radius: vw(12);
        border: none;
        outline: none;
        color: #ffffff;
        font-size: vw(36);
        cursor: pointer;

        button:hover {
          background-color: #271fb4;
        }
      }
      .getCode{
        width vw(240)
        height 80%
        color #fff
        display: flex;
        align-items: center;
        justify-content:center;
        background-color: #07c160
        border-radius vw(5)
      }
    }

    .edter {
      margin-top: vw(80);
      border: none;
    }

    .userName {
      border-top: 1px solid #d9d9d9;
    }
    .selectLogin{
      font-size:vw(28);
      margin-top:vw(30);
      text-align:center;
      color #424242
    }
  }
}
</style>
