<template>
  <div class="login">
    <div class="box">
      <div class="title">
        <img src="../assets/images/login/login.png" alt />
        <span>七分钱进件系统</span>
      </div>
      <p class="logintype">账号密码登录</p>
      <div class="item">
        <input v-model.trim="userName" type="text" placeholder="手机号码/邮箱" />
        <van-icon @click="clearName" name="close" />
      </div>
      <div class="item">
        <input v-model.trim="password" type="password" placeholder="登录密码" />
        <van-icon @click="clearPsd" name="close" />
      </div>
      <div class="item">
        <button @click="submitLogin">登录</button>
      </div>
    </div>
  </div>
</template>

<script >
/*  import {mapMutations} from 'vuex';*/
import storage from "../assets/modeljs/storage";
import { mapState } from "vuex";
import { login } from "../assets/api/interface";
import { Dialog } from "vant"; //弹窗函数，可直接调用
export default {
  name: "Login",
  data() {
    return {
      userName: null,
      password: null
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
    //判断账号密码非空
    submitLogin() {
      if (!this.userName) {
        Dialog({ message: "用户名账号不能为空" });
        return;
      }
      if (!this.password) {
        Dialog({ message: "密码不能为空" });
        return;
      }
      this.loginPost(); //登录请求
    },
    clearName() {
      this.userName = "";
    },
    clearPsd() {
      this.password = "";
    },
    async loginPost() {
      const params = {
        userName: this.userName,
        password: this.password,
        openId: this.openId,
        roleCode: this.role
      };
      console.log(params.userName);
      console.log(params.password);
      console.log(params.openId);
      console.log(params.roleCode);

      const loginData = await login.login(params);
      console.log(loginData);
      if (loginData.data.resultCode == 1) {
        Dialog({ message: "登录成功" });
        setTimeout(() => {
          if (this.role == "agent") {
            this.$router.push("Administrator");
          } else if (this.role == "salesman") {
            this.$router.push({
              name: "salesman",
              params: {
                type: "login"
              }
            });
          }
        }, 500);
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
      border-bottom: vw(1) solid #d9d9d9;

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

        &:hover {
          background-color: #271fb4;
        }
      }
    }

    .item:last-child {
      margin-top: vw(80);
      border: none;
    }
  }
}
</style>
