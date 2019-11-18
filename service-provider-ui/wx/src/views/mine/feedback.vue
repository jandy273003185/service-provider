<template>
    <div class="feedback">
      <van-nav-bar
              title="意见反馈"
              left-text="返回"
              left-arrow
              @click-left="onClickLeft"
      />
      <div class="textBox">
        <van-cell-group>
          <van-field
                  v-model="message"
                  rows="2"
                  type="textarea"
                  maxlength="200"
                  placeholder="请输入您的宝贵意见"
                  show-word-limit
          />

        </van-cell-group>
        <div class="phonenumber">
          <van-field v-model="phoneNumber" placeholder="请输入您的联系方式（选填）" />
          <button class="submit">提交</button>
        </div>

      </div>

    </div>
</template>
<script>
export default {
  name: "feedback",
  components: {

  },
  computed: {},
  created() {
  },
  data() {
    return {
      message:'',
      phoneNumber:''
    };
  },
  methods: {
    onClickLeft() {
      this.$router.go(-1);
    },
  }
};
</script>
<style lang="scss" scoped>
@import "../../style/views/incoming.scss";
.van-button--normal{
  margin: auto;
}

.feedback{
  width:100%;

  .van-nav-bar{
    background-color: #26a7e9;
    .van-icon-arrow-left:before,.van-nav-bar__text,.van-nav-bar__title{
      color: #FFFFFF;
    }
  }






  .textBox{
    width: vw(690);
    margin: vw(30) auto;

    .phonenumber{
      width: 100%;
      height: vw(280);
      margin-top: vw(50);
      display: flex;
      flex-direction: column;
      justify-content: space-between;
      align-items: center;
      .submit{
        width: vw(400);
        height: vw(80);
        font-size: cw(40);
        color: #FFFFFF;
        background-color: #26a7e9;
      }
    }
  }
}





</style>