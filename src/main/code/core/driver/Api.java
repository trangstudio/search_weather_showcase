package core.driver;


import io.restassured.RestAssured;
import lombok.experimental.Accessors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Accessors(fluent = true)
public class Api extends Engine {
    protected final Logger LOGGER = LoggerFactory.getLogger(Api.class);

    public Api() {
        driver = RestAssured.given();
    }

    @Override
    public Object getDriver() {
        // Always supplied a new fresh driver for API calls
        resetDriver();
        drivers.put(actor, driver);
        return driver;
    }

    private void resetDriver() {
        // Reset Driver here. Do not use outside
        driver = RestAssured.given();
        LOGGER.debug("Driver reset for actor " + actor);
    }

    @Override
    public void createDrivers(String[] actors) {
        for (String key : actors) {
            driver = RestAssured.given();
            drivers.put(key, driver);
        }
        super.createDrivers(actors);
        LOGGER.info("CREATE API DRIVERS SUCCESS");
    }

    @Override
    public void destroyDrivers(String[] actors) {
        drivers.clear();
        LOGGER.info("STOP API DRIVER SUCCESS");
    }
}
