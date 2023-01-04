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

import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.util.mapper.ExchangeRateConverter;
import org.apache.kafka.connect.data.Struct;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoinApiSourceTaskTest {
    private CoinApiSourceTask coinApiSourceTask = new CoinApiSourceTask();

    private static final Logger log = LoggerFactory.getLogger(CoinApiSourceTaskTest.class);

    @Test
    public void buildRecordValueTest() {
        ExchangeRate exchangeRate = ExchangeRate.builder()
                .rate("42249.215173113396889938027027")
                .assetIdQuote("BTC")
                .time("2022-03-09T17:15:35.8000000Z")
                .build();

        Struct struct = coinApiSourceTask.buildRecordValue(exchangeRate);
        assertDoesNotThrow(struct::validate);
        ExchangeRate rateFromStruct = ExchangeRateConverter.INSTANCE.to(struct);
        assertEquals(exchangeRate.getRate(), rateFromStruct.getRate());
        assertEquals(exchangeRate.getAssetIdQuote(), rateFromStruct.getAssetIdQuote());
        assertEquals(exchangeRate.getTime(), rateFromStruct.getTime());
    }
}
