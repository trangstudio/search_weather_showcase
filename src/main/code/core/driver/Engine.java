package core.driver;

import java.util.HashMap;
import java.util.Map;

public abstract class Engine {
    protected Map<String, Object> drivers;
    protected String actor;
    protected Object driver;

    Engine() {
        drivers = new HashMap<>();
        driver = new Object();
    }

    public void createDrivers(String[] actors) {
//        Concrete Engine create all driver. Then abstract engine make first driver created as default one
        if (drivers.isEmpty()) return;
        driver = drivers.values().iterator().next();
    }

    abstract public void destroyDrivers(String[] actors);

    public void switchToActor(String actor) {
        driver = drivers.get(actor);
        this.actor = actor;
    }

    public Object getDriver() {
        return driver;
    }

    protected Map<String, Object> getAllDrivers() {
        return drivers;
    }

}
