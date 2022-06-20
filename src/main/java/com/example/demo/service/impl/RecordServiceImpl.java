package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Record;
import com.example.demo.mapper.RecordMapper;
import com.example.demo.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public Object statistics(Integer userId) {
        QueryWrapper<Record> recordQueryWrapper = new QueryWrapper<>();
        recordQueryWrapper.eq("user_id", userId);
        List<Record> records = recordMapper.selectList(recordQueryWrapper);
        Map<String, Double> map = new LinkedHashMap<>();
        map.put("目标提取", 0.0);
        map.put("变化检测", 0.0);
        map.put("目标检测", 0.0);
        map.put("地物分类", 0.0);
        int total = records.size();
        if (total == 0) return map;
        int changeDetection = 0, objectDetection = 0, objectExtraction = 0, terrainClassification = 0;
        for (Record record : records) {
            switch (record.getType()) {
                case "changeDetection":
                    changeDetection++;
                    break;
                case "objectDetection":
                    objectDetection++;
                    break;
                case "objectExtraction":
                    objectExtraction++;
                    break;
                case "terrainClassification":
                    terrainClassification++;
                    break;
                default:
                    break;
            }
        }
        map.put("目标提取", (double)objectExtraction / total);
        map.put("变化检测", (double)changeDetection / total);
        map.put("目标检测", (double)objectDetection / total);
        map.put("地物分类", (double)terrainClassification / total);
        return map;
    }
}
