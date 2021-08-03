package openweather;

import java.util.Map;

public interface ReadableUI {
    Map<String, String> getAllActualTexts();

    Map<String, String> getAllExpectedTexts();

    boolean isDisplayed();

    boolean isDisplayedNow();
}
