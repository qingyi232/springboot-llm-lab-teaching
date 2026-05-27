package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.CollectionEntity;
import com.lab.entity.CourseMaterialEntity;
import com.lab.entity.VideoEntity;
import com.lab.mapper.CollectionMapper;
import com.lab.mapper.CourseMaterialMapper;
import com.lab.mapper.VideoMapper;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/collection")
public class CollectionController {

    @Autowired
    private CollectionMapper collectionMapper;

    @Autowired
    private VideoMapper videoMapper;

    @Autowired
    private CourseMaterialMapper courseMaterialMapper;

    @GetMapping("/page")
    public R page(HttpServletRequest request,
                  @RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String tablename) {
        Long userId = (Long) request.getAttribute("userId");
        QueryWrapper<CollectionEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userId);
        if (tablename != null && !tablename.isEmpty()) wrapper.eq("tablename", tablename);
        wrapper.orderByDesc("addtime");
        IPage<CollectionEntity> result = collectionMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/check")
    public R check(HttpServletRequest request,
                   @RequestParam Long refid,
                   @RequestParam String tablename) {
        Long userId = (Long) request.getAttribute("userId");
        CollectionEntity entity = collectionMapper.selectOne(
                new QueryWrapper<CollectionEntity>()
                        .eq("userid", userId)
                        .eq("refid", refid)
                        .eq("tablename", tablename)
        );
        return R.ok().put("data", entity != null);
    }

    @PostMapping("/save")
    public R save(@RequestBody CollectionEntity collection, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        collection.setUserid(userId);
        collection.setAddtime(new Date());
        collectionMapper.insert(collection);
        updateStorecount(collection.getRefid(), collection.getTablename(), 1);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(HttpServletRequest request,
                    @RequestParam Long refid,
                    @RequestParam String tablename) {
        Long userId = (Long) request.getAttribute("userId");
        collectionMapper.delete(
                new QueryWrapper<CollectionEntity>()
                        .eq("userid", userId)
                        .eq("refid", refid)
                        .eq("tablename", tablename)
        );
        updateStorecount(refid, tablename, -1);
        return R.ok();
    }

    private void updateStorecount(Long refid, String tablename, int delta) {
        if (refid == null || tablename == null) return;
        try {
            if ("video".equals(tablename)) {
                VideoEntity v = videoMapper.selectById(refid);
                if (v != null) {
                    int count = (v.getStorecount() == null ? 0 : v.getStorecount()) + delta;
                    v.setStorecount(Math.max(count, 0));
                    videoMapper.updateById(v);
                }
            } else if ("course_material".equals(tablename)) {
                CourseMaterialEntity m = courseMaterialMapper.selectById(refid);
                if (m != null) {
                    int count = (m.getStorecount() == null ? 0 : m.getStorecount()) + delta;
                    m.setStorecount(Math.max(count, 0));
                    courseMaterialMapper.updateById(m);
                }
            }
        } catch (Exception ignored) {
        }
    }
}
