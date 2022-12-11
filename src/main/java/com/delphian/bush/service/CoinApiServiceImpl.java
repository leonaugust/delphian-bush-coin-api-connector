package com.delphian.bush.service;

import com.delphian.bush.config.CoinApiSourceConnectorConfig;
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
import java.time.ZonedDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.delphian.bush.config.CoinApiSourceConnectorConfig.*;
import static com.delphian.bush.config.schema.ExchangeRateSchema.ASSET_ID_QUOTE_FIELD;
import static com.delphian.bush.config.schema.ExchangeRateSchema.TIME_FIELD;

public class CoinApiServiceImpl implements CoinApiService {

    public static final String TEST_PROFILE = "test";
    public static final String PROD_PROFILE = "prod";
    private static final Logger log = LoggerFactory.getLogger(CoinApiServiceImpl.class);

    private final CoinApiSourceConnectorConfig config;

    public CoinApiServiceImpl(CoinApiSourceConnectorConfig config) {
        this.config = config;
    }


    @Override
    public List<ExchangeRate> getFilteredRates(Optional<Map<String, Object>> sourceOffset) {
        List<ExchangeRate> filtered = getRates().stream()
                .filter(filterByOffset(sourceOffset))
                .sorted(Comparator.comparing(ExchangeRate::getAssetIdQuote))
                .collect(Collectors.toList());
        Boolean additionalDebugEnabled = config.getBoolean(DEBUG_ADDITIONAL_INFO);
        if (additionalDebugEnabled) {
            log.info("The amount of filtered rates which offset is greater than sourceOffset: {}", filtered.size());
        }
        return filtered;
    }

    private Predicate<ExchangeRate> filterByOffset(Optional<Map<String, Object>> sourceOffset) {
        Boolean additionalDebugEnabled = config.getBoolean(DEBUG_ADDITIONAL_INFO);
        if (additionalDebugEnabled && !sourceOffset.isPresent()) {
            log.info("Latest offset is not null, additional checking required");
        }

        return exchangeRate -> {
            if (sourceOffset.isPresent() &&
                    sourceOffset.get().get(ASSET_ID_QUOTE_FIELD) != null &&
                    sourceOffset.get().get(TIME_FIELD) != null
            ) {
                String offsetAssetIdQuote = (String) sourceOffset.get().get(ASSET_ID_QUOTE_FIELD);
                String offsetTime = (String) sourceOffset.get().get(TIME_FIELD);
                if (ZonedDateTime.parse(exchangeRate.getTime()).toLocalDateTime() // all rates have the same time. If coin-api received new rates, the time parameter will be increased
                        .isAfter(ZonedDateTime.parse(offsetTime).toLocalDateTime())) {
                    if (additionalDebugEnabled) {
                        log.info("time is later second case newsId: [{}] is bigger than latestOffset: [{}], added rate to result", exchangeRate.getAssetIdQuote(), offsetAssetIdQuote);
                    }
                    return true;
                }
            } else {
                if (additionalDebugEnabled) {
                    log.info("Latest offset was null, added rate to result");
                }
                return true;
            }
            return false;
        };
    }

    public List<ExchangeRate> getRates() {
        String profile = config.getString(PROFILE_ACTIVE_CONFIG);
        if (profile.equals(TEST_PROFILE)) {
            return getMockedExchangeRates().getRates();
        } else {
            return getExchangeRates().getRates();
        }
    }

    private ExchangeRateResponse getMockedExchangeRates() {
        log.info("Using test mocked rates");
        try {
            log.info("Response from mocked-rates file");
            return new ExchangeRateJsonServiceImpl(new ObjectMapper()).getFromJson();
        } catch (IOException e) {
            log.error("Something happened. {}", e.getMessage());
            throw new RuntimeException();
        }
    }


    private ExchangeRateResponse getExchangeRates() {
        try {
            TimeUnit.SECONDS.sleep(1L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String apiKey = config.getString(COIN_API_KEY_CONFIG);
        log.info("Getting rates from API");
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
