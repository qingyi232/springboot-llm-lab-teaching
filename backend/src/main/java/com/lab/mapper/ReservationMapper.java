package com.lab.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.lab.entity.ReservationEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReservationMapper extends BaseMapper<ReservationEntity> {
}
