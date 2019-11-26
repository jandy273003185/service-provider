<template>
  <div class="incoming">
    <van-nav-bar title="审核信息" left-text="返回" left-arrow @click-left="changePrepage" />
    <van-tabs v-model="infoType" color="#699dd7" swipeable>
      <van-tab title="商户基本信息">
        <div class="stepInfo" ref="baseform">
          <div class="row">
            <span class="label">商户编号</span>
            <input v-model="allInfoList.custId" readonly />
          </div>
          <div class="row">
            <span class="label">商户账号</span>
            <input v-model="allInfoList.merchantMobile" readonly />
          </div>
          <div class="row">
            <span class="label">商户类型</span>
            <select v-model="allInfoList.custType" disabled>
              <option value="">--</option>
              <option value="0">个人</option>
              <option value="1">企业</option>
            </select>
          </div>
          <div class="row">
            <span class="label">商户名称</span>
            <input v-model="allInfoList.custName" readonly />
          </div>
          <div class="row">
            <span class="label">商户简称</span>
            <input v-model="allInfoList.shortName" readonly />
          </div>
          <div class="row">
            <span class="label">商户地址</span>
            <input v-model="allInfoList.custAdd" readonly />
          </div>
          <div class="row">
            <span class="label">营业执照编号</span>
            <input v-model="allInfoList.businessLicense" readonly />
          </div>
          <div class="row">
            <span class="label">营业执照截止日期</span>
            <input v-model="allInfoList.businessTermEnd" readonly />
          </div>
        </div>
      </van-tab>
      <van-tab title="商户产品签约">
        <div class="stepInfo">
          <div class="signed contract">
            <div class="signTit">已签约产品</div>
            <div v-for="(item,idx) in proList" :key="idx">
              <div v-if="item.productStatus=='00'">
                <div class="row1">
                  <van-checkbox class="check" v-if="item.productId==8 && item.productStatus=='00' " v-model="checked" disabled>蜻蜓产品</van-checkbox>
                  <van-checkbox class="check" v-if="item.productId==2 && item.productStatus=='00' " v-model="checked" disabled>app产品</van-checkbox>
                  <van-checkbox class="check" v-if="item.productId==1 && item.productStatus=='00' " v-model="checked" disabled>扫码产品</van-checkbox>
                  <span class="name" v-if="item.productStatus=='00'">结算费率：</span>
                  <input  type="text" v-if="item.productStatus=='00'"  v-model="item.productRate" readonly />
                </div>
                <div class="sn" v-if="item.productId==8">
                  <span>SN:</span>
                  <input type="text" v-model="item.sn"  disabled />
                </div>
              </div>
            </div>

            <div class="signTit">待审核产品</div>
            <div v-for="(item,idx) in proList" :key="idx">
              <div v-if="item.productStatus=='01'">
                <div class="row1" >
                  <van-checkbox class="check" v-if="item.productId==8 " v-model="checked" disabled>蜻蜓产品</van-checkbox>
                  <van-checkbox class="check" v-if="item.productId==2 " v-model="checked" disabled>app产品</van-checkbox>
                  <van-checkbox class="check" v-if="item.productId==1  " v-model="checked" disabled>扫码产品</van-checkbox>
                  <span class="name">结算费率：</span>
                  <input  type="text" v-model="item.productRate" readonly />
                </div>
                <div class="sn" v-if="item.productId==8">
                  <span>SN:</span>
                  <input type="text" v-model="item.sn"  disabled />
                </div>
              </div>

            </div>



          </div>
          <div class="unsigned contract">
            <div class="signTit">可签约产品</div>
            <div class="row1" ref="qingting" v-if="qinting">
              <van-checkbox class="check" v-model="contract_qinting">蜻蜓产品</van-checkbox>
              <span class="name">结算费率：</span>
              <input type="number" v-model="qtRate" />
            </div>
            <div class="row" v-if="qinting">
              <input type="text" v-model="sn" style="border-color:#ccc" placeholder="请输入蜻蜓设备编号SN" />
            </div>
            <div class="row1" ref="scan" v-if="scan">
              <van-checkbox v-if="scan" class="check" v-model="contract_scan">扫码产品</van-checkbox>
              <span class="name">结算费率：</span>
              <input type="number" v-model="scanRate" />
            </div>
            <div class="row1" ref="app" v-if="app">
              <van-checkbox class="check" v-model="contract_app">app产品</van-checkbox>
              <span class="name">结算费率：</span>
              <input type="number" v-model="appRate" />
            </div>
          </div>
        </div>

        <div class="btn" @click="submitContract">提交</div>
      </van-tab>
    </van-tabs>
  </div>
