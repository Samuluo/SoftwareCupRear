package com.example.demo.web.controller;

import com.example.demo.common.JsonResponse;
import com.example.demo.common.TxCloudSmsUtil;
import com.example.demo.entity.SmsParams;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * @author Peter Hai
 * @program: SpringBoot_01
 * @description: 短信发送
 * @createDate: 2021-04-27 22:24
 **/
@Controller
@RequestMapping("/code")
public class SendCodeController {

    @Autowired
    private UserService userService;
    /**
     * 发送验证码
     * */
    private String Vcode = "";

    @RequestMapping(value = "/sendCode", method= RequestMethod.GET)
    @ResponseBody
    public JsonResponse getCode(@RequestParam String memPhone){
        System.out.println(memPhone);
        String p = memPhone.replace("+86","");
        p = p.replace("=","");
        String po = memPhone.replace("=","");
        System.out.println(p);
        List<User> list = new ArrayList<>();
        list = userService.list();
        for (User user : list) {
            if (p.equals(user.getPhone())) {
                //随机生成验证码
                String code = String.valueOf(new Random().nextInt(999999));
                Vcode = code;
                SmsParams smsParams = new SmsParams(po, code);
                TxCloudSmsUtil send = new TxCloudSmsUtil();
                //send.sendSms(smsParams);
                String codeKey = "VerifyCode"+memPhone+":code";
                System.out.println(code);
                Jedis jedis = new Jedis("127.0.0.1",6379);
                jedis.setex(codeKey,300,code);
                jedis.close();
                return JsonResponse.success("发送成功！");
            }
        }
        return JsonResponse.failure("手机号未注册！");

    }

    /***
     * 发送验证码  注册用
     */
    @RequestMapping(value = "/sendRegister", method= RequestMethod.GET)
    @ResponseBody
    public JsonResponse getRegister(@RequestParam String memPhone){
        System.out.println(memPhone);
        String p = memPhone.replace("+86","");
        p = p.replace("=","");
        String po = memPhone.replace("=","");
        System.out.println(p);

        //随机生成验证码
        String code = String.valueOf(new Random().nextInt(999999));
        Vcode = code;
        SmsParams smsParams = new SmsParams(po, code);
        TxCloudSmsUtil send = new TxCloudSmsUtil();
        //send.sendSms(smsParams);
        String codeKey = "VerifyCode"+memPhone+":code";
        System.out.println(code);
        Jedis jedis = new Jedis("127.0.0.1",6379);
        jedis.setex(codeKey,300,code);
        jedis.close();
        return JsonResponse.success("发送成功！");

    }


}

