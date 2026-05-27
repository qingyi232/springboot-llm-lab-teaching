package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.UserEntity;
import com.lab.mapper.UserMapper;
import com.lab.service.TokenService;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");

        UserEntity user = userMapper.selectOne(
                new QueryWrapper<UserEntity>()
                        .eq("username", username)
                        .eq("password", password)
        );

        if (user == null) {
            return R.error("账号或密码错误");
        }

        String token = tokenService.generateToken(user.getId(), user.getUsername(), "users", user.getRole());
        return R.ok().put("token", token).put("user", user);
    }

    @GetMapping("/session")
    public R session(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserEntity user = userMapper.selectById(userId);
        return R.ok().put("data", user);
    }

    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {
        return R.ok("退出成功");
    }

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String username) {
        QueryWrapper<UserEntity> wrapper = new QueryWrapper<>();
        if (username != null && !username.isEmpty()) {
            wrapper.like("username", username);
        }
        wrapper.orderByDesc("addtime");
        IPage<UserEntity> result = userMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        UserEntity user = userMapper.selectById(id);
        return R.ok().put("data", user);
    }

    @PostMapping("/save")
    public R save(@RequestBody UserEntity user) {
        UserEntity existing = userMapper.selectOne(
                new QueryWrapper<UserEntity>().eq("username", user.getUsername())
        );
        if (existing != null) {
            return R.error("用户名已存在");
        }
        user.setAddtime(new Date());
        userMapper.insert(user);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody UserEntity user) {
        userMapper.updateById(user);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            userMapper.deleteById(id);
        }
        return R.ok();
    }
}
