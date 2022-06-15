package com.example.demo.web.controller;

import com.example.demo.common.JsonResponse;
import com.example.demo.entity.Library;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.service.RecordService;
import com.example.demo.entity.Record;

import java.util.List;


/**
 *
 *  前端控制器
 *
 *
 * @author hjh
 * @since 2022-06-15
 * @version v1.0
 */
@Controller
@RequestMapping("/record")
public class RecordController {

    private final Logger logger = LoggerFactory.getLogger( RecordController.class );

    @Autowired
    private RecordService recordService;


    /**
     * 描述：获取图片库的所有图片
     *
     */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getAll(@RequestParam("userId") Integer userId) {
        List<Record> recordList = recordService.getAll(userId);
        return JsonResponse.success(recordList);
    }

    /**
     * 描述：删除图片库中的某一图片
     *
     */
    @RequestMapping(value = "/removeOne", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse removeOne(@RequestParam("id") Integer id) {
        Object result = recordService.removeOne(id);
        return JsonResponse.success(result);
    }

}

