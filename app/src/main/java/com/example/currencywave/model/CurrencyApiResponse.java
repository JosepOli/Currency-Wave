package com.example.currencywave.model;

import com.google.gson.annotations.SerializedName;
import java.util.Map;

public class CurrencyApiResponse {
    @SerializedName("base_code")
    private String base;

    @SerializedName("conversion_rates")
    private Map<String, Double> conversionRates;

    // Otros atributos como "error" si es necesario

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Map<String, Double> getConversionRates() {
        return conversionRates;
    }

    public void setConversionRates(Map<String, Double> conversionRates) {
        this.conversionRates = conversionRates;
    }

    // Getters y setters para otros atributos si es necesario
}
