const form = {
  validParams: function (that,params) { //校验必填信息 多形态   添加进件
    let arr = Object.keys(params);
    let errCount = 0;
    arr.map(function (item) {
      if (!params[item]&&item!='otherPhoto1'&&item!="otherPhoto2"&&item!="custId"&&item!='userId'&&item!='roleId'&&item!='custId'&&item!='licenceForOpeningAccounts'&&item!='bankCardFront') {
        console.log(item);
        errCount++;
      } else {
        if(that.params.compMainAcctType=='01'&& !params['licenceForOpeningAccounts']){
          console.log("未上传营业执照");
          errCount++;
        }
        if(that.params.compMainAcctType=='02'&& !params['bankCardFront']){
          console.log("未上传银行卡照片");
          errCount++;
        }
        if (item == 'merchantAccount' || item == 'contactMobile') {
          if (!(/^1[3456789]\d{9}$/.test(params[item]))) {
            that.$toast("手机号码格式有误！")
            errCount++;
          }
        }
        if (item == 'contactPhone') {
          if (!/^(\(\d{3,4}\)|\d{3,4}-|\s)?\d{7,14}$/.test(params[item])) {
            that.$toast('电话号码格式有误！');
            errCount++;
          }
        }
      }
    })
    console.log("errCount:"+errCount);
    return errCount;
  },
  throttle: function (fn, gapTime) {
    let _lastTime = null
    return function () {
      let _nowTime = +new Date()
      if (_nowTime - _lastTime > gapTime || !_lastTime) {
        fn.apply(this, arguments) //将this和参数传给原函数
        _lastTime = _nowTime
      }
    }
  },
  debounce: function (fn, delay) { //防抖函数 按钮多次提交执行最后一次
    let delays = delay || 2000,
      timer,
      _this = this,
      args = arguments;
    return () => {
      if (timer) {
        clearTimeout(timer);
      }
      timer = setTimeout(function () {
        timer = null;
        fn.apply(_this, args)
      }, delays)
    }
  },
}
export default form