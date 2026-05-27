package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.CourseTypeEntity;
import com.lab.mapper.CourseTypeMapper;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/courseType")
public class CourseTypeController {

    @Autowired
    private CourseTypeMapper courseTypeMapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String typename) {
        QueryWrapper<CourseTypeEntity> wrapper = new QueryWrapper<>();
        if (typename != null && !typename.isEmpty()) wrapper.like("typename", typename);
        wrapper.orderByDesc("addtime");
        IPage<CourseTypeEntity> result = courseTypeMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/list")
    public R list() {
        return R.ok().put("data", courseTypeMapper.selectList(new QueryWrapper<CourseTypeEntity>().orderByDesc("addtime")));
    }

    @PostMapping("/save")
    public R save(@RequestBody CourseTypeEntity courseType) {
        courseType.setAddtime(new Date());
        courseTypeMapper.insert(courseType);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody CourseTypeEntity courseType) {
        courseTypeMapper.updateById(courseType);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            courseTypeMapper.deleteById(id);
        }
        return R.ok();
    }
}
