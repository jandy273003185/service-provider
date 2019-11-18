<template>
  <div class="incoming">
    <van-nav-bar title="基本信息" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="1" />
    <div class="stepInfo">
      <form ref="baseform">
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.merchantAccount)}">商户账号</span>
          <input v-model="params.merchantAccount" placeholder="请输入手机号" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.custType)}">商户类型</span>
          <select v-model="params.custType">
            <option disabled value>请选择</option>
            <option value="0">个人</option>
            <option value="1">企业</option>
          </select>
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.custName)}">商户名字</span>
          <input v-model="params.custName" placeholder="请输入商户名称" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.shortName)}">商户简称</span>
          <input v-model="params.shortName" placeholder="请输入商户简称" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.contactPhone)}">客服号码</span>
          <input v-model="params.contactPhone" placeholder="请输入客服电话号码" />
        </div>
        <div class="row">
          <!-- params.custAdd -->
          <!--  province;// 省份代码
            city;// 城市代码
            country;// 区县码
          custAdd;地址.-->
          <span class="label" :class="{'active':(clickedNext&&!params.custAdd)}">商户地址</span>
          <input v-model="params.province" placeholder="请输入商户地址" />
        </div>
        <div class="row">
          <input v-model="params.custAdd" placeholder="请输入详细地址" />
        </div>
        <div class="row-img">
          <div class="stit" :class="{'active':(clickedNext&&!params.businessLicense)}">
            <!-- businessLicense -->
            营业执照照片
            <span>(必须)</span>
          </div>
          <van-uploader
            v-show="!businessLicenseUrl"
            name="businessLicense"
            :after-read="afterReadImg"
          >
            <van-button icon="photo" type="primary">上传营业执照</van-button>
          </van-uploader>
          <img :src="params.businessLicenseUrl" v-show="params.businessLicenseUrl" />
        </div>

        <div class="row">
          <span class="label">营业执照编号</span>
          <input v-model="params.businessLicense" placeholder="请输入编号" />
        </div>
        <div class="row">
          <span class="label">营业执照有效期</span>
          <!-- businessTerm -->
          <div class="data">
            <input
              placeholder="日期选择"
              v-model="params.businessTermStart"
              @focus="datepickerVisiable"
            /> -
            <input placeholder="日期选择" v-model="params.businessTermEnd" />
            <van-checkbox class="longterm" v-model="longterm">长期</van-checkbox>
          </div>
        </div>
        <van-datetime-picker
          class="datepicker"
          v-show="showDatepicker"
          type="year-month"
          :min-date="minDate"
          @confirm="confirmDate"
          @cancel="datepickerHide"
        />
        <div class="row-img">
          <div class="stit" :class="{'active':(clickedNext&&!params.shopFrontDesk)}">
            <!-- shopFrontDesk -->
            门头照照片
            <span>(必须)</span>
          </div>
          <div @click="beforeUploadImg('shopFrontDesk')">
            <van-uploader :after-read="uploadImg" v-show="!params.shopFrontDesk">
              <van-button icon="photo" type="primary">上传门头照照片</van-button>
            </van-uploader>
          </div>

          <img :src="params.shopFrontDesk" v-show="params.shopFrontDesk" />
        </div>
        <div class="row-img">
          <div class="stit" :class="{'active':(clickedNext&&!params.shopInterior)}">
            <!-- shopInterior -->
            店内照
            <span>(必须)</span>
          </div>
          <div @click="beforeUploadImg('shopInterior')">
            <van-uploader ref="shopInterior" :after-read="uploadImg" v-show="!params.shopInterior">
              <van-button icon="photo" type="primary">上传店内照片</van-button>
            </van-uploader>
          </div>

          <img :src="params.shopInterior" v-show="params.shopInterior" />
        </div>
        <div class="row-img">
          <!-- specialBusiness -->
          <div class="stit" :class="{'active':(clickedNext&&!params.specialBusiness)}">特殊行业资质照</div>
          <div @click="beforeUploadImg('specialBusiness')">
            <van-uploader :after-read="uploadImg" v-show="!params.specialBusiness">
              <van-button icon="photo" type="primary">上传特殊行业资质照</van-button>
            </van-uploader>
          </div>
          <img :src="params.specialBusiness" v-show="params.specialBusiness" />
        </div>
        <div class="row-img">
          <div
            class="stit"
            :class="{'active':(clickedNext&&!params.electronicSignaturePhoto)}"
          >电子签名照</div>
          <!-- electronicSignaturePhoto -->
          <div @click="beforeUploadImg('electronicSignaturePhoto')">
            <van-uploader :after-read="uploadImg" v-show="!params.electronicSignaturePhoto">
              <van-button icon="photo" type="primary">上传电子签名照</van-button>
            </van-uploader>
          </div>
          <img :src="params.electronicSignaturePhoto" v-show="params.electronicSignaturePhoto" />
        </div>
        <div class="row-img">
          <div class="stit" :class="{'active':(clickedNext&&!params.otherPhoto1)}">其他资料照</div>
          <!-- otherPhoto1 2 -->
          <div @click="beforeUploadImg('otherPhoto1')">
            <van-uploader :after-read="uploadImg" v-show="!params.otherPhoto1">
              <van-button icon="photo" type="primary">其他资料照</van-button>
            </van-uploader>
          </div>
          <img :src="params.otherPhoto1" v-show="params.otherPhoto1" />
          <div @click="beforeUploadImg('otherPhoto2')">
            <van-uploader :after-read="uploadImg" v-show="!params.otherPhoto2">
              <van-button icon="photo" type="primary">其他资料照</van-button>
            </van-uploader>
          </div>
          <img :src="params.otherPhoto2" v-show="params.otherPhoto2" />
        </div>
        <div class="btn" @click="getNextStep">下一步</div>
      </form>
    </div>
  </div>
