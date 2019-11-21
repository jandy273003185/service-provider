<template>
  <div class="incoming">
    <van-nav-bar title="法人信息" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="2" />
    <div class="stepInfo" ref="baseform">
      <div class="row-img">
        <div
          class="stit"
          :class="{'active':(clickedNext&&(!params.identityCardFront||!params.identityCardReverse))}"
        >
          法人身份证照片
          <span>(必须)</span>
        </div>
        <div class="img-col" @click="beforeUploadImg('certAttribute1')">
          <van-uploader
            v-model="photos.identityCardFront"
            :after-read="afterReadImg"
            :max-count="1"
            preview-size="auto"
          >
            <!-- v-show="!params.identityCardFront" -->
            <van-button icon="photo" type="primary">身份证正面照</van-button>
          </van-uploader>
        </div>
        <!--  <img :src="params.identityCardFront" v-show="params.identityCardFront" /> -->
        <div class="img-col" @click="beforeUploadImg('certAttribute2')">
          <van-uploader
            :after-read="afterReadImg"
            v-model="photos.identityCardReverse"
            :max-count="1"
            preview-size="auto"
          >
            <!--  v-show="!params.identityCardReverse" -->
            <van-button icon="photo" type="primary">身份证反面照</van-button>
          </van-uploader>
        </div>
        <!-- <img :src="params.identityCardFront" v-show="params.identityCardReverse" /> -->
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
        <span
          class="label"
          :class="{'active':(clickedNext&&!params.idTermStart&&!params.idTermEnd)}"
        >身份证有效期</span>
        <div class="data">
          <input
            placeholder="日期选择"
            v-model="params.idTermStart"
            readonly
            @click="datepickerVisiable('idTermStart')"
          /> -
          <input
            placeholder="日期选择"
            v-model="params.idTermEnd"
            readonly
            @click="datepickerVisiable('idTermEnd')"
          />
        </div>
        <!--  idTermEnd -->
      </div>
      <van-datetime-picker
        class="datepicker"
        v-show="showDatepicker"
        type="year-month"
        :max-date="maxDate"
        @confirm="confirmDate"
        @cancel="datepickerHide"
      />
      <div class="row">
        <span class="label" :class="{'active':(clickedNext&&!params.contactName)}">联系人名字</span>
        <input v-model="params.contactName" placeholder="请输入联系人名字" />
      </div>
      <div class="row">
        <span class="label" :class="{'active':(clickedNext&&!params.contactMobile)}">联系人手机</span>
        <input v-model="params.contactMobile" placeholder="请输入联系人手机" />
      </div>
      <div class="btn" @click="getNextStep">下一步</div>
      <div class="btn back" @click="getPreStep">返回</div>
    </div>
  </div>
</template>
<script>
import form from "@/lib/form.js";
import util from "@/lib/util.js";
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
  data() {
    return {
      clickedNext: false,
      indentifyType: "", //识别类型
      maxDate: new Date(),
      showDatepicker: false,
      dateType: "",
      photos: {
        identityCardFront: [],
        identityCardReverse: []
      },
      params: {
        identityCardFront: "",
        identityCardReverse: "",
        representativeName: "", //法人名字
        representativeCertNo: "", //法人身份证号
        idTermStart: "",
        idTermEnd: "",
        contactName: "",
        contactMobile: ""
      }
    };
  },
  computed: {
    ...mapState(["savephotos", "checkedState", "incoming", "incomingReturn"])
  },
  created() {
    //this.$route.params.type
    this.params = Object.assign(this.params, this.incoming);
    this.photos = this.savephotos;
    if (this.checkedState == "corvidae") {
      let custInfo = this.incomingReturn.custInfo;
      let photos = this.incomingReturn.custScanInfoList;
      let urlHead = this.incomingReturn.uri + "" + this.incomingReturn.url;
      util.getPhotos(this, urlHead, photos);
      let params = {
        representativeName: custInfo.representativeName, //法人名字
        representativeCertNo: custInfo.representativeCertNo, //法人身份证号
        idTermStart: custInfo.idTermStart,
        idTermEnd: custInfo.idTermEnd,
        contactName: custInfo.contactName,
        contactMobile: custInfo.contactMobile
      };
      this.params = Object.assign(this.params, params);
    }
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
        let fullParams = Object.assign(this.incoming, this.params);
        this.$store.commit("setincoming", fullParams);
        let incomingReturn = this.incomingReturn;
        let custInfo = this.incomingReturn.custInfo || {};
        let all = Object.assign(custInfo, fullParams);
        incomingReturn.custInfo = all;
        this.$store.commit("setincomingReturn", incomingReturn);
        this.$store.commit("setPhotos", this.photos);
        this.$store.commit("setCheckedState", this.checkedState);
        this.$router.push("merchant");
      }
    },
    getPreStep() {
      //返回
      this.$router.push("baseInfo");
    },
    beforeUploadImg(type) {
      //识别类型
      this.indentifyType = type;
    },
    afterReadImg(file) {
      this.getImgInfo(file.content);
    },
    datepickerVisiable(type) {
      this.dateType = type;
      this.showDatepicker = true;
    },
    datepickerHide() {
      this.showDatepicker = false;
    },
    confirmDate(e) {
      let getData = util.timeFormat(e);
      this.params[this.dateType] = getData;
      this.showDatepicker = false;
    },
    async getImgInfo(file) {
      //优图识别
      this.$toast.loading({
        message: "加载中...",
        forbidClick: true,
        duration: 0
      });
      const params = {
        str: file,
        flag: this.indentifyType
      };
      const info = await common.getImgInfo(params);
      const imgUrl = info.data.uri + "/" + info.data.url;
      this.$toast.clear();
      if (this.indentifyType == "certAttribute1") {
        if (info.data) {
          this.photos.identityCardFront = [{ url: imgUrl }];
          this.params.identityCardFront = imgUrl;
          this.params.representativeName = info.data.cardName;
          this.params.representativeCertNo = info.data.cardId;
        } else {
          this.photos.identityCardFront = [{ url:'' }];
          this.params.identityCardFront ='';
          this.$toast("身份证正面信息无法识别！");
        }
      }
      if (this.indentifyType == "certAttribute2") {
        if (info.data.cardValidDate) {
          this.params.identityCardReverse = imgUrl;
          this.photos.identityCardReverse = [{ url: imgUrl }];
          let arr = info.data.cardValidDate.split("-");
          this.params.idTermStart = arr[0];
          this.params.idTermEnd = arr[1];
        } else {
          this.params.identityCardReverse = '';
          this.photos.identityCardReverse = [{ url: '' }];
          this.$toast("身份证反面信息无法识别！");
        }
      }
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
</style>