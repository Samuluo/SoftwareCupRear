package com.example.demo.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.example.demo.shiro.util.JwtUtils;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AccountRealm extends AuthorizingRealm {
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserService userService;
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

/*
     *为用户添加角色与权限*/


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Integer userId = ((AccountProfile)principalCollection.getPrimaryPrincipal()).getId();
/*
         * 2、到数据库查是否有此对象  */

        User user = userService.getById(Long.valueOf(userId));
        if(user!=null){
//2.1、权限信息对象info,用来存放查出的用户的所有的角色（role）及权限（permission）

            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
//2.2、用户的角色集合

            info.addRole(user.getStatus().toString());
//2.3、用户的角色对应的所有权限，如果只使用角色定义访问权限，下面的四行可以不要
            /*List<Role> roleList=user.getRoleList();
            for (Role role : roleList) {
                info.addStringPermissions(role.getPermissionsName());
            }*/

            return info;
        }
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        JwtToken jwtToken =(JwtToken)authenticationToken;
        String userId = jwtUtils.getClaimByToken((String) jwtToken.getPrincipal()).getSubject();
        User user = userService.getById(Long.valueOf(userId));
        if (user == null) {
            throw new UnknownAccountException("账户不存在");
        }
        if (user.getStatus() == -1){
            throw new LockedAccountException("账户已被锁定");
        }
        AccountProfile accountProfile = new AccountProfile();
        BeanUtil.copyProperties(user,accountProfile);
        System.out.println("--------------");
        return new SimpleAuthenticationInfo(accountProfile,jwtToken.getCredentials(),getName());
    }
}
