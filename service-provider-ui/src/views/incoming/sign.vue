<template>
  <div class="incoming sign">
    <van-nav-bar title="产品签约" left-text="返回" left-arrow @click-left="changePrepage" />
    <Step currStep="4" />
    <div class="signTit" style="margin-top:0">签约产品</div>
    <div class="stepInfo sign">
      <div class="row1" ref="dragonfly">
        <van-checkbox class="check" v-model="dragonfly">蜻蜓产品</van-checkbox>
        <span class="name">结算费率：</span>
        <input type="number" v-model="dragonflyProductRate" placeholder="请输入费率" />
        <span class="unit">%</span>
      </div>
      <div class="row" v-show="dragonfly">
        <input type="text" v-model="sn" placeholder="请输入蜻蜓设备编号SN" />
      </div>
      <div class="row1" ref="scan">
        <van-checkbox class="check" v-model="scan">扫码产品</van-checkbox>
        <span class="name">结算费率：</span>
        <input type="number" v-model="scanProductRate" placeholder="请输入费率" />
        <span class="unit">%</span>
      </div>
      <div class="row1" ref="app">
        <van-checkbox class="check" v-model="app">APP产品</van-checkbox>
        <span class="name">结算费率：</span>
        <input type="number" v-model="appProductRate" placeholder="请输入费率" />
        <span class="unit">%</span>
      </div>
      <div class="mix-btn">
        <div class="btn save" v-if="checkedState!='corvidae'" @click="saveIncoming">保存</div>
        <div class="btn" @click="submitIncoming('01')">提交</div>
      </div>
      <!-- 完善保存资料提交 -->
      <div class="btn back" @click="changePrepage">返回</div>
    </div>
  </div>
</template>
<script>
import util from "@/lib/util.js";
import { mapState } from "vuex";
import { Dialog } from "vant";
import { incoming } from "@/assets/api/interface";
export default {
  name: "sign",
  components: {
    Step: () => import("@/components/step")
  },
  data() {
    return {
      clickedNext: false,
      dragonfly: false,
      scan: false,
      app: false,
      sn: "",
      dragonflyProductRate: 0.38,
      scanProductRate: 0.38,
      appProductRate: 0.38,
      params: {}
    };
  },
  computed: {
    ...mapState([
      "checkedState",
      "incoming",
      "incomingReturn",
      "savephotos",
      "roleId",
      "custId"
    ])
  },
  created() {
    console.log(this.$route.params);
    console.log(this.photos);
    this.params = Object.assign(
      { userId: this.$store.state.userId },
      this.incoming
    );
    if (this.checkedState == "corvidae") {
      let productInfos = this.incomingReturn.productInfoList;
      for (let i = 0; i < productInfos.length; i++) {
        if (productInfos[i].productId == "1") {//扫码
          this.scan = true;
          this.scanProductRate = productInfos[i].productRate;
        }
        if (productInfos[i].productId == "2") {//app
          this.app = true;
          this.appProductRate = productInfos[i].productRate;
        }
        if (productInfos[i].productId == "9") {//蜻蜓
          this.dragonfly = true;
          this.dragonflyProductRate = productInfos[i].productRate;
          this.sn = productInfos[i].sn;
        }
      }
    }
  },
  methods: {
    changePrepage() {
      this.$router.push("merchant");
    },
    saveIncoming() {
      //保存
      Dialog.confirm({
        message: "请确认是否保存？"
      })
        .then(() => {
          let count = 0;
          if (count < 1) {
            count++;
            let prolist = this.checkSignGoods();
            if(prolist){
              let fullParams = Object.assign(this.incoming, this.params, {
              roleId: this.roleId,
              state: "05"
            });
            console.log(JSON.stringify(prolist));
            fullParams.productInfos = JSON.stringify(prolist); //产品
            let custScanCopys = util.getAllPhotos(this.savephotos); //图片
            fullParams.custScanCopys = custScanCopys;
            this.$store.commit("setincoming", fullParams);
            this.insertIncoming(fullParams); //提交请求
            }
            
          }
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
            let count = 0;
            if (count < 1) {
              count++;
              let prolist = this.checkSignGoods();
              if (prolist&&prolist.length > 0) {
                let fullParams = Object.assign(this.incoming, this.params, {
                  roleId: this.roleId,
                  state: state,
                  custId: this.custId
                });
                fullParams.productInfos = JSON.stringify(prolist); //产品
                let custScanCopys = util.getAllPhotos(this.savephotos); //图片
                fullParams.custScanCopys = custScanCopys;
                this.$store.commit("setincoming", fullParams);
                this.insertIncoming(fullParams); //提交请求
              }
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
      if (info.data.code == 200) {
        this.$toast.success("成功");
      } else {
        this.$toast("进件信息添加失败！");
      }
      if(info.data.code == 200){
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
      //签约产品
      let prolist = [];
      let proObj = {};
      let count=0;
      if (this.dragonfly) {
        if (this.dragonfly && this.dragonflyProductRate) {
          proObj = {
            productId: "9",
            productRate: this.dragonflyProductRate,
            sn: this.sn||''
          };
          prolist.push(proObj);
        } else {
          this.$toast("请将蜻蜓产品信息补充完整");
          count++;
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
        } else {
          this.$toast("请将填写扫码费率");
          count++;
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
        } else {
          this.$toast("请将填写app费率");
          count++
        }
      }
      if(count<1){
        return prolist;
      }else{
        return false
      }
      
    }
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
</style>