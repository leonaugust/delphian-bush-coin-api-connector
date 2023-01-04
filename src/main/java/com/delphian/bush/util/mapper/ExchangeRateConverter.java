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