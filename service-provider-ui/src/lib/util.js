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
    getPhotos(that,urlHead,photos){
        for (let i = 0; i < photos.length; i++) {
            let imgname = this.getImgName(photos[i].scanCopyPath);
            if (photos[i].certifyType == "02") {
              //营业执照
              that.photos.businessLicense = [{ url: urlHead + "" + imgname }];
              //this.params.businessLicense=urlHead+''+imgname;
            }
            if (photos[i].certifyType == "20") {
              //门头照
              that.photos.shopFrontDesk = [{ url: urlHead + "" + imgname }];
              that.params.shopFrontDesk = urlHead + "" + imgname;
            }
            if (photos[i].certifyType == "21") {
              //店内照
              that.photos.shopInterior = [{ url: urlHead + "" + imgname }];
              that.params.shopInterior = urlHead + "" + imgname;
            }
            if (photos[i].certifyType == "11") {
              //行业资质
              that.photos.specialBusiness = [{ url: urlHead + "" + imgname }];
              that.params.specialBusiness = urlHead + "" + imgname;
            }
            if (photos[i].certifyType == "12") {
              //电子签名
              that.photos.electronicSignaturePhoto = [
                { url: urlHead + "" + imgname }
              ];
              that.params.electronicSignaturePhoto = urlHead + "" + imgname;
            }
            if (photos[i].certifyType == "23") {
              //其他资料照 1
              that.photos.otherPhoto1 = [{ url: urlHead + "" + imgname }];
              that.params.otherPhoto1 = urlHead + "" + imgname;
            }
            if (photos[i].certifyType == "24") {
              //其他资料照 2
              that.photos.otherPhoto2 = [{ url: urlHead + "" + imgname }];
              that.params.otherPhoto2 = urlHead + "" + imgname;
            }
            if (photos[i].certifyType == "00") {
                //身份证正面照
                that.photos.identityCardFront = [{ url: urlHead + "" + imgname }];
                that.params.identityCardFront = urlHead + "" + imgname;
              }
              if (photos[i].certifyType == "16") {
                //身份证反面照
                that.photos.identityCardReverse = [{ url: urlHead + "" + imgname }];
                that.params.identityCardReverse = urlHead + "" + imgname;
              }
              if (photos[i].certifyType == "03") {
                //身份证正面照
                that.photos.licenceForOpeningAccounts = [
                  { url: urlHead + "" + imgname }
                ];
                that.params.licenceForOpeningAccounts = urlHead + "" + imgname;
              }
          }
    },
}
export default util