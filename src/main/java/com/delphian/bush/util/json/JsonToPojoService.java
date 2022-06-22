package com.delphian.bush.util.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;

@Slf4j
public abstract class JsonToPojoService<T> {

    private final ObjectMapper objectMapper;
    private final InputStream inputStream;
    private final Class<T> targetClass;

    protected JsonToPojoService(InputStream inputStream, ObjectMapper objectMapper, Class<T> targetClass) {
        this.objectMapper = objectMapper;
        this.inputStream = inputStream;
        this.targetClass = targetClass;
    }

    public T getFromJson() {
        try {
            return objectMapper.readValue(inputStream, targetClass);
        } catch (IOException e) {
            log.debug("Json test service encountered unexpected exception: {}", e.getMessage());
            return null;
        }
    }

}