</template>
<script>
import form from "@/lib/form.js";
import upload from "@/lib/upload.js"
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
      incoming: state => state.incoming
    })
  },
  created() {
    this.params=Object.assign(this.params,this.incoming);
    //this.$route.params.type
    /*  Vue.set(this.obj, "password", "num"); */
  },
  data() {
    return {
      uploadType: "",
      clickedNext: false,
      longterm: false,
      minDate: new Date(),
      showDatepicker: false,
      businessLicenseUrl: "",
      params: {
        merchantAccount: "",
        custType: "",
        custName: "",
        shortName: "",
        contactPhone: "",
        province: "江西省", // 省份代码
        city: "赣州市", // 城市代码
        country: "章贡区", // 区县码
        custAdd: "经济开发区", //地址
        businessLicense: "", //营业执照编号
        businessTermStart: "", //有效期
        businessTermEnd: "",
        shopFrontDesk: "", //门头照
        shopInterior: "", //店内照
        specialBusiness: "", //特殊行业照
        electronicSignaturePhoto: "", //电子签名照
        otherPhoto1: "", //其他资料照
        otherPhoto2: ""
      },
      custScanCopys: []
    };
  },
  methods: {
    changePrepage() {
      //返回上一页
      this.$router.go(-1);
    },
    getNextStep() {
      //到下一步 法人信息
      this.clickedNext = true;
      let count = form.validParams(this.params);
      if (count == 0) {
        let fullParams = Object.assign(this.incoming, this.params);
        this.$store.commit("setincoming", fullParams);
        console.log(this.custScanCopys);
        this.$store.commit("custScanCopys", this.custScanCopys);
        this.$router.push("legalInfo");
      }
    },
    afterReadImg(file) {
      this.getImgInfo(file.content);
    },
    async getImgInfo(file) {
      //优图识别
      const params = {
        str: file,
        flag: "businessPhoto" //营业执照
      };
      const info = await common.getImgInfo(params);
      const imgUrl = info.data.uri + "/" + info.data.url;
      let businessLicense = info.data.businessLicense;
      let businessTermEnd = info.data.businessTermEnd;
      let businessTermStart = info.data.businessTermStart;
      this.params.businessLicense = businessLicense;
      this.params.businessTermEnd = businessTermEnd;
      this.params.businessTermStart = businessTermStart;
      this.businessLicenseUrl = info.data.uri + info.data.url;
       let imgObj = {
            certifyType:'02',//营业执照
            scanCopyPath:info.data.imagePath,
            certifyNo: ""
          };
          this.custScanCopys.push(imgObj);
    },
    beforeUploadImg(type) {
      this.uploadType = type;
    },
    uploadImg(file) {
      //图片上传
      upload.uploadImgRequest(this,file.file);
    },
    
    datepickerVisiable() {
      this.showDatepicker = true;
    },
    datepickerHide() {
      this.showDatepicker = false;
    },
    confirmDate(e) {
      this.showDatepicker = false;
      console.log(e);
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
</style>