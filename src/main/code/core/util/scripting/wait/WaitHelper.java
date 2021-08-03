package core.util.scripting.wait;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public final class WaitHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(WaitHelper.class);

    private WaitHelper() {
    }

    public static void wait(int milliSeconds) {
        try {
            LOGGER.info("Waiting: " + milliSeconds + " milliseconds");
            TimeUnit.MILLISECONDS.sleep(milliSeconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public interface Duration {
        int ONE_SECOND = 1000;
        int THREE_SECONDS = 3000;
        int FIVE_SECONDS = 5000;
        int TEN_SECONDS = 10000;
        int HALF_MINUTE = 30000;
        int ONE_MINUTE = 60000;
        int TWO_MINUTE = 120000;
    }
}
