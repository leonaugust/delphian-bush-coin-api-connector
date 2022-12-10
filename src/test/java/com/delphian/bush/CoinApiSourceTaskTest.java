package com.delphian.bush;

import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.util.converter.ExchangeRateConverter;
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
        ExchangeRate rateFromStruct = ExchangeRateConverter.INSTANCE.fromConnectData(struct);
        assertEquals(exchangeRate.getRate(), rateFromStruct.getRate());
        assertEquals(exchangeRate.getAssetIdQuote(), rateFromStruct.getAssetIdQuote());
        assertEquals(exchangeRate.getTime(), rateFromStruct.getTime());
    }
}
