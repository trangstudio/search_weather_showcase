package core.util.reporting.retry;

public enum RetryTestConfig {
    RETRY_LIMIT("retryLimit"),
    ;

    private final String key;

    RetryTestConfig(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return this.key;
    }
}
