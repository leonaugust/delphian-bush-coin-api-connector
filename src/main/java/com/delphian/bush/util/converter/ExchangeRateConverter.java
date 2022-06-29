package com.delphian.bush.util.converter;

import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.schema.ExchangeRateSchema;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.Struct;

public class ExchangeRateConverter implements ConnectPOJOConverter<ExchangeRate> {
    public static final ExchangeRateConverter INSTANCE = new ExchangeRateConverter();

    @Override
    public Schema getSchema() {
        return ExchangeRateSchema.EXCHANGE_RATE_SCHEMA;
    }

    @Override
    public ExchangeRate fromConnectData(Struct s) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setRate(s.getString(ExchangeRateSchema.RATE_FIELD));
        exchangeRate.setAssetIdQuote(s.getString(ExchangeRateSchema.ASSET_ID_QUOTE_FIELD));
        exchangeRate.setTime(s.getString(ExchangeRateSchema.TIME_FIELD));
        return exchangeRate;
    }

    @Override
    public Struct toConnectData(ExchangeRate exchangeRate) {

        return new Struct(ExchangeRateSchema.EXCHANGE_RATE_SCHEMA)
                .put(ExchangeRateSchema.RATE_FIELD, exchangeRate.getRate())
                .put(ExchangeRateSchema.TIME_FIELD, exchangeRate.getTime())
                .put(ExchangeRateSchema.ASSET_ID_QUOTE_FIELD, exchangeRate.getAssetIdQuote());
    }
}