package com.miro.test.pages;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.Duration;

import static com.miro.test.configs.ConfigManager.getBaseUrl;
import static com.miro.test.configs.ConfigManager.isBrowserShown;
import static org.testng.Assert.assertTrue;

public class BasePage {

    private final WebDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(BasePage.class);
    public static final String USER_DIRECTORY = System.getProperty("user.dir");
    private static final Long DRIVER_TIMEOUT = 60L;

    BasePage(WebDriver driver) {
        this.driver = driver;
    }

    private FluentWait<WebDriver> newWait() {
        return new WebDriverWait(driver, DRIVER_TIMEOUT)
                .pollingEvery(Duration.ofMillis(500))
                .ignoring(StaleElementReferenceException.class);
    }

    protected void get(String url) {
        driver.get(getBaseUrl() + url);
    }

    protected String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    protected void refreshPage() {
        LOGGER.info("Refreshing page " + getCurrentUrl());
        driver.navigate().refresh();
    }

    public void clearCookies() {
        driver.manage().deleteAllCookies();
    }

    protected void sendTextToFieldBy(String text, By by) {
        WebElement field = waitForElementInDom(10, by);
        field.click();
        field.clear();
        field.sendKeys(text);
    }

    protected void sendTextToFieldUsingActionsClass(String text, By by) {
        Actions actions = new Actions(driver);
        actions.click(waitForElement(2, by)).sendKeys(text)
                .build().perform();
    }

    public WebElement waitForElement(int timeOutInSeconds, By by) {
        return newWait().withTimeout(Duration.ofSeconds(timeOutInSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public WebElement waitForElement(int timeOutInSeconds, WebElement element) {
        return newWait().withTimeout(Duration.ofSeconds(timeOutInSeconds))
                .until(ExpectedConditions.visibilityOf(element));
    }

    protected WebElement waitForElementInDom(int timeOutInSeconds, By by) {
        return newWait().withTimeout(Duration.ofSeconds(timeOutInSeconds))
                .until(ExpectedConditions.presenceOfElementLocated(by));
    }


    protected boolean waitUntilElementAppears(By by) {
        try {
            newWait().until(ExpectedConditions.visibilityOfElementLocated(by));
            return true;
        } catch (TimeoutException exception) {
            return false;
        }

    }

    protected boolean waitUntilElementAppears(int timeoutInSeconds, By by) {
        try {
            newWait()
                    .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                    .until(ExpectedConditions.visibilityOf(waitForElementInDom(5, by)));
            return true;
        } catch (TimeoutException exception) {
            return false;
        }

    }

    protected boolean waitUntilElementDisappears(By by) {
        try {
            newWait().until(ExpectedConditions.invisibilityOfElementLocated(by));
            return true;
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public boolean waitUntilElementDisappears(int timeoutInSeconds, By by) {
        try {
            return newWait().withTimeout(Duration.ofSeconds(timeoutInSeconds))
                    .until(ExpectedConditions.invisibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            return false;
        }
    }

    protected WebElement waitForElementToBeClickable(By by) {
        return newWait().until(ExpectedConditions.elementToBeClickable(by));
    }

    protected WebElement waitForElementToBeClickable(int timeoutInSeconds, By by) {
        return newWait()
                .withTimeout(Duration.ofSeconds(timeoutInSeconds))
                .until(ExpectedConditions.elementToBeClickable(by));
    }

    protected void moveToElementAndClick(WebElement element, int x, int y) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element, x, y).click().build().perform();
    }

    protected void moveToElementAndDoubleClick(WebElement element, int x, int y) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element, 200, 40).doubleClick().build().perform();
    }

    protected void moveToElementAndClick(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).click().build().perform();
    }

    protected void moveToElementByOffsetAndClick(WebElement element, int xOffset, int yOffset) {
        Actions actions = new Actions(driver);
        moveToElementByOffsetAndClick(actions, element, xOffset, yOffset);
    }

    protected void moveToElementByOffsetAndClick(Actions actions, WebElement element, int xOffset, int yOffset) {
        actions.moveToElement(element, 0, 0)
                .moveByOffset(xOffset, yOffset)
                .click()
                .build()
                .perform();
    }

    protected void clickElementWithActionsClass(WebElement element) {
        Actions actions = new Actions(driver);
        actions.click(element).build().perform();
    }

    protected void moveToElement(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element).perform();
    }

    protected void dragAndDrop(WebElement from, int x, int y) {
        Actions actions = new Actions(driver);
        actions.dragAndDropBy(from, x, y)
                .build().perform();
    }

    protected Object executeScript(String script, Object... objects) {
        return ((JavascriptExecutor) driver).executeScript(script, objects);
    }

    protected void selectAllText(WebElement element) {
        String os = System.getProperty("os.name");
        if (os.equals("WINDOWS")) {
            element.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        } else {
            element.sendKeys(Keys.chord(Keys.COMMAND, "a"));
        }
    }

    protected void verifyScreenshot(WebElement element) {
        File screenshot = element.getScreenshotAs(OutputType.FILE);

        try {
            BufferedImage actualImage = ImageIO.read(screenshot);
            FileUtils.copyFile(screenshot, new File(USER_DIRECTORY + "/target/screenshots/actual.png"));

            BufferedImage expectedImage;
            if (isBrowserShown()) {
                File expectedFile = new File(USER_DIRECTORY + "/src/test/resources/expected.png");
                expectedImage = ImageIO.read(expectedFile);
            } else {
                File expectedFile = new File(USER_DIRECTORY + "/src/test/resources/expected_headless.png");
                expectedImage = ImageIO.read(expectedFile);

            }

            ImageDiff diff = new ImageDiffer().makeDiff(actualImage, expectedImage);
            BufferedImage difference = diff.getMarkedImage();
            if (diff.hasDiff()) {
                ImageIO.write(difference, "png",
                        new File(USER_DIRECTORY + "/target/screenshots/difference.png"));
                Assert.fail("Expected image " + expectedImage + "is not the same as actual image " + actualImage);
            }
            assertTrue(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
