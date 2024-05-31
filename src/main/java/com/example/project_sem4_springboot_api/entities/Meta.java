package com.example.project_sem4_springboot_api.entities;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Meta {

    private int page;

    private int pageSize;

    private int pages;

    private long total;

}