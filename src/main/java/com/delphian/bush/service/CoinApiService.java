package com.delphian.bush.service;

import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.dto.ExchangeRateResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CoinApiService {

   /**
    * Fetches unread rates from API, filters by offset and orders by offset.
    *
    * @param sourceOffset   - The offset connector stopped reading at.
    * @return The rates, ordered by offset, which time is after {@code sourceOffset}
    */
   List<ExchangeRate> getFilteredRates(Optional<String> sourceOffset);

}
