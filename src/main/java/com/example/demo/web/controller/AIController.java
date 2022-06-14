package com.example.demo.web.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.common.JsonResponse;
import com.example.demo.common.QiniuCloudUtil;
import com.example.demo.service.impl.AIService;
import com.example.demo.service.FileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
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
    private ResourceLoader resourceLoader;

    @Value("${runtime-environment}")
    private String runtimeEnvironment;

    public AIController(FileService fileService, ResourceLoader resourceLoader) {
        this.fileService = fileService;
        this.resourceLoader = resourceLoader;
    }

    /**
     * 变化检测
     * @param file1
     * @param file2
     * @param request
     * @return
     * @throws Exception
     */

    @ApiOperation(value = "文件上传", notes = "变化检测")
    @RequestMapping(value = "/changeDetection", method = RequestMethod.POST)
    public JsonResponse changeDetection(@RequestParam("file1") MultipartFile file1,
                               @RequestParam("file2") MultipartFile file2,
                               HttpServletRequest request) throws Exception {
        JsonResponse result = new JsonResponse();
        //如果为测试环境，返回示例图片
        if (runtimeEnvironment.equals("test")) {
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData("https://cdn.bewcf.info/softwareCup/0094ae4f-1cf8-4681-84a8-17a56a8ccdf6");
            return result;
        }
        //本地上传
        String path1 = fileService.upload(file1, "changeDetection");
        String path2 = fileService.upload(file2, "changeDetection");
        String result_name = UUID.randomUUID() +  ".png";
        String result_path = "static/changeDetection/results/" + result_name;
//        System.out.println(path1 + " " + path2);
        aiService.changeDetection(path1, path2, result_path);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path));
        String url = QiniuCloudUtil.put64image(bytes, result_name);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        return result;
    }

    /**
     * 目标检测
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传", notes = "目标检测")
    @RequestMapping(value = "/objectDetection", method = RequestMethod.POST)
    public JsonResponse objectDetection(@RequestParam("file") MultipartFile file,
                               HttpServletRequest request) throws Exception {
        JsonResponse result = new JsonResponse();
        //如果为测试环境，返回示例字典
        if (runtimeEnvironment.equals("test")) {
            result.setCode(200);
            result.setMessage("示例字典");
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("category_id", 0);
            jsonObject.put("category", "playground");
            jsonObject.put("bbox", new Double[]{306.23284912109375, 273.0307312011719, 177.12255859375, 410.4651184082031});
            jsonObject.put("score", 0.7360728979110718);
            result.setData(jsonObject);
            return result;
        }
        //本地上传
        String path = fileService.upload(file, "objectDetection");
        String output = aiService.objectDetection(path);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(output);
        return result;
    }

    /**
     *地物分类
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传", notes = "地物分类")
    @RequestMapping(value = "/terrainClassification", method = RequestMethod.POST)
    public JsonResponse terrainClassification(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {
        JsonResponse result = new JsonResponse();
        //如果为测试环境，返回示例图片
        if (runtimeEnvironment.equals("test")) {
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData("https://cdn.bewcf.info/softwareCup/d6ff800a-5895-44df-a0ba-be04b20442e1.png");
            return result;
        }
        //本地上传
        String path = fileService.upload(file, "terrainClassification");
        String result_name = UUID.randomUUID() +  ".png";
        String result_path = "static/terrainClassification/results/" + result_name;
        aiService.terrainClassification(path, result_path);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path));
        String url = QiniuCloudUtil.put64image(bytes, result_name);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        return result;
    }

    /**
     * 目标提取
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传", notes = "目标提取")
    @RequestMapping(value = "/objectExtraction", method = RequestMethod.POST)
    public JsonResponse objectExtraction(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {
        JsonResponse result = new JsonResponse();
        //如果为测试环境，返回示例图片
        if (runtimeEnvironment.equals("test")) {
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData("https://cdn.bewcf.info/softwareCup/d19b9f4e-ba6e-4526-bef3-ce1f69a4a0cc.png");
            return result;
        }
        //本地上传
        String path = fileService.upload(file, "objectExtraction");
        String result_name = UUID.randomUUID() +  ".png";
        String result_path = "static/objectExtraction/results/" + result_name;
        aiService.objectExtraction(path, result_path);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path));
        String url = QiniuCloudUtil.put64image(bytes, result_name);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        return result;
    }
}
