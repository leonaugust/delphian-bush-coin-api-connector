package com.delphian.bush.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponse {

    @JsonProperty("asset_id_base")
    private String assetIdBase;

    private List<ExchangeRate> rates;

    public ExchangeRateResponse(String assetIdBase, List<ExchangeRate> rates) {
        this.assetIdBase = assetIdBase;
        this.rates = rates;
    }

    public ExchangeRateResponse() {
    }

    public String getAssetIdBase() {
        return assetIdBase;
    }

    public void setAssetIdBase(String assetIdBase) {
        this.assetIdBase = assetIdBase;
    }

    public List<ExchangeRate> getRates() {
        return rates;
    }

    public void setRates(List<ExchangeRate> rates) {
        this.rates = rates;
    }
}
