package com.example.datadog.config;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class MetricsHelper {

    private final MeterRegistry registry;

    public void incrCounter(String metricName, String... tags) {
        Counter.Builder counter = Counter.builder(metricName);
        if (tags.length > 0) {
            counter.tags(Arrays.stream(tags).map(tag -> tag == null ? "" : tag).toArray(String[]::new));
        }
        counter.register(registry).increment();
    }

}
