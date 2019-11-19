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
}
export default util