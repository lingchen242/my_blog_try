package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogVO {
    private Long id;
    private String title;
    private String content;
    private String summary;
    private String category;
    private String authorName;
    private Long authorId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}