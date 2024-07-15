package com.example.project_sem4_springboot_api.service;

import com.example.project_sem4_springboot_api.entities.HomeWork;
import com.example.project_sem4_springboot_api.repositories.HomeWorkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SystemSchedule {
    /*
     * @Scheduled(cron = "0 * * * * *")
     * (cron = "0 * * * * *") : chạy mỗi phút
     * (cron = "0 0 * * * *") : chạy mỗi giờ
     * (cron = "0 * 9 * * *") : chạy mỗi phút vào lúc 9h
     * 0 0 0 * * * * : Seconds | Minutes | Hours | Day Of Month | Month | Day Of Week | Year
     * */
    private final HomeWorkRepository homeWorkRepository;

    @Scheduled(fixedRate = 60000) // Kiểm tra mỗi phút
    public void checkPerMinute(){
        checkAndUpdateHomeWork();
    }

    // kiểm tra và update bài tập về nhà đã hết hạn
    public void checkAndUpdateHomeWork() {
        Date now = new Date();
        List<HomeWork> homeWorks = homeWorkRepository.findAllByDueDateBeforeAndStatus(now,true);
        if(!homeWorks.isEmpty()){
            var hw= homeWorks.stream().map(h->{
                h.setStatus(false);
                h.setStatusName("Đã hết hạn");
                System.out.println("HomeWork: " + h.getId() + " đã hết hạn\t" + now);
                return h;
            }).toList();
            homeWorkRepository.saveAll(hw);
        }
    }

    // ktra và gửi thông báo cho phụ huynh về thời hạn nộp học phí

}
