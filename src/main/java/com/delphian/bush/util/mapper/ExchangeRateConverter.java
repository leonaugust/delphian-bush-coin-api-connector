package com.delphian.bush.util.mapper;

import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.config.schema.ExchangeRateSchema;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;

public class ExchangeRateConverter implements ConnectDataMapper<ExchangeRate> {
    public static final ExchangeRateConverter INSTANCE = new ExchangeRateConverter();

    /**
     * @inheritDoc
     */
    @Override
    public ExchangeRate to(Struct s) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(s.getString(ExchangeRateSchema.RATE_FIELD));
        exchangeRate.setAssetIdQuote(s.getString(ExchangeRateSchema.ASSET_ID_QUOTE_FIELD));
        exchangeRate.setTime(s.getString(ExchangeRateSchema.TIME_FIELD));
        return exchangeRate;
    }

    /**
     * @inheritDoc
     */
    @Override
    public Struct to(ExchangeRate exchangeRate) {

        return new Struct(getSchema())
                .put(ExchangeRateSchema.RATE_FIELD, exchangeRate.getRate())
                .put(ExchangeRateSchema.TIME_FIELD, exchangeRate.getTime())
                .put(ExchangeRateSchema.ASSET_ID_QUOTE_FIELD, exchangeRate.getAssetIdQuote());
    }

    /**
     * @inheritDoc
     */
    @Override
    public Schema getSchema() {
        return ExchangeRateSchema.EXCHANGE_RATE_SCHEMA;
    }
}