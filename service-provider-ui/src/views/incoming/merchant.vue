<template>
  <div class="incoming">
    <van-nav-bar title="结算信息" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="3" />
    <div class="stepInfo">
      <div ref="baseform">
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.compMainAcct)}">结算账号</span>
          <!-- compMainAcct -->
          <input
            v-model="params.compMainAcct"
            placeholder="请输入结算账号"
            @blur="getBankName(params.compMainAcct)"
          />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.compAcctBank)}">开户银行</span>
          <!--  compAcctBank -->
          <input v-model="params.compAcctBank" disabled placeholder="请输入开户银行" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.bankProvinceName)}">开户省份</span>
          <!--  province -->
          <input
            v-model="params.bankProvinceShow"
            placeholder="请选择省份"
            readonly
            @click="getInitAddress"
          />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.bankCityName)}">开户城市</span>
          <!-- city -->
          <input v-model="params.bankCityShow" placeholder="请选择城市" readonly @click="getCity" />
        </div>
        <van-picker
          v-if="provincepicker"
          show-toolbar
          title="省份"
          :columns="provinceList"
          value-key="bankProvinceName"
          @cancel="onCancelAdd"
          @confirm="onConfirmProvince"
        />
        <van-picker
          v-if="citypicker"
          show-toolbar
          title="城市"
          :columns="cityList"
          value-key="bankCityName"
          @cancel="onCancelAdd"
          @confirm="onConfirmCity"
        />
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
          <!-- v-show="!params.licenceForOpeningAccounts" -->
          <div @click="beforeUploadImg('licenceForOpeningAccounts')">
            <van-uploader
              :after-read="uploadImg"
              v-model="photos.licenceForOpeningAccounts"
              :max-count="1"
              preview-size="auto"
            >
              <van-button icon="photo" type="primary">上传开户许可证照</van-button>
            </van-uploader>
          </div>
          <!--  <img :src="params.licenceForOpeningAccounts" v-show="params.licenceForOpeningAccounts" /> -->
        </div>
        <div class="btn" @click="getNextStep">下一步</div>
        <div class="btn back" @click="getPreStep">返回</div>
      </div>
    </div>
  </div>
</template>
<script>
import form from "@/lib/form.js";
import upload from "@/lib/upload.js";
import util from "@/lib/util.js";
import bankInfo from "@/lib/bankInfo.js";
import { mapState } from "vuex";
import { common } from "@/assets/api/interface";
export default {
  name: "incoming",
  components: {
    /*     Modal: () => import("@/components/modal"), */

    Step: () => import("@/components/step")
  },
  data() {
    return {
      clickedNext: false,
      provincepicker: false,
      citypicker: false,
      provinceList: [],
      cityList: [],
      provinceId: "",
      cityId: "",
      photos: {
        licenceForOpeningAccounts: []
      },
      params: {
        compMainAcct: "",
        compAcctBank: "",
        bankProvinceName: "", //省
        bankProvinceShow: "",
        bankCityName: "", //市
        bankCityShow: "",
        branchBank: "",
        representativeName: "",
        compMainAcctType: "",
        licenceForOpeningAccounts: ""
      }
    };
  },
  computed: {
    ...mapState(["checkedState", "incoming", "incomingReturn", "savephotos"])
  },
  created() {
    //this.$route.params.type
    this.params = Object.assign(this.params, this.incoming);
    this.photos = this.savephotos;
    if (this.checkedState == "corvidae") {
      console.log("corvidae");
      let custInfo = this.incomingReturn.custInfo;
      let photos = this.incomingReturn.custScanInfoList;
      let urlHead = this.incomingReturn.uri + "" + this.incomingReturn.url;
      util.getPhotos(this, urlHead, photos);
      let bankProvinces = this.incomingReturn.bankProvinces[0];
      let params = {
        compMainAcct: custInfo.compMainAcct,
        compAcctBank: custInfo.compAcctBank,
        bankProvinceName: bankProvinces.bankProvinceId, //省
        bankProvinceShow: bankProvinces.bankProvinceName,
        bankCityName: bankProvinces.bankCityId, //市
        bankCityShow: bankProvinces.bankCityName,
        branchBank: custInfo.branchBank,
        representativeName: custInfo.representativeName,
        compMainAcctType: custInfo.compMainAcctType
      };
      this.params = Object.assign(this.params, params);
    }
  },

  methods: {
    changePrepage() {
      this.$router.go(-1);
    },
    getBankName(cardNo) {
      //获取银行名称
      if (cardNo && cardNo.length > 0) {
        let bank = bankInfo(cardNo);
        if (bank) {
          this.params.compAcctBank = bank.bankName;
          console.log(this.params.compAcctBank);
        } else {
          this.params.compAcctBank = "";
        }
      }
    },
    async getInitAddress() {
      //获取省份
      this.provincepicker = true;
      let res = await common.bankAddress();
      this.provinceList = res.data.resultMsg;
    },
    async getCity() {
      if (this.params.bankProvinceName) {
        this.citypicker = true;
        let res = await common.bankAddress({
          bankProvinceId: this.params.bankProvinceName
        });
        console.log(res);
        this.cityList = res.data.resultMsg;
      }
    },
    onCancelAdd() {
      this.provincepicker = false;
      this.citypicker = false;
      this.blockpicker = false;
    },
    async onConfirmProvince(value, index) {
      //确认省
      console.log("确认省份");
      this.params.bankProvinceName = value.bankProvinceId;
      this.params.bankProvinceShow = value.bankProvinceName;
      console.log(value.bankProvinceId);
      this.provincepicker = false;
    },

    async onConfirmCity(value, index) {
      //确认市
      console.log("确认市");
      this.params.bankCityName = value.bankCityId;
      this.params.bankCityShow = value.bankCityName;
      this.citypicker = false;
    },
    getNextStep() {
      //到下一步  签约产品
      this.clickedNext = true;
      let count = form.validParams(this.params);
      if (count == 0) {
        let fullParams = Object.assign(this.incoming, this.params);
        this.$store.commit("setincoming", fullParams);
        let incomingReturn = this.incomingReturn;
        let custInfo = this.incomingReturn.custInfo || {};
        let bankProvinces = [
          {
            bankProvinceId: this.params.bankProvinceName,
            bankProvinceName: this.params.bankProvinceShow,
            bankCityId: this.params.bankCityName,
            bankCityName: this.params.bankCityShow
          }
        ];
        let all = Object.assign(custInfo, fullParams);
        incomingReturn.custInfo = all;
        incomingReturn.bankProvinces = bankProvinces;
        this.$store.commit("setincomingReturn", incomingReturn);
        this.$store.commit("setPhotos", this.photos);
        this.$store.commit("setCheckedState", this.checkedState);
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