package com.example.project_sem4_springboot_api.service;

import com.example.project_sem4_springboot_api.dto.TeacherUpdateDto;
import org.springframework.http.ResponseEntity;

import java.security.Key;

public interface Ibase<S,K> {
    S getAll() ;
    S create(K k);
    S update(K s);
    S delete(Long e);
    S search(String e);
}
