package com.example.demo.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.common.JsonResponse;
import com.example.demo.service.UserService;
import com.example.demo.entity.User;

import javax.servlet.http.HttpServletRequest;


/**
 *
 *  前端控制器
 *
 *
 * @author hjh
 * @since 2022-05-31
 * @version v1.0
 */
@Controller
@RequestMapping("/user")
public class UserController {

    private final Logger logger = LoggerFactory.getLogger( UserController.class );

    @Autowired
    private UserService userService;

    /**
    * 描述：根据Id 查询
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public JsonResponse getById(@PathVariable("id") Long id, HttpServletRequest request)throws Exception {
        String token = request.getHeader("token");
        if(token.isEmpty()) {
            return JsonResponse.failure("用户未登录，请登录后操作");
        }
        User  user =  userService.getById(id);
        return JsonResponse.success(user);
    }

    /**
    * 描述：根据Id删除
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public JsonResponse deleteById(@PathVariable("id") Long id) throws Exception {
        userService.removeById(id);
        return JsonResponse.success(null);
    }


    /**
    * 描述：根据Id 更新
    *
    */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public JsonResponse updateUser(@PathVariable("id") Integer  id,@RequestBody User  user) throws Exception {
        user.setId(id);
        userService.updateById(user);
        return JsonResponse.success(null);
    }


    /**
    * 描述:创建User
    *
    */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public JsonResponse create(User  user) throws Exception {
        userService.save(user);
        return JsonResponse.success(null);
    }
}

