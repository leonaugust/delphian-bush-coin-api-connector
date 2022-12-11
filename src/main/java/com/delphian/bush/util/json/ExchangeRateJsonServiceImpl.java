package com.delphian.bush.util.json;

import com.delphian.bush.dto.ExchangeRateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class ExchangeRateJsonServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(ExchangeRateJsonServiceImpl.class);

    private final ObjectMapper objectMapper;
    private final InputStream inputStream;
    private final Class<ExchangeRateResponse> targetClass;

    public ExchangeRateJsonServiceImpl(ObjectMapper objectMapper) throws IOException {
        this.objectMapper = objectMapper;
        this.inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("mocked-stats.json");
        this.targetClass = ExchangeRateResponse.class;
    }


    public ExchangeRateResponse getFromJson() {
        try {
            return objectMapper.readValue(inputStream, targetClass);
        } catch (IOException e) {
            log.debug("Json test service encountered unexpected exception: {}", e.getMessage());
            return null;
        }
    }


}
