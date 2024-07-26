package com.example.project_sem4_springboot_api.service.impl;


import com.example.project_sem4_springboot_api.entities.UserDeviceToken;
import com.example.project_sem4_springboot_api.entities.request.UserDeviceTokenReq;
import com.example.project_sem4_springboot_api.exception.ArgumentNotValidException;
import com.example.project_sem4_springboot_api.exception.DataExistedException;
import com.example.project_sem4_springboot_api.repositories.UserDeviceTokenRepository;
import com.example.project_sem4_springboot_api.repositories.UserNotificationRepository;
import com.example.project_sem4_springboot_api.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
@RequiredArgsConstructor
public class UserNotificationService {

    private final ExecutorService executorService;
    private final UserRepository userRepository;
    private final UserNotificationRepository userNotificationRepository;
    private final UserDeviceTokenRepository userDeviceTokenRepository;


    /**
     * @apiNote thông báo cho phụ huynh khi gv điểm danh học sinh
     * @apiNote thông báo cho gv khi phụ huynh xin nghỉ cho học sinh
     * @apiNote thông báo cho phụ huynh khi gv phê duyệt xin nghỉ của học sinh
     * @apiNote thông báo cho phụ huynh khi
     * có đợt thu, điểm , kết quả học tập,
     * tkb, thanh toán học phí thành công
     * nhắc phụ huynh và gv chủ nhiệm or tài chính kế toán về hs chưa thanh toán học phí khi gần hết hạn
     *
     * */

    public ResponseEntity<?> updateUserDeviceToken(UserDeviceTokenReq data){
        var userId = AuthService.getUserId();
        var udtk =userDeviceTokenRepository.findByDeviceTokenAndOsAndUser_Id(
                data.getDeviceToken(),data.getDeviceType().getName(),userId);
        if(udtk.isPresent()){
            throw new DataExistedException("Device token is already taken !!!");
        }else{
            var user = userRepository.findById(userId);
            userDeviceTokenRepository.save(
                UserDeviceToken.builder()
                    .user(user.orElseThrow())
                    .deviceToken(data.getDeviceToken())
                    .os(data.getDeviceType().getName())
                    .createdAt(java.time.LocalDate.now())
                    .build()
            );
            return ResponseEntity.ok().body("Device token is updated !!!");
        }
    }

    public ResponseEntity<?>  getUserNotifications(Long page, Long size){
        return ResponseEntity.ok().body(
            size != null ?
                userNotificationRepository.findUserNotifications(
                        AuthService.getUserId(), PageRequest.of(page.intValue(), size.intValue())
                )  :
                userNotificationRepository.findAllByUserIdOrderByCreatedAtDesc(AuthService.getUserId())
            );
    }

    public ResponseEntity<?> updateUserNotificationsReadStatus(Long id){
        var unNoti = userNotificationRepository.findByIdAndUserId(id,AuthService.getUserId()).orElseThrow(
                ()-> new ArgumentNotValidException("Id thông báo không hợp lệ !!!","id",id.toString())
        );
        unNoti.setHasRead(true);
        userNotificationRepository.save(unNoti);
        return ResponseEntity.ok().body(unNoti.toData());
    }

}
