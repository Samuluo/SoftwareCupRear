package com.example.demo.web.controller;

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
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
     *
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
            Thread.sleep((int) (1000 + Math.random() * (1000)));
            String url = "https://cdn.bewcf.info/softwareCup/ed8b661c-a9bd-4406-b9a3-52fa9a98d1d9.png";
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData(url);
            if (userId != null && !userId.equals("")) recordService.saveOne(userId, file1, file2, url, "changeDetection");
            return result;
        }
        //根据图片url下载图片
        String filePath1 = fileService.download(file1, "changeDetection");
        String filePath2 = fileService.download(file2, "changeDetection");
        //定义输出文件夹
        String result_name = UUID.randomUUID() + "";
        String result_path = System.getProperty("user.dir") + "/static/changeDetection/results/" + result_name;
        //调用python后端
        String python_url = "http://127.0.0.1:8082/changeDetection?file1=" + filePath1 + "&file2=" + filePath2 + "&result_path=" + result_path;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(python_url, HttpMethod.POST, null, String.class);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path + ".png"));
        String url = QiniuCloudUtil.put64image(bytes, result_name + ".png");
        byte[] bytes2 = IOUtils.toByteArray(new FileInputStream(result_path + ".jpg"));
        String url2 = QiniuCloudUtil.put64image(bytes2, result_name + ".jpg");
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        //保存预测记录
        if (userId != null && !userId.equals("")) recordService.saveOne(userId, file1, file2, url, "changeDetection");
        return result;
    }


    /**
     * 目标检测
     *
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
        System.out.println(file);
        //如果为测试环境，返回示例图片
        if (runtimeEnvironment.equals("test")) {
            Thread.sleep((int) (1000 + Math.random() * (1000)));
            String url = "https://cdn.bewcf.info/softwareCup/visualize_0178e50c-04e4-496d-9220-5af240ef6ae8.jpg";
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData(url);
            if (userId != null && !userId.equals("")) recordService.saveOne(userId, file, null, url, "objectDetection");
            return result;
        }
        //根据图片url下载图片
        String[] split = file.split("/");
        String filename = split[split.length - 1];
        String filePath = fileService.download(file, "objectDetection");
        String result_path = System.getProperty("user.dir") + "/static/objectDetection/results";
        //调用python后端
        String python_url = "http://127.0.0.1:8082/objectDetection?file=" + filePath + "&result_path=" + result_path;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(python_url, HttpMethod.POST, null, String.class);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path + "\\visualize_" + filename));
        String url = QiniuCloudUtil.put64image(bytes, "visualize_" + filename);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        //保存预测记录
        if (userId != null && !userId.equals("")) recordService.saveOne(userId, file, null, url, "objectDetection");
        return result;
    }


    /**
     * 地物分类
     *
     * @param file
     * @param userId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传", notes = "地物分类")
    @RequestMapping(value = "/terrainClassification", method = RequestMethod.POST)
    public JsonResponse terrainClassification(@RequestParam("file") String file,
                                              @RequestParam(value = "userId", required = false) Integer userId) throws Exception {
        System.out.println(file);
        System.out.println(userId);
        JsonResponse result = new JsonResponse();
        //如果为测试环境，返回示例图片
        //Assert.isTrue(temp.getUserId().equals(ShiroUtil.getProfile().getId()),"没有权限编辑");
        if (runtimeEnvironment.equals("test")) {
            Thread.sleep((int) (1000 + Math.random() * (1000)));
            String url = "https://cdn.bewcf.info/softwareCup/71d31450-6ab0-4416-8796-d3c3669df2c4.png";
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData(url);
            if (userId != null && !userId.equals("")) recordService.saveOne(userId, file, null, url, "terrainClassification");
            return result;
        }
        //根据图片url下载图片
        String filePath = fileService.download(file, "terrainClassification");
        String result_name = UUID.randomUUID() + "";
        String result_path = System.getProperty("user.dir") + "/static/terrainClassification/results/" + result_name;
        //调用python后端
        String python_url = "http://127.0.0.1:8082/terrainClassification?file=" + filePath + "&result_path=" + result_path;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(python_url, HttpMethod.POST, null, String.class);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path + ".png"));
        String url = QiniuCloudUtil.put64image(bytes, result_name + ".png");
        byte[] bytes2 = IOUtils.toByteArray(new FileInputStream(result_path + ".jpg"));
        String url2 = QiniuCloudUtil.put64image(bytes2, result_name + ".jpg");
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        //保存预测记录
        if (userId != null && !userId.equals("")) recordService.saveOne(userId, file, null, url, "terrainClassification");
        return result;
    }


    /**
     * 目标提取
     *
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
            Thread.sleep((int) (1000 + Math.random() * (1000)));
            String url = "https://cdn.bewcf.info/softwareCup/daa2ca8b-5bbe-4401-b3b4-f8f6de56edcb.png";
            result.setCode(200);
            result.setMessage("示例图片");
            result.setData(url);
            if (userId != null && !userId.equals("")) recordService.saveOne(userId, file, null, url, "objectExtraction");
            return result;
        }
        //根据图片url下载图片
        String filePath = fileService.download(file, "objectExtraction");
        String result_name = UUID.randomUUID() + "";
        String result_path = System.getProperty("user.dir") + "/static/objectExtraction/results/" + result_name;
        //调用python后端
        String python_url = "http://127.0.0.1:8082/objectExtraction?file=" + filePath + "&result_path=" + result_path;
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.exchange(python_url, HttpMethod.POST, null, String.class);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path + ".png"));
        String url = QiniuCloudUtil.put64image(bytes, result_name + ".png");
        byte[] bytes2 = IOUtils.toByteArray(new FileInputStream(result_path + ".jpg"));
        String url2 = QiniuCloudUtil.put64image(bytes2, result_name + ".jpg");
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        //保存预测记录
        if (userId != null && !userId.equals("")) recordService.saveOne(userId, file, null, url, "objectExtraction");
        return result;
    }
}
