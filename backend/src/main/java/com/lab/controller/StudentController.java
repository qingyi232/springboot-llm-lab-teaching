package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.StudentEntity;
import com.lab.mapper.StudentMapper;
import com.lab.service.TokenService;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public R register(@RequestBody StudentEntity student) {
        StudentEntity existing = studentMapper.selectOne(
                new QueryWrapper<StudentEntity>().eq("xuehao", student.getXuehao())
        );
        if (existing != null) {
            return R.error("该学号已被注册");
        }
        student.setAddtime(new Date());
        studentMapper.insert(student);
        return R.ok("注册成功");
    }

    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> params) {
        String xuehao = params.get("username");
        String password = params.get("password");

        StudentEntity student = studentMapper.selectOne(
                new QueryWrapper<StudentEntity>()
                        .eq("xuehao", xuehao)
                        .eq("mima", password)
        );

        if (student == null) {
            return R.error("学号或密码错误");
        }

        String token = tokenService.generateToken(student.getId(), student.getXuehao(), "student", "学生");
        return R.ok().put("token", token).put("user", student).put("role", "学生");
    }

    @GetMapping("/session")
    public R session(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        StudentEntity student = studentMapper.selectById(userId);
        return R.ok().put("data", student);
    }

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String xingming,
                  @RequestParam(required = false) String xuehao,
                  @RequestParam(required = false) String zhuanye) {
        QueryWrapper<StudentEntity> wrapper = new QueryWrapper<>();
        if (xingming != null && !xingming.isEmpty()) wrapper.like("xingming", xingming);
        if (xuehao != null && !xuehao.isEmpty()) wrapper.like("xuehao", xuehao);
        if (zhuanye != null && !zhuanye.isEmpty()) wrapper.eq("zhuanye", zhuanye);
        wrapper.orderByDesc("addtime");
        IPage<StudentEntity> result = studentMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("data", studentMapper.selectById(id));
    }

    @GetMapping("/list")
    public R list() {
        return R.ok().put("data", studentMapper.selectList(new QueryWrapper<StudentEntity>().orderByDesc("addtime")));
    }

    /** 获取系统中已有学生的班级列表（去重），供教师布置作业时选择 */
    @GetMapping("/banjiList")
    public R banjiList() {
        List<StudentEntity> students = studentMapper.selectList(
                new QueryWrapper<StudentEntity>()
                        .select("banji")
                        .isNotNull("banji")
                        .ne("banji", "")
        );
        List<String> banjis = students.stream()
                .map(StudentEntity::getBanji)
                .filter(b -> b != null && !b.trim().isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        return R.ok().put("data", banjis);
    }

    @PostMapping("/save")
    public R save(@RequestBody StudentEntity student) {
        student.setAddtime(new Date());
        studentMapper.insert(student);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody StudentEntity student) {
        studentMapper.updateById(student);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            studentMapper.deleteById(id);
        }
        return R.ok();
    }
}
