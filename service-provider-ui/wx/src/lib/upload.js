import {
  common
} from "@/assets/api/interface";
const uploadImg = {
  async uploadImgRequest(that, file) { //图片上传
    //图片上传
    let data = new FormData();
    data.append("file", file);
    console.log(data);
    let info = await common.uploadImg(data);
    if (info.data.resultCode == "200") {
      let resultMsg = JSON.parse(info.data.resultMsg);
      let fullUrl = resultMsg.uri + "" + resultMsg.url;
      /* that[that.uploadType].push(fullUrl );  */
      that.params[that.uploadType] = fullUrl;
      that.photos[that.uploadType] = [{
        url: fullUrl
      }];
      /*           let certifyType='';
                switch (that.uploadType) {
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
                  case "otherPhoto2":
                    certifyType = "24";
                    break;
                  default:
                    certifyType = "";
                }
                let imgObj = {
                  certifyType: certifyType,
                  scanCopyPath:resultMsg.imagePath,
                  certifyNo: ""
                };
                that.custScanCopys.push(imgObj); */
    }
  },
  getAllPhotos(obj) {
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
        case "otherPhoto2":
          certifyType = "24";
          break;
        case "identityCardFront"://身份证正
          certifyType = "00";
          break;
          case "identityCardReverse"://身份证反
            certifyType = "16";
            break;
            case "licenceForOpeningAccounts"://开户许可证
            certifyType = "03";
            break;
        default:
          certifyType = "";
      }
      let imgObj = {
        certifyType: certifyType,
        scanCopyPath: obj[key][0].url,
        certifyNo: ""
      };
      custScanCopys.push(imgObj);
    }
    console.log(custScanCopys);
    return JSON.stringify(custScanCopys);
  },
}
//保存身份证正面00 个人身份证正面  01 税务登记证  02 营业执照 03 开户证件 04商户身份信息
//05 银行卡扫描件 06 其他证件 18店内照  11行业资质照  12电子签名照
//  *  13 银行卡正面  14  银行卡反面  15合作证明函  16 个人身份证反面   18 店面内景   19 手持身份证正面
// 20 店面门头照   21 店面前台照  22 合作证明函   23其他1 24其他2
export default uploadImg