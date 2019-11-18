<template>
  <div class="incoming">
    <van-nav-bar title="产品签约" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="4" />
    <div>签约产品</div>
    <div class="stepInfo sign">
      <div class="row" ref="dragonfly">
        <van-checkbox v-model="dragonfly" >蜻蜓产品</van-checkbox>
        <!--  productId  -->
        <span class="name">结算费率：</span>
        <!--  productRate -->
        <input type="text" v-model="dragonflyProductRate" placeholder="请输入费率"/>
      </div>
      <div class="row" v-show="dragonfly">
        <input type="text" v-model="sn" placeholder="请输入蜻蜓设备编号SN" />
      </div>
      <div class="row" ref="scan">
        <van-checkbox v-model="scan" >扫码产品</van-checkbox>
        <span class="name">结算费率：</span>
        <input type="text" v-model="scanProductRate" placeholder="请输入费率"/>
      </div>
      <div class="row" ref="app">
        <van-checkbox v-model="app" >APP产品</van-checkbox>
        <span class="name" >结算费率：</span>
        <input type="text" v-model="appProductRate" placeholder="请输入费率"/>
      </div>
      <div class="btn" @click="submitIncoming">提交</div>
      <div class="btn" @click="getPreStep">返回</div>
    </div>
  </div>
</template>
<script>
import form from "@/lib/form.js";
import { mapState } from "vuex";
import { common, incoming } from "@/assets/api/interface";
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
    this.params = Object.assign({}, this.incoming);
    //this.$route.params.type
  },
  data() {
    return {
      clickedNext: false,
      dragonfly: false,
      scan: false,
      app: false,
      sn: "",
      dragonflyProductRate: "",
      scanProductRate: "",
      appProductRate: "",
      params: {}
      /*   URL_BASE: this.$store.state.URL_BASE, */
    };
  },
  methods: {
    changePrepage() {
      this.$router.go(-1);
    },
    getPreStep() {
      //上一步  结算信息
      this.$router.push("merchant");
    },
    submitIncoming() {
      //提交
      this.clickedNext=true;
      if (this.dragonfly || this.scan || this.app) {
        let prolist = [];
        let proObj = {};
        if (this.dragonfly&&this.sn && this.dragonflyProductRate) {
            proObj = {
              productId: "8",
              productRate: this.dragonflyProductRate,
              sn: this.sn
            };
            prolist.push(proObj);
            this.$refs.dragonfly.className="row"
          } else {
            this.$refs.dragonfly.className="row active"
            console.log("请填写sn rate");
          }
        
        if (this.scan) {
          if (this.scanProductRate) {
            proObj = {
              productId: "1",
              productRate: this.scanProductRate,
              sn: ""
            };
            prolist.push(proObj);
             this.$refs.scan.className="row"
          } else {
             this.$refs.scan.className="row active"
            console.log("请填写 rate");
          }
        }
        if (this.app) {
          if (this.appProductRate) {
            proObj = {
              productId: "2",
              productRate: this.appProductRate,
              sn: ""
            };
            prolist.push(proObj);
             this.$refs.app.className="row"
          } else {
            this.$refs.app.className="row active"
            console.log("请填写 rate");
          }
        }
        if ((this.dragonfly && this.sn) || !this.dragonfly) {
          let fullParams = Object.assign(this.incoming, this.params);
          console.log(JSON.stringify(prolist));
          fullParams.productInfos = JSON.stringify(prolist);
          fullParams.custScanCopys = JSON.stringify(this.custScanCopys);
          this.$store.commit("setincoming", fullParams);
          this.$store.commit("custScanCopys", this.custScanCopys);
          this.insertIncoming(fullParams); //提交请求
        }
      } else {
        console.log("请选择产品");
      }
      //this.$router.push("sign");
      //提交进件信息
    },
    async insertIncoming(params) {
      let info = await incoming.insertIncoming(params);
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
</style>