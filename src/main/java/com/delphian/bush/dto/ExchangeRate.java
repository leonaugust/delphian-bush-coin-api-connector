package com.delphian.bush.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class ExchangeRate {

    private String time;

    @JsonProperty("asset_id_quote")
    private String assetIdQuote;

    private String rate;


    public ExchangeRate(String time, String assetIdQuote, String rate) {
        this.time = time;
        this.assetIdQuote = assetIdQuote;
        this.rate = rate;
    }

    public ExchangeRate() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAssetIdQuote() {
        return assetIdQuote;
    }

    public void setAssetIdQuote(String assetIdQuote) {
        this.assetIdQuote = assetIdQuote;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
