package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.NoticeEntity;
import com.lab.mapper.NoticeMapper;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/notice")
public class NoticeController {

    @Autowired
    private NoticeMapper noticeMapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String title,
                  @RequestParam(required = false) String name) {
        QueryWrapper<NoticeEntity> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) wrapper.like("title", title);
        if (name != null && !name.isEmpty()) wrapper.eq("name", name);
        wrapper.orderByDesc("addtime");
        IPage<NoticeEntity> result = noticeMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/list")
    public R list() {
        return R.ok().put("data", noticeMapper.selectList(
                new QueryWrapper<NoticeEntity>().orderByDesc("addtime").last("LIMIT 10")
        ));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        NoticeEntity entity = noticeMapper.selectById(id);
        if (entity != null) {
            entity.setClicknum(entity.getClicknum() + 1);
            noticeMapper.updateById(entity);
        }
        return R.ok().put("data", entity);
    }

    @PostMapping("/save")
    public R save(@RequestBody NoticeEntity notice) {
        notice.setAddtime(new Date());
        notice.setClicknum(0);
        noticeMapper.insert(notice);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody NoticeEntity notice) {
        noticeMapper.updateById(notice);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            noticeMapper.deleteById(id);
        }
        return R.ok();
    }
}
