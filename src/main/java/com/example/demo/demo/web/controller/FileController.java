package com.example.demo.demo.web.controller;

import com.example.demo.demo.common.JsonResponse;
import com.example.demo.demo.common.QiniuCloudUtil;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.UUID;


@RestController
@RequestMapping("/file")
public class FileController {


    /**
     * 七牛云文件上传
     */
    @ResponseBody
    @RequestMapping(value="/uploadImg", method= RequestMethod.POST)
    public JsonResponse uploadImg(@RequestParam("file") MultipartFile image) throws IOException {
        JsonResponse result = new JsonResponse();
        if (image.isEmpty()) {
            result.setCode(400);
            result.setMessage("文件为空，请重新上传");
            return result;
        }
        try {
            byte[] bytes = image.getBytes();
            String imageName = UUID.randomUUID().toString();
            try {
                //使用base64方式上传到七牛云
                String url = QiniuCloudUtil.put64image(bytes, imageName);
                result.setCode(200);
                result.setMessage("文件上传成功");
                result.setData(url);
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
}
