package com.learn.kafka.controller;

import com.learn.kafka.model.StudentDto;
import com.learn.kafka.service.KafkaSimpleProducerService;
import com.learn.kafka.service.KafkaStudentProducerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaController {
    private final KafkaSimpleProducerService producerService;
    private final KafkaStudentProducerService studentProducerService;

    public KafkaController(KafkaSimpleProducerService producerService, KafkaStudentProducerService studentProducerService) {
        this.producerService = producerService;
        this.studentProducerService = studentProducerService;
    }

    @PostMapping("/messages.simple.topic")
    public void sendMessageToKafka(@RequestBody String message) {
        producerService.sendMessage(message);
    }

    @PostMapping("/student.topic")
    public String sendMessageToKafka(@RequestBody StudentDto message) {
        return producerService.sendStudentMessage(message);
    }
}