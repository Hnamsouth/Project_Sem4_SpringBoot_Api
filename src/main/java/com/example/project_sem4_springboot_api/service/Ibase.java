package com.example.project_sem4_springboot_api.service;

public interface Ibase<S,K> {
    S getAll() ;
    S create(K k);
    S update(K s);
    S delete(Long e);
    S search(String e);
}
