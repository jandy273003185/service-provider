<template>
  <div class="incoming baseInfo">
    <van-nav-bar title="基本信息" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="1" />
    <div class="stepInfo">
      <div ref="baseform">
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.merchantAccount)}">商户账号</span>
          <input :readonly="pagetype=='corvidae'" v-model="params.merchantAccount" placeholder="请输入手机号" />
        </div>
        <div class="row">
          <span class="label" :class="{'active':(clickedNext&&!params.custType)}">商户类型</span>
          <select v-model="params.custType">
            <option disabled value="">请选择</option>
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
          <span class="label" :class="{'active':(clickedNext&&!params.custAdd)}">商户地址</span>
          <input
            class="address"
            v-model="params.provinceName"
            placeholder="选择省份"
            readonly
            @click="getInitAddress"
          />
          <input
            class="address"
            v-model="params.cityName"
            placeholder="选择市"
            @click="getTapCity"
            readonly
          />
          <input
            class="address"
            v-model="params.countryName"
            placeholder="选择区"
            @click="getTapCountry"
            readonly
          />
        </div>
        <van-picker
          v-if="provincepicker"
          show-toolbar
          title="省份"
          :columns="provinceList"
          value-key="provinceName"
          @cancel="onCancelAdd"
          @confirm="onConfirmProvince"
        />
        <van-picker
          v-if="citypicker"
          show-toolbar
          title="城市"
          :columns="cityList"
          value-key="cityName"
          @cancel="onCancelAdd"
          @confirm="onConfirmCity"
        />
        <van-picker
          v-if="blockpicker"
          show-toolbar
          title="区/县"
          :columns="blockList"
          value-key="areaName"
          @cancel="onCancelAdd"
          @confirm="onConfirmBlock"
        />
        <div class="row">
          <input v-model="params.custAdd" placeholder="请输入详细地址" />
        </div>
        <div class="row-img">
          <div class="stit" :class="{'active':(clickedNext&&!params.businessLicense)}">
            营业执照照片
            <span>(必须)</span>
          </div>
          <van-uploader
            name="businessLicense"
            :after-read="afterReadImg"
            :max-count="1"
            v-model="photos.businessLicense"
            preview-size="auto"
          >
            <van-button icon="photo" type="primary">上传营业执照</van-button>
          </van-uploader>
        </div>

        <div class="row">
          <span class="label">营业执照编号</span>
          <input v-model="params.businessLicense" placeholder="请输入编号" />
        </div>
        <div class="row">
          <span class="label">营业执照有效期</span>
          <div class="data">
            <input
              placeholder="开始日期"
              v-model="params.businessTermStart"
              readonly
              @click="datepickerVisiable('businessTermStart')"
            /> -
            <input
              placeholder="截止日期"
              v-model="params.businessTermEnd"
              readonly
              @click="datepickerVisiable('businessTermEnd')"
            />
          </div>
        </div>
        <van-datetime-picker
          class="datepicker"
          v-show="showDatepicker"
          type="year-month"
          :min-date="minDate"
          :max-date="maxDate"
          @confirm="confirmDate"
          @cancel="datepickerHide"
        />

        <div class="row-img">
          <div class="stit" :class="{'active':(clickedNext&&!params.shopFrontDesk)}">
            门头照照片
            <span>(必须)</span>
          </div>
          <div @click="beforeUploadImg('shopFrontDesk')">
            <van-uploader
              :after-read="uploadImg"
              v-model="photos.shopFrontDesk"
              :max-count="1"
              preview-size="auto"
            >
              <van-button icon="photo" type="primary">上传门头照照片</van-button>
            </van-uploader>
          </div>
        </div>
        <div class="row-img" :class="{'active':(clickedNext&&!params.shopInterior)}">
          <div class="stit" :class="{'active':(clickedNext&&!params.shopInterior)}">
            店内照
            <span>(必须)</span>
          </div>
          <div @click="beforeUploadImg('shopInterior')">
            <van-uploader
              ref="shopInterior"
              v-model="photos.shopInterior"
              :after-read="uploadImg"
              :max-count="1"
              preview-size="auto"
            >
              <van-button icon="photo" type="primary">上传店内照片</van-button>
            </van-uploader>
          </div>
        </div>
        <div class="row-img">
          <!-- specialBusiness -->
          <div class="stit" :class="{'active':(clickedNext&&!params.specialBusiness)}">特殊行业资质照</div>
          <div @click="beforeUploadImg('specialBusiness')">
            <van-uploader
              :after-read="uploadImg"
              v-model="photos.specialBusiness"
              :max-count="1"
              preview-size="auto"
            >
              <van-button icon="photo" type="primary">上传特殊行业资质照</van-button>
            </van-uploader>
          </div>
        </div>
        <div class="row-img">
          <div
            class="stit"
            :class="{'active':(clickedNext&&!params.electronicSignaturePhoto)}"
          >电子签名照</div>
          <div @click="beforeUploadImg('electronicSignaturePhoto')">
            <van-uploader
              v-model="photos.electronicSignaturePhoto"
              :after-read="uploadImg"
              :max-count="1"
              preview-size="auto"
            >
              <van-button icon="photo" type="primary">上传电子签名照</van-button>
            </van-uploader>
          </div>
        </div>
        <div class="row-img">
          <div class="stit">其他资料照</div>
          <div class="img-col" @click="beforeUploadImg('otherPhoto1')">
            <van-uploader
              v-model="photos.otherPhoto1"
              :after-read="uploadImg"
              :max-count="1"
              preview-size="auto"
            >
              <van-button icon="photo" type="primary">其他资料照</van-button>
            </van-uploader>
          </div>
          <div class="img-col" @click="beforeUploadImg('otherPhoto2')">
            <van-uploader
              :after-read="uploadImg"
              v-model="photos.otherPhoto2"
              :max-count="1"
              preview-size="auto"
            >
              <van-button icon="photo" type="primary">其他资料照</van-button>
            </van-uploader>
          </div>
        </div>
        <div class="btn" @click="getNextStep">下一步</div>
      </div>
    </div>
  </div>
