package com.example.project_sem4_springboot_api.config;

import com.example.project_sem4_springboot_api.controller.service.WebSocketController;
import com.example.project_sem4_springboot_api.entities.request.Notifications;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Scheduled(fixedRate = 30000)
    public void scheduleTaskWithFixedRate() {
        WebSocketController webSocketController = new WebSocketController();
        webSocketController.sendMessage(
                Notifications.builder()
                        .content("FIRST MESSAGE")
                        .sender("ADMIN")
                        .type(Notifications.NTF_Type.DEFAULT)
                        .build()
        );
//        System.out.println("Fixed Rate Task :: Execution Time - " + System.currentTimeMillis() / 1000);
    }
    /*
    * (cron = "0 * * * * *") : chạy mỗi phút
    * (cron = "0 0 * * * *") : chạy mỗi giờ
    * (cron = "0 * 9 * * *") : chạy mỗi phút vào lúc 9h
    * 0 0 0 * * * * : Seconds | Minutes | Hours | Day Of Month | Month | Day Of Week | Year
    * */
    // @Scheduled(cron = "0 * 9 * * ?") // chạy mỗi phút
//    @Scheduled(cron = "0 30 13 * * *")
//    public void scheduleTaskWithCronExpression() {
//        System.out.println("Cron Task :: Execution Time - " + System.currentTimeMillis() / 1000);
//    }

//    @Scheduled(fixedDelay = 2000)
//    public void scheduleTaskWithFixedDelay() {
//        System.out.println("Fixed Delay Task :: Execution Time - " + System.currentTimeMillis() / 1000);
//    }

//    @Scheduled(initialDelay = 3000, fixedDelay = 5000)
//    public void scheduleTaskWithInitialDelay() {
//        System.out.println("Initial Delay Task :: Execution Time - " + System.currentTimeMillis() / 1000);
//    }
}
