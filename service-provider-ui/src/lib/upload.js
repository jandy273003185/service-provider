import {
  common
} from "@/assets/api/interface";
const uploadImg = {
  async uploadImgRequest(blob,base64,name,that) { //图片上传
    //图片上传
    console.log(name);
    let data = new FormData();
    data.append("file", blob, ".jpg");
    let info = await common.uploadImg(data);
    if (info.data.resultCode == "200") {
      let resultMsg = JSON.parse(info.data.resultMsg);
      let fullUrl = resultMsg.uri + "" + resultMsg.url;
      if(name!='businessLicense'){
        that.params[name] = fullUrl;
      }
      that.photos[name] = [{
        url: fullUrl
      }];
    }
    that.$toast.clear();
  },
  /**
   * 获取到的二进制文件 转 base64文件
   * @param blob
   */
  blobToBase64(blob,name, context) {
    const self = this; // 绑定this
    const reader = new FileReader(); //实例化一个reader文件
    reader.readAsDataURL(blob); // 添加二进制文件
    reader.onload = function (event) {
      const base64 = event.target.result; // 获取到它的base64文件
      const scale = 0.8; // 设置缩放比例 （0-1）
      self.compressImg(base64, scale,name, context, self.uploadImgRequest); // 调用压缩方法
    };
  },
  /**
   * 压缩图片方法
   * @param base64  ----baser64文件
   * @param scale ----压缩比例 画面质量0-9，数字越小文件越小画质越差
   * @param callback ---回调函数
   */
  compressImg(base64, scale,name, context, callback) {
    const img = new Image();
    img.src = base64;
    img.onload = function () {
      const canvas = document.createElement("canvas");
      const ctx = canvas.getContext("2d");
      canvas.setAttribute("width", this.width);
      canvas.setAttribute("height", this.height);
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      ctx.drawImage(img, 0, 0, canvas.width, canvas.height);
      // 转成base64 文件
      let base64 = canvas.toDataURL("image/jpeg");
      // 根据自己需求填写大小
      while (base64.length > 1024 * 1024) {
        scale -= 0.2;
        base64 = canvas.toDataURL("image/jpeg", scale);
      }
      // baser64 TO blob 这一块如果不懂可以自行百度，我就不加注释了
      const arr = base64.split(",");
      const mime = arr[0].match(/:(.*?);/)[1];
      const bytes = atob(arr[1]);
      const bytesLength = bytes.length;
      const u8arr = new Uint8Array(bytesLength);
      for (let i = 0; i < bytes.length; i++) {
        u8arr[i] = bytes.charCodeAt(i);
      }
      const blob = new Blob([u8arr], {
        type: mime
      });
      // 回调函数 根据需求返回二进制数据或者base64数据，我的项目都给返回了
      callback(blob, base64, name,context);
    };
  },
}
//保存身份证正面00 个人身份证正面  01 税务登记证  02 营业执照 03 开户证件 04商户身份信息
//05 银行卡扫描件 06 其他证件 18店内照  11行业资质照  12电子签名照
//  *  13 银行卡正面  14  银行卡反面  15合作证明函  16 个人身份证反面   18 店面内景   19 手持身份证正面
// 20 店面门头照   21 店面前台照  22 合作证明函   23其他1 24其他2
export default uploadImg