</template>
<script>
import form from "@/lib/form.js";
import util from "@/lib/util.js";
import upload from "@/lib/upload.js";
import { mapState } from "vuex";
import { common, incoming } from "@/assets/api/interface";
export default {
  name: "baseInfo",
  components: {
    Step: () => import("@/components/step")
  },
  data() {
    return {
      pagetype: "",
      provincepicker: false,
      citypicker: false,
      blockpicker: false,
      provinceList: [],
      cityList: [],
      blockList: [],
      provinceId: "",
      cityId: "",
      uploadType: "",
      clickedNext: false,
      longterm: false,
      minDate: new Date(2000, 1, 1),
      maxDate: new Date(2029, 1, 1),
      showDatepicker: false,
      dateType: "",
      photos: {
        businessLicense: [], //营业执照
        shopFrontDesk: [], //门头照
        shopInterior: [], //店内照
        specialBusiness: [], //特殊行业照
        electronicSignaturePhoto: [], //电子签名照
        otherPhoto1: [], //其他资料照 1
        otherPhoto2: [] //其他资料照 2
      },
      params: {
        merchantAccount: "",
        custType: "",
        custName: "",
        shortName: "",
        contactPhone: "",
        province: "", // 省份代码
        provinceName: "",
        city: "", // 城市代码
        cityName: "",
        country: "", // 区县码
        countryName: "",
        custAdd: "", //地址
        businessLicense: "", //营业执照编号
        businessTermStart: "", //有效期
        businessTermEnd: "",
        shopFrontDesk: "", //门头照
        shopInterior: "", //店内照
        specialBusiness: "", //特殊行业照
        electronicSignaturePhoto: "", //电子签名照
      }
    };
  },
  computed: {
    ...mapState(["incoming", "savephotos", "incomingReturn", "custId"])
  },
  created() {
    let type =this.$route.params.type;// 
    this.photos = Object.assign({}, this.savephotos);
    if (type) {
      this.pagetype = type;
      this.$store.commit("setCheckedState", type);
      if (type == "new") {
        this.params = {};
      }
      if (type == "corvidae") {
        //待完善
        this.getIncomingInfo();
      }
    } else {
      this.params = Object.assign(this.params, this.incoming);
    }
  },

  methods: {
    changePrepage() {
      //返回上一页
      this.$router.go(-1);
    },
    getNextStep() {
      //到下一步 法人信息
      this.clickedNext = true;
      console.log(this.params);
      let count = form.validParams(this, this.params);
      if (count == 0) {
        let fullParams = Object.assign(this.incoming, this.params);
        console.log(fullParams);
        this.$store.commit("setincoming", fullParams);
/*         let incomingReturn = this.incomingReturn;
        console.log(incomingReturn);
        let custInfo = this.incomingReturn.custInfo || {};
        let all = Object.assign(custInfo, fullParams);
        incomingReturn.custInfo = all;
        this.$store.commit("setincomingReturn", incomingReturn); */
        this.$store.commit("setPhotos", this.photos);
        this.$router.push("legalInfo");
      }
    },
    async getIncomingInfo() {
      //获取之前提交进件数据
      this.$toast.loading({
        message: "加载中..",
        forbidClick: true,
        duration: 0
      });
      let res = await incoming.getIncoming({ custId:this.custId });
      this.$toast.clear();
      this.$store.commit("setincomingReturn", res.data.resultMsg);
      let custInfo = res.data.resultMsg.custInfo;
      let provinces = res.data.resultMsg.provinces[0];
      let params = {
        merchantAccount: custInfo.merchantAccount,
        custType: custInfo.custType,
        custName: custInfo.custName,
        shortName: custInfo.shortName,
        contactPhone: custInfo.contactPhone,
        province: provinces.provinceId, // 省份代码
        provinceName: provinces.provinceName,
        city: provinces.cityId, // 城市代码
        cityName: provinces.cityName,
        country: provinces.areaId, // 区县码
        countryName: provinces.areaName,
        custAdd: custInfo.custAdd, //地址
        businessLicense: custInfo.businessLicense, //营业执照编号
        businessTermStart: custInfo.businessTermStart, //有效期
        businessTermEnd: custInfo.businessTermEnd,
      };
      this.params =Object.assign({},params);
      let photos = res.data.resultMsg.custScanInfoList;
      let urlHead = res.data.resultMsg.uri + "" + res.data.resultMsg.url;
      util.getPhotos(this, urlHead, photos);
    },
    async getInitAddress() {
      //获取省份
      this.provincepicker = true;
      let res = await common.getAddress();
      this.provinceList = res.data.resultMsg;
    },
    async getTapCity() {
      if (this.params.province) {
        let res = await common.getAddress({
          provinceId: this.params.province
        });
        this.cityList = res.data.resultMsg; 
        this.citypicker = true;
      } else {
        this.$toast("请先选择省份");
      }
    },
   async getTapCountry() {
      if (this.params.province && this.params.city) {
        let res = await common.getAddress({
          cityId: this.params.city
        });
        this.blockList = res.data.resultMsg;
        this.blockpicker = true;
      } else {
        this.$toast("请先选择省市");
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
      console.log(index);
      this.params.province = value.provinceId;
      this.params.provinceName = value.provinceName;
      this.provincepicker = false;
      let res = await common.getAddress({
        provinceId: value.provinceId
      });
      this.cityList = res.data.resultMsg;
      this.citypicker = true;
    },
    async onConfirmCity(value, index) {
      //确认市
      console.log("确认市");
      console.log(index);
      this.params.city = value.cityId;
      this.params.cityName = value.cityName;
      this.citypicker = false;
      let res = await common.getAddress({
        cityId: value.cityId
      });
      this.blockList = res.data.resultMsg;
      this.blockpicker = true;
    },
    async onConfirmBlock(value, index) {
      //确认区
      console.log("确认区");
      console.log(index);
      this.params.country = value.areaId;
      this.params.countryName = value.areaName;
      this.blockpicker = false;
    },
    afterReadImg(file) {
      this.getImgInfo(file.content);
    },
    async getImgInfo(file) {
      //识别营业执照
      this.$toast.loading({
        message: "识别中..",
        forbidClick: true,
        duration: 0
      });
      const params = {
        str: file,
        flag: "businessPhoto" //营业执照
      };
      const info = await common.getImgInfo(params);
      this.$toast.clear();
      if (info.data.result && info.data.result == "SUCCESS") {
        const imgUrl = info.data.uri + "" + info.data.url;
        console.log(imgUrl);
        let businessLicense = info.data.businessLicense;
        let businessTermEnd = info.data.businessTermEnd;
        let businessTermStart = info.data.businessTermStart;
        this.params.businessLicense = businessLicense;
        this.params.businessTermEnd = businessTermEnd;
        this.params.businessTermStart = businessTermStart;
        this.businessLicense = info.data.uri + info.data.url;
        this.photos.businessLicense = [{ url: imgUrl }];
      } else {
        this.photos.businessLicense = [{ url: "" }];
        this.$toast("营业执照信息无法识别！");
      }
    },
    beforeUploadImg(type) {
      this.uploadType = type;
    },
    uploadImg(file) {
      //图片上传
      this.$toast.loading({
        message: "图片上传中..",
        forbidClick: true,
        duration: 0
      });
      upload.blobToBase64(file.file, this);
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
      if (this.dateType == "businessTermStart") {
        console.log("businessTermStart");
        this.minDate = new Date(getData);
      } else {
        this.minDate = new Date(2000, 1, 1);
      }
      this.showDatepicker = false;
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
</style>