import Vue from "vue";
import Vuex from "vuex";
import Cookie from "js-cookie";
Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        token:'', //localStorage.getItem("token") || null,,
        openId:'', //进入首页即请求openID
        role:'', //进入页面的角色，管理员agent或者业务员salesman，登录时传给后台
        salesID:'', //管理员查找业务员时得到的ID，可用于查看业务员个人信息
        LookName:'', //管理员正在查看的业务员的名字
        /*userId:''  //管理员或者业务员的个人id*/
        custId:'',
        userId:'',
        roleId:'',//3是管理员，2是业务员
        incoming: {
            userId:'7a25180eefee423a992d29d9712b6f9d',
        },
        custScanCopys:[]
    },



    mutations: {
        setToken(state, obj) {
            state.token = obj;
        },
        setOpenID(state, obj) {
            state.oPenID = obj;
        },
    
        setRole(state, obj) {
            state.role = obj; //确认是哪种角色进入
        },
        selectName(state,sales){//管理员想查看的业务员的名字
            state.LookName = sales.name;
            state.salesID = sales.id;
        },
        setCustId(state,obj){
            state.custId=obj;
        },
        setUserId(state,obj){
            state.userId=obj;
        },
        setincoming(state, obj) {//进件参数
            state.incoming = obj;
        },
        custScanCopys(state,obj){//图片数组
          state.custScanCopys=obj
        },
        setRoleId(state,obj){
            state.roleId=obj;
        }
    },
    actions: {

    }
});

