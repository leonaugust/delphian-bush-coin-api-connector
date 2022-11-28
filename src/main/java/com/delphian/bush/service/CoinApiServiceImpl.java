package com.delphian.bush.service;

import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.dto.ExchangeRateResponse;
import com.delphian.bush.util.json.ExchangeRateJsonServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.delphian.bush.schema.ExchangeRateSchema.ASSET_ID_QUOTE_FIELD;
import static com.delphian.bush.schema.ExchangeRateSchema.TIME_FIELD;

public class CoinApiServiceImpl implements CoinApiService {

    private static final Logger log = LoggerFactory.getLogger(CoinApiServiceImpl.class);

    public static final String TEST_PROFILE = "test";
    public static final int START_PAGE = 1;

    public static List<ExchangeRate> filterRates(Optional<Map<String, Object>> sourceOffset, ExchangeRateResponse exchangeResponse) {
        return exchangeResponse.getRates().stream()
                .filter(filterByOffset(sourceOffset))
                .sorted(Comparator.comparing(ExchangeRate::getAssetIdQuote))
                .collect(Collectors.toList());
    }

    public static Predicate<ExchangeRate> filterByOffset(Optional<Map<String, Object>> sourceOffset) {
        return exchangeRate -> {
            if (sourceOffset.isPresent() &&
                    sourceOffset.get().get(ASSET_ID_QUOTE_FIELD) != null &&
                    sourceOffset.get().get(TIME_FIELD) != null
            ) {
                String offsetAssetIdQuote = (String) sourceOffset.get().get(ASSET_ID_QUOTE_FIELD);
                String offsetTime = (String) sourceOffset.get().get(TIME_FIELD);

                log.info("Latest offset is not null, additional checking required");
                // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");
                //ZonedDateTime zonedDateTime = ZonedDateTime.parse("2015-05-05 10:15:30 Europe/Paris", formatter);
                if (ZonedDateTime.parse(exchangeRate.getTime()).toLocalDateTime() // all rates have the same time. If coin-api received new rates, the time parameter will be increased
                        .isAfter(ZonedDateTime.parse(offsetTime).toLocalDateTime())) {
                    log.info("time is later second case newsId: [{}] is bigger than latestOffset: [{}], added rate to result", exchangeRate.getAssetIdQuote(), offsetAssetIdQuote);
                    return true;
                }
            } else {
                log.info("Latest offset was null, added rate to result");
                return true;
            }
            return false;
        };
    }

    @Override
    public ExchangeRateResponse getExchangeRatesByProfile(String profile, String apiKey) {
        if (profile.equals(TEST_PROFILE)) {
            return getMockedExchangeRates();
        } else {
            return getExchangeRates(apiKey);
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


    private static ExchangeRateResponse getExchangeRates(String apiKey) {
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        log.info("Getting news from API");
        Map<String, Object> params = new HashMap<>();
        params.put("apiKey", apiKey);
        params.put("invert", "true");

        HttpResponse<JsonNode> response = Unirest.get("https://rest.coinapi.io/v1/exchangerate/USD")
                .header("accept", "application/json")
                .queryString(params)
                .asJson();

        try {
            ExchangeRateResponse exchangeRateResponse = new ObjectMapper().readValue(response.getBody().toString(), ExchangeRateResponse.class);
            return exchangeRateResponse;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
