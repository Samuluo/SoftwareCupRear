package com.example.demo.demo.mapper;

import com.example.demo.demo.model.domain.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hjh
 * @since 2022-05-31
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
