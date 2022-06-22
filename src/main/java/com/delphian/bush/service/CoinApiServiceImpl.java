package com.delphian.bush.service;

import com.delphian.bush.dto.ExchangeRateResponse;
import com.delphian.bush.util.json.ExchangeRateJsonServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static com.delphian.bush.util.WebUtil.getRestTemplate;

@Slf4j
public class CoinApiServiceImpl implements CoinApiService {

    public static final String TEST_PROFILE = "test";
    public static final int START_PAGE = 1;

    @Override
    public ExchangeRateResponse getExchangeRatesByProfile(String profile, String apiKey) {
        if (profile.equals(TEST_PROFILE)) {
            return getMockedExchangeRates();
        } else {
            return getExchangeRates(apiKey, String.valueOf(START_PAGE));
        }
    }

    private static ExchangeRateResponse getMockedExchangeRates() {
        log.info("Using test mocked rates");
        try {
            log.info("Response from mocked-rates file");
            return new ExchangeRateJsonServiceImpl(new ObjectMapper()).getFromJson();
        } catch (IOException e) {
            log.error("Something happened. {}", e.getMessage());
            throw new RuntimeException();
        }
    }


    private ExchangeRateResponse getExchangeRates(String cryptoPanicKey, String page) {
        try {
            TimeUnit.SECONDS.sleep(5L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Getting news from API");
        String apiUrl = "https://cryptopanic.com/api/v1/posts/" +
                "?auth_token=" + cryptoPanicKey +
                "&public=true" +
                "&page=" + page;
        ResponseEntity<ExchangeRateResponse> responseEntity = getRestTemplate().getForEntity(apiUrl, ExchangeRateResponse.class);
        return responseEntity.getBody();

    }
}
