package core.util.scripting.interaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import openweather.ReadableUI;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

public class Guesser {
    private static Logger LOGGER = LoggerFactory.getLogger(Guesser.class.getName());

    /**
     * Problem: Given multiple screen may displayed, which screen is actually displaying
     * Solution: Checking *.isDisplayed() on each possible screen in parallel. Stop on first response.
     * Usage:
     * - Java:
     * currentScreen = guessDisplayingUI(new FirstScreen(), new SecondScreen, new ThirdScreen());
     * if (currentScreen instanceof FirstScreen) {...}
     * if (currentScreen instanceof SecondScreen) {...}
     * - Kotlin:
     * when (guessDisplayingUI(new FirstScreen(), new SecondScreen, new ThirdScreen())) {
     * is FirstScreen -> {}
     * is SecondScreen -> {}
     * is ThirdScreen -> {}
     * }
     *
     * @param screens all readableUI implementation
     * @return a class object of the first displayed screen.
     */
    public static ReadableUI guessDisplayingUI(ReadableUI... screens) {
        ExecutorService executor = Executors.newCachedThreadPool();
        Map<ReadableUI, Future> workResult = new HashMap<>();

        do {
            if (workResult.isEmpty()) {
                Arrays.stream(screens).forEach(screen -> {
                    workResult.put(screen, executor.submit(() -> screen.isDisplayedNow()));
                });
            }
            Iterator iterator = workResult.entrySet().stream().iterator();
            Map.Entry possible = null;
            while (iterator.hasNext()) {
                possible = (Map.Entry) iterator.next();
                Future checkResult = (Future) possible.getValue();
                if (!checkResult.isDone()) continue;
            }
            workResult.remove(possible.getKey());
            try {
                if (Boolean.TRUE.equals(((Future) (possible.getValue())).get())) {
                    executor.shutdownNow();
                    return (ReadableUI) possible.getKey();
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        } while (!workResult.isEmpty());
        return null;
    }

    public static ReadableUI guessDisplayingUI(int timeout, TimeUnit unit, ReadableUI... screens) {
        long endTime = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(timeout, unit);
        ReadableUI foundScreen;
        do {
            // Recapture screens
            for (int i = 0; i < screens.length; i++) {
                try {
                    screens[i] = screens[i].getClass().newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            foundScreen = guessDisplayingUI(screens);
            if (foundScreen != null) return foundScreen;
        } while (System.currentTimeMillis() < endTime);
        return null;
    }
}
