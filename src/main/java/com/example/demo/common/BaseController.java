package com.example.demo.common;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author nedli
 */
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class BaseController<S extends IService<T>,T>{

    protected S service;

    /**
     * 描述：条件列表 查询
     *
     */
    @ApiOperation(value = "列表查询,若参数存在,则分页查询;若参数不存在,则全量查询" )
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public JsonResponse list(Wrapper<T> wrapper,Integer pageNo,Integer pageSize) {
        List<T> resultList = null;
        if( pageNo > 0 && pageSize> 0) {
            Page page = new Page<>();
            page.setCurrent(pageNo).setSize(pageSize);
            resultList = service.page(page,wrapper).getRecords();
        }else{
            resultList = service.list(wrapper);
        }
        return JsonResponse.success(resultList);
    }

    /**
     * 保存实体对象,TableId 注解存在更新记录,否者插入一条记录
     *
     */
    @ApiOperation(value = "保存实体对象,TableId 注解存在更新记录,否者插入一条记录" )
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public JsonResponse save(@RequestBody T  model) {
        service.saveOrUpdate(model);
        return JsonResponse.success("保存成功");
    }

    /**
     * 根据Id查询
     *
     */
    @ApiOperation(value = "根据Id查询" )
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    public JsonResponse findById(@PathVariable("id") Long id) {
        T model =  service.getById(id);
        return JsonResponse.success(model);
    }

    /**
     * 根据Id删除
     *
     */
    @ApiOperation(value = "根据Id删除" )
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public JsonResponse deleteById(@PathVariable("id") Long id) {
        service.removeById(id);
        return JsonResponse.success("删除成功");
    }
}
