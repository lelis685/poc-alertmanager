package com.example.prometheus.repository;

import java.util.Objects;

public record UserData(String name, Boolean isSingle, String country, String id) {

    public UserData {
        Objects.requireNonNull(name);
        Objects.requireNonNull(isSingle);
        Objects.requireNonNull(country);
        Objects.requireNonNull(id);
    }

}
