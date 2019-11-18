<template>
  <div class="incoming">
    <van-nav-bar title="法人信息" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="2" />
    <form class="stepInfo" ref="baseform">
      <div class="row-img">
        <div class="stit" :class="{'active':(clickedNext&&(!params.identityCardFront||!params.identityCardReverse))}">
          法人身份证照片
          <span>(必须)</span>
        </div>
        <div @click="beforeUploadImg('certAttribute1')">
          <van-uploader :after-read="afterReadImg" v-show="!params.identityCardFront">
            <van-button icon="photo" type="primary">身份证正面照</van-button>
          </van-uploader>
        </div>
        <img :src="params.identityCardFront" v-show="params.identityCardFront" />
        <div @click="beforeUploadImg('certAttribute2')">
          <van-uploader :after-read="afterReadImg" v-show="!params.identityCardReverse">
            <van-button icon="photo" type="primary">身份证反面照</van-button>
          </van-uploader>
        </div>
        <img :src="params.identityCardFront" v-show="params.identityCardReverse" />
      </div>
      <div class="row">
        <span class="label" :class="{'active':(clickedNext&&!params.representativeName)}">法人名字</span>
        <input v-model="params.representativeName" placeholder="请输入法人名字" />
      </div>
      <div class="row">
        <span class="label" :class="{'active':(clickedNext&&!params.representativeCertNo)}">法人身份证号</span>
        <input v-model="params.representativeCertNo" placeholder="请输入法人身份证号" />
        <!-- certifyNo  -->
      </div>
      <div class="row">
        <span class="label" :class="{'active':(clickedNext&&!params.idTermStart&&!params.idTermEnd)}">身份证有效期</span>
        <div class="data">
          <input placeholder="日期选择" v-model="params.idTermStart" @focus="datepickerVisiable" /> -
          <input placeholder="日期选择" v-model="params.idTermEnd" />
          <van-checkbox class="longterm">长期</van-checkbox>
        </div>
        <!--  idTermEnd -->
      </div>
      <div class="row">
        <span class="label" :class="{'active':(clickedNext&&!params.contactName)}">联系人名字</span>
        <input v-model="params.contactName" placeholder="请输入联系人名字" />
      </div>
      <div class="row">
        <span class="label" :class="{'active':(clickedNext&&!params.contactMobile)}">联系人手机</span>
        <input v-model="params.contactMobile" placeholder="请输入联系人手机" />
      </div>
      <div class="btn" @click="getNextStep">下一步</div>
      <div class="btn" @click="getPreStep">返回</div>
    </form>
  </div>
</template>
<script>
import form from "@/lib/form.js";
import { mapState } from "vuex";
import { common } from "@/assets/api/interface";
/* import minify from "@/lib/compressFile.js";
import util from "@/lib/util.js";
import API from "@/lib/api.js"; */
export default {
  name: "incoming",
  components: {
    /*     Modal: () => import("@/components/modal"), */

    Step: () => import("@/components/step")
  },
  computed: {
      ...mapState({
      incoming: state => state.incoming,
      custScanCopys:state => state.custScanCopys
    })
  },
  created() {
    //this.$route.params.type
     this.params=Object.assign(this.params,this.incoming);
     console.log(this.custScanCopys);
  },
  data() {
    return {
      clickedNext:false,
      indentifyType: "", //识别类型
      params: {
        identityCardFront: "",
        identityCardReverse: "",
        representativeName: "", //法人名字
        representativeCertNo: "", //法人身份证号
        idTermStart: "",
        idTermEnd: "",
        contactName: "",
        contactMobile: ""
      },
    };
  },
  methods: {
    changePrepage() {
      this.$router.go(-1);
    },
    getNextStep() {
      //到下一步 法人信息
       this.clickedNext = true;
      let count = form.validParams(this.params);
      if (count == 0) {
      let fullParams=Object.assign(this.incoming,this.params) 
      this.$store.commit("setincoming", fullParams);
      this.$store.commit("custScanCopys", this.custScanCopys);
      this.$router.push("merchant");
      }
    },
    getPreStep() {
      //返回
      this.$router.push("baseInfo");
    },
    submitBase() {
      console.log(this.baseInfo);
      //提交基本信息
    },
    datepickerVisiable() {},
    beforeUploadImg(type) {
      //识别类型
      this.indentifyType = type;
    },
    afterReadImg(file) {
      this.getImgInfo(file.content);
    },
    async getImgInfo(file) {
      //优图识别
      const params = {
        str: file,
        flag: this.indentifyType 
      };
      const info = await common.getImgInfo(params);
      const imgUrl = info.data.uri + "/" + info.data.url;
      if (this.indentifyType == "certAttribute1") {
        this.params.identityCardFront = imgUrl;
        this.params.representativeName = info.data.cardName;
        this.params.representativeCertNo = info.data.cardId;
        let imgObj = {
            certifyType:'00',//身份证正面
            scanCopyPath:info.data.imagePath,
            certifyNo: ""
          };
          this.custScanCopys.push(imgObj);
      }
      if (this.indentifyType == "certAttribute2") {
        this.params.identityCardReverse = imgUrl;
        let arr = info.data.cardValidDate.split("-");
        console.log(arr);
        this.params.idTermStart = arr[0];
        this.params.idTermEnd = arr[1];
        let imgObj = {
            certifyType:'16',//身份证反面
            scanCopyPath:info.data.imagePath,
            certifyNo: ""
          };
          this.custScanCopys.push(imgObj);
      }

      console.log(info);
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
</style>