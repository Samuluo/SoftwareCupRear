package com.example.demo.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.JsonResponse;
import com.example.demo.common.QiniuCloudUtil;
import com.example.demo.service.RecordService;
import com.example.demo.service.impl.AIService;
import com.example.demo.service.FileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import java.io.*;
import java.util.UUID;

/**
 * @author Peter Hai
 */
@RestController
@RequestMapping("/ai")
@Slf4j
public class AIController {
    @Autowired
    private AIService aiService;
    @Autowired
    private FileService fileService;
    @Autowired
    private RecordService recordService;

    @Value("${runtime-environment}")
    private String runtimeEnvironment;

    /**
     * 变化检测
     * @param file1
     * @param file2
     * @param userId
     * @return
     * @throws Exception
     */

    @ApiOperation(value = "文件上传", notes = "变化检测")
    @RequestMapping(value = "/changeDetection", method = RequestMethod.POST)
    public JsonResponse changeDetection(@RequestParam("file1") String file1,
                                        @RequestParam("file2") String file2,
                                        @RequestParam(value = "userId", required = false) Integer userId) throws Exception {
        JsonResponse result = new JsonResponse();
        //如果为测试环境，返回示例图片
        if (runtimeEnvironment.equals("test")) {
            String url = "https://cdn.bewcf.info/softwareCup/c2069e82-29ec-4eb6-a342-e2da8c9735ee.png";
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData(url);
            if (userId != null) recordService.saveOne(userId, file1, file2, url, "changeDetection");
            return result;
        }
        //根据图片url下载图片
        String filePath1 = fileService.download(file1, "changeDetection");
        String filePath2 = fileService.download(file2, "changeDetection");
        //定义输出文件夹
        String result_name = UUID.randomUUID() + suffix(filePath1);
        String result_path = "static/changeDetection/results/" + result_name;
        //使用python脚本预测
        aiService.changeDetection(filePath1, filePath2, result_path);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path));
        String url = QiniuCloudUtil.put64image(bytes, result_name);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        //保存预测记录
        if (userId != null) recordService.saveOne(userId, file1, file2, url, "changeDetection");
        return result;
    }


    /**
     * 目标检测
     * @param file
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传", notes = "目标检测")
    @RequestMapping(value = "/objectDetection", method = RequestMethod.POST)
    public JsonResponse objectDetection(@RequestParam("file") String file,
                                        @RequestParam(value = "userId", required = false) Integer userId) throws Exception {
        JsonResponse result = new JsonResponse();
        //如果为测试环境，返回示例字典
        if (runtimeEnvironment.equals("test")) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("category_id", 0);
            jsonObject.put("category", "playground");
            jsonObject.put("bbox", new Double[]{306.23284912109375, 273.0307312011719, 177.12255859375, 410.4651184082031});
            jsonObject.put("score", 0.7360728979110718);
            result.setCode(200);
            result.setMessage("示例字典");
            result.setData(jsonObject);
            if (userId != null) recordService.saveOne(userId, file, null, jsonObject.toJSONString(), "objectDetection");
            return result;
        }
        //根据图片url下载图片
        String filePath = fileService.download(file, "objectDetection");
        String output = aiService.objectDetection(filePath);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(output);
        //保存预测记录
        if (userId != null) recordService.saveOne(userId, file, null, output, "objectDetection");
        return result;
    }


    /**
     *地物分类
     * @param file
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传", notes = "地物分类")
    @RequestMapping(value = "/terrainClassification", method = RequestMethod.POST)
    public JsonResponse terrainClassification(@RequestParam("file") String file,
                                              @RequestParam(value = "userId", required = false) Integer userId) throws Exception {
        JsonResponse result = new JsonResponse();
        //如果为测试环境，返回示例图片
        if (runtimeEnvironment.equals("test")) {
            String url = "https://cdn.bewcf.info/softwareCup/d6ff800a-5895-44df-a0ba-be04b20442e1.png";
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData(url);
            if (userId != null) recordService.saveOne(userId, file, null, url, "terrainClassification");
            return result;
        }
        //根据图片url下载图片
        String filePath = fileService.download(file, "terrainClassification");
        String result_name = UUID.randomUUID() + suffix(filePath);
        String result_path = "static/terrainClassification/results/" + result_name;
        aiService.terrainClassification(filePath, result_path);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path));
        String url = QiniuCloudUtil.put64image(bytes, result_name);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        //保存预测记录
        if (userId != null) recordService.saveOne(userId, file, null, url, "terrainClassification");
        return result;
    }


    /**
     * 目标提取
     * @param file
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传", notes = "目标提取")
    @RequestMapping(value = "/objectExtraction", method = RequestMethod.POST)
    public JsonResponse objectExtraction(@RequestParam("file") String file,
                                         @RequestParam(value = "userId", required = false) Integer userId) throws Exception {
        JsonResponse result = new JsonResponse();
        //如果为测试环境，返回示例图片
        if (runtimeEnvironment.equals("test")) {
            String url = "https://cdn.bewcf.info/softwareCup/d19b9f4e-ba6e-4526-bef3-ce1f69a4a0cc.png";
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData(url);
            if (userId != null) recordService.saveOne(userId, file, null, url, "objectExtraction");
            return result;
        }
        //根据图片url下载图片
        String filePath = fileService.download(file, "objectExtraction");
        String result_name = UUID.randomUUID() + suffix(filePath);
        String result_path = "static/objectExtraction/results/" + result_name;
        aiService.objectExtraction(filePath, result_path);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path));
        String url = QiniuCloudUtil.put64image(bytes, result_name);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        //保存预测记录
        if (userId != null) recordService.saveOne(userId, file, null, url, "objectExtraction");
        return result;
    }

    /**
     * 后缀名或empty："a.png" => ".png"
     */
    private static String suffix(String fileName) {
        int i = fileName.lastIndexOf('.');
        return i == -1 ? "" : fileName.substring(i);
    }
}
