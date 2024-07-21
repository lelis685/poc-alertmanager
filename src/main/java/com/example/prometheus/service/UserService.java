package com.example.prometheus.service;


import com.example.prometheus.config.MetricsHelper;
import com.example.prometheus.repository.UserData;
import com.example.prometheus.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final MetricsHelper metrics;

    public void saveUser(UserData user) throws JsonProcessingException, InterruptedException {
        validateRequest(user);
        log.info("Saving user.");
        repository.save(user.id(), user);
        metrics.incrCounter("process_save_country",
                "type","save",
                "country",user.country(),
                "is_single", String.valueOf(user.isSingle()));
    }

    public UserData findUserByKey(String id) throws JsonProcessingException {
        log.info("Finding user.");
        UserData user = repository.findByKey(id);
        metrics.incrCounter("process_find_country",
                "country",user.country(),
                "is_single", String.valueOf(user.isSingle()));
        return user;
    }

    public List<UserData> list() {
        log.info("Listing user.");
        return repository.list();
    }


    private void validateRequest(UserData userData) throws InterruptedException {
        //fake implementation
        log.info("Validating user.");
        Thread.sleep(generateRandomInt());
    }

    private int generateRandomInt() {
        return new Random().ints(50, 600)
                .findFirst()
                .getAsInt();
    }

}
