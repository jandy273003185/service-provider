(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-d8986822"],{1911:function(e,t,s){e.exports=s.p+"assets/img/login.png"},a55b:function(e,t,s){"use strict";s.r(t);var r=function(){var e=this,t=e.$createElement,s=e._self._c||t;return s("div",{staticClass:"login"},[s("div",{staticClass:"box"},[e._m(0),s("p",{staticClass:"logintype"},[e._v("账号密码登录")]),s("div",{staticClass:"item"},[s("input",{directives:[{name:"model",rawName:"v-model.trim",value:e.userName,expression:"userName",modifiers:{trim:!0}}],attrs:{type:"text",placeholder:"手机号码/邮箱"},domProps:{value:e.userName},on:{input:function(t){t.target.composing||(e.userName=t.target.value.trim())},blur:function(t){return e.$forceUpdate()}}}),s("van-icon",{attrs:{name:"close"},on:{click:e.clearName}})],1),s("div",{staticClass:"item"},[s("input",{directives:[{name:"model",rawName:"v-model.trim",value:e.password,expression:"password",modifiers:{trim:!0}}],attrs:{type:"password",placeholder:"登录密码"},domProps:{value:e.password},on:{input:function(t){t.target.composing||(e.password=t.target.value.trim())},blur:function(t){return e.$forceUpdate()}}}),s("van-icon",{attrs:{name:"close"},on:{click:e.clearPsd}})],1),s("div",{staticClass:"item"},[s("button",{on:{click:e.submitLogin}},[e._v("登录")])])])])},o=[function(){var e=this,t=e.$createElement,r=e._self._c||t;return r("div",{staticClass:"title"},[r("img",{attrs:{src:s("1911"),alt:""}}),r("span",[e._v("七分钱进件系统")])])}],n=(s("8e6e"),s("ac6a"),s("456d"),s("96cf"),s("3b8d")),a=(s("e17f"),s("2241")),i=s("bd86"),c=(s("e18b"),s("2f62")),l=s("943c");function u(e,t){var s=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter(function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable})),s.push.apply(s,r)}return s}function p(e){for(var t=1;t<arguments.length;t++){var s=null!=arguments[t]?arguments[t]:{};t%2?u(s,!0).forEach(function(t){Object(i["a"])(e,t,s[t])}):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(s)):u(s).forEach(function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(s,t))})}return e}var d={name:"Login",data:function(){return{userName:null,password:null}},mounted:function(){console.log(this.$store.state.role),console.log(this.role)},computed:p({},Object(c["c"])(["role","openId"])),methods:{submitLogin:function(){this.userName?this.password?this.loginPost():Object(a["a"])({message:"密码不能为空"}):Object(a["a"])({message:"用户名账号不能为空"})},clearName:function(){this.userName=""},clearPsd:function(){this.password=""},loginPost:function(){var e=Object(n["a"])(regeneratorRuntime.mark(function e(){var t,s,r=this;return regeneratorRuntime.wrap(function(e){while(1)switch(e.prev=e.next){case 0:return t={userName:this.userName,password:this.password,openId:this.openId,roleCode:this.role},console.log(t.userName),console.log(t.password),console.log(t.openId),console.log(t.roleCode),e.next=7,l["f"].login(t);case 7:s=e.sent,console.log(s),1==s.data.resultCode?(Object(a["a"])({message:"登录成功"}),setTimeout(function(){"agent"==r.role?r.$router.push("Administrator"):"salesman"==r.role&&r.$router.push("salesman")},500)):Object(a["a"])({message:s.data.resultMsg});case 10:case"end":return e.stop()}},e,this)}));function t(){return e.apply(this,arguments)}return t}()}},m=d,f=(s("db4d"),s("2877")),g=Object(f["a"])(m,r,o,!1,null,"138256ac",null);t["default"]=g.exports},db4d:function(e,t,s){"use strict";var r=s("ddac"),o=s.n(r);o.a},ddac:function(e,t,s){}}]);