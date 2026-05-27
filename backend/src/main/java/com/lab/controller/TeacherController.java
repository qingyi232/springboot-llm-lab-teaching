package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.TeacherEntity;
import com.lab.mapper.TeacherMapper;
import com.lab.service.TokenService;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/teacher")
public class TeacherController {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public R register(@RequestBody TeacherEntity teacher) {
        TeacherEntity existing = teacherMapper.selectOne(
                new QueryWrapper<TeacherEntity>().eq("gonghao", teacher.getGonghao())
        );
        if (existing != null) {
            return R.error("该工号已被注册");
        }
        teacher.setAddtime(new Date());
        teacherMapper.insert(teacher);
        return R.ok("注册成功");
    }

    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> params) {
        String gonghao = params.get("username");
        String password = params.get("password");

        TeacherEntity teacher = teacherMapper.selectOne(
                new QueryWrapper<TeacherEntity>()
                        .eq("gonghao", gonghao)
                        .eq("mima", password)
        );

        if (teacher == null) {
            return R.error("工号或密码错误");
        }

        String token = tokenService.generateToken(teacher.getId(), teacher.getGonghao(), "teacher", "教师");
        return R.ok().put("token", token).put("user", teacher).put("role", "教师");
    }

    @GetMapping("/session")
    public R session(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok().put("data", teacherMapper.selectById(userId));
    }

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String jiaoshixingming,
                  @RequestParam(required = false) String gonghao) {
        QueryWrapper<TeacherEntity> wrapper = new QueryWrapper<>();
        if (jiaoshixingming != null && !jiaoshixingming.isEmpty()) wrapper.like("jiaoshixingming", jiaoshixingming);
        if (gonghao != null && !gonghao.isEmpty()) wrapper.like("gonghao", gonghao);
        wrapper.orderByDesc("addtime");
        IPage<TeacherEntity> result = teacherMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("data", teacherMapper.selectById(id));
    }

    @GetMapping("/list")
    public R list() {
        return R.ok().put("data", teacherMapper.selectList(new QueryWrapper<TeacherEntity>().orderByDesc("addtime")));
    }

    @PostMapping("/save")
    public R save(@RequestBody TeacherEntity teacher) {
        teacher.setAddtime(new Date());
        teacherMapper.insert(teacher);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody TeacherEntity teacher) {
        teacherMapper.updateById(teacher);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            teacherMapper.deleteById(id);
        }
        return R.ok();
    }
}
