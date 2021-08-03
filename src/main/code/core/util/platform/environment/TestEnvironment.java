package core.util.platform.environment;

import core.util.platform.host.file.YamlLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class TestEnvironment {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestEnvironment.class);
    private static final SupportedEnvironment defaultEnvironment = SupportedEnvironment.SIT;
    private static SupportedEnvironment runningEnvironment = detectRunningEnvironment();
    private static final Map<String, Object> settings = loadEnvironmentSettings();


    private static Map<String, Object> loadEnvironmentSettings() {
        Map<String, Object> loadedSettings;
        loadedSettings = YamlLoader.loadConfig("openweather/environment-" + runningEnvironment.toString().toLowerCase() + ".yaml");
        LOGGER.info("The environment settings for " + runningEnvironment + " is loaded successfully.");
        return loadedSettings;
    }

    public static Object getValue(Object key) {
        return settings.get(key.toString());
    }

    public static SupportedEnvironment getRunningEnvironment() {
        if (runningEnvironment != null) return runningEnvironment;
        else {
            runningEnvironment = detectRunningEnvironment();
            return runningEnvironment;
        }
    }


    private static SupportedEnvironment detectRunningEnvironment() {
        String environment = System.getProperty("environment");
        LOGGER.info("The " + environment + " environment initiating. ");
        SupportedEnvironment detectedEnvironment;
        if (environment == null || "".equals(environment)) {
            detectedEnvironment = defaultEnvironment;
        } else {
            detectedEnvironment = SupportedEnvironment.getSupportedEnvironmentByName(environment);
        }
        LOGGER.info("The supported " + detectedEnvironment + " environment initiated successfully.");
        return detectedEnvironment;
    }

    public enum SupportedEnvironment {
        LOCAL, DEV, SIT, UAT;

        public static SupportedEnvironment getSupportedEnvironmentByName(String name) {
            for (SupportedEnvironment one : SupportedEnvironment.values()) {
                if (one.toString().equals(name)) {
                    return one;
                }
            }
            return defaultEnvironment;
        }
    }

    public static Object getValueUnsafe(Object key, SupportedEnvironment environment){
        Map<String, Object> loadedSettings;
        loadedSettings = YamlLoader.loadConfig("openweather/environment-" + environment.toString().toLowerCase() + ".yaml");
        LOGGER.info("The environment settings for exceptional " + environment + " is loaded successfully.");
        return loadedSettings.get(key.toString());
    }
}
