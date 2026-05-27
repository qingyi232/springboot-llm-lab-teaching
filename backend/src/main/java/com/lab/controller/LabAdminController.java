package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.LabAdminEntity;
import com.lab.mapper.LabAdminMapper;
import com.lab.service.TokenService;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/labAdmin")
public class LabAdminController {

    @Autowired
    private LabAdminMapper labAdminMapper;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/register")
    public R register(@RequestBody LabAdminEntity labAdmin) {
        LabAdminEntity existing = labAdminMapper.selectOne(
                new QueryWrapper<LabAdminEntity>().eq("gonghao", labAdmin.getGonghao())
        );
        if (existing != null) {
            return R.error("该工号已被注册");
        }
        labAdmin.setAddtime(new Date());
        labAdminMapper.insert(labAdmin);
        return R.ok("注册成功");
    }

    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> params) {
        String gonghao = params.get("username");
        String password = params.get("password");

        LabAdminEntity labAdmin = labAdminMapper.selectOne(
                new QueryWrapper<LabAdminEntity>()
                        .eq("gonghao", gonghao)
                        .eq("mima", password)
        );

        if (labAdmin == null) {
            return R.error("工号或密码错误");
        }

        String token = tokenService.generateToken(labAdmin.getId(), labAdmin.getGonghao(), "lab_admin", "实验室管理员");
        return R.ok().put("token", token).put("user", labAdmin).put("role", "实验室管理员");
    }

    @GetMapping("/session")
    public R session(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return R.ok().put("data", labAdminMapper.selectById(userId));
    }

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String xingming,
                  @RequestParam(required = false) String gonghao) {
        QueryWrapper<LabAdminEntity> wrapper = new QueryWrapper<>();
        if (xingming != null && !xingming.isEmpty()) wrapper.like("xingming", xingming);
        if (gonghao != null && !gonghao.isEmpty()) wrapper.like("gonghao", gonghao);
        wrapper.orderByDesc("addtime");
        IPage<LabAdminEntity> result = labAdminMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("data", labAdminMapper.selectById(id));
    }

    @PostMapping("/save")
    public R save(@RequestBody LabAdminEntity labAdmin) {
        labAdmin.setAddtime(new Date());
        labAdminMapper.insert(labAdmin);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody LabAdminEntity labAdmin) {
        labAdminMapper.updateById(labAdmin);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            labAdminMapper.deleteById(id);
        }
        return R.ok();
    }
}
