package com.example.project_sem4_springboot_api.service.impl;

import com.example.project_sem4_springboot_api.entities.enums.UserNotificationActionType;
import com.example.project_sem4_springboot_api.entities.request.UserNotifyRes;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    private final String TEACHER_TOPIC = "teacher-t2207a";
    private final String TEACHER_PARENT = "parent-t2207a";
    private final String TEACHER_BGH = "bgh-t2207a";
    private final String TEACHER_NV = "nv-t2207a";

    private final ExecutorService executorService;



    private final String ANDROID_TK="ev4i5OVGRbyiyHngoQflNm:APA91bH0KWavda1t91Hcdyka56CZzASzAR4KGK9XxYnVynzJnuHL_3zCcHPgMRNoRJ2umVvDJ4PYljEVoLwzd7uFuFaje1SjNkgbG0otU3YufNmhn7rjY0tbiKsOTIZYYd7fmSgJRp4W";
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

    public void sendAllNotification(UserNotifyRes req) {
        executorService.submit(()->{
            try {
                req.setData(Map.of(
                        "data1", "asda sd",
                        "data3", "asda aasfsfsd",
                        "data2", "asdasd asd"
                        ));
                List<Message> messageList = req.getTokens().stream()
                        .map(tk->allPlatformsMessage(tk,req)).toList();
                var response = FirebaseMessaging.getInstance().sendAll(messageList);
                System.out.println("Successfully sendAll message: "+req.getTitle()+"\t" + response.getSuccessCount() + "/"+req.getTokens().size());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    public Message allPlatformsMessage(String token,UserNotifyRes req) {
        // [START multi_platforms_message]
        return Message.builder()
//                .setTopic("industry-tech")
                .setToken(token)
                .setAndroidConfig(androidConfig(req))
                .setWebpushConfig(webpushConfig(req))
                .build();
        // [END multi_platforms_message]
    }

    public AndroidConfig androidConfig(UserNotifyRes req){
        return AndroidConfig.builder()
                .setTtl(3600 * 1000)
                .setNotification(AndroidNotification.builder()
                        .setIcon("stock_ticker_update")
                        .setColor("#f45342")
                        .build())
                .build();
    }

    public WebpushConfig webpushConfig(UserNotifyRes req){
        // [START webpush_config]
        return WebpushConfig.builder()
                .putHeader("ttl", "300")
                .putAllData(req.getData())
                .setNotification( WebpushNotification.builder()
                        .setTitle(req.getTitle())
                        .setBody(req.getBody())
                        .setDirection(WebpushNotification.Direction.LEFT_TO_RIGHT)
                        .setRequireInteraction(true)
                        .addAllActions(List.of(
                                new WebpushNotification.Action(
                                        "http://localhost:3000/",
                                        UserNotificationActionType.STUDENT_ATTENDANCE.getRouterPath(),
                                        "https://my-server/order/123"
                                )
                        ))
                        .build())
                .setFcmOptions(WebpushFcmOptions.withLink("http://localhost:3000"+UserNotificationActionType.STUDENT_ATTENDANCE.getRouterPath()))
                .build();
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

    public Message webpushMessage(UserNotifyRes req) {
        // [START webpush_message]
        Message message = Message.builder()
            .setWebpushConfig(WebpushConfig.builder()
                .putAllData(req.getData()==null?Map.of():req.getData())
                .putHeader("ttl", "300")
                .setNotification(
                    WebpushNotification.builder()
                        .addAllActions(List.of(
                            new WebpushNotification.Action(
                                UserNotificationActionType.STUDENT_ATTENDANCE.toString(),
                                UserNotificationActionType.STUDENT_ATTENDANCE.getRouterPath(),
                                "https://my-server/order/123"
                            )
                        ))
                        .setData(req.getData())
                        .setImage(req.getImage())
                        .setTitle(req.getTitle())
                        .setBody(req.getBody())
                        .build())
                .setFcmOptions(WebpushFcmOptions.withLink("https://my-server/page-to-open-on-click"))
                .build())
            .setTopic("industry-tech")
            .build();
        // [END webpush_message]
        return message;
    }




    public void subscribeToTopic(List<String> registrationTokens,String topic) throws FirebaseMessagingException {
        executorService.submit(()->{
            // Subscribe the devices corresponding to the registration tokens to the topic
            try {
                 var response = FirebaseMessaging.getInstance().subscribeToTopic(
                        registrationTokens, topic);
                System.out.println(response.getSuccessCount() + " tokens were subscribed successfully");

            } catch (FirebaseMessagingException e) {
                throw new RuntimeException(e);
            }
            // See the TopicManagementResponse reference documentation
            // for the contents of response.
            // [END subscribe]
        });
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