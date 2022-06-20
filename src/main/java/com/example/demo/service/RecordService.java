package com.example.demo.service;

import com.example.demo.entity.Record;
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
public interface RecordService extends IService<Record> {

    void saveOne(Integer userId, String file1, String file2, String result, String type);

    List<Record> getAll(Integer userId);

    Object removeOne(Integer id);

    Object statistics(Integer userId);
}
