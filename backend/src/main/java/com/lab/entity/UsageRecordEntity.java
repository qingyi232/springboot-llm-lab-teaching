package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("usage_record")
public class UsageRecordEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addtime;

    private String labId;
    private String labName;
    private String shiyanshileixing;
    private Integer shiyongrenshu;
    private String shiyongjilu;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date shiyongshijian;

    private String xingming;
    private String zhuanye;
    private String banji;
    private Long userid;
    private String tablename;
}
