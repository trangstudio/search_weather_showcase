package openweather.api.external.common;

import lombok.Getter;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Accessors(fluent = true)
public enum Channel {
    SmartApp("Channel.SmartApp"),
    Ussd("Channel.USSD"),
    None("None");

    private final String headerValue;
    private final Map<String, Object> settings = new HashMap<>();
    @Getter
    private String deviceId;
    @Getter
    private String deviceInfo;

    Channel(String channel) {
        this.headerValue = channel;
    }

    public Channel set(Object key, Object value) {
        this.settings.put(key.toString(), value.toString());
        return this;
    }

}
