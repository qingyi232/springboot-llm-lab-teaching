package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("homework_submit")
public class HomeworkSubmitEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addtime;

    private Long homeworkId;
    private String homeworkTitle;
    private String content;
    private String fujian;
    private String xingming;
    private String xuehao;
    private Long userid;
    private String zhuangtai;
}
