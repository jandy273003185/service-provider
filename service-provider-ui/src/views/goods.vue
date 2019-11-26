<template>
  <div>
    <BaseHeader></BaseHeader>
    <div class="goods">
      <div class="searchBob">
        <van-search
                v-model="mchName"
                placeholder="请输入商户名称"
                show-action
                shape="round"
                @search="onSearch"
        >
          <div slot="action" @click="onSearch">搜索</div>
        </van-search>
      </div>
      <van-tabs class="p_f" v-model="active" swipeable @click="onClick"  title-active-color="#699dd7" color="#699dd7" >
        <div class="sunxu">
          <div :class="{rank:selectRank=='transactionNum'}" @click="number">
            <span>笔数排名</span>
            <img   src="../assets/images/home/up_down.png" alt="">
          </div>
          <div  :class="{rank:selectRank=='transactionAmount'}" @click="sum">
            <span>金额排名</span>
            <img src="../assets/images/home/up_down.png" alt="">
          </div>
        </div>
        <van-tab title="全部"  name="a">
          <ul>
            <van-list v-if="allShow"
                      v-model="loading"
                      :finished="finished"
                      finished-text="暂无更多数据"
                      @load="loadList"
            >
              <li v-for="(item, index) in shopList" :key="index">
                <span class="shopName">{{index+1}} {{ item.cust_name }} </span>
                <span class="number  wd">{{ item.transactionNum }} 笔</span>
                <span class="sum  wd">{{ item.transactionAmount }} 元</span>
              </li>
            </van-list>
          </ul>
        </van-tab>
        <van-tab title="近7天"  name="b">
          <ul>
            <van-list v-if="active=='b'"
                      v-model="loading"
                      :finished="finished"
                      finished-text="暂无更多数据"
                      @load="loadList"
            >
              <li v-for="(item, index) in shopList" :key="index">
                <span class="shopName">{{index+1}} {{ item.cust_name }} </span>
                <span class="number  wd">{{ item.transactionNum }} 笔</span>
                <span class="sum  wd">{{ item.transactionAmount }} 元</span>
              </li>
            </van-list>
        </ul>
        </van-tab>
        <van-tab title="近30天"  name="c">
          <ul>
            <van-list v-if="active=='c'"
                      v-model="loading"
                      :finished="finished"
                      finished-text="暂无更多数据"
                      @load="loadList"
            >
              <li v-for="(item, index) in shopList" :key="index">
                <span class="shopName">{{index+1}} {{ item.cust_name }} </span>
                <span class="number  wd">{{ item.transactionNum }} 笔</span>
                <span class="sum  wd">{{ item.transactionAmount }} 元</span>
              </li>
            </van-list>
        </ul>
        </van-tab>
        <van-tab title="自定义时间" name="d" >
          <ul>
            <van-list v-if="customShow"
                      v-model="loading"
                      :finished="finished"
                      finished-text="暂无更多数据"
                      @load="loadList"
            >
              <li v-for="(item, index) in shopList" :key="index">
                <span class="shopName">{{index+1}} {{ item.cust_name }} </span>
                <span class="number wd">{{ item.transactionNum }} 笔</span>
                <span class="sum wd">{{ item.transactionAmount }} 元</span>
              </li>
            </van-list>
          </ul>
        </van-tab>
      </van-tabs>
      <div class="modal-wrap" v-show="showTime">
        <div class="timePopup">
          <div class="text flex_center"><span class="titleBar">时间</span></div>
          <div class="text flex_s_b" plain type="primary" @click="showPopFn('timeStart')">
            <button>开始时间</button>
            <span>{{timeStart}}</span>
          </div>
          <div class="text flex_s_b "  plain type="primary" @click="showPopFn('timeEnd')">
            <button>结束时间</button>
            <span>{{timeEnd}}</span>
          </div>
          <van-popup v-model="show" position="bottom" :style="{ height: '40%' }">
            <van-datetime-picker v-model="currentDate" type="date" :max-date="maxDate" @change="changeFn()" @confirm="confirmFn()" @cancel="cancelFn()" />
          </van-popup>
          <div class="btn flex_r">
            <button class="cancel" @click="cancel">取消</button>
            <button class="affirm" @click="affirm">确认</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
/*import { goods } from "../assets/api/interface";*/
import BaseHeader from "../components/baser-header.vue";
import { Dialog } from 'vant';//弹窗函数，可直接调用
import storage from '../assets/modeljs/storage.js';
import {goodsInfo} from "../assets/api/interface";
import {mapState} from 'vuex';

