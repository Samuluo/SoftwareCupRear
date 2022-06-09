package com.example.demo.demo.web.controller;

import com.example.demo.demo.common.JsonResponse;
import com.example.demo.demo.common.QiniuCloudUtil;
import com.example.demo.demo.service.impl.AIService;
import com.example.demo.demo.service.FileService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.UUID;

/**
 * @author Peter Hai
 */
@RestController
@RequestMapping("/changeDetection")
@Slf4j
public class AIController {
    @Autowired
    private AIService changeDetectionService;
    @Autowired
    protected FileService fileService;
    protected ResourceLoader resourceLoader;

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

    @ApiOperation(value = "文件上传", notes = "文件上传")
    @RequestMapping(value = "/predict", method = RequestMethod.POST)
    public JsonResponse upload(@RequestParam("file1") MultipartFile file1,
                               @RequestParam("file2") MultipartFile file2,
                               HttpServletRequest request) throws Exception {
        JsonResponse result = new JsonResponse();
        //本地上传
        String path1 = fileService.upload(file1, "changeDetection");
        String path2 = fileService.upload(file2, "changeDetection");
        String result_name = UUID.randomUUID() +  ".png";
        String result_path = "static/changeDetection/results/" + result_name;
//        System.out.println(path1 + " " + path2);
        changeDetectionService.changeDetection(path1, path2, result_path);
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
    @ApiOperation(value = "文件上传", notes = "文件上传")
    @RequestMapping(value = "/objectDetection", method = RequestMethod.POST)
    public JsonResponse upload2(@RequestParam("file") MultipartFile file,
                               HttpServletRequest request) throws Exception {
        JsonResponse result = new JsonResponse();
        //本地上传
        String path1 = fileService.upload(file, "objectDetection");
        String result_name = UUID.randomUUID() +  ".png";
        String result_path = "static/objectDetection/results/" + result_name;
//        System.out.println(path1 + " " + path2);
        changeDetectionService.objectDetection(path1, result_path);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path));
        String url = QiniuCloudUtil.put64image(bytes, result_name);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        return result;
    }

    /**
     *地物分类
     * @param file
     * @param request
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "文件上传", notes = "文件上传")
    @RequestMapping(value = "/objectDetection", method = RequestMethod.POST)
    public JsonResponse upload3(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {
        JsonResponse result = new JsonResponse();
        //本地上传
        String path = fileService.upload(file, "terrainClassification");
        String result_name = UUID.randomUUID() +  ".png";
        String result_path = "static/terrainClassification/results/" + result_name;
//        System.out.println(path1 + " " + path2);
        changeDetectionService.terrainClassification(path, result_path);
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
    @ApiOperation(value = "文件上传", notes = "文件上传")
    @RequestMapping(value = "/objectExtraction", method = RequestMethod.POST)
    public JsonResponse upload4(@RequestParam("file") MultipartFile file,
                                HttpServletRequest request) throws Exception {
        JsonResponse result = new JsonResponse();
        //本地上传
        String path = fileService.upload(file, "objectExtraction");
        String result_name = UUID.randomUUID() +  ".png";
        String result_path = "static/objectExtraction/results/" + result_name;
//        System.out.println(path1 + " " + path2);
        changeDetectionService.objectExtraction(path, result_path);
        //结果上传至云端，返回图片链接
        byte[] bytes = IOUtils.toByteArray(new FileInputStream(result_path));
        String url = QiniuCloudUtil.put64image(bytes, result_name);
        result.setCode(200);
        result.setMessage("预测成功");
        result.setData(url);
        return result;
    }
}
