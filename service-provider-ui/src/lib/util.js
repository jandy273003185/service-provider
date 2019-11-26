const util = {
  fun_date: function (num) {
    var date1 = new Date();
    //今天时间
    let timeEnd = this.timeFormat(date1);
    var date2 = new Date(date1);
    date2.setDate(date1.getDate() - num);
    //num是正数表示之后的时间，num负数表示之前的时间，0表示今天
    let timeStart = this.timeFormat(date2);
    let obj = {
      timeEnd: timeEnd,
      timeStart: timeStart
    }
    return obj
  },

  timeFormat: function (time) { // 时间格式化 2019-09-08
    let year = time.getFullYear();
    let month = time.getMonth() + 1;
    let day = time.getDate();
    return year + '-' + month + '-' + day
  },
  getImgName(path) {
    let filename;
    if (path.indexOf("/") > 0) //如果包含有"/"号 从最后一个"/"号+1的位置开始截取字符串
    {
      filename = path.substring(path.lastIndexOf("/") + 1, path.length);
    } else {
      filename = path;
    }
    return filename;
  },
  getPhotos(that, urlHead, photos) { //将获取的照片赋值到相应位置
    let obj={},name;
    for (let i = 0; i < photos.length; i++) {
      let imgname = this.getImgName(photos[i].scanCopyPath);
      switch (photos[i].certifyType) {
        case "02": //营业执照
          name = "businessLicense";
          break;
        case "20": //门头照
          name = "shopFrontDesk";
          break;
        case "18": //店内照
          name = "shopInterior";
          break;
        case "11": //行业资质
          name = "specialBusiness";
          break;
        case "12": // 电子签名照
          name = "electronicSignaturePhoto";
          break;
        case "23": //其他资料照
          name = "otherPhoto1";
          break;
        case "24": //其他资料照
          name = "otherPhoto2";
          break;
        case "00": //身份证正
         name = "identityCardFront";
          break;
        case "16": //身份证反
          name = "identityCardReverse";
          break;
        case "03": //开户许可证
          name = "licenceForOpeningAccounts";
          break;
        default:
          name = "";
      }
      if(name){
        obj[name]=[{
            url: urlHead + "" + imgname,
          }]
        }
       /*  that.photos[name] = [{
          url: urlHead + "" + imgname
        }]; */
        if(name!='businessLicense'){
          that.params[name] = urlHead + "" + imgname;
        }
      }
    return obj;
  },
  getAllPhotos(obj) { //提交图片集合
    console.log(obj);
    let custScanCopys = [];
    let certifyType;
    for (let key in obj) {
      switch (key) {
        case "businessLicense": //营业执照
          certifyType = "02";
          break;
        case "shopFrontDesk": //门头照
          certifyType = "20";
          break;
        case "shopInterior": //店内照
          certifyType = "18";
          break;
        case "specialBusiness": //行业资质
          certifyType = "11";
          break;
        case "electronicSignaturePhoto": // 电子签名照
          certifyType = "12";
          break;
        case "otherPhoto1": //其他资料照
          certifyType = "23";
          break;
        case "otherPhoto2": //其他资料照
          certifyType = "24";
          break;
        case "identityCardFront": //身份证正
          certifyType = "00";
          break;
        case "identityCardReverse": //身份证反
          certifyType = "16";
          break;
        case "licenceForOpeningAccounts": //开户许可证
          certifyType = "03";
          break;
        default:
          certifyType = "";
      }
      let imgObj = {
        certifyType: certifyType,
        scanCopyPath: this.getImgName(obj[key][0].url),
        certifyNo: ""
      };
      custScanCopys.push(imgObj);
    }
    console.log(custScanCopys);
    return JSON.stringify(custScanCopys);
  },
}
export default util