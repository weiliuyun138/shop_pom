package com.qf.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
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


    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    @RequestMapping("/uploader")
    @ResponseBody
    public String imgsUpload(MultipartFile file) {
        //获得最后一个 . 的下标 xxxxxx.jpg
        int index = file.getOriginalFilename().lastIndexOf(".");
        String houzui = file.getOriginalFilename().substring(index + 1);
        System.out.println("后缀："+houzui);
        //上传到FastDFS
        try {
            StorePath storePath  = fastFileStorageClient.uploadImageAndCrtThumbImage(file.getInputStream(), file.getSize(), houzui, null);
            //获得上传到FasDFS中的图片访问路径
            String fullPath = storePath.getFullPath();
            return "{\"uploadPath\":\""+fullPath+"\"}";//{"uploadPath":"path"}
        } catch (IOException e) {
            e.printStackTrace();
        }
        //上传到本地
//        try (
//                InputStream in = file.getInputStream();
//                OutputStream out = new FileOutputStream(UPLOAD_PATH+ UUID.randomUUID().toString());
//                ) {
//            System.out.println("正在上传!");
//            IOUtils.copy(in, out);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        return null;
    }

}
