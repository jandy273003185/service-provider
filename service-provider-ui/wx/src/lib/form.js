const form = {
    focusEvent: function (context, ele) { //输入框聚焦
        context.$refs[ele].style.color = '#333'
    },
    blurEvent: function (context, ele, type) { //输入框失去焦点
      if (type) {
        if (context.params[ele].length == 0) {
          context.$refs[ele].style.color = '#fd5e6b'
        } else {
          context.$refs[ele].style.color = '#333'
        }
      } else {
        if (context[ele].length == 0) {
          context.$refs[ele].style.color = '#fd5e6b'
        } else {
          context.$refs[ele].style.color = '#333'
        }
      }
  
    },
    validInpParams: function (context, params) { //提交文本校验 一对一校验 登录
      let arr = Object.keys(params);
      let errCount = 0;
      arr.map(function (item, index) {
        if (params[item]) {
          context.$refs[item].style.color = '#333'
        } else {
          errCount++;
          context.$refs[item].style.color = '#fd5e6b'
        }
      })
      return errCount;
    },
    validParams: function (params) { //校验必填信息 多形态   添加进件
      let arr = Object.keys(params);
      let errCount = 0;
      arr.map(function (item, index) {
        if (!params[item]) {
          errCount++;
        } 
      })
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