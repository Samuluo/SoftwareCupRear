package com.example.demo.web.controller;

import com.example.demo.common.JsonResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.service.LibraryService;
import com.example.demo.entity.Library;

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
@RequestMapping("/library")
public class LibraryController {

    private final Logger logger = LoggerFactory.getLogger( LibraryController.class );

    @Autowired
    private LibraryService libraryService;

    /**
    * 描述：获取图片库的所有图片
    *
    */
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getAll(@RequestParam("userId") Integer userId) {
        List<Library> libraryList = libraryService.getAll(userId);
        return JsonResponse.success(libraryList);
    }

    /**
     * 描述：删除图片库中的某一图片
     *
     */
    @RequestMapping(value = "/removeOne", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse removeOne(@RequestParam("id") Integer id) {
        Object result = libraryService.removeOne(id);
        return JsonResponse.success(result);
    }
}

