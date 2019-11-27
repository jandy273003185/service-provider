<template>
  <div class="failedBox">
    <van-nav-bar class="navBar" left-text="返回" title="详情"  left-arrow @click-left="onClickLeft" />
    <div class="failed">
      <img src="../assets/images/home/warning.png" alt="失败" />
      <div>
        <span>审核失败原因</span>
        <span class="font_E">{{allInfoList.audit_time}}</span>
      </div>
      <div>
        <span class="font_E">{{allInfoList.message}}</span>
      </div>
    </div>
    <div class="buttonBox">
      <van-button color="#699dd7" type="primary" @click="toUpdatePage" size="large">更新资料</van-button>
      <div class="retuan" @click="onClickLeft">返回</div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { mapState } from "vuex";
import { shopAuditInfo } from "../assets/api/interface";
export default {
  name: "searchShop",
  data() {
    return {
      value: "",
      allInfoList: ""
    };
  },
  computed: {
    ...mapState(["custId"])
  },
  mounted() {
    this.getShopDetail();
  },
  methods: {
    toUpdatePage() {
      this.$router.push({
        name: "baseInfo",
        params: { type: "corvidae" }
      });
    },
    onClickLeft() {
      this.$router.go(-1);
    },

    async getShopDetail() {
      //请求商户基本信息
      let listInfo = await shopAuditInfo.shopDefeatedInfo({
        custId: this.custId //商户Id
      });

      this.allInfoList = listInfo.data.resultMsg[0];
    }
  }
};
</script>

<style scoped lang="stylus" ref="stylesheet/stylus">
@import '../style/common/base.styl';

.failedBox {
  background-color: #eeeeee;

  .failed {
    width: 100%;
    height: vw(400);
    margin: vw(20) auto;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
    align-items: center;
    background-color: #ffffff;

    img {
      width: vw(120);
      height: vw(120);
    }

    div {
      display: flex;
      flex-direction: column;
      justify-content: space-around;
      align-items: center;

      .font_E {
        color: #9a9a9a;
      }
    }
  }

  .buttonBox {
    width: 90%;
    margin: vw(60) auto;
    height: vw(170);
    display: flex;
    justify-content: space-between;
    align-items: center;
    flex-direction: column;

    .retuan {
      color: #699dd7;
    }
  }
}
</style>
