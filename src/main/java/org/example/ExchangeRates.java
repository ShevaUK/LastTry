package org.example;

import java.util.Map;

public class ExchangeRates {
    private String result;
    private String documentation;
    private String terms_of_use;
    private long time_last_update_unix;
    private String time_last_update_utc;
    private long time_next_update_unix;
    private String time_next_update_utc;
    private String base_code;
    private Map<String, Double> conversion_rates;

    public String getResult() {
        return result;
    }

    public String getDocumentation() {
        return documentation;
    }

    public String getTermsOfUse() {
        return terms_of_use;
    }

    public long getTimeLastUpdateUnix() {
        return time_last_update_unix;
    }

    public String getTimeLastUpdateUtc() {
        return time_last_update_utc;
    }

    public long getTimeNextUpdateUnix() {
        return time_next_update_unix;
    }

    public String getTimeNextUpdateUtc() {
        return time_next_update_utc;
    }

    public String getBaseCode() {
        return base_code;
    }

    public Map<String, Double> getConversionRates() {
        return conversion_rates;
    }
}
