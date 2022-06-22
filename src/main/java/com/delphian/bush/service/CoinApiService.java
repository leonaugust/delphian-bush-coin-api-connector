package com.delphian.bush.service;

import com.delphian.bush.dto.ExchangeRateResponse;

import java.util.Optional;

public interface CoinApiService {

   ExchangeRateResponse getExchangeRatesByProfile(String profile, String apiKey);

}
