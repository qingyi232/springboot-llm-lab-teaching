package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("reservation")
public class ReservationEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addtime;

    private String labId;
    private String labName;
    private String shiyanshileixing;
    private String tupian;
    private String weizhi;
    private String yuyueshuoming;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date yuyueshijian;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date yuyuejieshu;

    private String xingming;
    private String xuehao;
    private String zhuanye;
    private String banji;
    private Integer yuyuerenshu;
    private String zhuangtai;
    private String sfsh;
    private String shhf;
    private Long userid;
    private String tablename;
}
