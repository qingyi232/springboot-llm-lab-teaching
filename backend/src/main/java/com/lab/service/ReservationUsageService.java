package com.lab.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lab.entity.ReservationEntity;
import com.lab.entity.UsageRecordEntity;
import com.lab.mapper.ReservationMapper;
import com.lab.mapper.UsageRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ReservationUsageService {

    @Autowired
    private ReservationMapper reservationMapper;

    @Autowired
    private UsageRecordMapper usageRecordMapper;

    /**
     * 审核通过时根据预约生成使用记录（幂等，避免重复插入）
     */
    public void createUsageRecordIfAbsent(ReservationEntity reservation) {
        if (reservation == null || !"已通过".equals(reservation.getSfsh())) {
            return;
        }
        QueryWrapper<UsageRecordEntity> exist = new QueryWrapper<>();
        exist.eq("userid", reservation.getUserid());
        if (reservation.getLabId() != null && !reservation.getLabId().isEmpty()) {
            exist.eq("lab_id", reservation.getLabId());
        } else {
            exist.eq("lab_name", reservation.getLabName());
        }
        if (reservation.getYuyueshijian() != null) {
            exist.eq("shiyongshijian", reservation.getYuyueshijian());
        }
        if (usageRecordMapper.selectCount(exist) > 0) {
            return;
        }

        UsageRecordEntity record = new UsageRecordEntity();
        record.setAddtime(new Date());
        record.setLabId(reservation.getLabId());
        record.setLabName(reservation.getLabName());
        record.setShiyanshileixing(reservation.getShiyanshileixing());
        record.setShiyongrenshu(reservation.getYuyuerenshu() != null ? reservation.getYuyuerenshu() : 1);
        record.setShiyongjilu(reservation.getYuyueshuoming());
        record.setShiyongshijian(reservation.getYuyueshijian() != null ? reservation.getYuyueshijian() : new Date());
        record.setXingming(reservation.getXingming());
        record.setZhuanye(reservation.getZhuanye());
        record.setBanji(reservation.getBanji());
        record.setUserid(reservation.getUserid());
        record.setTablename(reservation.getTablename());
        usageRecordMapper.insert(record);
    }

    /**
     * 为指定用户补全「已通过」但尚未生成使用记录的预约（兼容历史数据与错误审核路径）
     */
    public int syncUsageRecordsForUser(Long userId) {
        if (userId == null) {
            return 0;
        }
        List<ReservationEntity> approved = reservationMapper.selectList(
                new QueryWrapper<ReservationEntity>()
                        .eq("userid", userId)
                        .eq("sfsh", "已通过")
        );
        int created = 0;
        Date now = new Date();
        for (ReservationEntity r : approved) {
            Date end = r.getYuyuejieshu() != null ? r.getYuyuejieshu() : r.getYuyueshijian();
            if (end != null && end.after(now)) {
                continue;
            }
            long before = usageRecordMapper.selectCount(buildExistWrapper(r));
            createUsageRecordIfAbsent(r);
            long after = usageRecordMapper.selectCount(buildExistWrapper(r));
            if (after > before) {
                created++;
            }
        }
        return created;
    }

    private QueryWrapper<UsageRecordEntity> buildExistWrapper(ReservationEntity reservation) {
        QueryWrapper<UsageRecordEntity> exist = new QueryWrapper<>();
        exist.eq("userid", reservation.getUserid());
        if (reservation.getLabId() != null && !reservation.getLabId().isEmpty()) {
            exist.eq("lab_id", reservation.getLabId());
        } else {
            exist.eq("lab_name", reservation.getLabName());
        }
        if (reservation.getYuyueshijian() != null) {
            exist.eq("shiyongshijian", reservation.getYuyueshijian());
        }
        return exist;
    }
}
