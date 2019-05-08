package com.uploadfile.www.uploadfile.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


/**
 * @author zhangqiyang
 * @date 2019/4/16 - 10:07
 * 单文件上传
 */

@RestController
public class FileUploadController {
    SimpleDateFormat sdf= new SimpleDateFormat("yyyy/MM/dd/");
    @PostMapping("/upload")
    public String uplaod(MultipartFile uploadFile, HttpServletRequest request){
        //设置上传文件保存路径为项目运行目录下的uploadFile文件夹，并在文件夹中通过日期对所上传的文件归类保存
        String realPath = request.getSession().getServletContext().getRealPath("/uploadFile/");
        String format = sdf.format(new Date());
        File file = new File(realPath + format);
        if(!file.isDirectory()){
            file.mkdirs();
        }
        //上传文件重命名，为了避免文件重名
        String oldName = uploadFile.getOriginalFilename();
        String newName = UUID.randomUUID().toString()+oldName.substring(oldName.lastIndexOf("."),oldName.length());
        try {
            //文件保存操作
            uploadFile.transferTo(new File(file,newName));
            //生成上传文件的访问路径，并将访问路径返回
            String  filePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/uploadFile/"+format+newName;
            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }
}
