package com.example.demo.web.controller;

import com.example.demo.common.JsonResponse;
import com.example.demo.common.QiniuCloudUtil;
import com.example.demo.entity.Library;
import com.example.demo.service.LibraryService;
import com.example.demo.service.impl.LibraryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;


@RestController
@RequestMapping("/file")
public class FileController {


    @Autowired
    private LibraryService libraryService;

    /**
     * 七牛云文件上传,如果有userId,则把图片保存至用户图片库
     */
    @RequestMapping(value="/uploadImg", method= RequestMethod.POST)
    public JsonResponse uploadImg(@RequestParam("file") MultipartFile image,
                                  @RequestParam(value = "userId", required = false) Integer userId) {
        JsonResponse result = new JsonResponse();
        if (image.isEmpty()) {
            result.setCode(400);
            result.setMessage("文件为空，请重新上传");
            return result;
        }
        try {
            byte[] bytes = image.getBytes();
            String imageName = UUID.randomUUID() + suffix(image.getOriginalFilename());
            try {
                //使用base64方式上传到七牛云
                String url = QiniuCloudUtil.put64image(bytes, imageName);
                result.setStatus(true);
                result.setCode(200);
                result.setData(url);
                //把图片保存至用户图片库

                if (userId != null) {
                    Library li = new Library().setUserId(userId).setFile(url).setName(image.getOriginalFilename()).setTime(LocalDateTime.now());
                    libraryService.save(li);
                    result.setMessage(li.getId().toString());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            result.setCode(500);
            result.setMessage("文件上传发生异常！");
        } finally {
            return result;
        }
    }

    /**
     * 后缀名或empty："a.png" => ".png"
     */
    private static String suffix(String fileName) {
        int i = fileName.lastIndexOf('.');
        return i == -1 ? "" : fileName.substring(i);
    }
}
