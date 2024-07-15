package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.StudentYearInfo;
import com.example.project_sem4_springboot_api.entities.UserNotification;
import com.example.project_sem4_springboot_api.repositories.SchoolYearClassRepository;
import com.example.project_sem4_springboot_api.repositories.StudentRepository;
import com.example.project_sem4_springboot_api.repositories.StudentYearInfoRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;


@Service
@RequiredArgsConstructor
public class FCMService {

    /**
     * @apiNote thông báo cho phụ huynh khi gv điểm danh học sinh
     * @apiNote thông báo cho gv khi phụ huynh xin nghỉ cho học sinh
     * @apiNote thông báo cho phụ huynh khi gv phê duyệt xin nghỉ của học sinh
     * @apiNote thông báo cho phụ huynh khi có đợt thu mới
     * @apiNote thông báo cho phụ huynh khi có tkb mới
     * nếu phụ huynh ko có device token thì
     *
     * */
    private final String TITLE_DIEM_DANH = "Thông báo điểm danh";

    private final SchoolYearClassRepository schoolYearClassRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final StudentRepository studentRepository;
    private final ExecutorService executorService;

    public void sendNotification(String token, String title, String body) {
        executorService.submit(()->{
            try {
                Notification notification = Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .setImage("https://images.pexels.com/photos/18939401/pexels-photo-18939401/free-photo-of-den-va-tr-ng-thanh-ph-xe-h-i-giao-thong.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1")
                        .build();
                Message message = Message.builder()
                        .putAllData(Map.of(
                                "data1", "asda sd",
                                "data3", "asda aasfsfsd",
                                "data2", "asdasd asd"
                                )
                        )
                        .setToken(token)
                        .setNotification(notification)
                        .build();
                String response = FirebaseMessaging.getInstance().send(message);
//            FirebaseMessaging.getInstance().sendAll();
                System.out.println("Successfully sent message: " + response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void sendAllNotification(List<String> tokens, Map<String,String> data, String title, String body, String image) {
        executorService.submit(()->{
            try {
                Notification notification = Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .setImage(image)
                        .build();
                List<Message> messageList = tokens.stream().map(tk->Message.builder()
                        .setToken(tk)
                        .putAllData(data)
                        .setNotification(notification)
                        .build()).toList();
                var response = FirebaseMessaging.getInstance().sendAll(messageList);
                System.out.println("Successfully sendAll message: "+title+"\t" + response.getSuccessCount() + "/"+response.getFailureCount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void attendanceNotificationToParent(LocalDate date, Long teacherSchoolYearId){
        // ngay diem danh - teacherId
        // get device token để gửi thông báo và list parent list để tạo thông báo
        var students = studentYearInfoRepository.findAllBySchoolYearClass_TeacherSchoolYear_Id(teacherSchoolYearId);
        var studentList = students.stream().map(StudentYearInfo::getStudents).toList();
//        var parents = studentList.
        UserNotification.builder().build();
    }




}