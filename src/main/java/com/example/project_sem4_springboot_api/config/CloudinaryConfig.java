package com.example.project_sem4_springboot_api.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "doyi7x2s3",
                "api_key", "642753163166488",
                "api_secret", "bjRR3CDlmpr3rsen0Tc-J5_QQCU"
        ));
    }
}
