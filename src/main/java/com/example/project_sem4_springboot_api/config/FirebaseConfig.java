package com.example.project_sem4_springboot_api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        String FIREBASE_SERVICE_ACC = "src/main/resources/cloudmessages4-2875f-firebase-adminsdk-tzqok-de6f82a0ef.json";
        FileInputStream serviceAccount =
                new FileInputStream(FIREBASE_SERVICE_ACC);
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();
        FirebaseApp app = FirebaseApp.initializeApp(options);
        return FirebaseMessaging.getInstance(app);
    }

}
