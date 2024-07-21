package com.example.prometheus.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;


@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {

    private final RedisTemplate<String, String> template;
    private final ObjectMapper mapper;

    public void save(String key, UserData value) throws JsonProcessingException {
        String valor = mapper.writeValueAsString(value);
        template.opsForValue().set(key, valor);
    }

    public UserData findByKey(String key) throws JsonProcessingException {
        var value = template.opsForValue().get(key);
        return mapper.readValue(value, UserData.class);
    }


    @SneakyThrows
    public List<UserData> list(){
        var keys = template.keys("*");
        return keys.stream().map(k -> {
            try {
                return findByKey(k);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

}
