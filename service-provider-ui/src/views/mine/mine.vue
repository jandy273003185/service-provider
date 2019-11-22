<template>
    <div class="mine">
        <div class="info">
            <img class="photo"
                 src="https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2018939532,1617516463&fm=26&gp=0.jpg"
            />
            <div class="text">
                <span>xxxx有限公司</span>
                <div class="name">
                    <span v-if="roleId==2">管理员:</span>
                    <span v-if="roleId==3">业务员:</span>
                    <!--<span>{{userName}}</span>-->
                </div>
            </div>
        </div>
        <div class="menu">
            <router-link to="/amendPsd" class="item" v-if="roleId=='3'">修改登录密码
                <van-icon class="icon" name="arrow"/>
            </router-link>
            <router-link to="/salesManage" class="item" v-if="roleId=='2'">业务员管理
                <van-icon class="icon" name="arrow"/>
            </router-link>
            <router-link to="/about" class="item">关于
                <van-icon class="icon" name="arrow"/>
            </router-link>
            <div class="item">
                <span>联系客服</span>
                <span>400-633-0707</span>
            </div>
        </div>
        <!--<div class="logout">退出登录</div>-->
        <BaseHeader></BaseHeader>
    </div>
</template>
<script>
    import {mapState} from 'vuex';
    import BaseHeader from "@/components/baser-header.vue";
    import {mineInfo} from "../../assets/api/interface";
    export default {
        name: "mine",
        components: {
            BaseHeader
        },
        data(){
            return {
                userName:'',
                companyName:''
            };
        },
        computed: {
                ...mapState(['roleId','userId'])
        },
        created(){

        },
        mounted(){
            this.getUserName();

        },
        methods: {
            async getUserName(){
                if(this.roleId == '2'){//管理员
                    const infoData=await mineInfo.agent({custId:this.userId});
                    console.log('管理员信息');
                    console.log(infoData);
                }
                if(this.roleId == '3'){//业务员
                    const infoData=await mineInfo.salesman({id:this.userId});
                    console.log('业务员信息');
                    console.log(infoData);
                }
            }

        }
    };
</script>
<style lang="scss" scoped>
    @import "../../style/views/mine.scss";

    .mine {

        .menu {

            .item {
                display: flex;
                justify-content: space-between;
                align-items: center;
            }

        }
    }
</style>