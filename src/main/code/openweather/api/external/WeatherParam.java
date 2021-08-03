package openweather.api.external;

import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

public enum WeatherParam {
    q("CityName"),
    id("CityId"),
    lat("Latitude"),
    lon("Longitude"),
    bbox("Rectangle"),
    zip("Zipcode"),
    cnt("NumberOfCity");

    public final String label;

    private WeatherParam(String label) {
        this.label = label;
    }

    public static String valueOfLabel(String label) {
        for (WeatherParam e : values()) {
            if (e.label.equals(label)) {
                return e.toString();
            }
        }
        return null;
    }

}
