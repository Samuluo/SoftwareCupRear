package com.example.demo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.demo.entity.Library;
import com.example.demo.mapper.LibraryMapper;
import com.example.demo.service.LibraryService;
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
public class LibraryServiceImpl extends ServiceImpl<LibraryMapper, Library> implements LibraryService {

    @Autowired
    private LibraryMapper libraryMapper;

    @Override
    public void saveOne(Integer userId, String url) {
        Library library = new Library();
        library.setUserId(userId);
        library.setFile(url);
        library.setTime(LocalDateTime.now());
        libraryMapper.insert(library);
    }

    @Override
    public List<Library> getAll(Integer userId) {
        QueryWrapper<Library> libraryQueryWrapper = new QueryWrapper<>();
        libraryQueryWrapper.eq("user_id", userId);
        List<Library> libraryList = libraryMapper.selectList(libraryQueryWrapper);
        return libraryList;
    }

    @Override
    public Object removeOne(Integer id) {
        return libraryMapper.deleteById(id);
    }
}
