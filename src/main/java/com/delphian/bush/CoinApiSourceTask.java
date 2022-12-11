package com.delphian.bush;

import com.delphian.bush.config.CoinApiSourceConnectorConfig;
import com.delphian.bush.config.schema.ExchangeRateSchema;
import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.service.CoinApiService;
import com.delphian.bush.service.CoinApiServiceImpl;
import com.delphian.bush.util.TimeUtil;
import com.delphian.bush.util.VersionUtil;
import com.delphian.bush.util.converter.ExchangeRateConverter;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

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

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        config = new CoinApiSourceConnectorConfig(props);
        coinApiService = new CoinApiServiceImpl(config);
    }

    @Override
    public List<SourceRecord> poll() throws InterruptedException {
        Long seconds = config.getLong(POLL_TIMEOUT_CONFIG);
        if (latestPoll == null || now().isAfter(latestPoll.plusSeconds(seconds))) {
            latestPoll = LocalDateTime.now();
        } else {
            log.info("Poll timeout: [{}] seconds", seconds);
            TimeUnit.SECONDS.sleep(seconds);
        }
        List<ExchangeRate> filteredRates = coinApiService.getFilteredRates(getLatestSourceOffset());

        List<SourceRecord> records = new ArrayList<>();
        if (!CollectionUtils.isEmpty(filteredRates)) {
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

    public Struct buildRecordValue(ExchangeRate exchangeRate) {
        return ExchangeRateConverter.INSTANCE.toConnectData(exchangeRate);
    }

    @Override
    public void stop() {

    }
}
