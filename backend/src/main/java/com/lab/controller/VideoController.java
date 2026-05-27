package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.VideoEntity;
import com.lab.mapper.VideoMapper;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoMapper videoMapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String title,
                  @RequestParam(required = false) String coursetype,
                  @RequestParam(required = false) String faburenxingming) {
        QueryWrapper<VideoEntity> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) wrapper.like("title", title);
        if (coursetype != null && !coursetype.isEmpty()) wrapper.eq("coursetype", coursetype);
        if (faburenxingming != null && !faburenxingming.isEmpty()) wrapper.eq("faburenxingming", faburenxingming);
        wrapper.orderByDesc("addtime");
        IPage<VideoEntity> result = videoMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/front/list")
    public R frontList(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "12") Integer limit,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) String coursetype) {
        QueryWrapper<VideoEntity> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) wrapper.like("title", title);
        if (coursetype != null && !coursetype.isEmpty()) wrapper.eq("coursetype", coursetype);
        wrapper.orderByDesc("addtime");
        IPage<VideoEntity> result = videoMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        VideoEntity entity = videoMapper.selectById(id);
        if (entity != null) {
            entity.setClicknum(entity.getClicknum() + 1);
            videoMapper.updateById(entity);
        }
        return R.ok().put("data", entity);
    }

    @PostMapping("/thumbsup/{id}")
    public R thumbsup(@PathVariable Long id, @RequestParam String type) {
        VideoEntity entity = videoMapper.selectById(id);
        if (entity != null) {
            if ("thumbsup".equals(type)) {
                entity.setThumbsup(entity.getThumbsup() + 1);
            } else {
                entity.setCrazynum(entity.getCrazynum() + 1);
            }
            videoMapper.updateById(entity);
        }
        return R.ok();
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest request,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) request.getAttribute("userId");
        QueryWrapper<VideoEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("faburenid", userId).orderByDesc("addtime");
        IPage<VideoEntity> result = videoMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @PostMapping("/save")
    public R save(@RequestBody VideoEntity video) {
        video.setAddtime(new Date());
        video.setClicknum(0);
        video.setThumbsup(0);
        video.setCrazynum(0);
        video.setStorecount(0);
        videoMapper.insert(video);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody VideoEntity video) {
        videoMapper.updateById(video);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            videoMapper.deleteById(id);
        }
        return R.ok();
    }
}
