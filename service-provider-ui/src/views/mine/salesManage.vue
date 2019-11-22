<template>
  <div class="sales">
    <van-nav-bar title="业务员管理" left-text="返回" left-arrow @click-left="changePrepage" />
    <van-row class="row">
      <van-col span="6">姓名</van-col>
      <van-col span="10">账号</van-col>
      <van-col span="5"></van-col>
      <van-col span="3"></van-col>
    </van-row>
    <van-row class="row" v-for="(item,idx) in saleList" :key="idx">
      <van-col span="6">{{item.userName}}</van-col>
      <van-col span="10">{{item.userPhone}}</van-col>
      <van-col span="5" @click="resetPwd(item.custId,item.salesmanId)">重置密码</van-col>
      <van-col
        span="3"
        v-if="item.status==1"
        @click="deleteSale(item.custId,item.salesmanId,item.status)"
      >冻结</van-col>
      <van-col
        span="3"
        v-if="item.status==0"
        @click="undeleteSale(item.custId,item.salesmanId,item.status)"
      >解冻</van-col>
    </van-row>
    <van-row class="row" v-show="adding">
      <van-col span="6">
        <input autofocus v-model="inpName" placeholder="请输入名字" />
      </van-col>
      <van-col span="10">
        <input v-model="inpAccount" placeholder="请输入手机号" />
      </van-col>
      <van-col span="5">
        <span @click="saveSales">保存</span>
      </van-col>
      <van-col span="3">
        <span @click="cancelAdding">取消</span>
      </van-col>
    </van-row>
    <div class="add" v-show="!adding" @click="addSales">
      <van-icon name="plus" />
    </div>
  </div>
</template>
<script>
import { adminIndex, common } from "@/assets/api/interface";
import { Dialog } from "vant";

export default {
  name: "sales",
  components: {

  },
  computed: {},
  created() {
    this.getInitList();
  },
  data() {
    return {
      adding: false,
      saleList: [],
      inpName: "",
      inpAccount: "",
    };
  },
  methods: {
    async getInitList() {
      let list = await adminIndex.searchSales({
        custId: ""
      });
  console.log(list);
      this.saleList = list.data.resultMsg;
    },
    changePrepage() {
      //;返回
      this.$router.go(-1);
    },
    addSales() {
      //添加业务员
      this.adding = true;
    },
    cancelAdding() {
      //取消业务员添加
      this.adding = false;
    },
    async resetPwd(custId, id) {
      //重置密码
      let res = await common.resetPwd({
        custId: custId,
        id: id
      });
      if (res.data.resultCode == "1") {
        console.log("重置密码成功");
        Dialog({message: "重置密码成功"});
      }
    },
    deleteSale(custId, id, status) {
      //删除业务员
      Dialog.confirm({
        title: "提示",
        message: "是否确定冻结账号"
      })
        .then(() => {
          // on confirm
          common.updateSales({
              custId: custId,
              id: id,
              status: 0
            })
            .then(res => {
              this.getInitList();
              console.log("冻结");
            });
        })
        .catch(() => {
          // on cancel
        });
    },
    undeleteSale(custId, id, status) {
      //解冻业务员
      Dialog.confirm({
        title: "提示",
        message: "是否确定解冻账号"
      })
        .then(() => {
          // on confirm
          common.updateSales({
              custId: custId,
              id: id,
              status: 1
            })
            .then(res => {
              console.log(res);
              this.getInitList();
              console.log("解冻");
            });
        })
        .catch(() => {
          // on cancel
        });
    },
    async saveSales() {
      let res = await common.insertSales({
        userName: this.inpName,
        password: "123456",
        userPhone: this.inpAccount,
        custId: this.$store.state.userId,//管理员的userId
        status: 1
      });
      console.log(res);
      if (res.data.resultCode == "1") {
        this.getInitList();
        Dialog({message: "添加业务员成功"});
        this.adding = false;
      }
    },


  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/mine.scss";
</style>