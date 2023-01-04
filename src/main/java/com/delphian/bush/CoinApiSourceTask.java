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

package com.delphian.bush;

import com.delphian.bush.config.CoinApiSourceConnectorConfig;
import com.delphian.bush.config.schema.ExchangeRateSchema;
import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.service.CoinApiService;
import com.delphian.bush.service.CoinApiServiceImpl;
import com.delphian.bush.util.TimeUtil;
import com.delphian.bush.util.VersionUtil;
import com.delphian.bush.util.mapper.ExchangeRateConverter;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.delphian.bush.config.CoinApiSourceConnectorConfig.*;
import static com.delphian.bush.config.schema.ExchangeRateSchema.*;
import static java.time.LocalDateTime.now;

public class CoinApiSourceTask extends SourceTask {

    private static final Logger log = LoggerFactory.getLogger(CoinApiSourceTask.class);

    private LocalDateTime latestPoll = null;

    private CoinApiSourceConnectorConfig config;

    private CoinApiService coinApiService;

    private Long timeoutSeconds;

    /**
     * @inheritDoc
     */
    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    /**
     * Sets the initial properties of the connector at the start.
     */
    @Override
    public void start(Map<String, String> props) {
        config = new CoinApiSourceConnectorConfig(props);
        coinApiService = new CoinApiServiceImpl(config);
        timeoutSeconds = config.getLong(POLL_TIMEOUT_CONFIG);
    }

    /**
     * @return Rates records.
     * @throws InterruptedException on sleep().
     */
    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        if (latestPoll != null && !now().isAfter(latestPoll.plusSeconds(timeoutSeconds))) {
            log.info("Poll timeout: [{}] seconds", timeoutSeconds);
            TimeUnit.SECONDS.sleep(timeoutSeconds);
        }
        latestPoll = LocalDateTime.now();
        List<ExchangeRate> filteredRates = coinApiService.getFilteredRates(getLatestSourceOffset());

        List<SourceRecord> records = new ArrayList<>();
        if (filteredRates != null && !filteredRates.isEmpty()) {
            for (ExchangeRate rate : filteredRates) {
                records.add(generateRecordFromNews(rate));
            }
        }

        return records;
    }

    private Optional<String> getLatestSourceOffset() {
        if (context.offsetStorageReader() != null) {
            Map<String, Object> offset = context.offsetStorageReader().offset(sourcePartition());
            if (offset != null) {
                log.info("Offset is not null");
                Object timeField = offset.get(TIME_FIELD);
                if (timeField != null) {
                    String latestTime = (String) timeField;
                    log.debug("latestOffset: {}", latestTime);
                    return Optional.of(latestTime);
                }
            }
        }

        return Optional.empty();
    }

    private SourceRecord generateRecordFromNews(ExchangeRate rate) {
        return new SourceRecord(
                sourcePartition(),
                sourceOffset(rate),
                config.getString(TOPIC_CONFIG),
                null,
                ExchangeRateSchema.EXCHANGE_RATE_KEY_SCHEMA,
                buildRecordKey(rate),
                ExchangeRateSchema.EXCHANGE_RATE_SCHEMA,
                buildRecordValue(rate),
                Instant.now().toEpochMilli()
        );
    }

    private Map<String, String> sourcePartition() {
        Map<String, String> partitionProperties = new HashMap<>();
        partitionProperties.put(APPLICATION_CONFIG, config.getString(APPLICATION_CONFIG));
        return partitionProperties;
    }

    private Map<String, String> sourceOffset(ExchangeRate rate) {
        Map<String, String> map = new HashMap<>();
        map.put(TIME_FIELD, rate.getTime());
        return map;
    }

    private Struct buildRecordKey(ExchangeRate exchangeRate) {
        return new Struct(ExchangeRateSchema.EXCHANGE_RATE_KEY_SCHEMA)
                .put(APPLICATION_CONFIG, config.getString(APPLICATION_CONFIG))
                .put(ASSET_ID_QUOTE_FIELD, exchangeRate.getAssetIdQuote())
                .put(TIME_FIELD, exchangeRate.getTime())
                .put(DATE_FIELD, TimeUtil.nowFormatted().toString());
    }

    /**
     * @param exchangeRate rate
     * @return Struct value from rate.
     */
    public Struct buildRecordValue(ExchangeRate exchangeRate) {
        return ExchangeRateConverter.INSTANCE.to(exchangeRate);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void stop() {

    }
}
