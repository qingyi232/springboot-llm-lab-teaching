package com.lab.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("video")
public class VideoEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date addtime;

    private String title;
    private String coursetype;
    private String videourl;
    private String tupian;
    private String content;
    private String faburenxingming;
    private Long faburenid;
    private Integer clicknum;
    private Integer thumbsup;
    private Integer crazynum;
    private Integer storecount;
}
