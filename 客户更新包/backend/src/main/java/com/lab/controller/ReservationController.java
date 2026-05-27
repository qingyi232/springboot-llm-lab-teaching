package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.ReservationEntity;
import com.lab.entity.UsageRecordEntity;
import com.lab.mapper.ReservationMapper;
import com.lab.mapper.UsageRecordMapper;
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
    private UsageRecordMapper usageRecordMapper;

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
        QueryWrapper<ReservationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userId).orderByDesc("addtime");
        IPage<ReservationEntity> result = reservationMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/info/{id}")
    public R info(@PathVariable Long id) {
        return R.ok().put("data", reservationMapper.selectById(id));
    }

    @PostMapping("/add")
    public R add(@RequestBody ReservationEntity reservation, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        reservation.setUserid(userId);
        reservation.setAddtime(new Date());
        reservation.setSfsh("待审核");
        reservation.setZhuangtai("待审核");
        reservationMapper.insert(reservation);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody ReservationEntity reservation) {
        reservationMapper.updateById(reservation);
        return R.ok();
    }

    @PostMapping("/sh")
    public R sh(@RequestBody ReservationEntity reservation) {
        ReservationEntity entity = reservationMapper.selectById(reservation.getId());
        entity.setSfsh(reservation.getSfsh());
        entity.setShhf(reservation.getShhf());
        entity.setZhuangtai(reservation.getSfsh());
        reservationMapper.updateById(entity);

        if ("已通过".equals(reservation.getSfsh())) {
            UsageRecordEntity record = new UsageRecordEntity();
            record.setAddtime(new Date());
            record.setLabId(entity.getLabId());
            record.setLabName(entity.getLabName());
            record.setShiyanshileixing(entity.getShiyanshileixing());
            record.setShiyongrenshu(entity.getYuyuerenshu() != null ? entity.getYuyuerenshu() : 1);
            record.setShiyongjilu(entity.getYuyueshuoming());
            record.setShiyongshijian(entity.getYuyueshijian());
            record.setXingming(entity.getXingming());
            record.setZhuanye(entity.getZhuanye());
            record.setBanji(entity.getBanji());
            record.setUserid(entity.getUserid());
            usageRecordMapper.insert(record);
        }
        return R.ok();
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
