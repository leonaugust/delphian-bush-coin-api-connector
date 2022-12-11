package com.delphian.bush.service;

import com.delphian.bush.dto.ExchangeRate;
import com.delphian.bush.dto.ExchangeRateResponse;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CoinApiService {

   List<ExchangeRate> getFilteredRates(Optional<String> sourceOffset);

}
