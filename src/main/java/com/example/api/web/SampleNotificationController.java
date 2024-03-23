package com.example.api.web;

import com.example.api.service.NotificationService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SubmissionPublisher;

@RestController
public class SampleNotificationController {

    @Autowired
    private final NotificationService notificationService;

    public SampleNotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    /**
     * @title 로그인 한 유저 sse 연결
     */
    @GetMapping(value = "/api/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe() {

        SseEmitter emitter = new SseEmitter(400000L);
        ExecutorService sseMvcExecutor = Executors.newSingleThreadExecutor();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; i < 40; i++) {

                    JSONObject event = new JSONObject();
                    event.put("data", i);
                    event.put("date", LocalTime.now().toString());
                    emitter.send(event);
                    Thread.sleep(1000);
                }
                emitter.complete();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        return emitter;
    }
}
