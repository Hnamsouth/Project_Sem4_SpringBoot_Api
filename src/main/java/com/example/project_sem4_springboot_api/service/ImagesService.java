package com.example.project_sem4_springboot_api.service;

import com.example.project_sem4_springboot_api.entities.ImageData;
import com.example.project_sem4_springboot_api.repositories.ImageRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ImagesService {

    @Autowired
    ImageRepository imageRepository;

    public List<ImageData> list(){
        return imageRepository.findByOrderById();
    }

    public Optional<ImageData> getOne(int id){
        return imageRepository.findById(id);
    }

    public void save(ImageData image){
        imageRepository.save(image);
    }

    public void delete(int id){
        imageRepository.deleteById(id);
    }

    public boolean exists(int id){
        return imageRepository.existsById(id);
    }
}

