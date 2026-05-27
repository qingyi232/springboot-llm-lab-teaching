package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.ForumEntity;
import com.lab.mapper.ForumMapper;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/forum")
public class ForumController {

    @Autowired
    private ForumMapper forumMapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String title) {
        QueryWrapper<ForumEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("parentid", "0");
        if (title != null && !title.isEmpty()) wrapper.like("title", title);
        wrapper.orderByDesc("addtime");
        IPage<ForumEntity> result = forumMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String title) {
        QueryWrapper<ForumEntity> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) wrapper.like("title", title);
        wrapper.orderByDesc("addtime");
        IPage<ForumEntity> result = forumMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        ForumEntity forum = forumMapper.selectById(id);
        List<ForumEntity> replies = forumMapper.selectList(
                new QueryWrapper<ForumEntity>()
                        .eq("parentid", String.valueOf(id))
                        .orderByAsc("addtime")
        );
        return R.ok().put("data", forum).put("replies", replies);
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest request,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) request.getAttribute("userId");
        QueryWrapper<ForumEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userId).eq("parentid", "0").orderByDesc("addtime");
        IPage<ForumEntity> result = forumMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @PostMapping("/save")
    public R save(@RequestBody ForumEntity forum, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        forum.setUserid(userId);
        if (forum.getUsername() == null) forum.setUsername(username);
        forum.setAddtime(new Date());
        forum.setIsdone(0);
        if (forum.getParentid() == null) forum.setParentid("0");
        forumMapper.insert(forum);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody ForumEntity forum) {
        forumMapper.updateById(forum);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            forumMapper.delete(new QueryWrapper<ForumEntity>().eq("parentid", String.valueOf(id)));
            forumMapper.deleteById(id);
        }
        return R.ok();
    }
}
