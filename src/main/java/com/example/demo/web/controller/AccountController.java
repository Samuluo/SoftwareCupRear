package com.example.demo.web.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.common.JsonResponse;

import com.example.demo.common.dto.LoginDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.shiro.util.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;
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
        User user = userService.getOne(new QueryWrapper<User>().eq("phone",loginDto.getName()));
        if(user == null) {
            user = userService.getOne(new QueryWrapper<User>().eq("name",loginDto.getName()));
        }
        if(user==null) {
            return new JsonResponse().setMessage("用户不存在").setCode(500);
        }
        //对象封装操作类
        if (!(SecureUtil.md5(user.getPassword())).equals(SecureUtil.md5(loginDto.getPassword()))){
            return JsonResponse.failure("密码不正确").setCode(409);
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
    @PostMapping("/login2")
    public JsonResponse login2(@RequestBody User us, HttpServletResponse response, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return JsonResponse.failure(bindingResult.getFieldError().getDefaultMessage());
        }
        User user = userService.getOne(new QueryWrapper<User>().eq("phone",us.getPhone()));
        //对象封装操作类
        Assert.notNull(user,"用户不存在");

        if (!getRedisCode(us.getPhone(),us.getVerity())){
            return JsonResponse.failure("验证不正确或已经失效").setCode(505);
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
    public JsonResponse register(@RequestBody User user,@RequestParam(value = "code")String code) {
        if(getRedisCode(user.getPhone(),code)) {
            user.setAvatar("https://cdn.bewcf.info/softwareCup/DefaultAvatar.jpg");
            userService.save(user);
            return JsonResponse.success("注册成功！").setCode(200);
        }
        return JsonResponse.success("验证码错误").setCode(409);
    }

    /**
     * 注册时检验用户名是否重复
     */
    @GetMapping("/isExist")
    public boolean isExist(@RequestParam(value = "name")String name) {
        if(userService.getOne(new QueryWrapper<User>().eq("name",name))!=null){
            return true;
        }
        return false;
    }

    /**
     * 校验验证码
     */
    public boolean getRedisCode(String phone,String code) {
        //从redis获取验证码
        Jedis jedis = new Jedis("127.0.0.1",6379);
        //验证码key
        String codeKey = "VerifyCode"+phone+":code";
        try {
            String redisCode = jedis.get(codeKey);
            //判断
            if(redisCode.equals(code)) {
                jedis.close();
                return true;
            }else {
                jedis.close();
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }
}

