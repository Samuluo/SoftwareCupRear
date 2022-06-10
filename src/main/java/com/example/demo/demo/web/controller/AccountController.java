package com.example.demo.demo.web.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.demo.common.JsonResponse;

import com.example.demo.demo.common.dto.LoginDto;
import com.example.demo.demo.model.domain.User;
import com.example.demo.demo.service.UserService;
import com.example.demo.demo.shiro.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
/**
 * @author Peter Hai
 */
@RestController
public class AccountController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    /**
     * 登录验证码SessionKey
     */
    public static final String LOGIN_VALIDATE_CODE = "login_validate_code";

    @PostMapping("/login")
    public JsonResponse login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResponse.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        User user = userService.getOne(new QueryWrapper<User>().eq("name",loginDto.getName()));
        //对象封装操作类
            Assert.notNull(user,"用户不存在");
            if (!(SecureUtil.md5(user.getPassword())).equals(SecureUtil.md5(loginDto.getPassword()))){
            return JsonResponse.failure("密码不正确");
            }
        String jwt = jwtUtils.generateToken(user.getId());
            response.addHeader("Authorization",jwt);
            response.setHeader("Access- -Expose-Headers",jwt);
            return JsonResponse.success(MapUtil.builder()
                    .put("id",user.getId())
                    .put("name",user.getName())
                    .put("avatar",user.getAvatar())
                    .put("status",user.getStatus())
                    .put("token",jwt)
                    .map()
            );
    }

    @RequiresAuthentication
    @GetMapping("/logout")
    public JsonResponse logout() {
        SecurityUtils.getSubject().logout();
        return JsonResponse.success(null);
    }

    /**
     * 注册
     */
    @PostMapping("/register")
    public JsonResponse register(@RequestBody User user) {
        userService.save(user);
        return JsonResponse.success(null);
    }
}

