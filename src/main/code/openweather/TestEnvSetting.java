package openweather;

public enum TestEnvSetting {
    OPEN_WEATHER_DOMAIN_API("openWeatherDomainAPI"),
    OPEN_WEATHER_DOMAIN_APP_ID("openWeatherDomainAppId"),
    SINGLE_LOCATION_PARAM("singleLocationParam"),
    MULTI_LOCATION_PARAM("multiLocationParam"),
    MULTI_LOCATION_CIRCLE_PARAM("multiLocationCircleParam");


    private final String key;

    TestEnvSetting(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
