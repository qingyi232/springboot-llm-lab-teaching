package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.HomeworkEntity;
import com.lab.entity.HomeworkScoreEntity;
import com.lab.entity.HomeworkSubmitEntity;
import com.lab.entity.StudentEntity;
import com.lab.mapper.HomeworkMapper;
import com.lab.mapper.HomeworkScoreMapper;
import com.lab.mapper.HomeworkSubmitMapper;
import com.lab.mapper.StudentMapper;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/homework")
public class HomeworkController {

    @Autowired
    private HomeworkMapper homeworkMapper;

    @Autowired
    private HomeworkSubmitMapper submitMapper;

    @Autowired
    private HomeworkScoreMapper scoreMapper;

    @Autowired
    private StudentMapper studentMapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String title,
                  @RequestParam(required = false) String coursetype,
                  @RequestParam(required = false) String faburenxingming) {
        QueryWrapper<HomeworkEntity> wrapper = new QueryWrapper<>();
        if (title != null && !title.isEmpty()) wrapper.like("title", title);
        if (coursetype != null && !coursetype.isEmpty()) wrapper.eq("coursetype", coursetype);
        if (faburenxingming != null && !faburenxingming.isEmpty()) wrapper.eq("faburenxingming", faburenxingming);
        wrapper.orderByDesc("addtime");
        IPage<HomeworkEntity> result = homeworkMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/list")
    public R list(@RequestParam(required = false) String coursetype, HttpServletRequest request) {
        QueryWrapper<HomeworkEntity> wrapper = new QueryWrapper<>();
        if (coursetype != null && !coursetype.isEmpty()) wrapper.eq("coursetype", coursetype);
        String role = (String) request.getAttribute("role");
        if ("学生".equals(role)) {
            Long userId = (Long) request.getAttribute("userId");
            StudentEntity student = studentMapper.selectById(userId);
            if (student != null && student.getBanji() != null && !student.getBanji().trim().isEmpty()) {
                String banji = student.getBanji();
                wrapper.and(w -> w.isNull("banji").or().eq("banji", "").or().eq("banji", banji));
            }
        }
        wrapper.orderByDesc("addtime");
        return R.ok().put("data", homeworkMapper.selectList(wrapper));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        return R.ok().put("data", homeworkMapper.selectById(id));
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest request,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) request.getAttribute("userId");
        QueryWrapper<HomeworkEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("faburenid", userId).orderByDesc("addtime");
        IPage<HomeworkEntity> result = homeworkMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @PostMapping("/save")
    public R save(@RequestBody HomeworkEntity homework) {
        homework.setAddtime(new Date());
        homeworkMapper.insert(homework);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody HomeworkEntity homework) {
        homeworkMapper.updateById(homework);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            homeworkMapper.deleteById(id);
        }
        return R.ok();
    }

    // ===== 作业提交 =====
    @GetMapping("/submit/page")
    public R submitPage(@RequestParam(defaultValue = "1") Integer page,
                        @RequestParam(defaultValue = "10") Integer limit,
                        @RequestParam(required = false) Long homeworkId,
                        @RequestParam(required = false) String xingming,
                        @RequestParam(required = false) String homeworkTitle,
                        @RequestParam(required = false) String zhuangtai,
                        @RequestParam(required = false) String faburenxingming) {
        QueryWrapper<HomeworkSubmitEntity> wrapper = new QueryWrapper<>();
        if (homeworkId != null) wrapper.eq("homework_id", homeworkId);
        if (xingming != null && !xingming.isEmpty()) wrapper.like("xingming", xingming);
        if (homeworkTitle != null && !homeworkTitle.isEmpty()) wrapper.like("homework_title", homeworkTitle);
        if (zhuangtai != null && !zhuangtai.isEmpty()) wrapper.eq("zhuangtai", zhuangtai);
        if (faburenxingming != null && !faburenxingming.isEmpty()) {
            QueryWrapper<HomeworkEntity> hwWrapper = new QueryWrapper<>();
            hwWrapper.eq("faburenxingming", faburenxingming).select("id");
            java.util.List<HomeworkEntity> teacherHw = homeworkMapper.selectList(hwWrapper);
            if (!teacherHw.isEmpty()) {
                java.util.List<Long> hwIds = new java.util.ArrayList<>();
                for (HomeworkEntity h : teacherHw) hwIds.add(h.getId());
                wrapper.in("homework_id", hwIds);
            } else {
                return R.ok().put("data", new PageUtils(new Page<>(page, limit)));
            }
        }
        wrapper.orderByDesc("addtime");
        IPage<HomeworkSubmitEntity> result = submitMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/submit/mylist")
    public R submitMylist(HttpServletRequest request,
                          @RequestParam(defaultValue = "1") Integer page,
                          @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) request.getAttribute("userId");
        QueryWrapper<HomeworkSubmitEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userId).orderByDesc("addtime");
        IPage<HomeworkSubmitEntity> result = submitMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @PostMapping("/submit/save")
    public R submitSave(@RequestBody HomeworkSubmitEntity submit, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if (!"学生".equals(role)) {
            return R.error("只有学生可以提交作业");
        }
        Long userId = (Long) request.getAttribute("userId");
        submit.setUserid(userId);
        submit.setAddtime(new Date());
        submit.setZhuangtai("已提交");
        submitMapper.insert(submit);
        return R.ok();
    }

    @DeleteMapping("/submit/delete")
    public R submitDelete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            submitMapper.deleteById(id);
        }
        return R.ok();
    }

    // ===== 作业成绩 =====
    @GetMapping("/score/page")
    public R scorePage(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RequestParam(required = false) Long homeworkId,
                       @RequestParam(required = false) String xingming) {
        QueryWrapper<HomeworkScoreEntity> wrapper = new QueryWrapper<>();
        if (homeworkId != null) wrapper.eq("homework_id", homeworkId);
        if (xingming != null && !xingming.isEmpty()) wrapper.like("xingming", xingming);
        wrapper.orderByDesc("addtime");
        IPage<HomeworkScoreEntity> result = scoreMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/score/mylist")
    public R scoreMylist(HttpServletRequest request,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) request.getAttribute("userId");
        QueryWrapper<HomeworkScoreEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userId).orderByDesc("addtime");
        IPage<HomeworkScoreEntity> result = scoreMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @PostMapping("/score/save")
    public R scoreSave(@RequestBody HomeworkScoreEntity score) {
        score.setAddtime(new Date());
        scoreMapper.insert(score);
        HomeworkSubmitEntity submit = submitMapper.selectById(score.getSubmitId());
        if (submit != null) {
            submit.setZhuangtai("已批改");
            submitMapper.updateById(submit);
        }
        return R.ok();
    }

    @PutMapping("/score/update")
    public R scoreUpdate(@RequestBody HomeworkScoreEntity score) {
        scoreMapper.updateById(score);
        return R.ok();
    }

    @DeleteMapping("/score/delete")
    public R scoreDelete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            scoreMapper.deleteById(id);
        }
        return R.ok();
    }
}
