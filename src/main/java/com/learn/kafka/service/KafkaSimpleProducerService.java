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
import java.util.concurrent.ExecutionException;

@Service
public class KafkaSimpleProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${com.topic.simple}") private String simpleTopic;
    @Value("${com.topic.student}") private String studentTopic;

    @Autowired
    public KafkaSimpleProducerService(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public String sendSimpleTextMessage(String message) {
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(simpleTopic, message);
        CompletableFuture<Void> voidCompletableFuture = handleSuccessOrException(message, future)
                .thenAccept(result -> {
                    System.out.println("Received final result: " + result);
        });

        String str = "Simple Message Sent to Topic :: " + simpleTopic;

        voidCompletableFuture.thenRun(() -> System.out.println(str));

        return str;
    }

    public String sendStudentMessage(StudentDto message) {
        String ret = "";
        try {
            String messageJson = objectMapper.writeValueAsString(message); // Convert StudentDto to JSON
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(studentTopic, messageJson);
            CompletableFuture<String> stringCompletableFuture = handleSuccessOrException(messageJson, future);

            ret =  stringCompletableFuture.get();
        } catch (JsonProcessingException | InterruptedException | ExecutionException e) {
            System.out.println("Error serializing StudentDto to JSON: " + e.getMessage());
        }
        return ret;
    }

    private static CompletableFuture<String> handleSuccessOrException(String message, CompletableFuture<SendResult<String, String>> future) {
        return future
                .thenApply(result -> "Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]" + result.getProducerRecord().headers())
                .exceptionally(ex -> "Unable to send message=[" + message + "] due to: " + ex.getMessage())
                .thenApplyAsync(resultMessage -> {
                    // Perform additional asynchronous processing
                    String processedResult = resultMessage + " - Additional Processing";
                    System.out.println(processedResult);
                    return processedResult;
                });
    }
}