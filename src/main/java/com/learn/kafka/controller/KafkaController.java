package com.learn.kafka.controller;

import com.learn.kafka.model.StudentDto;
import com.learn.kafka.service.KafkaSimpleProducerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    private final KafkaSimpleProducerService producerService;

    public KafkaController(KafkaSimpleProducerService producerService) {
        this.producerService = producerService;
    }

    @PostMapping("/messages.simple.topic")
    public ResponseEntity<String> sendMessageToKafka(@RequestBody String message) {

        return ResponseEntity.ok(producerService.sendSimpleTextMessage(message));

    }

    @PostMapping("/student.topic")
    public ResponseEntity<String> sendMessageToKafka(@RequestBody StudentDto message) {
        return ResponseEntity.ok(producerService.sendStudentMessage(message));
    }
}