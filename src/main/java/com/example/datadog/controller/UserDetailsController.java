package com.example.datadog.controller;

import com.example.datadog.repository.UserData;
import com.example.datadog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserDetailsController {

    private final UserService service;


    @Timed(value = "http_request_user", extraTags = {"type", "list"}, percentiles = {0.99, 0.95, 0.75})
    @GetMapping
    public ResponseEntity<List<UserData>> list(){
        return ResponseEntity.ok(service.list());
    }


    @Timed(value = "http_request_user", extraTags = {"type", "find"}, percentiles = {0.99, 0.95, 0.75})
    @GetMapping("/{id}")
    public ResponseEntity<UserData> findById(@PathVariable String id) throws JsonProcessingException {
        log.info("Received request.");
        putMDC(id, "find");
        return ResponseEntity.ok(service.findUserByKey(id));
    }

    @Timed(value = "http_request_user", extraTags = {"type", "save"}, percentiles = {0.99, 0.95, 0.75})
    @PostMapping
    public ResponseEntity<Void> save(@RequestBody UserData data) throws JsonProcessingException, InterruptedException {
        log.info("Received request.");
        putMDC(data.id(), "save");
        service.saveUser(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private void putMDC(String userId, String endpoint) {
        if (MDC.getMDCAdapter() != null) MDC.clear();
        MDC.put("UserId", userId);
        MDC.put("TraceId", UUID.randomUUID().toString());
        MDC.put("Endpoint", endpoint);
    }


}
