package com.example.demo.demo.service.impl;

import com.example.demo.demo.model.domain.User;
import com.example.demo.demo.mapper.UserMapper;
import com.example.demo.demo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjh
 * @since 2022-05-31
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
