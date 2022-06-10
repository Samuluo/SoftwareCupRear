package com.example.demo.shiro.util;

import com.example.demo.shiro.AccountProfile;
import org.apache.shiro.SecurityUtils;

/**
 * @author Peter Hai
 */

public class ShiroUtil {

    public static AccountProfile getProfile() {
        return (AccountProfile) SecurityUtils.getSubject().getPrincipal();
    }

}
