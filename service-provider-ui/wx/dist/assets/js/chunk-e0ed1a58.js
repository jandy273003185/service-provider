(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-e0ed1a58"],{"1ea9":function(t,e){t.exports="data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABcAAAAXCAIAAABvSEP3AAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA25pVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOk9yaWdpbmFsRG9jdW1lbnRJRD0ieG1wLmRpZDo1NjgwZDNiNC04MDUyLWU3NGItOWI3ZC01MmQ3OTE5NjNkYjgiIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6NTRFNUNGOEIwMTUzMTFFQTlGMDZCQ0Q3MzVGMDU0QjIiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6NTRFNUNGOEEwMTUzMTFFQTlGMDZCQ0Q3MzVGMDU0QjIiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENTNiAoV2luZG93cykiPiA8eG1wTU06RGVyaXZlZEZyb20gc3RSZWY6aW5zdGFuY2VJRD0ieG1wLmlpZDo0REU4MjUyQzNGMDBFQTExQjM0MUFBQjcwMjA1NjVCRiIgc3RSZWY6ZG9jdW1lbnRJRD0ieG1wLmRpZDo1NjgwZDNiNC04MDUyLWU3NGItOWI3ZC01MmQ3OTE5NjNkYjgiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz53J+vTAAAAfElEQVR42mJ89+4dAwaoXv8SSLYGihMpzoRpxL4bXxlIBEyYRuy9/oUiU8gzAsUUso1AmEKJEVBTKDQCCFiA2EmDG4gwY5T8OKIoXEZNGfamsJChBy1NAlMspW4BGuGsycNEuREUhQvcCPJNQTaCTFPQjCDTFDQjgAAgwADoOS+E7iXsxQAAAABJRU5ErkJggg=="},"92ae":function(t,e,s){"use strict";var i=s("e86f"),n=s.n(i);n.a},e524:function(t,e,s){"use strict";s.r(e);var i=function(){var t=this,e=t.$createElement,i=t._self._c||e;return i("div",[i("BaseHeader"),i("div",{staticClass:"goods"},[i("div",{staticClass:"searchBob"},[i("van-search",{attrs:{placeholder:"请输入商户名称","show-action":"",shape:"round"},on:{search:t.onSearch},model:{value:t.mchName,callback:function(e){t.mchName=e},expression:"mchName"}},[i("div",{attrs:{slot:"action"},on:{click:t.onSearch},slot:"action"},[t._v("搜索")])])],1),i("van-tabs",{staticClass:"p_f",attrs:{swipeable:"","title-active-color":"#699dd7",color:"#699dd7"},on:{click:t.onClick},model:{value:t.active,callback:function(e){t.active=e},expression:"active"}},[i("div",{staticClass:"sunxu"},[i("div",{on:{click:t.number}},[i("span",[t._v("笔数排名")]),i("img",{attrs:{src:s("1ea9"),alt:""}})]),i("div",{on:{click:t.sum}},[i("span",[t._v("金额排名")]),i("img",{attrs:{src:s("1ea9"),alt:""}})])]),i("van-tab",{attrs:{title:"全部",name:"a"}},[i("ul",[t.allShow?i("van-list",{attrs:{finished:t.finished,"finished-text":"暂无更多数据"},on:{load:t.loadList},model:{value:t.loading,callback:function(e){t.loading=e},expression:"loading"}},t._l(t.shopList,function(e,s){return i("li",{key:s},[i("span",{staticClass:"shopName"},[t._v(t._s(s+1)+" "+t._s(e.cust_name)+" ")]),i("span",{staticClass:"number  wd"},[t._v(t._s(e.transactionNum)+" 笔")]),i("span",{staticClass:"sum  wd"},[t._v(t._s(e.transactionAmount)+" 元")])])}),0):t._e()],1)]),i("van-tab",{attrs:{title:"近7天",name:"b"}},[i("ul",["b"==t.active?i("van-list",{attrs:{finished:t.finished,"finished-text":"暂无更多数据"},on:{load:t.loadList},model:{value:t.loading,callback:function(e){t.loading=e},expression:"loading"}},t._l(t.shopList,function(e,s){return i("li",{key:s},[i("span",{staticClass:"shopName"},[t._v(t._s(s+1)+" "+t._s(e.cust_name)+" ")]),i("span",{staticClass:"number  wd"},[t._v(t._s(e.transactionNum)+" 笔")]),i("span",{staticClass:"sum  wd"},[t._v(t._s(e.transactionAmount)+" 元")])])}),0):t._e()],1)]),i("van-tab",{attrs:{title:"近30天",name:"c"}},[i("ul",["c"==t.active?i("van-list",{attrs:{finished:t.finished,"finished-text":"暂无更多数据"},on:{load:t.loadList},model:{value:t.loading,callback:function(e){t.loading=e},expression:"loading"}},t._l(t.shopList,function(e,s){return i("li",{key:s},[i("span",{staticClass:"shopName"},[t._v(t._s(s+1)+" "+t._s(e.cust_name)+" ")]),i("span",{staticClass:"number  wd"},[t._v(t._s(e.transactionNum)+" 笔")]),i("span",{staticClass:"sum  wd"},[t._v(t._s(e.transactionAmount)+" 元")])])}),0):t._e()],1)]),i("van-tab",{attrs:{title:"自定义时间",name:"d"}},[i("ul",[t.customShow?i("van-list",{attrs:{finished:t.finished,"finished-text":"暂无更多数据"},on:{load:t.loadList},model:{value:t.loading,callback:function(e){t.loading=e},expression:"loading"}},t._l(t.shopList,function(e,s){return i("li",{key:s},[i("span",{staticClass:"shopName"},[t._v(t._s(s+1)+" "+t._s(e.cust_name)+" ")]),i("span",{staticClass:"number wd"},[t._v(t._s(e.transactionNum)+" 笔")]),i("span",{staticClass:"sum wd"},[t._v(t._s(e.transactionAmount)+" 元")])])}),0):t._e()],1)])],1),i("div",{directives:[{name:"show",rawName:"v-show",value:t.showTime,expression:"showTime"}],staticClass:"modal-wrap"},[i("div",{staticClass:"timePopup"},[t._m(0),i("div",{staticClass:"text flex_s_b",attrs:{plain:"",type:"primary"},on:{click:function(e){return t.showPopFn("timeStart")}}},[i("button",[t._v("开始时间")]),i("span",[t._v(t._s(t.timeStart))])]),i("div",{staticClass:"text flex_s_b ",attrs:{plain:"",type:"primary"},on:{click:function(e){return t.showPopFn("timeEnd")}}},[i("button",[t._v("结束时间")]),i("span",[t._v(t._s(t.timeEnd))])]),i("van-popup",{style:{height:"40%"},attrs:{position:"bottom"},model:{value:t.show,callback:function(e){t.show=e},expression:"show"}},[i("van-datetime-picker",{attrs:{type:"date","max-date":t.maxDate},on:{change:function(e){return t.changeFn()},confirm:function(e){return t.confirmFn()},cancel:function(e){return t.cancelFn()}},model:{value:t.currentDate,callback:function(e){t.currentDate=e},expression:"currentDate"}})],1),i("div",{staticClass:"btn flex_r"},[i("button",{staticClass:"cancel",on:{click:t.cancel}},[t._v("取消")]),i("button",{staticClass:"affirm",on:{click:t.affirm}},[t._v("确认")])])],1)])],1)],1)},n=[function(){var t=this,e=t.$createElement,s=t._self._c||e;return s("div",{staticClass:"text flex_center"},[s("span",{staticClass:"titleBar"},[t._v("时间")])])}],a=(s("8e6e"),s("ac6a"),s("456d"),s("e17f"),s("2241")),o=(s("96cf"),s("3b8d")),c=s("bd86"),r=s("dae5"),l=s("e18b"),h=s("943c"),u=s("2f62");function m(t,e){var s=Object.keys(t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(t);e&&(i=i.filter(function(e){return Object.getOwnPropertyDescriptor(t,e).enumerable})),s.push.apply(s,i)}return s}function d(t){for(var e=1;e<arguments.length;e++){var s=null!=arguments[e]?arguments[e]:{};e%2?m(s,!0).forEach(function(e){Object(c["a"])(t,e,s[e])}):Object.getOwnPropertyDescriptors?Object.defineProperties(t,Object.getOwnPropertyDescriptors(s)):m(s).forEach(function(e){Object.defineProperty(t,e,Object.getOwnPropertyDescriptor(s,e))})}return t}var p={name:"goods",components:{BaseHeader:r["a"]},data:function(){return{loading:!1,finished:!1,active:0,pageNum:0,mchName:"",shopList:[],userId:"",maxDate:new Date,currentDate:new Date,changeDate:new Date,show:!1,timeStart:"",startMs:"",timeEnd:"",endMs:"",showTime:!1,type:"",customShow:!1,allShow:!0,Numranking:!1,Amountranking:!1,rankingCode:"transactionNum desc"}},computed:d({},Object(u["c"])(["roleId"])),mounted:function(){this.userId=l["a"].get("userId"),console.log(this.userId+"交易"),console.log(this.timeStart),console.log(this.timeEnd)},methods:{loadList:function(){console.log("loadlist"),this.pageNum+=1,this.getAllShopList()},getAllShopList:function(){var t=Object(o["a"])(regeneratorRuntime.mark(function t(){var e,s;return regeneratorRuntime.wrap(function(t){while(1)switch(t.prev=t.next){case 0:return console.log(897788),t.next=3,h["c"].goodsInfo({custName:this.mchName,queryStartDate:this.timeStart,queryEndDate:this.timeEnd,userId:this.userId,pageSize:"20",pageNum:this.pageNum,rankingCode:this.rankingCode,roleId:this.roleId});case 3:e=t.sent,this.shopList=e.data.resultMsg.list,s=e.data.resultMsg.total,console.log(e),console.log(this.shopList),this.shopList.length>=s&&(this.finished=!0,this.loading=!1);case 9:case"end":return t.stop()}},t,this)}));function e(){return t.apply(this,arguments)}return e}(),onSearch:function(){this.pageNum=1,this.active="a",this.customShow=!1,this.allShow=!0,this.timeEnd="",this.timeStart="",this.getAllShopList()},showPopFn:function(t){this.show=!0,this.type=t},changeFn:function(){this.changeDate=this.currentDate},confirmFn:function(){console.log(this.type),"timeStart"==this.type&&(this.timeStart=this.timeFormat(this.currentDate)),"timeEnd"==this.type&&(this.timeEnd=this.timeFormat(this.currentDate)),this.show=!1},cancelFn:function(){this.show=!1},timeFormat:function(t){var e=t.getFullYear(),s=t.getMonth()+1,i=t.getDate();return e+"-"+s+"-"+i},cancel:function(){this.showTime=!1},affirm:function(){this.startMs=new Date(this.timeStart).getTime(),this.endMs=new Date(this.timeEnd).getTime(),this.startMs&&this.endMs?this.startMs<=this.endMs?(this.showTime=!1,console.log(this.timeStart,this.timeEnd),this.customShow=!0):Object(a["a"])({message:"查看的开始时间必须小于或等于结束时间！！"}):Object(a["a"])({message:"请选择查看开始时间及结束时间！！"})},time_7:function(){this.fun_date(7)},time_30:function(){this.fun_date(30)},fun_date:function(t){var e=new Date;this.timeEnd=this.timeFormat(e);var s=new Date(e);s.setDate(e.getDate()-t),this.timeStart=this.timeFormat(s),console.log(this.timeStart,this.timeEnd)},number:function(){this.Numranking=!this.Numranking,this.Numranking?this.rankingCode="transactionNum asc ":this.rankingCode="transactionNum desc ",this.pageNum=1,this.getAllShopList()},sum:function(){this.Amountranking=!this.Amountranking,this.Amountranking?this.rankingCode="transactionAmount  desc":this.rankingCode="transactionAmount asc ",this.pageNum=1,this.getAllShopList()},onClick:function(t){this.allShow=!1,this.customShow=!1,console.log(t),"a"==t&&(this.allShow=!0,this.timeEnd="",this.timeStart=""),"b"==t&&this.time_7(),"c"==t&&this.time_30(),"d"==t&&(this.showTime=!0),this.loading=!1,this.finished=!1,this.pageNum=0,this.mchName="",this.shopList=[]}}},g=p,v=(s("92ae"),s("2877")),f=Object(v["a"])(g,i,n,!1,null,"5ad97a40",null);e["default"]=f.exports},e86f:function(t,e,s){}}]);