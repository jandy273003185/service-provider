import http from "./http";
export const common={//图片识别 上传
  getImgInfo: params =>http.post1('/common/youTu',params),
  uploadImg: params =>http. post('/common/upload',params),
  getAddress:params=>http.post1('/common/province',params),//商户省市区
  bankAddress:params=>http.post1('/common/bankProvince',params),//银行省市
  resetPwd:params=>http.post1('/salesman/regPassword',params),//重置密码
  updateSales:params=>http.post1('/salesman/update',params),//更新业务员
  insertSales:params=>http.post1('/salesman/insert',params),//新增业务员
}
export const incoming={//进件
  checkPhone:params=>http.post1('/merchant/checkMobile',params),//校验商户账号
  insertIncoming: params =>http.post1('/merchant/insertMerchant',params),
  getIncoming:params=>http.post1('/merchant/queryMerchant',params),  //获取进件信息
};
export const adminIndex={//管理员主页
  searchSales: param =>http.post1('/salesman/query',param),//搜索业务员
  SalesRanking: param =>http.post1('/salesman/performance',param),//业务员排名
  salesDetail: param =>http.post1('/salesman/get',param),//点击单个业务员查看业绩详情
};
export const login={//登录接口
  login:param =>http.post1('/user/loginBinding',param),//登录页接口
  firstLogin:param =>http.post1('/user/login',param),//进入主页时判断是否绑定
  getOpenId:param =>http.post1('/wx/callback',param)//获取openid
};
export const homeShopInfo={//首页里的商户信息
  shopInfo:param =>http.post1('/merchant/getStatCommercial',param)  //首页商户信息
};
export const shopAuditInfo={//商户页面审核信息
  allShopAuditInfo:param =>http.post1('/salesman/selectSalesmanCommercialInfo',param),  //管理员商户页面审核信息接口
  shopAuditInfo:param =>http.post1('/merchant/selectCommercialInfo',param),  //业务员商户页面审核信息接口
  shopsucceedInfo:param =>http.post1('/merchant/queryMerchantById',param),  //审核通过信息接口
  shopDefeatedInfo:param =>http.post1('/merchant/getCommerAuditCause',param),  //审核失败信息接口
  pro_contract:param =>http.post1('/merchant/insertProduct',param),//商户审核通过后的产品签约提交
  contractInfo:param =>http.post1('/merchant/queryProduct',param)//商户审核通过后的产品已签约信息
};
export const goodsInfo={//交易栏的商户交易排名信息
  goodsInfo:param =>http.post1('/merchant/getDealRanking',param),  //业务员交易栏的商户交易排名信息
  allGoodsInfo:param =>http.post1('/salesman/getServiceProviderDealRanking',param)  //管理员交易栏的商户交易排名信息
};
/*export const mineInfo={//我的信息
  salesman:param =>http.post1('/salesman/query',param),  //业务员请求个人信息
  agent:param =>http.post1('/merchant/queryMerchantById',param)  //管理员请求个人信息
};*/
