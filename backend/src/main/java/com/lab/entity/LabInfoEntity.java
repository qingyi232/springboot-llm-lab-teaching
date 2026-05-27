package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("lab_info")
public class LabInfoEntity implements Serializable {
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
    private Integer rongna;
    private String zhuangtai;
    private String kaifangshijian;
    private String jianjie;
    private String guanliyuan;

    @TableField(exist = false)
    private Integer yiyuyue;

    @TableField(exist = false)
    private Integer shenyuzuowei;
}
