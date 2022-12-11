package com.delphian.bush;

import com.delphian.bush.config.CoinApiSourceConnectorConfig;
import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.service.CoinApiServiceImpl;
import com.delphian.bush.util.TimeUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.delphian.bush.config.CoinApiSourceConnectorConfig.COIN_API_KEY_CONFIG;
import static com.delphian.bush.config.CoinApiSourceConnectorConfig.PROFILE_ACTIVE_CONFIG;
import static com.delphian.bush.config.schema.ExchangeRateSchema.ASSET_ID_QUOTE_FIELD;
import static com.delphian.bush.config.schema.ExchangeRateSchema.TIME_FIELD;
import static com.delphian.bush.service.CoinApiServiceImpl.PROD_PROFILE;
import static com.delphian.bush.service.CoinApiServiceImpl.TEST_PROFILE;
import static com.delphian.bush.util.PropertiesUtil.getApiKey;
import static com.delphian.bush.util.PropertiesUtil.getPropertiesOverridden;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CoinApiServiceImplTest {

    public static final int MOCKED_RATES_SIZE = 5;

    @Test
    void getFilteredNewsIsSortedTest() {
        Map<String, String> properties = new HashMap<>();
        properties.put(PROFILE_ACTIVE_CONFIG, PROD_PROFILE);
        properties.put(COIN_API_KEY_CONFIG, getApiKey());
        CoinApiServiceImpl coinApiService = new CoinApiServiceImpl(getConfig(properties));
        List<ExchangeRate> rates = coinApiService.getFilteredRates(Optional.empty());
        List<ExchangeRate> expectedSorted = rates.stream().sorted(Comparator.comparing(ExchangeRate::getAssetIdQuote))
                .collect(Collectors.toList());
        assertEquals(expectedSorted, rates);
    }

    @Test
    void getFilteredRatesIsFilteredByOffsetTest() {
        Map<String, String> properties = new HashMap<>();
        properties.put(PROFILE_ACTIVE_CONFIG, PROD_PROFILE);
        properties.put(COIN_API_KEY_CONFIG, getApiKey());
        CoinApiServiceImpl coinApiService = new CoinApiServiceImpl(getConfig(properties));
        List<ExchangeRate> rates = coinApiService.getFilteredRates(Optional.empty());
        int pivot = rates.size() / 2;
        ExchangeRate pivotRate = rates.get(pivot - 1);
        List<ExchangeRate> filtered = coinApiService.getFilteredRates(Optional.of(pivotRate.getTime()));
        assertTrue(filtered.size() < rates.size());
    }

    @Test
    void getRatesWithTestProfileTest() {
        Map<String, String> properties = new HashMap<>();
        properties.put(PROFILE_ACTIVE_CONFIG, TEST_PROFILE);
        properties.put(COIN_API_KEY_CONFIG, null);
        CoinApiServiceImpl coinApiService = new CoinApiServiceImpl(getConfig(properties));
        List<ExchangeRate> rates = coinApiService.getRates();
        assertEquals(MOCKED_RATES_SIZE, rates.size());
    }

    @Test
    void getRatesWithProdProfileTest() {
        Map<String, String> properties = new HashMap<>();
        properties.put(PROFILE_ACTIVE_CONFIG, PROD_PROFILE);
        properties.put(COIN_API_KEY_CONFIG, getApiKey());
        CoinApiServiceImpl coinApiService = new CoinApiServiceImpl(getConfig(properties));
        List<ExchangeRate> rates = coinApiService.getRates();
        LocalDateTime ratesDate = TimeUtil.parse(rates.get(0).getTime());
        assertEquals(TimeUtil.nowFormatted().getDayOfMonth(), ratesDate.getDayOfMonth());
        assertTrue(rates.size() > MOCKED_RATES_SIZE);
    }

    private CoinApiSourceConnectorConfig getConfig(Map<String, String> overriddenProperties) {
        return new CoinApiSourceConnectorConfig(getPropertiesOverridden(overriddenProperties));
    }
}
