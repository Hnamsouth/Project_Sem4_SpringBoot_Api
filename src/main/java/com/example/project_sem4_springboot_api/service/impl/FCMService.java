package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.StudentYearInfo;
import com.example.project_sem4_springboot_api.entities.UserNotification;
import com.example.project_sem4_springboot_api.repositories.SchoolYearClassRepository;
import com.example.project_sem4_springboot_api.repositories.StudentRepository;
import com.example.project_sem4_springboot_api.repositories.StudentYearInfoRepository;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
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

    private final String IMG_URL = "https://images.pexels.com/photos/18939401/pexels-photo-18939401/free-photo-of-den-va-tr-ng-thanh-ph-xe-h-i-giao-thong.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";

    public void sendNotification(String token, String title, String body) {
        executorService.submit(()->{
            try {
                Notification notification = Notification.builder()
                        .setTitle(title)
                        .setBody(body)
                        .setImage(IMG_URL)
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
                List<Message> messageList = tokens.stream().map(tk->allPlatformsMessage(tk,data,notification)).toList();
                var response = FirebaseMessaging.getInstance().sendAll(messageList);
                System.out.println("Successfully sendAll message: "+title+"\t" + response.getSuccessCount() + "/"+response.getFailureCount());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public Message allPlatformsMessage(String token,Map<String,String> data,Notification notification) {
        // [START multi_platforms_message]
        return Message.builder()
                .setTopic("industry-tech")
                .setToken(token)
                .putAllData(data)
                .setNotification(notification)
                .setNotification(Notification.builder()
                        .setTitle("$GOOG up 1.43% on the day")
                        .setBody("$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.")
                        .build())
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000)
                        .setNotification(AndroidNotification.builder()
                                .setIcon("stock_ticker_update")
                                .setColor("#f45342")
                                .build())
                        .build())
                .setApnsConfig(ApnsConfig.builder()
                        .setAps(Aps.builder()
                                .setBadge(42)
                                .build())
                        .build())
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(new WebpushNotification(
                                "$GOOG up 1.43% on the day",
                                "$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.",
                                "https://my-server/icon.png"))
                        .setFcmOptions(WebpushFcmOptions.withLink("https://my-server/page-to-open-on-click"))
                        .build())
                .build();
        // [END multi_platforms_message]
    }

    public Message androidMessage() {
        // [START android_message]
        Message message = Message.builder()
                .setAndroidConfig(AndroidConfig.builder()
                        .setTtl(3600 * 1000) // 1 hour in milliseconds
                        .setPriority(AndroidConfig.Priority.NORMAL)
                        .setNotification(AndroidNotification.builder()
                                .setTitle("$GOOG up 1.43% on the day")
                                .setBody("$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.")
                                .setIcon("stock_ticker_update")
                                .setColor("#f45342")
                                .build())
                        .build())
                .setTopic("industry-tech")
                .build();
        // [END android_message]
        return message;
    }

    public Message apnsMessage() {
        // [START apns_message]
        Message message = Message.builder()
                .setApnsConfig(ApnsConfig.builder()
                        .putHeader("apns-priority", "10")
                        .setAps(Aps.builder()
                                .setAlert(ApsAlert.builder()
                                        .setTitle("$GOOG up 1.43% on the day")
                                        .setBody("$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.")
                                        .build())
                                .setBadge(42)
                                .build())
                        .build())
                .setTopic("industry-tech")
                .build();
        // [END apns_message]
        return message;
    }

    public Message webpushMessage() {
        // [START webpush_message]
        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(new WebpushNotification(
                                "$GOOG up 1.43% on the day",
                                "$GOOG gained 11.80 points to close at 835.67, up 1.43% on the day.",
                                "https://my-server/icon.png"))
                        .setFcmOptions(WebpushFcmOptions.withLink("https://my-server/page-to-open-on-click"))
                        .build())
                .setTopic("industry-tech")
                .build();
        // [END webpush_message]
        return message;
    }


    public void subscribeToTopic() throws FirebaseMessagingException {
        String topic = "highScores";
        // [START subscribe]
        // These registration tokens come from the client FCM SDKs.
        List<String> registrationTokens = Arrays.asList(
                "YOUR_REGISTRATION_TOKEN_1",
                // ...
                "YOUR_REGISTRATION_TOKEN_n"
        );

        // Subscribe the devices corresponding to the registration tokens to the
        // topic.
        TopicManagementResponse response = FirebaseMessaging.getInstance().subscribeToTopic(
                registrationTokens, topic);
        // See the TopicManagementResponse reference documentation
        // for the contents of response.
        System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");
        // [END subscribe]
    }

    public void unsubscribeFromTopic() throws FirebaseMessagingException {
        String topic = "highScores";
        // [START unsubscribe]
        // These registration tokens come from the client FCM SDKs.
        List<String> registrationTokens = Arrays.asList(
                "YOUR_REGISTRATION_TOKEN_1",
                // ...
                "YOUR_REGISTRATION_TOKEN_n"
        );

        // Unsubscribe the devices corresponding to the registration tokens from
        // the topic.
        TopicManagementResponse response = FirebaseMessaging.getInstance().unsubscribeFromTopic(
                registrationTokens, topic);
        // See the TopicManagementResponse reference documentation
        // for the contents of response.
        System.out.println(response.getSuccessCount() + " tokens were unsubscribed successfully");
        // [END unsubscribe]
    }


}