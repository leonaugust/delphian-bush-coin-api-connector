package com.delphian.bush;

import com.delphian.bush.config.CoinApiSourceConnectorConfig;
import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.dto.ExchangeRateResponse;
import com.delphian.bush.schema.ExchangeRateSchema;
import com.delphian.bush.service.CoinApiService;
import com.delphian.bush.service.CoinApiServiceImpl;
import com.delphian.bush.util.VersionUtil;
import com.delphian.bush.util.converter.ExchangeRateConverter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.delphian.bush.config.CoinApiSourceConnectorConfig.*;
import static com.delphian.bush.schema.ExchangeRateSchema.ASSET_ID_QUOTE_FIELD;
import static com.delphian.bush.schema.ExchangeRateSchema.TIME_FIELD;
import static java.time.LocalDateTime.now;

@Slf4j
public class CoinApiSourceTask extends SourceTask {

    private LocalDateTime latestPoll = null;

    private CoinApiSourceConnectorConfig config;

    private final CoinApiService coinApiService = new CoinApiServiceImpl();

    @Override
    public String version() {
        return VersionUtil.getVersion();
    }

    @Override
    public void start(Map<String, String> props) {
        config = new CoinApiSourceConnectorConfig(props);
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
        List<SourceRecord> records = new ArrayList<>();
        Optional<Map<String, Object>> sourceOffset = getLatestSourceOffset();
        String profile = config.getString(PROFILE_ACTIVE_CONFIG);
        String coinApiKey = config.getString(CRYPTO_PANIC_KEY_CONFIG);
        ExchangeRateResponse exchangeResponse = coinApiService.getExchangeRatesByProfile(profile, coinApiKey);

        if (exchangeResponse != null && exchangeResponse.getRates() != null) {
            List<ExchangeRate> filteredRates = exchangeResponse.getRates().stream()
                    .filter(filterByOffset(sourceOffset))
                    .sorted(Comparator.comparing(ExchangeRate::getAssetIdQuote))
                    .collect(Collectors.toList());

            log.info("The amount of filtered rates which offset is greater than sourceOffset: {}", filteredRates.size());
            if (!CollectionUtils.isEmpty(filteredRates)) {
                for (ExchangeRate rate : filteredRates) {
                    records.add(generateRecordFromNews(rate));
                }
            }
        }

        return records;
    }

    private Predicate<ExchangeRate> filterByOffset(Optional<Map<String, Object>> sourceOffset) {
        return exchangeRate -> {
            if (sourceOffset.isPresent() &&
                    sourceOffset.get().get(ASSET_ID_QUOTE_FIELD) != null &&
                    sourceOffset.get().get(TIME_FIELD) != null
            ) {
                String offsetAssetIdQuote = (String) sourceOffset.get().get(ASSET_ID_QUOTE_FIELD);
                String offsetTime = (String) sourceOffset.get().get(TIME_FIELD);

                log.info("Latest offset is not null, additional checking required");
                if (exchangeRate.getAssetIdQuote().compareTo(offsetAssetIdQuote) > 0) {
                    log.info("newsId: [{}] is bigger than latestOffset: [{}], added rate to result", exchangeRate.getAssetIdQuote(), offsetAssetIdQuote);
                    return true;
                } else if ((exchangeRate.getAssetIdQuote().compareTo(offsetAssetIdQuote) == 0) && !exchangeRate.getTime().equals(offsetTime)) {
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

    private Optional<Map<String, Object>> getLatestSourceOffset() {
        if (context.offsetStorageReader() != null) {
            sourcePartition();
            if (context.offsetStorageReader().offset(sourcePartition()) != null) {
                Map<String, Object> offset = context.offsetStorageReader().offset(sourcePartition());
                return Optional.of(offset);
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

    // Track which source we have been reading.
    private Map<String, String> sourcePartition() {
        Map<String, String> partitionProperties = new HashMap<>();
        partitionProperties.put(APPLICATION_CONFIG, config.getString(APPLICATION_CONFIG));
        return partitionProperties;
    }

    //  Track the exact place we have been reading
    // do something with pagination and size
    private Map<String, String> sourceOffset(ExchangeRate rate) {
        Map<String, String> map = new HashMap<>();
        map.put(ASSET_ID_QUOTE_FIELD, rate.getAssetIdQuote());
        map.put(TIME_FIELD, rate.getTime());
        return map;
    }

    private Struct buildRecordKey(ExchangeRate exchangeRate) {
        // Key Schema
        return new Struct(ExchangeRateSchema.EXCHANGE_RATE_KEY_SCHEMA)
                .put(APPLICATION_CONFIG, config.getString(APPLICATION_CONFIG))
                .put(ASSET_ID_QUOTE_FIELD, exchangeRate.getAssetIdQuote())
                .put(TIME_FIELD, exchangeRate.getTime());
    }

    public Struct buildRecordValue(ExchangeRate exchangeRate) {
        Struct struct = ExchangeRateConverter.INSTANCE.toConnectData(exchangeRate);
//        log.debug("Resulting struct: {}", struct);
        return struct;
    }

    @Override
    public void stop() {

    }
}
