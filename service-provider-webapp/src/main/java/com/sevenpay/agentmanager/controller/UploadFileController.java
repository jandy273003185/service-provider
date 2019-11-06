package com.sevenpay.agentmanager.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;
import com.sevenpay.agentmanager.pojo.ImagesResultBean;
import com.sevenpay.agentmanager.pojo.Paths;
import com.sevenpay.agentmanager.utils.YouTuUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("common")
public class UploadFileController {
    private static final Logger logger = LoggerFactory.getLogger(UploadFileController.class);

    @PostMapping("upload")
    @ResponseBody
    public ImagesResultBean fileUpload(MultipartFile file){

        // 获取文件名后缀名
        String suffix = file.getOriginalFilename();
        String prefix = suffix.substring(suffix.lastIndexOf("."));
        String Filename = UUID.randomUUID().toString();
        if (!file.isEmpty()) {//文件不为空
            try {
                //上传路径
                StringBuilder filePath = new StringBuilder(Paths.FilePathAbsolute).append(Paths.FilePath).append(Filename).append(prefix);
                File saveDir = new File(String.valueOf(filePath));
                if (!saveDir.getParentFile().exists()){
                    saveDir.getParentFile().mkdirs();
                }

                // 转存文件
                file.transferTo(saveDir);

                return new ImagesResultBean("200",String.valueOf(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ImagesResultBean("404","网络延迟，请重新提交");

    }

    /**
     * 优图解析，并上传到本地
     * @return
     */
    @RequestMapping("youTu.do")
    @ResponseBody
    public String youTu(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("********************获取图片内容********************");
        JSONObject object = new JSONObject();
        try {
            //优图工具
            YouTuUtils youto = new YouTuUtils();
            //文件base64字符串
            String str = request.getParameter("str");
            //图片标识
            String flag = request.getParameter("flag");

            //图片上传，返回路径
            String imagePath = youto.BASE64CodeToBeImage(str);
            //解析图片，返回图片信息
            object = youto.youTu(str, flag);
            object.put("imagePath",imagePath);
        } catch (Exception e) {
            logger.error("解析图片出现问题" + e);
            object.put("result", "FAIL");
            object.put("message", e.getMessage());
        }
        return object.toJSONString();
    }


}
