package com.lab.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lab.entity.LabInfoEntity;
import com.lab.entity.ReservationEntity;
import com.lab.mapper.LabInfoMapper;
import com.lab.mapper.ReservationMapper;
import com.lab.utils.PageUtils;
import com.lab.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/labInfo")
public class LabInfoController {

    @Autowired
    private LabInfoMapper labInfoMapper;

    @Autowired
    private ReservationMapper reservationMapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String labName,
                  @RequestParam(required = false) String shiyanshileixing,
                  @RequestParam(required = false) String zhuangtai) {
        QueryWrapper<LabInfoEntity> wrapper = new QueryWrapper<>();
        if (labName != null && !labName.isEmpty()) wrapper.like("lab_name", labName);
        if (shiyanshileixing != null && !shiyanshileixing.isEmpty()) wrapper.eq("shiyanshileixing", shiyanshileixing);
        if (zhuangtai != null && !zhuangtai.isEmpty()) wrapper.eq("zhuangtai", zhuangtai);
        wrapper.orderByDesc("addtime");
        IPage<LabInfoEntity> result = labInfoMapper.selectPage(new Page<>(page, limit), wrapper);
        return R.ok().put("data", new PageUtils(result));
    }

    @GetMapping("/list")
    public R list(@RequestParam(required = false) String shiyanshileixing) {
        QueryWrapper<LabInfoEntity> wrapper = new QueryWrapper<>();
        if (shiyanshileixing != null && !shiyanshileixing.isEmpty()) {
            wrapper.eq("shiyanshileixing", shiyanshileixing);
        }
        wrapper.orderByDesc("addtime");
        return R.ok().put("data", labInfoMapper.selectList(wrapper));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        LabInfoEntity lab = labInfoMapper.selectById(id);
        if (lab != null && lab.getLabId() != null) {
            QueryWrapper<ReservationEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("lab_id", lab.getLabId()).eq("sfsh", "已通过");
            java.util.List<ReservationEntity> approved = reservationMapper.selectList(wrapper);
            int yiyuyue = 0;
            for (ReservationEntity r : approved) {
                if (r.getYuyuerenshu() != null) {
                    yiyuyue += r.getYuyuerenshu();
                }
            }
            lab.setYiyuyue(yiyuyue);
            int rongna = lab.getRongna() != null ? lab.getRongna() : 0;
            lab.setShenyuzuowei(Math.max(0, rongna - yiyuyue));
        }
        return R.ok().put("data", lab);
    }

    @PostMapping("/save")
    public R save(@RequestBody LabInfoEntity labInfo) {
        labInfo.setAddtime(new Date());
        labInfoMapper.insert(labInfo);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody LabInfoEntity labInfo) {
        labInfoMapper.updateById(labInfo);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) {
            labInfoMapper.deleteById(id);
        }
        return R.ok();
    }
}
