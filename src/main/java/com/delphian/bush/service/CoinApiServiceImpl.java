/*
 * MIT License
 *
 * Copyright (c) 2023 Leon Galushko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.delphian.bush.service;

import com.delphian.bush.config.CoinApiSourceConnectorConfig;
import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.dto.ExchangeRateResponse;
import com.delphian.bush.util.TimeUtil;
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
import java.util.stream.Collectors;

import static com.delphian.bush.config.CoinApiSourceConnectorConfig.*;

public class CoinApiServiceImpl implements CoinApiService {

  public static final String TEST_PROFILE = "test";
  public static final String PROD_PROFILE = "prod";
  private static final Logger log = LoggerFactory.getLogger(CoinApiServiceImpl.class);

  private final CoinApiSourceConnectorConfig config;

  public CoinApiServiceImpl(CoinApiSourceConnectorConfig config) {
    this.config = config;
  }

  /**
   * @inheritDoc
   */
  @Override
  public List<ExchangeRate> getFilteredRates(Optional<String> sourceOffset) {
    Boolean additionalDebugEnabled = config.getBoolean(DEBUG_ADDITIONAL_INFO);
    if (additionalDebugEnabled && !sourceOffset.isPresent()) {
      log.info("Latest offset is not null, additional checking required");
    }

    List<ExchangeRate> filtered = getRates().stream()
        .filter(exchangeRate -> {
          if (sourceOffset.isPresent()) {
            String offsetTime = sourceOffset.get();
            if (ZonedDateTime.parse(exchangeRate.getTime()).toLocalDateTime()
                .isAfter(ZonedDateTime.parse(offsetTime).toLocalDateTime())) {
              if (additionalDebugEnabled) {
                log.info(
                    "ExchangeRate time[{}] is later than recorded in sourceOffset[{}], added rate to result",
                    exchangeRate.getTime(), offsetTime);
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
        })
        .sorted(Comparator.comparing(e -> TimeUtil.parse(e.getTime())))
        .collect(Collectors.toList());
    log.info("The amount of filtered rates which offset is greater than sourceOffset: {}",
        filtered.size());
    return filtered;
  }

  public List<ExchangeRate> getRates() {
    String profile = config.getString(PROFILE_ACTIVE_CONFIG);
    if (profile.equals(TEST_PROFILE)) {
      return getMockedExchangeRates();
    } else {
      return getRatesFromApi(1).getRates();
    }
  }

  private List<ExchangeRate> getMockedExchangeRates() {
    try {
      log.info("Reading rates from mocked-rates file");
      return new ExchangeRateJsonServiceImpl(new ObjectMapper()).getFromJson().getRates();
    } catch (IOException e) {
      log.error("Encountered unexpected exception: {}", e.getMessage());
      throw new RuntimeException();
    }
  }


  private ExchangeRateResponse getRatesFromApi(int timeoutSeconds) {
    if (timeoutSeconds > 0) {
      try {
        TimeUnit.SECONDS.sleep(timeoutSeconds);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
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
      return new ObjectMapper().readValue(response.getBody().toString(),
          ExchangeRateResponse.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e.getMessage());
    }

  }
}
