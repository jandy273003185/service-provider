<template>
  <div class="incoming">
    <van-nav-bar title="产品签约" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="4" />
    <div class="signTit">签约产品</div>
    <div class="stepInfo sign">
      <div class="row" ref="dragonfly">
        <van-checkbox v-model="dragonfly">蜻蜓产品</van-checkbox>
        <!--  productId  -->
        <span class="name">结算费率：</span>
        <!--  productRate -->
        <input type="number" v-model="dragonflyProductRate" placeholder="请输入费率" />
      </div>
      <div class="row" v-show="dragonfly">
        <input type="text" v-model="sn" placeholder="请输入蜻蜓设备编号SN" />
      </div>
      <div class="row" ref="scan">
        <van-checkbox v-model="scan">扫码产品</van-checkbox>
        <span class="name">结算费率：</span>
        <input type="number" v-model="scanProductRate" placeholder="请输入费率" />
      </div>
      <div class="row" ref="app">
        <van-checkbox v-model="app">APP产品</van-checkbox>
        <span class="name">结算费率：</span>
        <input type="number" v-model="appProductRate" placeholder="请输入费率" />
      </div>
      <div class="mix-btn" >
        <div class="btn save" v-if="checkedState!='corvidae'"  @click="saveIncoming">保存</div>
        <div class="btn" @click="submitIncoming('01')">提交</div>
      </div>
      <!-- 完善保存资料提交 -->
      <div class="btn back" @click="getPreStep">返回</div>
    </div>
  </div>
</template>
<script>
import util from "@/lib/util.js";
import { mapState } from "vuex";
import { Dialog } from "vant";
import { incoming } from "@/assets/api/interface";
export default {
  name: "incoming",
  components: {
    /*     Modal: () => import("@/components/modal"), */
    Step: () => import("@/components/step")
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
  computed: { 
    ...mapState(["checkedState", "incoming", "incomingReturn", "savephotos",'roleId'])
  },
  created() {
    console.log(this.$route.params);
    this.params = Object.assign(
      {
        userId: this.$store.state.userId
      },
      this.incoming
    );
    if (this.checkedState == "corvidae") {
      let productInfos = this.incomingReturn.productInfoList;
      for (let i = 0; i < productInfos.length; i++) {
        if (productInfos[i].productId == "1") {
          this.scan = true;
          this.scanProductRate = productInfos[i].productRate;
          this.sn = productInfos[i].sn;
        }
        if (productInfos[i].productId == "2") {
          this.app = true;
          this.appProductRate = productInfos[i].productRate;
        }
        if (productInfos[i].productId == "8") {
          this.dragonfly = true;
          console.log(productInfos[i].productRate);
          this.dragonflyProductRate = productInfos[i].productRate;
        }
      }
    }
  },
  methods: {
    changePrepage() {
      this.$router.go(-1);
    },
    getPreStep() {
      //上一步  结算信息
      this.$router.push("merchant");
    },
    saveIncoming() {
      console
      //保存
      Dialog.confirm({
        message: "请确认是否保存？"
      })
        .then(() => {
          let prolist = this.checkSignGoods();
          let fullParams = Object.assign(this.incoming, this.params, {
            roleId:this.roleId,
            state: "05"
          });
          console.log(JSON.stringify(prolist));
          fullParams.productInfos = JSON.stringify(prolist); //产品
          let custScanCopys = util.getAllPhotos(this.savephotos); //图片
          fullParams.custScanCopys = custScanCopys;
          this.$store.commit("setincoming", fullParams);
          this.insertIncoming(fullParams); //提交请求
        })
        .catch(() => {
          // on cancel
        });
    },
    submitIncoming(state) {
      //提交
      this.clickedNext = true;
      if (this.dragonfly || this.scan || this.app) {
        //至少选择一款
        Dialog.confirm({
          message: "请确认是否提交？"
        })
          .then(() => {
            let prolist = this.checkSignGoods();
            if (prolist.length > 0) {
              let fullParams = Object.assign(this.incoming, this.params, {
                roleId:this.roleId,
                state: state
              });
              fullParams.productInfos = JSON.stringify(prolist); //产品
              let custScanCopys = util.getAllPhotos(this.savephotos); //图片
              fullParams.custScanCopys = custScanCopys;
              this.$store.commit("setincoming", fullParams);
              this.insertIncoming(fullParams); //提交请求
            }
          })
          .catch(() => {
            // on cancel
          });
      } else {
        this.$toast("请至少选择一款签约产品");
      }
    },
    async insertIncoming(params) {
      let info = await incoming.insertIncoming(params);
      if (info.data.resultCode == 1) {
        if (this.roleId == "3") {
          //业务员
          this.$router.push("/salesman");
        }
        if (this.roleId == "2") {
          //管理员
          this.$router.push("/Administrator");
        }
      }
    },
    checkSignGoods() {
      //校验产品签约，至少签约一款
      let prolist = [];
      let proObj = {};
      if (this.dragonfly) {
        if (this.dragonfly && this.sn && this.dragonflyProductRate) {
          proObj = {
            productId: "8",
            productRate: this.dragonflyProductRate,
            sn: this.sn
          };
          prolist.push(proObj);
          //this.$refs.dragonfly.className = "row";
        } else {
          //this.$refs.dragonfly.className = "row active";
          this.$toast("请将蜻蜓产品信息补充完整");
        }
      }
      if (this.scan) {
        if (this.scanProductRate) {
          proObj = {
            productId: "1",
            productRate: this.scanProductRate,
            sn: ""
          };
          prolist.push(proObj);
          // this.$refs.scan.className = "row";
        } else {
          this.$toast("请将填写扫码费率");
          //this.$refs.scan.className = "row active";
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
          // this.$refs.app.className = "row";
        } else {
          // this.$refs.app.className = "row active";
          this.$toast("请将填写app费率");
        }
      }
      return prolist;
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
</style>