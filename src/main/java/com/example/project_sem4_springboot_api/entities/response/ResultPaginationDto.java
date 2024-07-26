package com.example.project_sem4_springboot_api.entities.response;

import com.example.project_sem4_springboot_api.entities.Meta;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultPaginationDto {

    private Meta meta;

    private Object result;

}

