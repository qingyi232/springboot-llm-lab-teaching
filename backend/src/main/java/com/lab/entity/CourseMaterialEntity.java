package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("course_material")
public class CourseMaterialEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addtime;

    private String title;
    private String coursetype;
    private String content;
    private String fujian;
    private String tupian;
    private String faburenxingming;
    private Long faburenid;
    private Integer clicknum;
    private Integer storecount;
}