</template>
<script>
import { mapState } from "vuex";
import { shopAuditInfo } from "../../assets/api/interface";
import { Dialog } from 'vant';//弹窗函数，可直接调用

export default {
  name: "audit",
  components: {

  },
  created() {
    //this.$route.params.type
  },
  data() {
    return {
      checked: true,
      value: "1",
      pageNum: 1,
      userId: "",
      infoType: 0,
      params: {},
      qinting: true, //可签约显示
      scan: true,
      app: true,
      contract_qinting: false, //选中可签约产品
      contract_scan: false,
      contract_app: false,
      qtRate: "",
      scanRate: "",
      appRate: "",
      sn: "",
      allInfoList: {},
      contractList: "",
      proList: [{ productId: 1, productRate: "0.38", sn: "122245",productStatus:'00' }] //已签约产品数据格式
    };
  },

  computed: {
    ...mapState(["custId"])
  },

  mounted() {
    //this.userId = storage.get('userId');
    this.getShopDetail();
    this.getcontractInfo();
    console.log(this.$store.state.custId);
  },
  methods: {
    changePrepage() {
      //返回上一页
      this.$router.go(-1);
    },
    async getShopDetail() {
      //请求商户基本信息
      let listInfo = await shopAuditInfo.shopsucceedInfo({
        custId: this.custId //商户Id
      });
      //console.log(listInfo);
      this.allInfoList = listInfo.data.resultMsg;
    },
    async getcontractInfo() {
      //请求已签约信息
      let contractInfo = await shopAuditInfo.contractInfo({
        mchCustId: this.custId //商户Id
      });
      console.log(contractInfo);
      this.proList = contractInfo.data.resultMsg;
      for (let i = 0; i < this.proList.length; i++) {
        if (this.proList[i].productId == 8) {
          if(this.proList[i].productStatus == '00' || this.proList[i].productStatus == '01' ){
            this.qinting = false;
          }
        }

        if (this.proList[i].productId == 2) {
          if(this.proList[i].productStatus == '00' || this.proList[i].productStatus == '01' ){
            this.app = false;
          }
        }

        if (this.proList[i].productId == 1) {
          if(this.proList[i].productStatus == '00' || this.proList[i].productStatus == '01' ){
            this.scan = false;
          }
        }
      }

    },

    async submitContract() {//提交签约
      const _this=this;
      let obj = {},
        newProlist = [];
      if (this.contract_qinting == true) {
        if (this.qtRate && this.sn) {
          obj = {
            productId: 8,
            productRate: this.qtRate,
            sn: this.sn
          };
          newProlist.push(obj);
          this.$refs['qingting'].className = "row1";
        } else {
          this.$refs['qingting'].className = "row1 active";
          Dialog({ message: '请填写结算费率及SN码' });
          return
        }
      }

      if (this.contract_app == true) {
        if (this.appRate) {
          obj = {
            productId: 2,
            productRate: this.appRate,
            sn: ""
          };
          newProlist.push(obj);
          this.$refs['app'].className = "row1";
        } else {
          this.$refs['app'].className = "row1 active";
          Dialog({ message: '请填写结算费率' });
          return
        }
      }

    if (this.contract_scan == true) {
        if (this.scanRate) {
          obj = {
            productId: 1,
            productRate: this.scanRate,
            sn: ""
          };
          newProlist.push(obj);
          this.$refs['scan'].className = "row1";
        } else {
          this.$refs['scan'].className = "row1 active";
          Dialog({ message: '请填写结算费率' });
          return
        }
      }
      let listInfo = await shopAuditInfo.pro_contract({
        productInfos:JSON.stringify(newProlist) ,
        custId:this.custId
      });
      if(listInfo.data.resultCode=="1"){
        Dialog({ message: listInfo.data.resultMsg ,closeOnPopstate:true});
        setTimeout(function(){
          _this.$router.go(-1);
        },1500)
        
      }
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
.stepInfo {
  .contract {
    .signTit {
      height: vw(80);
      line-height: vw(80);
      margin: vw(20) 0;
      padding-left: vw(20);
      background: #c7dff9;
      color: #666
    }
    .sn{
      height: vw(80);
      font-size: vw(26);
      display: flex;
      justify-content: flex-end;
      align-items: flex-end;
      padding:0 vw(20);
      span{
        color: #c4c4c4;
        margin-right: vw(20);
      }
      input{
        padding-bottom: vw(20);
      }
    }
    input {
      border-bottom: vw(1) solid #a5acff;
    }
  }
  
}
</style>