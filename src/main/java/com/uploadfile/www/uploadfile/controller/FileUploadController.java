package com.uploadfile.www.uploadfile.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


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
        String Name = uploadFile.getOriginalFilename();//获取上传时的文件名
        String newName = Name.substring(0, Name.lastIndexOf("."));
        String realPath ="F:/test" ;
        String format = sdf.format(new Date());
        File file=file = new File(realPath+"/"+newName);
        System.out.println("file+++++++++"+file);
        if(!file.isDirectory()){
            file.mkdirs();
        }
//        if(!file.getParentFile().exists()){ //判断文件父目录是否存在
//            file.getParentFile().mkdir();
//        }

        //String newName = UUID.randomUUID().toString()+oldName.substring(oldName.lastIndexOf("."),oldName.length());
        try {
            //文件保存操作
            uploadFile.transferTo(new File(file,Name));
            //生成上传文件的访问路径，并将访问路径返回
           // String  filePath=request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+"/uploadFile/"+format+Name;

           // return filePath;
            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }

    /**
     * 文件下载
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response,@RequestParam("fileName") String fileName){
        System.out.println(fileName);
        String fileUrl = fileName+"/"+fileName+".pptx";
        System.out.println(fileUrl);
        if (fileUrl != null) {
            String realPath =  "F:/test" ;
            File file = new File(realPath+"/"+fileUrl);
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.addHeader("Content-Disposition",
                        "attachment;fileName=" +  fileName);// 设置文件名
                byte[] buffer = new byte[1024];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    int i = bis.read(buffer);
                    while (i != -1) {
                        os.write(buffer, 0, i);
                        i = bis.read(buffer);
                    }
                    System.out.println("success");
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return null;
    }

}
