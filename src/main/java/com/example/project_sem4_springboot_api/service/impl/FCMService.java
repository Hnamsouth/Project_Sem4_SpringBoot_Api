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


@Service
@RequiredArgsConstructor
public class FCMService {

    /**
     * @thông báo cho phụ huynh khi gv điểm danh học sinh
     * @thông báo cho gv khi phụ huynh xin nghỉ cho học sinh
     * @thông báo cho phụ huynh khi gv phê duyệt xin nghỉ của học sinh
     * @thông báo cho phụ huynh khi có đợt thu mới
     * @thông báo cho phụ huynh khi có tkb mới
     * nếu phụ huynh ko có device token thì
     *
     * */
    private final String TITLE_DIEM_DANH = "Thông báo điểm danh";

    private final SchoolYearClassRepository schoolYearClassRepository;
    private final StudentYearInfoRepository studentYearInfoRepository;
    private final StudentRepository studentRepository;

    public void sendNotification(String token, String title, String body) {

        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();
        Message message = Message.builder()
                .setToken(token)
                .setNotification(notification)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
//            FirebaseMessaging.getInstance().sendAll();
            System.out.println("Successfully sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendAllNotification(){

    }

    public void attendanceNotificationToParent(LocalDate date, Long teacherSchoolYearId){
        // ngay diem danh - teacherId
        // get device token để gửi thông báo và list parent list để tạo thông báo
        var students = studentYearInfoRepository.findAllBySchoolYearClass_TeacherSchoolYear_Id(teacherSchoolYearId);
        var studentList = students.stream().map(StudentYearInfo::getStudents).toList();
//        var parents = studentList.
        UserNotification.builder().build();
    }

    public void attendanceNotificationFromTeacher(){

    }

    public void takeLeaveNotificationToParent(){

    }



}