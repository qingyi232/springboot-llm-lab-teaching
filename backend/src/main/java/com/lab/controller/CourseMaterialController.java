package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.CourseMaterialEntity;
import com.lab.mapper.CourseMaterialMapper;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/courseMaterial")
public class CourseMaterialController {

    @Autowired
    private CourseMaterialMapper courseMaterialMapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String title,
                  @RequestParam(required = false) String coursetype,
                  @RequestParam(required = false) String faburenxingming) {
        QueryWrapper<CourseMaterialEntity> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) wrapper.like("title", title);
        if (coursetype != null && !coursetype.isEmpty()) wrapper.eq("coursetype", coursetype);
        if (faburenxingming != null && !faburenxingming.isEmpty()) wrapper.eq("faburenxingming", faburenxingming);
        wrapper.orderByDesc("addtime");
        IPage<CourseMaterialEntity> result = courseMaterialMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/front/list")
    public R frontList(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "12") Integer limit,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) String coursetype,
                       @RequestParam(required = false) String sort) {
        QueryWrapper<CourseMaterialEntity> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) wrapper.like("title", title);
        if (coursetype != null && !coursetype.isEmpty()) wrapper.eq("coursetype", coursetype);
        if ("clicknum".equals(sort)) {
            wrapper.orderByDesc("clicknum");
        } else {
            wrapper.orderByDesc("addtime");
        }
        IPage<CourseMaterialEntity> result = courseMaterialMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        CourseMaterialEntity entity = courseMaterialMapper.selectById(id);
        if (entity != null) {
            entity.setClicknum(entity.getClicknum() + 1);
            courseMaterialMapper.updateById(entity);
        }
        return R.ok().put("data", entity);
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest request,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) request.getAttribute("userId");
        QueryWrapper<CourseMaterialEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("faburenid", userId).orderByDesc("addtime");
        IPage<CourseMaterialEntity> result = courseMaterialMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @PostMapping("/save")
    public R save(@RequestBody CourseMaterialEntity material, HttpServletRequest request) {
        material.setAddtime(new Date());
        material.setClicknum(0);
        material.setStorecount(0);
        courseMaterialMapper.insert(material);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody CourseMaterialEntity material) {
        courseMaterialMapper.updateById(material);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            courseMaterialMapper.deleteById(id);
        }
        return R.ok();
    }
}
