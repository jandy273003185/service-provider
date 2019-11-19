import http from "./http";
export const common={//图片识别 上传
  getImgInfo: params =>http.post1('/common/youTu.do',params),
  uploadImg: params =>http. post('/common/upload.do',params),
  getAddress:params=>http.post1('/common/province.do',params),//商户省市区
  bankAddress:params=>http.post1('/common/bankProvince.do',params),//银行省市
  resetPwd:params=>http.post1('/salesman/regPassword.do',params),//重置密码
  updateSales:params=>http.post1('/salesman/update.do',params),//更新业务员
  insertSales:params=>http.post1('/salesman/insert.do',params),//新增业务员
}
export const incoming={//进件
  insertIncoming: params =>http.post1('/wx/insertMerchant.do',params),
  getIncoming:params=>http.post1('/wx/queryMerchant.do',params),  //获取进件信息
};
export const adminIndex={//管理员主页
  searchSales: param =>http.post1('/salesman/query.do',param),//搜索业务员
  SalesRanking: param =>http.post1('/salesman/performance.do',param),//业务员排名
  salesDetail: param =>http.post1('/salesman/get.do',param),//点击单个业务员查看业绩详情
};
export const login={//登录接口
  login:param =>http.post1('/user/loginBinding',param),//登录页接口
  firstLogin:param =>http.post1('/user/login',param)//进入主页时判断是否绑定
};
export const homeShopInfo={//首页里的商户信息
  shopInfo:param =>http.post1('/wx/getStatCommercial.do',param)  //首页商户信息
};
export const shopAuditInfo={//商户页面审核信息
  shopAuditInfo:param =>http.post1('/wx/selectCommercialInfo.do',param),  //商户页面审核信息接口
  shopsucceedInfo:param =>http.post1('/wx/queryMerchantById.do',param),  //审核通过信息接口
  shopDefeatedInfo:param =>http.post1('/wx/getCommerAuditCause.do',param),  //审核失败信息接口
  pro_contract:param =>http.post1('/wx/insertProduct.do',param),//商户审核通过后的产品签约提交
  contractInfo:param =>http.post1('/wx/queryProduct.do',param)//商户审核通过后的产品已签约信息
};
export const goodsInfo={//交易栏的商户交易排名信息
  goodsInfo:param =>http.post1('/wx/getDealRanking.do',param)  //交易栏的商户交易排名信息
};
