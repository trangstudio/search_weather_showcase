package core.util.scripting.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

public class Clipboard {
    private static HashMap<String, Object> savedData = new HashMap<>();
    private final static Clipboard INSTANCE = new Clipboard();
    private static Logger LOGGER = LoggerFactory.getLogger(Clipboard.class);

    public static Clipboard copyTo(String key, String value) {
        savedData.put(key, value);
        LOGGER.info(String.format("Capture %s ➡ %s ", value, key));
        return INSTANCE;
    }

    public static Clipboard copyTo(String key, Object value) {
        savedData.put(key, value);
        LOGGER.info(String.format("Capture %s ➡ %s ", value, key));
        return INSTANCE;
    }

    public static String paste(String key) {
        String value = (String) savedData.get(key);
        LOGGER.info(String.format("Retrieve %s ⬅ %s ", value, key));
        return value;
    }

    public static Object pasteObject(String key) {
        Object value = savedData.get(key);
        LOGGER.info(String.format("Retrieve %s ⬅ %s ", value, key));
        return value;
    }

    public static Clipboard clean() {
        savedData.clear();
        LOGGER.debug("Clear all");
        return INSTANCE;
    }

    public static Clipboard clean(String key) {
        savedData.remove(key);
        LOGGER.debug(key + " and it value is removed");
        return INSTANCE;
    }

    public static String getActivatingActorPersonalDeviceId() {
        return paste(Key.KEY_ACTIVATING_ACTOR_ACTIVATING_PERSONAL_DEVICE_ID).toString();
    }

    public static String getActivatingActorKioskDeviceId() {
        return paste(Key.KEY_ACTIVATING_ACTOR_ACTIVATING_KIOSK_DEVICE_ID).toString();
    }

    public static Clipboard setUsingPersonalDevice(String deviceId) {
        copyTo(Key.KEY_ACTIVATING_ACTOR_ACTIVATING_PERSONAL_DEVICE_ID, deviceId);
        return INSTANCE;
    }

    public static Clipboard setUsingKiosk(String deviceId) {
        copyTo(Key.KEY_ACTIVATING_ACTOR_ACTIVATING_KIOSK_DEVICE_ID, deviceId);
        return INSTANCE;
    }

    public static final class Key {

        public static final String ACTIVATING_ACTOR = "core.util.scripting.io.Clipboard.ACTIVATING_ACTOR";
        public static final String ACTIVATING_KIOSK_DEVICE_ID = "ACTIVATING_KIOSK_DEVICE_ID";
        public static final String ACTIVATING_PERSONAL_DEVICE_ID = "ACTIVATING_PERSONAL_DEVICE_ID";
        public static String KEY_ACTIVATING_ACTOR_ACTIVATING_PERSONAL_DEVICE_ID = String.format("%s::%s",
                Clipboard.paste(Key.ACTIVATING_ACTOR), Key.ACTIVATING_PERSONAL_DEVICE_ID
        );
        public static String KEY_ACTIVATING_ACTOR_ACTIVATING_KIOSK_DEVICE_ID = String.format("%s::%s",
                Clipboard.paste(Key.ACTIVATING_ACTOR), Key.ACTIVATING_KIOSK_DEVICE_ID);
    }
}