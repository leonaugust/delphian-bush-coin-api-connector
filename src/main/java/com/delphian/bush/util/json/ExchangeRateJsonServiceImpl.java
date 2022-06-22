package com.delphian.bush.util.json;

import com.delphian.bush.dto.ExchangeRateResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

public class ExchangeRateJsonServiceImpl extends JsonToPojoService<ExchangeRateResponse> {

    public ExchangeRateJsonServiceImpl(ObjectMapper objectMapper) throws IOException {
        super(new ClassPathResource("mocked-stats.json").getInputStream(), objectMapper, ExchangeRateResponse.class);
    }

}
