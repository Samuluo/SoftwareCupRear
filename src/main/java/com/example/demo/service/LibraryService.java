package com.example.demo.service;

import com.example.demo.entity.Library;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hjh
 * @since 2022-06-15
 */
public interface LibraryService extends IService<Library> {

    void saveOne(Integer userId, String url);

    List<Library> getAll(Integer userId);

    Object removeOne(Integer id);
}
