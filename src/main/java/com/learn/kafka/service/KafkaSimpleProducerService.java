package com.learn.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learn.kafka.model.StudentDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaSimpleProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    @Value("${com.topic.simple}")
    private String simpleTopic;

    @Value("${com.topic.student}")
    private String studentTopic;

    @Autowired
    public KafkaSimpleProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(simpleTopic, message);
        handleSuccessOrException(message, future);
    }

    public String sendStudentMessage(StudentDto message) {
        String ret;
        try {
            String messageJson = objectMapper.writeValueAsString(message); // Convert StudentDto to JSON
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(studentTopic, messageJson);
            handleSuccessOrException(messageJson, future);
        } catch (JsonProcessingException e) {
            System.out.println("Error serializing StudentDto to JSON: " + e.getMessage());
        }
        return message.getFullName();
    }

    private static void handleSuccessOrException(String message, CompletableFuture<SendResult<String, String>> future) {
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            } else {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }
}