<template>
  <div class="incoming">
    <van-nav-bar title="结算信息" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="3" />
    <div class="stepInfo">
      <form ref="baseform">
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.compMainAcct)}">银行卡号</span>
          <!-- compMainAcct -->
          <input v-model="params.compMainAcct" placeholder="请输入银行卡号" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.compAcctBank)}">开户银行</span>
          <!--  compAcctBank -->
          <input v-model="params.compAcctBank" placeholder="请输入开户银行" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.bankProvinceName)}">开户省份</span>
          <!--  province -->
          <input v-model="params.bankProvinceName" placeholder="请输入省份" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.bankCityName)}">开户城市</span>
          <!-- city -->
          <input v-model="params.bankCityName" placeholder="请输入城市" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.branchBank)}">开户支行</span>
          <!--  branchBank -->
          <input v-model="params.branchBank" placeholder="开户支行" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.representativeName)}">开户人</span>
          <!-- representativeName -->
          <input v-model="params.representativeName" placeholder="请输入开户人" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.compMainAcctType)}">结算类型</span>
          <!--  compMainAcctType -->
          <select v-model="params.compMainAcctType">
            <option disabled value>请选择</option>
            <option value="01">企业</option>
            <option value="02">个人</option>
          </select>
        </div>
        <div class="row-img">
          <div class="stit" :class="{'active':(clickedNext&&!params.licenceForOpeningAccounts)}">
            开户许可证照片
            <span>(必须)</span>
          </div>
          <div @click="beforeUploadImg('licenceForOpeningAccounts')">
            <van-uploader :after-read="uploadImg" v-show="!params.licenceForOpeningAccounts">
              <van-button icon="photo" type="primary">上传电子签名照</van-button>
            </van-uploader>
          </div>
          <img :src="params.licenceForOpeningAccounts" v-show="params.licenceForOpeningAccounts" />
        </div>
        <div class="btn" @click="getNextStep">下一步</div>
        <div class="btn" @click="getPreStep">返回</div>
      </form>
    </div>
  </div>
</template>
<script>
import form from "@/lib/form.js";
import upload from "@/lib/upload.js";
import bankInfo from "@/lib/bankInfo.js";
import { mapState } from "vuex";
import { common } from "@/assets/api/interface";
export default {
  name: "incoming",
  components: {
    /*     Modal: () => import("@/components/modal"), */

    Step: () => import("@/components/step")
  },
  computed: {
    ...mapState({
      incoming: state => state.incoming,
      custScanCopys: state => state.custScanCopys
    })
  },
  created() {
    //this.$route.params.type
    this.params = Object.assign(this.params, this.incoming);
  },
  data() {
    return {
      clickedNext: false,
      params: {
        compMainAcct: "",
        compAcctBank: "",
        bankProvinceName: "",
        bankCityName: "",
        branchBank: "",
        representativeName: "",
        compMainAcctType: "",
        licenceForOpeningAccounts: ""
      }
    };
  },
  methods: {
    changePrepage() {
      this.$router.go(-1);
    },
    getNextStep() {
      //到下一步  签约产品
      this.clickedNext = true;
      let count = form.validParams(this.params);
      if (count == 0) {
        let fullParams = Object.assign(this.incoming, this.params);
        this.$store.commit("setincoming", fullParams);
        this.$store.commit("custScanCopys", this.custScanCopys);
        this.$router.push("sign");
      }
    },
    getPreStep() {
      //返回  法人信息
      this.$router.push("legalInfo");
    },
    beforeUploadImg(type) {
      this.uploadType = type;
    },
    uploadImg(file) {
      //图片上传
      upload.uploadImgRequest(this, file.file);
      // this.$store.dispatch(file.file);
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
</style>