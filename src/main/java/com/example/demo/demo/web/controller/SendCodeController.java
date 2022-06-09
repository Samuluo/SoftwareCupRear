package com.example.demo.demo.web.controller;

import com.example.demo.demo.common.JsonResponse;
import com.example.demo.demo.common.TxCloudSmsUtil;
import com.example.demo.demo.model.domain.SmsParams;
import com.example.demo.demo.model.domain.User;
import com.example.demo.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public JsonResponse getCode(@RequestBody String memPhone){
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
                send.sendSms(smsParams);
                Map<String,Object> map = new HashMap<>();
                map.put("code",code);
                map.put("user",user);
                return JsonResponse.success(map);
            }
        }
        return JsonResponse.failure("手机号未注册！");

    }

    /***
     * 发送验证码  注册用
     */
    @RequestMapping(value = "/sendRegister", method= RequestMethod.GET)
    @ResponseBody
    public JsonResponse getRegister(@RequestBody String memPhone){
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
        send.sendSms(smsParams);
        Map<String,Object> map = new HashMap<>();
        map.put("code",code);
        return JsonResponse.success(map);

    }
}

