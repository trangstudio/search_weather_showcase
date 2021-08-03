package core.controller;

import core.driver.*;
import core.util.scripting.io.Clipboard;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import lombok.Getter;
import lombok.experimental.Accessors;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.util.Collection;
import java.util.stream.Stream;

@Accessors(fluent = true)
public final class TestController {
    protected static final Logger LOGGER = LoggerFactory.getLogger(TestController.class);
    private static long elapsed;
    private static final TestController INSTANCE = new TestController();

    // There is opportunity when multiple Scenarios plays with the same Actor's name in one execution,
    // Then we need to separate each Scenario into separate Threads

    private static final ThreadLocal<String[]> SCENARIO_BOUNDED_WEB_ACTORS = new ThreadLocal<>();
    private static final ThreadLocal<String[]> SCENARIO_BOUNDED_API_ACTORS = new ThreadLocal<>();

    @Getter
    private static Web WEB;
    @Getter
    private static Api API;

    public TestController() {
        WEB = new Web();
        API = new Api();
        LOGGER.info(this.getClass().getName() + " is initialized. " + this);
    }

    public static TestController getInstance() {
        return INSTANCE;
    }


    public void setDriverMap(Scenario scenario) {
        Collection<String> tags = scenario.getSourceTagNames();
        String[] webActors = {};
        String[] apiActors = {};

        for (String tag : tags) {

            if (tag.contains(Tag.Web.toString())) {
                webActors = new String[]{"NAB"};

            }
            if (tag.contains(Tag.Api.toString())) {
                apiActors = new String[]{"NAB"};

            }
        }
        SCENARIO_BOUNDED_WEB_ACTORS.set(webActors);
        SCENARIO_BOUNDED_API_ACTORS.set(apiActors);
    }

    public void createDriverMap(String[] webActors, String[] apiActors) {
        if (webActors.length > 0) {
            WEB.createDrivers(webActors);
        }
        if (apiActors.length > 0) {
            // Any actor is reward with an API driver by default
            String[] allActors = Stream.of(
                    webActors,
                    apiActors
            ).flatMap(Stream::of).toArray(String[]::new);
            API.createDrivers(allActors);
        }
    }

    public void clearDriverMap(String[] webActors, String[] apiActors) {

        if (webActors.length > 0) {
            WEB.destroyDrivers(webActors);
        }
        if (apiActors.length > 0) {
            API.destroyDrivers(apiActors);
        }
    }

    @Before()
    public void startTest() {
        LOGGER.info("NAB IS STARTING TEST");
        elapsed = Clock.systemDefaultZone().millis();
    }

    @Before()
    public void initDrivers(Scenario scenario) {
        setDriverMap(scenario);
        createDriverMap(

                SCENARIO_BOUNDED_WEB_ACTORS.get(),
                SCENARIO_BOUNDED_API_ACTORS.get()

        );
    }

    @Before
    @After(order = 1)
    public void cleanClipboard() {
        Clipboard.clean();
    }

    @After(order = 700)
    public void terminateDrivers() {
        clearDriverMap(
                SCENARIO_BOUNDED_WEB_ACTORS.get(),
                SCENARIO_BOUNDED_API_ACTORS.get()
        );
    }

    @After(order = 800)
    public void snapshotAtTheEnd(Scenario scenario) {
        if (scenario.isFailed()) {
            Collection<String> tags = scenario.getSourceTagNames();
            for (String tag : tags) {
                if (tag.contains(Tag.Web.toString())) {
                    scenario.attach(((TakesScreenshot) WEB.getDriver()).getScreenshotAs(OutputType.BYTES), "image/png", "screenshot");
                }
                scenario.log("Scenario failed.");
            }
        } else {
            scenario.log("Scenario Passed");
        }
    }

    public Object getActingDriver(SupportedDrivers supportedDrivers) {
        switch (supportedDrivers) {
            case API:
                return API.getDriver();
            case WEB:
                return WEB.getDriver();
            default:
                return null;
        }
    }

    public enum SupportedDrivers {
         WEB, API
    }
}
