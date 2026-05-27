package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.UsageRecordEntity;
import com.lab.mapper.UsageRecordMapper;
import com.lab.service.ReservationUsageService;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/usageRecord")
public class UsageRecordController {

    @Autowired
    private UsageRecordMapper usageRecordMapper;

    @Autowired
    private ReservationUsageService reservationUsageService;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String labName,
                  @RequestParam(required = false) String xingming) {
        QueryWrapper<UsageRecordEntity> wrapper = new QueryWrapper<>();
        if (labName != null && !labName.isEmpty()) wrapper.like("lab_name", labName);
        if (xingming != null && !xingming.isEmpty()) wrapper.like("xingming", xingming);
        wrapper.orderByDesc("addtime");
        IPage<UsageRecordEntity> result = usageRecordMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest request,
                    @RequestParam(defaultValue = "1") Integer page,
                    @RequestParam(defaultValue = "10") Integer limit) {
        Long userId = (Long) request.getAttribute("userId");
        String tablename = (String) request.getAttribute("tablename");
        reservationUsageService.syncUsageRecordsForUser(userId);
        QueryWrapper<UsageRecordEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("userid", userId);
        if (tablename != null && !tablename.isEmpty()) {
            wrapper.and(w -> w.eq("tablename", tablename).or().isNull("tablename").or().eq("tablename", ""));
        }
        wrapper.orderByDesc("addtime");
        IPage<UsageRecordEntity> result = usageRecordMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @PostMapping("/save")
    public R save(@RequestBody UsageRecordEntity record) {
        record.setAddtime(new Date());
        usageRecordMapper.insert(record);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody UsageRecordEntity record) {
        usageRecordMapper.updateById(record);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            usageRecordMapper.deleteById(id);
        }
        return R.ok();
    }
}
