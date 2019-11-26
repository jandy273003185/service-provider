<template>
    <div>
        <van-nav-bar class="navBar" left-text="搜索商户"  left-arrow @click-left="onClickLeft"/>
        <van-search
                v-model="value"
                placeholder="请输入商户名称"
                show-action autofocus="autofocus"
                shape="round"
                @search="onSearch"
        >
            <div slot="action" @click="onSearch">搜索</div>
        </van-search>
        <ul>
            <van-list v-if="active"
                      v-model="loading"
                      :finished="finished"
                      finished-text="暂无更多数据"
                      @load="loadList"
            >
                <li v-for="(item, index) in statesList" :key="index" @click="toDetail(item.state,item.custId)">
                    <div>
                        <span class="shopName">{{ item.custName }}</span>
                        <span class="time">{{ item.createTime }}</span>
                    </div>
                    <span v-if="item.state=='00'" class=" state state_0">审核通过</span>
                    <span v-if="item.state=='01'"  class=" state state_1">待审核</span>
                    <span v-if="item.state=='04'" class=" state state_4">审核失败</span>
                    <span v-if="item.state=='05'" class=" state state_5">待完善</span>


                </li>
            </van-list>
        </ul>
    </div>

</template>

<script type="text/ecmascript-6">

    import {shopAuditInfo} from "../assets/api/interface";
    import storage from '../assets/modeljs/storage.js';
    import {mapState} from 'vuex';
    import {mapMutations} from 'vuex';

export default {
    name: "searchShop",
    data(){
        return {
            //按页加载
            loading:false,
            finished: false,
            pageNum:0,//页数
            value:'',
            active:false,
            statesList:[]
        }
    },
    computed:{
        ...mapState(['roleId','userId'])
    },
    created(){
        this.$store.commit("setincoming", {});
        this.$store.commit("setPhotos", []);
        this.$store.commit("setCheckedState", "");
    },
    mounted(){
        console.log(this.userId + '商户');
    },
    methods: {
        onClickLeft() {
            this.$router.go(-1);
        },
        onSearch(){//将this.value传到后台
            if(this.value){
                this.pageNum=1;
                this.timeStart='';//选取的开始时间
                this.timeEnd='';
                this.active = true;
                this.getAllShopList();
            }else {
                this.$toast('请输入商户名称');
                return;
            }

        },
        loadList(){
            this.pageNum+=1;
            this.getAllShopList();
        },

        async getAllShopList(){

            let listInfo=await shopAuditInfo.shopAuditInfo({
                custName:this.value,//商户名
                userId:this.userId,
                pageSize:'20',
                pageNum:this.pageNum,
                roleId:this.roleId
            });
            let total=listInfo.data.resultMsg.total;
            let list = listInfo.data.resultMsg.data;
            this.statesList = this.statesList.concat(list);


            console.log(listInfo.data.resultMsg.list);
            console.log(this.statesList.length);
            this.loading = false;
            if(this.statesList.length>=total){//判断已加载完成
                this.finished=true;
            }
        },
        //查看审核失败信息和审核成功信息
        toDetail(state,custId){
            if(state=='04'){
                this.$router.push('whyFailed');
                this.setCustId(custId);
            }
            if(state=='00'){
                this.$router.push('/audit/pass');
                this.setCustId(custId);
            }
            if (state == "05") {
                this.$router.push({
                    name: "baseInfo",
                    params: { type: "corvidae", custId: custId }
                });
            }
        },


        ...mapMutations(['setCustId']),


    }
}
</script>

<style scoped lang="stylus" ref="stylesheet/stylus">

    @import "../style/common/base.styl";
    ul{
        width: 100%;
        margin-top: vw(20);
        background-color: #fff;

    li{
        width: vw(720);
        height: vw(94);
        margin: 0 auto;
        display: flex;
        justify-content:space-between;
        align-items: center;
        border-bottom: 1px solid #EEEEEE;

    div{
        color: #333333;
        height: 100%;
        margin: 0;
        display: flex;
        flex-direction: column;
        justify-content:space-around;
        align-items: flex-start;
    .shopName{
        color: #030303;
        font-size: vw(32);
    }
    .time{
        color: #969696;
        font-size: vw(22);
    }
    }
    .state{
        font-size: vw(32);
    }
    .state_0{ color: #3fd016}
    .state_1{color: #aeaeae;}
    .state_4{color: #ff495d;}
    .state_5{color: #3f9aff;}
    }
    }



</style>
