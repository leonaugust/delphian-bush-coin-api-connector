//package com.delphian.bush;
//
//import com.delphian.bush.util.converter.ExchangeRateConverter;
//import org.apache.kafka.connect.data.Struct;
//import org.junit.jupiter.api.Test;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class CoinApiSourceTaskTest {
//    private CoinApiSourceTask coinApiSourceTask = new CoinApiSourceTask();
//
//    @Test
//    public void buildRecordValueTest() {
//        NewsSource newsSource = NewsSource.builder()
//                .domain("bitcoinist.com")
//                .path(null)
//                .region("en")
//                .title("Bitcoinist")
//                .build();
//
//        Currency currency = Currency.builder()
//                .code("SHIB")
//                .title("Shiba Inu")
//                .slug("shiba-inu")
//                .url("https://cryptopanic.com/news/shiba-inu/")
//                .build();
//
//        CryptoNews cryptoNews = CryptoNews.builder()
//                .source(newsSource)
//                .currencies(Collections.singletonList(currency))
//                .createdAt("2022-06-13T08:04:01Z")
//                .domain("bitcoinist.com")
//                .id("15482265")
//                .kind("news")
//                .url("https://cryptopanic.com/news/15482265/Top-crypto-projects-that-are-actually-trying-to-do-better-for-the-world")
//                .publishedAt("2022-06-13T08:04:01Z")
//                .slug("Top-crypto-projects-that-are-actually-trying-to-do-better-for-the-world")
//                .title("Top crypto projects that are actually trying to do better for the world")
//                .build();
//
//        Struct struct = coinApiSourceTask.buildRecordValue(cryptoNews);
//        assertDoesNotThrow(struct::validate);
//
//        Struct newsFromStruct = (Struct) struct.get(NewsSourceSchema.SCHEMA_NAME);
//        assertEqualsNewsSource(newsSource, NewsSourceConverter.INSTANCE.fromConnectData(newsFromStruct));
//
//        List<Struct> currenciesFromStruct = (List<Struct>) struct.get(CurrencySchema.SCHEMA_NAME);
//        assertEqualsCurrencies(Collections.singletonList(currency),
//                currenciesFromStruct.stream().map(c -> CurrencyConverter.INSTANCE.fromConnectData(c))
//                        .collect(Collectors.toList())
//        );
//
//        assertEqualsCryptoNews(cryptoNews, struct);
//    }
//
//    private void assertEqualsCryptoNews(CryptoNews expected, Struct struct) {
//        CryptoNews actual = ExchangeRateConverter.INSTANCE.fromConnectData(struct);
//        assertEquals(expected.getKind(), actual.getKind());
//        assertEquals(expected.getDomain(), actual.getDomain());
//        assertEquals(expected.getTitle(), actual.getTitle());
//        assertEquals(expected.getPublishedAt(), actual.getPublishedAt());
//        assertEquals(expected.getSlug(), actual.getSlug());
//        assertEquals(expected.getId(), actual.getId());
//        assertEquals(expected.getUrl(), actual.getUrl());
//        assertEquals(expected.getCreatedAt(), actual.getCreatedAt());
//    }
//
//    private void assertEqualsNewsSource(NewsSource expected, NewsSource actual) {
//        assertEquals(expected.getTitle(), actual.getTitle());
//        assertEquals(expected.getPath(), actual.getPath());
//        assertEquals(expected.getDomain(), actual.getDomain());
//        assertEquals(expected.getRegion(), actual.getRegion());
//    }
//
//    /**
//     *
//     * Checks only first element. Not null-safe
//     */
//    private void assertEqualsCurrencies(List<Currency> expected, List<Currency> actual) {
//        Currency expectedCurrency = expected.get(0);
//        Currency actualCurrency = actual.get(0);
//
//        assertEquals(expectedCurrency.getUrl(), actualCurrency.getUrl());
//        assertEquals(expectedCurrency.getCode(), actualCurrency.getCode());
//        assertEquals(expectedCurrency.getSlug(), actualCurrency.getSlug());
//        assertEquals(expectedCurrency.getTitle(), actualCurrency.getTitle());
//    }
//
//}
