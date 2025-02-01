package com.alikuslu.housefit.demo.config;

import com.fasterxml.jackson.core.StreamWriteConstraints;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> builder.postConfigurer(objectMapper -> {
            objectMapper.getFactory()
                    .setStreamWriteConstraints(StreamWriteConstraints.builder()
                            .maxNestingDepth(2000)
                            .build());
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        });
    }
}
