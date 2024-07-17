package com.example.project_sem4_springboot_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
@EnableAsync
public class ExecutorConfig {

    @Bean
    public ExecutorService executorService() {
        var thr= Executors.newCachedThreadPool();
        return Executors.newCachedThreadPool(); // Tạo một thread pool với 10 luồng
        /**
         * newCachedThreadPool
         * Khi không biết trước số lượng tác vụ hoặc khi yêu cầu xử lý tác vụ thay đổi đột ngột.
         * Khi ứng dụng có tính chất động và cần sự linh hoạt cao trong việc quản lý luồng.
         * */
    }
}
