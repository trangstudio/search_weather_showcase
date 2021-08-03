package core.util.scripting.interaction.web;

import core.util.platform.host.file.YamlLoader;
import core.util.platform.host.os.OsHelper;
import core.util.scripting.io.RandomGenerator;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.Collator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static core.util.platform.host.file.FileHelper.convertDirectory;

public class WebInteractionBuilder {
    protected static final Logger LOGGER = LoggerFactory.getLogger(WebInteractionBuilder.class);

    private static final Map webConfig = YamlLoader.loadConfig(WebDriverConfig.WEB_CONFIG_FILE_PATH.toString());
    private static final long longTimeout = Long.parseLong((String) webConfig.get(WebDriverConfig.LONG_TIMEOUT.toString()));
    private static final long shortTimeout = Long.parseLong((String) webConfig.get(WebDriverConfig.SHORT_TIMEOUT.toString()));

    /* Web Browser */
    public static void goToUrl(Object rawDriver, String url) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.get(url);
    }

    public static String getCurrentPageUrl(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        return driver.getCurrentUrl();
    }

    public static String getCurrentPageTitle(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        return driver.getTitle();
    }

    public static String getCurrentPageSourceCode(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        return driver.getPageSource();
    }

    public static void backToPreviousPage(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.navigate().back();
        LOGGER.info("Go back to previous page");
    }

    public static void forwardToNextPage(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.navigate().forward();
        LOGGER.info("Go forward to next page");
    }

    public static void refreshCurrentPage(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.navigate().refresh();
        LOGGER.info("Refreshed current page");
    }

    public static void acceptAlert(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.switchTo().alert().accept();
        LOGGER.info("Accepted alert");

    }

    public static void cancelAlert(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.switchTo().alert().dismiss();
        LOGGER.info("Canceled alert");

    }

    public static String getTextInAlert(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        return driver.switchTo().alert().getText();
    }

    public static void sendKeysToAlert(Object rawDriver, String value) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.switchTo().alert().sendKeys(value);
        LOGGER.info("Send keys [" + value + "] to alert");

    }

    /* Web Element */
    public static void clickToElement(Object rawDriver, String locator) {
        clickToElement(rawDriver, locator, null);
    }

    public static void clickToElement(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = dynamicValue == null ? locator : String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        long end = System.currentTimeMillis() + 15000; // time out 15 seconds
        do {
            try {
                element.click();
                LOGGER.info("Click on " + element.toString());
                break;
            } catch (StaleElementReferenceException | NoSuchElementException | ElementNotInteractableException
                    exception
            ) {
                LOGGER.debug(exception.getMessage());
            }
        }
        while (System.currentTimeMillis() < end);
    }

    public static void clickToRandomElement(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        int random = RandomGenerator.getInt(0, elements.size());
        elements.get(random).click();
        LOGGER.info("Click on " + elements.get(random).toString());

    }

    public static void sendKeysToElement(Object rawDriver, String locator, String valueToSend) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = fluentWait(driver, longTimeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        element.clear();
        element.sendKeys(valueToSend);
        LOGGER.info("Send keys '" + valueToSend + "' on element " + element.toString());

    }

    public static void sendKeysToElement(Object rawDriver, String locator, String valueToSend, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = fluentWait(driver, longTimeout).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
        element.clear();
        element.sendKeys(valueToSend);
        LOGGER.info("Send keys '" + valueToSend + "' on element " + element.toString());

    }

    public static void selectItemInDropDownText(Object rawDriver, String locator, String value) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        Select select = new Select(element);
        select.selectByVisibleText(value);
        LOGGER.info("Selected item '" + value + "' in drop down " + element.toString());

    }

    public static void selectItemInDropDownText(Object rawDriver, String locator, String value, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        Select select = new Select(element);
        select.selectByVisibleText(value);
        LOGGER.info("Selected item '" + value + "' in drop down " + element.toString());

    }

    public static void selectItemInDropDownValue(Object rawDriver, String locator, String value) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        Select select = new Select(element);
        select.selectByValue(value);
        LOGGER.info("Selected item '" + value + "' in drop down " + element.toString());

    }

    public static void selectItemInDropDownValue(Object rawDriver, String locator, String value, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        Select select = new Select(element);
        select.selectByValue(value);
        LOGGER.info("Selected item '" + value + "' in drop down " + element.toString());

    }

    public static String getSelectedItemInDropDown(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText();
    }

    public static String getSelectedItemInDropDown(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText();
    }

    public static void selectItemInCustomDropDown(Object rawDriver, String parentLocator, String allItemsLocator, String value) {
        WebDriver driver = (WebDriver) rawDriver;
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement parentElement = driver.findElement(By.xpath(parentLocator));
        wait.until(ExpectedConditions.elementToBeClickable(parentElement));
        parentElement.click();
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(allItemsLocator)));
        List<WebElement> allItems = driver.findElements(By.xpath(allItemsLocator));
        for (WebElement item : allItems) {
            if (item.getText().equals(value)) {
                if (item.isDisplayed()) {
                    item.click();
                } else {
                    js.executeScript("arguments[0].scrollIntoView(true);", item);
                    js.executeScript("arguments[0].click();", item);
                }
                break;
            }
        }
        LOGGER.info("Selected item '" + value + "' in drop down " + parentElement.toString());

    }

    public static void selectMultipleItemsInCustomDropDown(Object rawDriver, String parentLocator, String allItemsLocator, List<String> expectedItemsValue) {
        WebDriver driver = (WebDriver) rawDriver;
        JavascriptExecutor js = (JavascriptExecutor) driver;
        WebElement parentElement = driver.findElement(By.xpath(parentLocator));
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        js.executeScript("arguments[0].click();", parentElement);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(allItemsLocator)));
        List<WebElement> allItems = driver.findElements(By.xpath(allItemsLocator));
        for (String itemValue : expectedItemsValue) {
            for (WebElement item : allItems) {
                if (item.getText().equals(itemValue)) {
                    if (item.isDisplayed()) {
                        item.click();
                    } else {
                        js.executeScript("arguments[0].scrollIntoView(true);", item);
                        js.executeScript("arguments[0].click();", item);
                    }
                    break;
                }
            }
        }
        LOGGER.info("Selected item '" + expectedItemsValue + "' in drop down " + parentElement.toString());

    }

    public static String getAttributeValue(Object rawDriver, String locator, String attribute) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        return element.getAttribute(attribute);
    }

    public static String getAttributeValue(Object rawDriver, String locator, String attribute, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        return element.getAttribute(attribute);
    }

    public static List<String> getAttributeValueOfElements(Object rawDriver, String locator, String attribute) {
        WebDriver driver = (WebDriver) rawDriver;
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        return elements.stream().map(WebElement -> WebElement.getAttribute(attribute)).collect(Collectors.toList());
    }

    public static String getTextOfElement(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        return element.getText();
    }

    public static String getTextOfElement(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        return element.getText();
    }

    public static List<String> getTextOfElements(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        return elements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public static List<String> getTextOfElements(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        return elements.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public static int countNumberOfElements(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        return elements.size();
    }

    public static void checkToCheckBox(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        if (!element.isSelected()) {
            element.click();
            LOGGER.info("Checked to check box " + element.toString());

        }
    }

    public static void checkToCheckBox(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        if (!element.isSelected()) {
            element.click();
            LOGGER.info("Checked to check box " + element.toString());

        }
    }

    public static void uncheckToCheckBox(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        if (element.isSelected()) {
            element.click();
            LOGGER.info("Unchecked to check box " + element.toString());

        }
    }

    public static boolean isElementDisplayed(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        overrideGlobalTimeout(driver, shortTimeout);
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        if (elements.size() == 0) {
            return false;
        } else if (elements.size() > 0 && elements.get(0).isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isElementDisplayed(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        overrideGlobalTimeout(driver, shortTimeout);
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        if (elements.size() == 0) {
            return false;
        } else if (elements.size() > 0 && elements.get(0).isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isElementNotDisplayed(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        if (elements.size() == 0) {
            return true;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isElementNotDisplayed(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        if (elements.size() == 0) {
            return true;
        } else if (elements.size() > 0 && !elements.get(0).isDisplayed()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isElementSelected(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        return element.isSelected();
    }

    public static boolean isElementEnabled(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        return element.isEnabled();
    }

    public static boolean isElementEnabled(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        return element.isEnabled();
    }

    public static boolean isElementDisabled(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        return !element.isEnabled();
    }

    public static boolean isElementDisabled(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        return !element.isEnabled();
    }

    /* Windows /Frame /iFrame */
    public static void switchToWindowByTitle(Object rawDriver, String title) {
        WebDriver driver = (WebDriver) rawDriver;
        Set<String> allWindows = driver.getWindowHandles();
        for (String currentWindow : allWindows) {
            driver.switchTo().window(currentWindow);
            String currentTitle = driver.getTitle();
            if (currentTitle.equals(title)) {
                LOGGER.info("Switched to window by title [" + title + "]");

                break;
            }
        }

    }

    public static void closeAllWindowsExceptParent(Object rawDriver, String parentWindow) {
        WebDriver driver = (WebDriver) rawDriver;
        Set<String> allWindows = driver.getWindowHandles();
        for (String currentWindow : allWindows) {
            if (!currentWindow.equals(parentWindow)) {
                driver.switchTo().window(currentWindow);
                driver.close();
                LOGGER.info("Closed window [" + currentWindow + "]");
            }
        }
        LOGGER.info("Got back to window [" + parentWindow + "]");

    }

    public static String getWindowID(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        return driver.getWindowHandle();
    }

    public static int getNumberOfOpeningWindow(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        Set<String> allWindows = driver.getWindowHandles();
        return allWindows.size();
    }

    public static void switchToFrame(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        driver.switchTo().frame(element);
        LOGGER.info("Switched to frame " + element.toString());
    }

    public static void switchBackDefault(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.switchTo().defaultContent();
        LOGGER.info("Backed to top window");
    }

    /* User Interactions */
    public static void doubleClickToElement(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        Actions action = new Actions(driver);
        action.doubleClick(element).perform();
        LOGGER.info("Double clicked to element " + element.toString());
    }

    public static void hoverMouseToElement(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
        LOGGER.info("Hover mouse to element " + element.toString());
    }

    public static void hoverMouseToElement(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebElement element = driver.findElement(By.xpath(locator));
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
        LOGGER.info("Hover mouse to element " + element.toString());
    }

    public static void rightClickToElement(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        Actions action = new Actions(driver);
        action.contextClick(element).perform();
        LOGGER.info("Right clicked to element " + element.toString());
    }

    public static void dragAndDropFromSourceToTarget(Object rawDriver, String sourceLocator, String targetLocator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement source = driver.findElement(By.xpath(sourceLocator));
        WebElement target = driver.findElement(By.xpath(targetLocator));
        Actions action = new Actions(driver);
        action.dragAndDrop(source, target).perform();
        LOGGER.info("Dragged from element " + source.toString() + " then dropped to element " + target.toString());
    }

    public static void sendKeyboardToElement(Object rawDriver, String locator, Keys key) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        Actions action = new Actions(driver);
        action.sendKeys(element, key).perform();
        LOGGER.info("Send keys to element " + element.toString());
    }

    /* Upload Files */
    public static void attachFile(String commentFile, String... folder) {
        try {
            String pathAttachLocation = "";
            String folder_upload = "/src/test/resources/test_data/bankstatement/";
            String sDirPath = System.getProperty("user.dir");
            if (folder.length > 0) {
                pathAttachLocation = convertDirectory(sDirPath + folder[0]);
            } else {
                pathAttachLocation = convertDirectory(sDirPath + folder_upload);
            }
            Thread.sleep(2000);
            File file = new File(pathAttachLocation + commentFile);
            StringSelection stringSelection = new StringSelection(file.getAbsolutePath());
            LOGGER.info("Path is: " + pathAttachLocation + commentFile);
            //Copy to clipboard
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            Robot robot = new Robot();
            if (OsHelper.isMac()) {

                // Cmd + Tab is needed since it launches a Java app and the browser looses focus
                robot.keyPress(KeyEvent.VK_META);
                robot.keyPress(KeyEvent.VK_TAB);
                robot.keyRelease(KeyEvent.VK_META);
                robot.keyRelease(KeyEvent.VK_TAB);
                robot.delay(500);

                //Open Goto window
                robot.keyPress(KeyEvent.VK_META);
                robot.keyPress(KeyEvent.VK_SHIFT);
                robot.keyPress(KeyEvent.VK_G);
                robot.keyRelease(KeyEvent.VK_META);
                robot.keyRelease(KeyEvent.VK_SHIFT);
                robot.keyRelease(KeyEvent.VK_G);

                //Paste the clipboard value
                robot.keyPress(KeyEvent.VK_META);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_META);
                robot.keyRelease(KeyEvent.VK_V);

                //Press Enter key to close the Goto window and Upload window
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                robot.delay(500);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            } else {
                robot.delay(2);
                Thread.sleep(1000);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
                Thread.sleep(1000);
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                Thread.sleep(1000);
                robot.keyPress(KeyEvent.VK_ENTER);
                robot.keyRelease(KeyEvent.VK_ENTER);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void pressEscKey() {
        try {
            Robot robot = new Robot();
            robot.keyPress(KeyEvent.VK_ESCAPE);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    public static void uploadFile(Object rawDriver, String locator, String filePath) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        element.sendKeys(filePath);
        LOGGER.info("Uploaded file '" + filePath + "' to element " + element.toString());
    }

    public static void uploadMultipleFiles(Object rawDriver, String locator, List<String> filesPath) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        for (String path : filesPath) {
            element.sendKeys(path);
            LOGGER.info("Uploaded file '" + path + "' to element " + element.toString());
        }
    }

    /* Javascript Executor */
    public static void executeJavascript(Object rawDriver, String javascript) {
        WebDriver driver = (WebDriver) rawDriver;
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript(javascript);
    }

    public static Object executeJavascriptToBrowser(Object rawDriver, String javascript) {
        WebDriver driver = (WebDriver) rawDriver;
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        return javascriptExecutor.executeScript(javascript);
    }

    public static Object executeJavascriptToElement(Object rawDriver, String locator, String javascript) {
        WebDriver driver = (WebDriver) rawDriver;
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath(locator));
        return javascriptExecutor.executeScript(javascript, element);
    }

    public static void scrollToBottomPage(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        javascriptExecutor.executeScript("window.scrollBy(0,document.body.scrollHeight)");
        LOGGER.info("Scrolled to bottom of page");

    }

    public static void scrollToElement(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath(locator));
        javascriptExecutor.executeScript("arguments[0].scrollIntoView(true);", element);
        LOGGER.info("Scrolled to element " + element.toString());
    }

    private static void highlightElement(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        boolean enable = false;
        if (System.getProperty(WebDriverConfig.HIGHLIGHT.toString()) != null) {
            enable = Boolean.parseBoolean(System.getProperty(WebDriverConfig.HIGHLIGHT.toString()));
        }
        if (enable) {
            JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
            String originalStyle = driver.findElement(By.xpath(locator)).getAttribute("style");
            javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", driver.findElement(By.xpath(locator)), "style", "border: 3px solid red; border-style: dashed;");
            sleepInTime(200);
            javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", driver.findElement(By.xpath(locator)), "style", originalStyle);
        }
    }

    public static void clearTextElement(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.findElement(By.xpath(locator)).clear();
    }

    public static void clearTextElement(Object rawDriver, String locator,String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        driver.findElement(By.xpath(locator)).clear();
    }

    public static void highlightElement(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath(locator));
        String originalStyle = element.getAttribute("style");
        javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", "border: 3px solid red; border-style: dashed;");
        sleepInTime(200);
        javascriptExecutor.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, "style", originalStyle);
    }

    public static void removeAttributeOfElement(Object rawDriver, String locator, String attribute) {
        WebDriver driver = (WebDriver) rawDriver;
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath(locator));
        javascriptExecutor.executeScript("arguments[0].removeAttribute('" + attribute + "');", element);
    }

    public static void removeAttributeOfElement(Object rawDriver, String locator, String attribute, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath(locator));
        javascriptExecutor.executeScript("arguments[0].removeAttribute('" + attribute + "');", element);
    }

    public static void setValueAttribute(Object rawDriver, String locator, String attribute, String valueToSet, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath(locator));
        javascriptExecutor.executeScript("document.getElementByXpath('" + locator + "').setAttribute('" + attribute + "', '" + valueToSet + "')");
    }

    public static boolean checkAnyImageLoaded(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        WebElement element = driver.findElement(By.xpath(locator));
        boolean status = (Boolean) javascriptExecutor.executeScript("\"return arguments[0].complete && typeof arguments[0].naturalWidth != 'undefined' && arguments[0].naturalWidth > 0\"", element);
        return status;
    }

    public static boolean verifyTextInInnerText(Object rawDriver, String textExpected) {
        WebDriver driver = (WebDriver) rawDriver;
        JavascriptExecutor javascriptExecutor = (JavascriptExecutor) driver;
        String textActual = (String) javascriptExecutor.executeScript("return document.documentElement.innerText.match('" + textExpected + "')[0]");
        LOGGER.info("Text actual = " + textActual);
        return textActual.equals(textExpected);
    }

    /* Wait */
    public static void waitForElementToBePresent(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        By elementBy = By.xpath(locator);
        wait.until(ExpectedConditions.presenceOfElementLocated(elementBy));
        highlightElement(driver, locator);
    }

    public static void waitForElementToBePresent(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        By elementBy = By.xpath(locator);
        wait.until(ExpectedConditions.presenceOfElementLocated(elementBy));
        highlightElement(driver, locator);
    }

    public static void waitForAllElementsToBePresent(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        By elementsBy = By.xpath(locator);
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementsBy));
        highlightElement(driver, locator);
    }

    public static void waitForElementToBeVisible(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator)));
            highlightElement(driver, locator);
        } catch (Exception e) {
            LOGGER.error("==================== Element not found ====================");
            LOGGER.error(e.getMessage());
        }
    }

    public static void waitForElementToBeVisible(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        try {
            wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(locator))));
            highlightElement(driver, locator);
        } catch (Exception e) {
            LOGGER.error("==================== Error when waiting for element to become visible ====================");
            LOGGER.error(e.getMessage());
        }
    }

    public static void waitForElementToBeInvisible(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
    }

    public static void waitForElementToBeInvisible(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(locator)));
    }

    public static void waitForElementToBeClickable(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(locator)));
        wait.until(ExpectedConditions.elementToBeClickable((WebElement) driver.findElement(By.xpath(locator))));
        highlightElement(driver, locator);
    }

    public static void waitForElementToBeClickable(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        wait.until(ExpectedConditions.elementToBeClickable((WebElement) driver.findElement(By.xpath(locator))));
        highlightElement(driver, locator);
    }

    public static void waitForElementToBeRefreshed(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(driver.findElement(By.xpath(locator)))));
    }

    public static void waitForElementToBeRefreshed(Object rawDriver, String locator, String... dynamicValue) {
        WebDriver driver = (WebDriver) rawDriver;
        locator = String.format(locator, dynamicValue);
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        wait.until(ExpectedConditions.refreshed(ExpectedConditions.stalenessOf(driver.findElement(By.xpath(locator)))));
    }

    public static void waitForNestedElementsToBeVisible(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        wait.until(ExpectedConditions.visibilityOfNestedElementsLocatedBy((WebElement) driver.findElement(By.xpath(locator)), By.tagName("tr")));
    }

    public static void waitForAlertToBePresent(Object rawDriver, String xpathDynamicAccountLabelText, String textEverydayAccountPageTitle) {
        WebDriver driver = (WebDriver) rawDriver;
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        wait.until(ExpectedConditions.alertIsPresent());
    }

    public static void waitForPageLoad(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        ExpectedCondition<Boolean> pageLoadCondition = new
                ExpectedCondition<Boolean>() {
                    public Boolean apply(WebDriver driver) {
                        return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
                    }
                };
        WebDriverWait wait = new WebDriverWait(driver, longTimeout);
        wait.until(pageLoadCondition);
    }

    /* Sorting */
    public static boolean isStringSortedAscending(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        ArrayList<String> actualList = new ArrayList<String>();
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        for (WebElement element : elements) {
            actualList.add(element.getText());
        }
        ArrayList<String> expectedList = new ArrayList<String>();
        for (String child : actualList) {
            expectedList.add(child);
        }
        Collections.sort(expectedList, Collator.getInstance(Locale.ENGLISH));
        return actualList.equals(expectedList);
    }

    public static boolean isStringSortedDescending(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        ArrayList<String> actualList = new ArrayList<String>();
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        for (WebElement element : elements) {
            actualList.add(element.getText());
        }
        ArrayList<String> expectedList = new ArrayList<String>();
        for (String child : actualList) {
            expectedList.add(child);
        }
        Collections.sort(expectedList, Collator.getInstance(Locale.ENGLISH));
        Collections.reverse(expectedList);
        return actualList.equals(expectedList);
    }

    public static boolean isDateSortedAscending(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        ArrayList<Date> actualList = new ArrayList<Date>();
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        for (WebElement element : elements) {
            try {
                actualList.add(formatter.parse(element.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Date> expectedList = new ArrayList<Date>();
        for (Date child : actualList) {
            expectedList.add(child);
        }
        Collections.sort(expectedList);
        return actualList.equals(expectedList);
    }

    public static boolean isDateSortedDescending(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        ArrayList<Date> actualList = new ArrayList<Date>();
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        for (WebElement element : elements) {
            try {
                actualList.add(formatter.parse(element.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Date> expectedList = new ArrayList<Date>();
        for (Date child : actualList) {
            expectedList.add(child);
        }
        Collections.sort(expectedList);
        Collections.reverse(expectedList);
        return actualList.equals(expectedList);
    }

    public static boolean isNumberSortedAscending(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        ArrayList<Float> actualList = new ArrayList<Float>();
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        for (WebElement element : elements) {
            actualList.add(Float.parseFloat(element.getText().replace("$", "").replace(",", "")));
        }
        ArrayList<Float> expectedList = new ArrayList<Float>();
        for (Float child : actualList) {
            expectedList.add(child);
        }
        Collections.sort(expectedList);
        return actualList.equals(expectedList);
    }

    public static boolean isNumberSortedDescending(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        ArrayList<Float> actualList = new ArrayList<>();
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        for (WebElement element : elements) {
            actualList.add(Float.parseFloat(element.getText().replace("$", "").replace(",", "")));
        }
        ArrayList<Float> expectedList = new ArrayList<>();
        for (Float child : actualList) {
            expectedList.add(child);
        }
        Collections.sort(expectedList);
        Collections.reverse(expectedList);
        return actualList.equals(expectedList);
    }

    public static boolean isDataSortedAscending(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        List<String> actualList = elements.stream().map(n -> n.getText()).collect(Collectors.toList());
        List<String> expectedList = actualList;
        Collections.sort(expectedList);
        return actualList.equals(expectedList);
    }

    public static boolean isDataSortedDescending(Object rawDriver, String locator) {
        WebDriver driver = (WebDriver) rawDriver;
        List<WebElement> elements = driver.findElements(By.xpath(locator));
        List<String> actualList = elements.stream().map(n -> n.getText()).collect(Collectors.toList());
        List<String> expectedList = actualList;
        Collections.sort(expectedList);
        Collections.reverse(expectedList);
        return actualList.equals(expectedList);
    }

    /* Override Implicit Wait Timeout */
    private static void overrideGlobalTimeout(Object rawDriver, long timeout) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
    }

    private static void sleepInTime(long millisecond) {
        try {
            Thread.sleep(millisecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static boolean isElementDisplay(Object rawDriver, String locator, long timeout) {
        WebDriver driver = (WebDriver) rawDriver;
        WebElement element = driver.findElement(By.xpath(locator));
        fluentWait(driver, timeout)
                .until(ExpectedConditions.visibilityOf(element));
        return element.isDisplayed();
    }

    public static void switchToPopUp(Object rawDriver) {
        WebDriver driver = (WebDriver) rawDriver;
        driver.switchTo().window(driver.getWindowHandle());
    }

    public static Wait<WebDriver> fluentWait(Object rawDriver, long timeout) {
        WebDriver driver = (WebDriver) rawDriver;
        return new FluentWait<>(driver).withTimeout(java.time.Duration.ofSeconds(timeout)).pollingEvery(java.time.Duration.ofSeconds(1))
                .ignoring(NullPointerException.class);
    }

    public static void waitForDisappeared(Object rawDriver, String locator, int time, TimeUnit unit) {
        WebDriver driver = (WebDriver) rawDriver;
        long end = System.currentTimeMillis() + TimeUnit.MILLISECONDS.convert(time, unit);
        do {
            try {
                driver.findElement(By.xpath(locator));
            } catch (NoSuchElementException exception) {
                return;
            }
        } while (System.currentTimeMillis() < end);
    }

}