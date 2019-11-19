<template>
  <div class="incoming">
    <van-nav-bar title="基本信息" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="1" />
    <div class="stepInfo">
      <div ref="baseform">
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
          <input
            class="address"
            v-model="params.provinceName"
            placeholder="省"
            readonly
            @click="getInitAddress"
          />
          <input class="address" v-model="params.cityName" placeholder="市" readonly />
          <input class="address" v-model="params.countryName" placeholder="区" readonly />
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
            <!-- businessLicense -->
            营业执照照片
            <span>(必须)</span>
          </div>
          <van-uploader
            name="businessLicense"
            :after-read="afterReadImg"
            :max-count="1"
            v-model="photos.businessLicense"
          >
            <!--  v-modal="businessLicenseUrl" -->
            <!-- v-show="!businessLicenseUrl" -->
            <van-button icon="photo" type="primary">上传营业执照</van-button>
          </van-uploader>
          <!--   <img :src="params.businessLicenseUrl" v-show="params.businessLicenseUrl" /> -->
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
            <!--  <van-checkbox class="longterm" v-model="longterm">长期</van-checkbox> -->
          </div>
        </div>
        <van-datetime-picker
          class="datepicker"
          v-show="showDatepicker"
          type="year-month"
          :max-date="maxDate"
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
            <van-uploader :after-read="uploadImg" v-model="photos.shopFrontDesk" :max-count="1">
              <!---->
              <van-button icon="photo" type="primary">上传门头照照片</van-button>
            </van-uploader>
          </div>
          <!--   <img :src="params.shopFrontDesk" v-show="params.shopFrontDesk" /> -->
        </div>
        <div class="row-img" :class="{'active':(clickedNext&&!params.shopInterior)}">
          <div class="stit" :class="{'active':(clickedNext&&!params.shopInterior)}">
            <!-- shopInterior -->
            店内照
            <span>(必须)</span>
          </div>
          <div @click="beforeUploadImg('shopInterior')">
            <van-uploader
              ref="shopInterior"
              v-model="photos.shopInterior"
              :after-read="uploadImg"
              :max-count="1"
            >
              <van-button icon="photo" type="primary">上传店内照片</van-button>
            </van-uploader>
          </div>

          <!--   <img :src="params.shopInterior" v-show="params.shopInterior" /> -->
        </div>
        <div class="row-img">
          <!-- specialBusiness -->
          <div class="stit" :class="{'active':(clickedNext&&!params.specialBusiness)}">特殊行业资质照</div>
          <div @click="beforeUploadImg('specialBusiness')">
            <van-uploader :after-read="uploadImg" v-model="photos.specialBusiness" :max-count="1">
              <!-- v-show="!params.specialBusiness" -->
              <van-button icon="photo" type="primary">上传特殊行业资质照</van-button>
            </van-uploader>
          </div>
          <!--  <img :src="params.specialBusiness" v-show="params.specialBusiness" /> -->
        </div>
        <div class="row-img">
          <div
            class="stit"
            :class="{'active':(clickedNext&&!params.electronicSignaturePhoto)}"
          >电子签名照</div>
          <!-- electronicSignaturePhoto -->
          <div @click="beforeUploadImg('electronicSignaturePhoto')">
            <van-uploader
              v-model="photos.electronicSignaturePhoto"
              :after-read="uploadImg"
              :max-count="1"
            >
              <van-button icon="photo" type="primary">上传电子签名照</van-button>
            </van-uploader>
          </div>
          <!--  <img :src="params.electronicSignaturePhoto" /> -->
        </div>
        <div class="row-img">
          <div class="stit">其他资料照</div>
          <!-- :class="{'active':(clickedNext&&!params.otherPhoto1)}" -->
          <!-- otherPhoto1 2 -->
          <div class="img-col" @click="beforeUploadImg('otherPhoto1')">
            <van-uploader v-model="photos.otherPhoto1" :after-read="uploadImg" :max-count="1">
              <!-- v-show="!params.otherPhoto1" -->
              <van-button icon="photo" type="primary">其他资料照</van-button>
            </van-uploader>
          </div>
          <!--  <img :src="params.otherPhoto1" v-show="params.otherPhoto1" /> -->
          <div class="img-col" @click="beforeUploadImg('otherPhoto2')">
            <van-uploader :after-read="uploadImg" v-model="photos.otherPhoto2" :max-count="1">
              <!--  v-show="!params.otherPhoto2" -->
              <van-button icon="photo" type="primary">其他资料照</van-button>
            </van-uploader>
          </div>
          <!--    <img :src="params.otherPhoto2" v-show="params.otherPhoto2" /> -->
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
  name: "incoming",
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
      maxDate: new Date(),
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
        otherPhoto1: "", //其他资料照 1
        otherPhoto2: "" //其他资料照 2
      },
    };
  },
  computed: {
    ...mapState(["incoming", "savephotos","incomingReturn"])
  },
  created() {
    let type = this.$route.params.type;
    this.photos = this.savephotos;
    if (type) {
      this.pagetype = type;
      this.$store.commit("setCheckedState", type);
      if (type == "new") {
        this.params = {};
      }
      if (type == "corvidae") {
        //待完善
        let custId = this.$route.params.custId;
        this.getIncomingInfo(custId);
      }
      if(type=="getback"){

      }
    } else {
      this.params = Object.assign(this.params, this.incoming);
    }
  },

  methods: {
    async getIncomingInfo(custId) {
      //获取之前提交进件数据
      console.log(custId);
      let res = await incoming.getIncoming({ custId: custId });
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
        shopFrontDesk: "", //门头照
        shopInterior: "", //店内照
        specialBusiness: "", //特殊行业照
        electronicSignaturePhoto: "", //电子签名照
        otherPhoto1: "", //其他资料照 1
        otherPhoto2: "" //其他资料照 2
      };
      let photos = res.data.resultMsg.custScanInfoList;
      let urlHead = res.data.resultMsg.uri + "" + res.data.resultMsg.url;
      for (let i = 0; i < photos.length; i++) {
        let imgname = util.getImgName(photos[i].scanCopyPath);
        if (photos[i].certifyType == "02") {
          //营业执照
          this.photos.businessLicense = [{ url: urlHead + "" + imgname }];
          //this.params.businessLicense=urlHead+''+imgname;
        }
        if (photos[i].certifyType == "20") {
          //门头照
          this.photos.shopFrontDesk = [{ url: urlHead + "" + imgname }];
          this.params.shopFrontDesk = urlHead + "" + imgname;
        }
        if (photos[i].certifyType == "21") {
          //店内照
          this.photos.shopInterior = [{ url: urlHead + "" + imgname }];
          this.params.shopInterior = urlHead + "" + imgname;
        }
        if (photos[i].certifyType == "11") {
          //行业资质
          this.photos.specialBusiness = [{ url: urlHead + "" + imgname }];
          this.params.specialBusiness = urlHead + "" + imgname;
        }
        if (photos[i].certifyType == "12") {
          //电子签名
          this.photos.electronicSignaturePhoto = [
            { url: urlHead + "" + imgname }
          ];
          this.params.electronicSignaturePhoto = urlHead + "" + imgname;
        }
        if (photos[i].certifyType == "23") {
          //其他资料照 1
          this.photos.otherPhoto1 = [{ url: urlHead + "" + imgname }];
          this.params.otherPhoto1 = urlHead + "" + imgname;
        }
        if (photos[i].certifyType == "24") {
          //其他资料照 2
          this.photos.otherPhoto2 = [{ url: urlHead + "" + imgname }];
          this.params.otherPhoto2 = urlHead + "" + imgname;
        }
      }
      this.params = params;
    },
    async getInitAddress() {
      //获取省份
      this.provincepicker = true;
      let res = await common.getAddress();
      this.provinceList = res.data.resultMsg;
    },
    onCancelAdd() {
      this.provincepicker = false;
      this.citypicker = false;
      this.blockpicker = false;
    },
    async onConfirmProvince(value, index) {
      //确认省
      console.log("确认省份");
      console.log(value.provinceId);
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
      this.params.city = value.cityId;
      this.params.cityName = value.cityName;
      console.log(value.cityId);
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
      console.log(value.areaId);
      this.params.country = value.areaId;
      this.params.countryName = value.areaName;
      this.blockpicker = false;
    },
    onChangeAdd(value, index) {
      console.log(value);
      console.log(index);
    },
    changePrepage() {
      //返回上一页
      this.$router.go(-1);
    },
    getNextStep() {
      //到下一步 法人信息
      this.clickedNext = true;
      let count = form.validParams(this.params);
      console.log(count);
      if (count == 0) {
        let fullParams = Object.assign(this.incoming, this.params);
        console.log(fullParams);
        this.$store.commit("setincoming", fullParams);
        let incomingReturn=this.incomingReturn;
        console.log(incomingReturn);
        let custInfo=this.incomingReturn.custInfo||{};
        let all=Object.assign(custInfo,fullParams);
        incomingReturn.custInfo=all;
        this.$store.commit("setincomingReturn",incomingReturn);
        this.$store.commit("setPhotos", this.photos);
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
      this.businessLicense = info.data.uri + info.data.url;
      this.photos.businessLicense = [{ url: info.data.uri + info.data.url }];
      /*       let imgObj = {
        certifyType: "02", //营业执照
        scanCopyPath: info.data.imagePath,
        certifyNo: ""
      };
      this.custScanCopys.push(imgObj); */
    },
    deleteImg(type) {
      console.log(this[type]);
      this[type] = [];
      console.log(this[type]);
    },
    beforeUploadImg(type) {
      this.uploadType = type;
    },
    uploadImg(file) {
      //图片上传
      upload.uploadImgRequest(this, file.file);
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
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
</style>