export default {
  name: "goods",
  components: { BaseHeader },
  data() {
    return {
      /*//按页加载*/
      loading:false,
      finished: false,
      active:0,//切换tab时，active的为tab的索引值
      pageNum:0,//页数
      /*//搜索商户*/
      mchName:'',
      shopList:[],//交易数据列表
      userId:'',
      /*自定义时间*/
      maxDate: new Date(),
      currentDate: new Date(),
      changeDate: new Date(),
      show: false, // 用来显示日期弹出层
      timeStart:'',//选取的开始时间
      startMs:'',//选取的开始时间的毫秒数
      timeEnd:'',//选取的结束时间
      endMs:'',//选取的结束时间的毫秒数
      showTime:false,//点击选取时间时打开的遮罩层判断
      type:'',
      customShow:false, //自定义栏是否请求数据，默认false，选取时间后为true
      allShow:true,//全部数据栏是否请求数据，默认false，选取时间后为true
      Numranking:false,
      Amountranking:false,
      selectRank:'transactionNum',
      rankingCode:'transactionNum desc'//transactionNum  笔数排名  transactionAmount 金额排名 desc大到小，asc小到大
    };
  },
  computed:{
          ...mapState(['roleId'])
},
  mounted() {
    this.userId = storage.get('userId');
    console.log(this.userId + '交易');
    console.log(this.timeStart);
    console.log(this.timeEnd);
  },
  methods: {


    //tab栏调用请求

    loadList(){
      this.pageNum+=1;
      this.getAllShopList();
    },

    //请求商户交易数据
    async getAllShopList(){
      let listInfo= await goodsInfo.goodsInfo({
        custName:this.mchName,//商户名
        queryStartDate:this.timeStart,//开始时间
        queryEndDate:this.timeEnd,//结束时间
        userId:this.userId,//用户Id
        pageSize:'20', //单页最多数据条数
        pageNum:this.pageNum, //请求哪一页
        rankingCode:this.rankingCode,
        roleId:this.roleId
      });
      this.shopList = listInfo.data.resultMsg.data;
      let total=listInfo.data.resultMsg.total;
      console.log(listInfo);
      console.log(this.shopList);
      if(this.shopList.length>=total){//判断已加载完成
        this.finished=true;
        this.loading = false;
      }
    },

/*@param userId 管理员wx/getDealRanking.do
* @param rankingCode transaction 笔数排名 money 金额排名
* @param custName 商户名称
* @param queryStartDate 查询起始时间
* @param queryEndDate 查询终止时间
* @param pageSize 页面条数
* @param pageNum 当前页数*/


//搜索商户数据


onSearch(){////将this.value传到后台
  if(this.mchName){
    this.pageNum=1;
    this.active = 'a';
    this.customShow = false;//将自定义栏false
    this.allShow = true;//将“全部”栏显示
    this.timeEnd='';
    this.timeStart='';
    this.getAllShopList();
  }else {
    this.$toast('请输入商户名称');
    return;
  }

},

    //时间选择函数
    showPopFn(tm) {//点击打开日历，通过传值tm判断是开始时间还是结束时间
      this.show = true;
      this.type = tm;
    },
    changeFn() { // 值变化是触发
      this.changeDate = this.currentDate // Tue Sep 08 2020 00:00:00 GMT+0800 (中国标准时间)
    },
    confirmFn() { // 日历上的确定按钮
      console.log(this.type);
      if(this.type=='timeStart'){//选择开始时间
        this.timeStart = this.timeFormat(this.currentDate);
      }
      if(this.type=='timeEnd'){//选择结束时间
        this.timeEnd = this.timeFormat(this.currentDate);

      }
      this.show = false;
    },
    cancelFn(){//取消按钮
      this.show = false;
    },
    timer(time){//将个位数日期变两位数
      return time < 10 ? '0' + time : '' + time;
    },
    timeFormat(time) { // 时间格式化 2019-09-08
      let year = time.getFullYear();
      let month = time.getMonth() + 1;
      month = this.timer(month);
      let day = time.getDate();
      day = this.timer(day);
      return year + '-' + month + '-' + day
    },
    cancel(){//取消选择自定义日期
      this.showTime = false;
    },
    affirm(){//确认选择的自定义日期
      this.startMs = new Date(this.timeStart).getTime();//开始日期的毫秒数
      this.endMs = new Date(this.timeEnd).getTime();//结束日期的毫秒数
      if(this.startMs && this.endMs){
        if(this.startMs <= this.endMs){
          this.showTime = false;
          console.log(this.timeStart,this.timeEnd);
          //请求分页数据数据
          this.customShow = true;
        }else {
          Dialog({ message: '查看的开始时间必须小于或等于结束时间！！' })
        }
      }else {
        Dialog({ message: '请选择查看开始时间及结束时间！！' })
      }

    },

    /*自定义选择最近多少天*/

    time_7(){//近七天
      this.fun_date(7);
    },
    time_30(){//近三十天
      this.fun_date(30);
    },
    fun_date(num) { //计算最近多少天
      var date1 = new Date();
      //今天时间
      this.timeEnd = this.timeFormat(date1);
      var date2 = new Date(date1);
      date2.setDate(date1.getDate() - num);
      //num是正数表示之后的时间，num负数表示之前的时间，0表示今天
      this.timeStart = this.timeFormat(date2);
      console.log(this.timeStart,this.timeEnd);
    },

/*rankingCode transactionNum 笔数排名 transactionAmount 金额排名大到小desc,小到大asc*/
    number(){//笔数排名
        this.Numranking=! this.Numranking
        if(this.Numranking){
          this.rankingCode = 'transactionNum asc ';
        }else {
          this.rankingCode = 'transactionNum desc ';
        }
        this.selectRank = 'transactionNum';
        this.pageNum=1;
        this.getAllShopList();
      },

    sum(){//金额排名
      this.Amountranking=! this.Amountranking
      if(this.Amountranking){
        this.rankingCode = 'transactionAmount  desc';
      }else {
        this.rankingCode = 'transactionAmount asc ';
      }
      this.selectRank = 'transactionAmount';
      this.pageNum=1;
      this.getAllShopList();
    },


    onClick(name) {//点击切换tab，数据初始化
      this.allShow = false;
      this.customShow = false;
      console.log(name);
      if(name=='a'){
        this.allShow = true;
        this.timeEnd='';
        this.timeStart='';
      }
      if(name=='b'){
        this.time_7();
      }
      if(name=='c'){
        this.time_30();
      }
      if(name=='d'){
        this.showTime = true;
      }
      this.loading=false;
      this.finished=false;
      this.pageNum=0;//页码重置
      this.mchName='';//搜索的商户名
      this.shopList=[]; //清空数组

    }
  }
};
</script>
<style lang="stylus" ref="stylesheet/stylus" scoped>

  @import "../style/common/base.styl";
  .searchBob{
    margin-bottom: vw(10);
  }

  .p_f{
    /*position: relative;*/

  .sunxu{
    width:100% ;
    height: vw(60);
    font-size: vw(28);
    display: flex;
    justify-content: flex-end;
    align-items: center;
    background-color: #eeeeee;
    position:absolute;
    top:vw(88);
    z-index:999;
    .rank{
      color:#699dd7;
      border-bottom-color:#699dd7;
    }

  div{
    margin-right: vw(30);

  img{
    width: vw(22);
    height: vw(22);
  }
  }
  }
  }

  ul {
    width: 100%;
    padding: vw(60) vw(40) vw(100) vw(40);
    box-sizing:border-box;
    background-color:#fff;

    li {
      width: 100%;
      height: vw(94);
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 1px solid #EEEEEE;
      background-color: #ffffff;
      .shopName{
        font-size: vw(32);
        color: #333333;
        width: vw(360);

      }
      .number{
        text-align: left;
      }
      .sum{
        text-align: right;
      }
      .wd{
        font-size: vw(28);
        color: #999999;
        width: vw(200);
        display: inline-block;
      }
    }
  }
  .modal-wrap{
    width: 100%;
    height: 100%;
    position: fixed;
    top:0;
    right: 0;
    bottom: 0;
    left: 0;
    background-color: rgba(0,0,0,0.5);
    z-index: 999;
    .timePopup{
      width: 95%;
      margin: auto;
      /* height: vw(340);*/
      border-radius: vw(10);
      position: relative;
      top: 30%;
      background-color: #FFFFFF;
      padding:0 vw(30);
      box-sizing: border-box;
      .text{
        height: vw(80);
        .titleBar{
          font-size: vw(40);
        }
        span{
          font-size: vw(30);
        }
        button{
          width: vw(160);
          height: vw(60);
          background-color: #699dd7;
          color: #fff;
          border-radius: 3px;
        }
       }

    .btn{
      height: vw(100);

      button{
        width: vw(170);
        height: vw(60);
        border-radius: 3px;

      }
      .cancel{
        border:none;
        color: #699dd7;
        background-color: #fff;
      }
      .affirm{
        background-color: #699dd7;
        color: #fff;
        border: none;
        margin-left: vw(10);
      }
    }
  }
  }


</style>
