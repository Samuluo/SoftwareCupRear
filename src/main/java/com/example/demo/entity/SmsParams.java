package com.example.demo.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author Peter Hai
 */
@Data
@Accessors(chain = true)
public class SmsParams {

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 手机号码
     */
    private String phone;


    public SmsParams(String phone, String verifyCode) {
        this.phone = phone;
        this.verifyCode = verifyCode;
    }
}
