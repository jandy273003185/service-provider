package com.sevenpay.agentmanager.controller;

import com.alibaba.fastjson.JSONObject;
import com.sevenpay.agentmanager.pojo.ResultBean;
import com.sevenpay.agentmanager.utils.DateUtils;
import com.sevenpay.agentmanager.utils.YouTuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

/**
 * 文件上传和优图解析
 */
@Controller
@RequestMapping("common")
public class UploadFileController {
    private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @Value("${images.uri}")
    private String uri;
    @Value("${images.relativePath}")
    private String relativePath;
    //存储路径
    @Value("${images.absolutePaths}")
    private String absolutePaths;


    @PostMapping("upload")
    @ResponseBody
    public ResultBean fileUpload(@RequestParam("file")MultipartFile file){

        // 获取文件名后缀名
        String suffix = file.getOriginalFilename();
        String prefix = suffix.substring(suffix.lastIndexOf("."));
        String Filename = DateUtils.getDateStr8()+"_"+UUID.randomUUID().toString().replaceAll("-","");
        if (!file.isEmpty()) {//文件不为空
            try {
                //上传路径
                StringBuilder filePath = new StringBuilder(absolutePaths).append("/").append(Filename).append(prefix);
                File saveDir = new File(String.valueOf(filePath));
                // 转存文件
                file.transferTo(saveDir);
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("imagePath",Filename);
                jsonObject.put("uri",uri);
                jsonObject.put("url",new StringBuilder(relativePath).append(Filename).append(prefix));
                return new ResultBean("200",jsonObject.toJSONString());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("上传失败");
            }
        }
        logger.error("上传文件为空");
        return new ResultBean("404","网络延迟，请重新提交");

    }

    /**
     * 优图解析，并上传到本地
     * @return
     */
    @RequestMapping("youTu")
    @ResponseBody
    public String youTu(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("********************获取图片内容********************");
        JSONObject object = new JSONObject();
        try {
            //优图工具
            YouTuUtils youto = new YouTuUtils();
            //文件base64字符串
            String base64String = request.getParameter("str");
            //图片标识
            String str = base64String.substring(base64String.lastIndexOf(",")+1);//去掉头部的base64
            String flag = request.getParameter("flag");//图片类型标识
            String ext = base64String.substring(base64String.indexOf("/")+1,str.indexOf(";"));//获取图片后缀名
            //文件名称
            String uploadFileName = DateUtils.getDateStr8()+"_"+UUID.randomUUID().toString().replaceAll("-","") + "."+ext;
            //存储地址
            StringBuilder path = new StringBuilder(absolutePaths).append("/").append(uploadFileName);
            File saveFile = new File(String.valueOf(path));
            BASE64Decoder decoder = new BASE64Decoder();
            OutputStream out = new FileOutputStream(saveFile);
            byte[] b = decoder.decodeBuffer(str);
            for (int i = 0; i <b.length ; i++) {
                if (b[i] <0) {
                    b[i]+=256;
                }
            }
            out.write(b);
            out.flush();
            logger.info("********************图片上传成功********************");
            //解析图片，返回图片信息
            object = youto.youTu(str, flag);
            logger.info("********************图片上传成功********************");
            object.put("imagePath",uploadFileName);
            object.put("uri",uri);
            object.put("url",new StringBuilder(relativePath).append(uploadFileName));
        } catch (Exception e) {
            logger.error("解析图片出现问题" + e);
            object.put("result", "FAIL");
            object.put("message", e.getMessage());
        }
        return object.toJSONString();
    }


}
