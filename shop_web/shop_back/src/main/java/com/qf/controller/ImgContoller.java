package com.qf.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

@Controller
@RequestMapping("/imgs")
public class ImgContoller {
    public static final String UPLOAD_PATH="D:\\kaifa\\IDEAWorkSpace\\upload\\";


    @RequestMapping("/uploader")
    @ResponseBody
    public String imgsUpload(MultipartFile file){
        try (
                InputStream in = file.getInputStream();
                OutputStream out = new FileOutputStream(UPLOAD_PATH+ UUID.randomUUID().toString());
                ) {
            System.out.println("正在上传!");
            IOUtils.copy(in, out);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "succ";
    }

}
