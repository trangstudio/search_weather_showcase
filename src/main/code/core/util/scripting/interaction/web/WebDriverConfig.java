package core.util.scripting.interaction.web;

public enum WebDriverConfig {
    BROWSER_TYPE("browserType"),
    DRIVER_VERSION("driverVersion"),
    WEB_MOCK("webMock"),
    HIGHLIGHT("highlight"),

    WEB_CONFIG_FILE_PATH("openweather/web/web-business.yaml"),
    LONG_TIMEOUT("longTimeout"),
    SHORT_TIMEOUT("shortTimeout"),
    WEB_MOCK_FILE_PATH("webMockFilePath");

    private final String key;

    WebDriverConfig(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }

    public static boolean mockConfig() {
        if (System.getProperty(WebDriverConfig.WEB_MOCK.toString()) != null)
            return Boolean.parseBoolean(System.getProperty(WebDriverConfig.WEB_MOCK.toString()));
        else return false;
    }
}
