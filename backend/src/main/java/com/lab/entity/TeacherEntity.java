package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("teacher")
public class TeacherEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addtime;

    private String gonghao;
    private String mima;
    private String jiaoshixingming;
    private String touxiang;
    private String xingbie;
    private Integer nianling;
    private String zhicheng;
    private String shouji;
    private String youxiang;
}
