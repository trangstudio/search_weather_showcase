package core.driver;

import core.util.platform.host.file.YamlLoader;
import core.util.platform.host.os.OsHelper;
import core.util.scripting.interaction.web.WebDriverConfig;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.Getter;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Web extends Engine {
    protected static final Logger LOGGER = LoggerFactory.getLogger(Web.class);
    @Getter
    private static final Map webConfig = YamlLoader.loadConfig(WebDriverConfig.WEB_CONFIG_FILE_PATH.toString());
    @Getter
    private static final long longTimeout = Long.parseLong((String) webConfig.get(WebDriverConfig.LONG_TIMEOUT.toString()));


    @Override
    public void createDrivers(String[] actors) {
        String browserType, driverVersion;
        if (System.getProperty(WebDriverConfig.BROWSER_TYPE.toString()) != null) {
            browserType = System.getProperty(WebDriverConfig.BROWSER_TYPE.toString());
        } else {
            browserType = "chrome";
        }
        if (System.getProperty(WebDriverConfig.DRIVER_VERSION.toString()) != null) {
            driverVersion = System.getProperty(WebDriverConfig.DRIVER_VERSION.toString());
        } else {
            driverVersion = null;
        }
        for (String key : actors) {
            drivers.put(key, startDriver(browserType, driverVersion));
        }
        super.createDrivers(actors);
        LOGGER.info("---------- CREATE WEB DRIVERS SUCCESS ----------");
    }

    @Override
    public void destroyDrivers(String[] actors) {
        for (String key : actors) {
            closeBrowser(drivers.get(key));
        }
        closeDriverProcess();
        drivers.clear();
    }

    public WebDriver startDriver(String browserType, String driverVersion) {
        switch (browserType) {
            case "chrome":
                WebDriverManager.chromedriver().version(driverVersion).setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                Map<String, Object> prefs = new HashMap<>();
                prefs.put("download.prompt_for_download", false);
                prefs.put("download.default_directory", System.getProperty("user.dir") + "/Download");
                chromeOptions.addExtensions(new File((String) webConfig.get(WebDriverConfig.WEB_MOCK_FILE_PATH.toString())));
                chromeOptions.addArguments("--disable-web-security");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "headless_chrome":
                WebDriverManager.chromedriver().version(driverVersion).setup();
                chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("headless");
                chromeOptions.addArguments("window-size=1920x1080");
                driver = new ChromeDriver(chromeOptions);
                break;
            case "firefox":
                WebDriverManager.firefoxdriver().version(driverVersion).setup();
                driver = new FirefoxDriver();
                break;
            case "headless_firefox":
                WebDriverManager.firefoxdriver().version(driverVersion).setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.setHeadless(true);
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "ie":
                WebDriverManager.iedriver().version(driverVersion).setup();
                driver = new InternetExplorerDriver();
                break;
            case "edge":
                WebDriverManager.edgedriver().version(driverVersion).setup();
                driver = new EdgeDriver();
                break;
            default:
                System.out.println("The browser is unsupported");
        }
        ((WebDriver) driver).manage().timeouts().implicitlyWait(longTimeout, TimeUnit.SECONDS);
        ((WebDriver) driver).manage().window().maximize();
        return (WebDriver) driver;
    }

    public void closeBrowser(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        if (driver != null) {
            driver.quit();
        }
    }

    public void closeDriverProcess() {
        try {
            LOGGER.info("OS name = " + OsHelper.getOsFullName());
            String cmd = "";
            if (driver.toString().toLowerCase().contains("chrome")) {
                if (OsHelper.isMac()) {
                    cmd = "pkill chromedriver";
                } else if (OsHelper.isWindows()) {
                    cmd = "taskkill /F /FI \"IMAGENAME eq chromedriver*\"";
                }
                Process process = Runtime.getRuntime().exec(cmd);
                process.waitFor();
            }

            if (driver.toString().toLowerCase().contains("internetexplorer")) {
                if (OsHelper.isWindows()) {
                    cmd = "taskkill /F /FI \"IMAGENAME eq IEDriverServer*\"";
                    Process process = Runtime.getRuntime().exec(cmd);
                    process.waitFor();
                }
            }
            LOGGER.info("---------- STOP WEB DRIVERS SUCCESS ----------");
        } catch (Exception e) {
            LOGGER.info("---------- FAIL TO STOP WEB DRIVER ----------");
            LOGGER.info(e.getMessage());
        }
    }

}