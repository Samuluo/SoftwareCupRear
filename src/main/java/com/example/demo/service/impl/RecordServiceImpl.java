package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Record;
import com.example.demo.mapper.RecordMapper;
import com.example.demo.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hjh
 * @since 2022-06-15
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public void saveOne(Integer userId, String file1, String file2, String result, String type) {
        Record record = new Record();
        record.setUserId(userId);
        record.setFile1(file1);
        record.setFile2(file2);
        record.setResult(result);
        record.setType(type);
        record.setTime(LocalDateTime.now());
        recordMapper.insert(record);
    }

    @Override
    public List<Record> getAll(Integer userId) {
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("user_id", userId);
        List<Record> recordList = recordMapper.selectList(recordQueryWrapper);
        return recordList;
    }

    @Override
    public Object removeOne(Integer id) {
        return recordMapper.deleteById(id);
    }
}
