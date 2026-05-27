package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.ReservationEntity;
import com.lab.mapper.ReservationMapper;
import com.lab.service.ReservationUsageService;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private ReservationUsageService reservationUsageService;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String labName,
                  @RequestParam(required = false) String xingming,
                  @RequestParam(required = false) String sfsh) {
        QueryWrapper<ReservationEntity> wrapper = new QueryWrapper<>();
        if (labName != null && !labName.isEmpty()) wrapper.like("lab_name", labName);
        if (xingming != null && !xingming.isEmpty()) wrapper.like("xingming", xingming);
        if (sfsh != null && !sfsh.isEmpty()) wrapper.eq("sfsh", sfsh);
        wrapper.orderByDesc("addtime");
        IPage<ReservationEntity> result = reservationMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest request,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) request.getAttribute("userId");
        String tablename = (String) request.getAttribute("tablename");
        QueryWrapper<ReservationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userId);
        if (tablename != null) wrapper.eq("tablename", tablename);
        wrapper.orderByDesc("addtime");
        IPage<ReservationEntity> result = reservationMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("data", reservationMapper.selectById(id));
    }

    @PostMapping("/add")
    public R add(@RequestBody ReservationEntity reservation, HttpServletRequest request) {
        String role = (String) request.getAttribute("role");
        if ("实验室管理员".equals(role)) {
            return R.error("实验室管理员无需预约实验室");
        }
        Long userId = (Long) request.getAttribute("userId");
        String tablename = (String) request.getAttribute("tablename");
        reservation.setUserid(userId);
        reservation.setTablename(tablename);
        reservation.setAddtime(new Date());
        reservation.setSfsh("待审核");
        reservation.setZhuangtai("待审核");
        reservationMapper.insert(reservation);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody ReservationEntity reservation) {
        ReservationEntity old = reservationMapper.selectById(reservation.getId());
        reservationMapper.updateById(reservation);
        ReservationEntity latest = reservationMapper.selectById(reservation.getId());
        if (latest != null && "已通过".equals(latest.getSfsh())
                && (old == null || !"已通过".equals(old.getSfsh()))) {
            reservationUsageService.createUsageRecordIfAbsent(latest);
        }
        return R.ok();
    }

    @PostMapping("/sh")
    public R sh(@RequestBody ReservationEntity reservation) {
        ReservationEntity entity = reservationMapper.selectById(reservation.getId());
        if (entity == null) {
            return R.error("预约不存在");
        }
        entity.setSfsh(reservation.getSfsh());
        entity.setShhf(reservation.getShhf());
        entity.setZhuangtai(reservation.getSfsh());
        reservationMapper.updateById(entity);

        if ("已通过".equals(reservation.getSfsh())) {
            reservationUsageService.createUsageRecordIfAbsent(entity);
        }
        return R.ok();
    }

    /** 补全当前用户已通过且预约时段已结束、但缺少使用记录的预约 */
    @PostMapping("/syncUsageRecords")
    public R syncUsageRecords(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        int created = reservationUsageService.syncUsageRecordsForUser(userId);
        return R.ok().put("created", created);
    }

    @PostMapping("/cancel/{id}")
    public R cancel(@PathVariable Long id) {
        ReservationEntity entity = reservationMapper.selectById(id);
        if (entity != null) {
            entity.setSfsh("已取消");
            entity.setZhuangtai("已取消");
            reservationMapper.updateById(entity);
        }
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            reservationMapper.deleteById(id);
        }
        return R.ok();
    }
}
