package com.delphian.bush.config.schema;

import com.delphian.bush.dto.ExchangeRate;
import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.data.SchemaBuilder;

import static com.delphian.bush.config.CoinApiSourceConnectorConfig.APPLICATION_CONFIG;
import static com.delphian.bush.util.VersionUtil.FIRST_VERSION;

public class ExchangeRateSchema {

    public static final String SCHEMA_NAME = ExchangeRate.class.getName();

    public static final String TIME_FIELD = "time";

    public static final String ASSET_ID_QUOTE_FIELD = "asset_id_quote";

    public static final String DATE_FIELD = "date";

    public static final String RATE_FIELD = "rate";

    public static final Schema EXCHANGE_RATE_SCHEMA = SchemaBuilder.struct()
            .name(SCHEMA_NAME)
            .field(TIME_FIELD, Schema.OPTIONAL_STRING_SCHEMA)
            .field(ASSET_ID_QUOTE_FIELD, Schema.OPTIONAL_STRING_SCHEMA)
            .field(RATE_FIELD, Schema.OPTIONAL_STRING_SCHEMA)
            .version(FIRST_VERSION)
            .optional()
            .build();

    public static final Schema EXCHANGE_RATE_KEY_SCHEMA = SchemaBuilder.struct()
            .version(FIRST_VERSION)
            .field(APPLICATION_CONFIG, Schema.STRING_SCHEMA)
            .field(TIME_FIELD, Schema.STRING_SCHEMA)
            .field(ASSET_ID_QUOTE_FIELD, Schema.STRING_SCHEMA)
            .field(DATE_FIELD, Schema.STRING_SCHEMA);

}
