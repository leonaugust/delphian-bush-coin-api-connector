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
