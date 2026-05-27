package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.LabTypeEntity;
import com.lab.mapper.LabTypeMapper;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/labType")
public class LabTypeController {

    @Autowired
    private LabTypeMapper labTypeMapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String typename) {
        QueryWrapper<LabTypeEntity> wrapper = new QueryWrapper<>();
        if (typename != null && !typename.isEmpty()) wrapper.like("typename", typename);
        wrapper.orderByDesc("addtime");
        IPage<LabTypeEntity> result = labTypeMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/list")
    public R list() {
        return R.ok().put("data", labTypeMapper.selectList(new QueryWrapper<LabTypeEntity>().orderByDesc("addtime")));
    }

    @PostMapping("/save")
    public R save(@RequestBody LabTypeEntity labType) {
        labType.setAddtime(new Date());
        labTypeMapper.insert(labType);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody LabTypeEntity labType) {
        labTypeMapper.updateById(labType);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            labTypeMapper.deleteById(id);
        }
        return R.ok();
    }
}
