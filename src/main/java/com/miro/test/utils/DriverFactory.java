package com.miro.test.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import static com.miro.test.configs.ConfigManager.isBrowserShown;

public class DriverFactory {

    public static WebDriver getDriver(Browser browser) {
        switch (browser) {
            case CHROME: {
                WebDriverManager.chromedriver().setup();
                return new ChromeDriver(getDefaultChromeOptions());
            }
            case FIREFOX: {
                WebDriverManager.firefoxdriver().setup();
                return new FirefoxDriver();
            }
            default: {
                throw new RuntimeException("Brower" + browser + "not supported!!");
            }
        }
    }

    public enum Browser {
        CHROME,
        FIREFOX
    }

    private static ChromeOptions getDefaultChromeOptions() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                "--disable-popup-blocking",
                "--ignore-certificate-errors",
                "--disable-default-apps",
                "--enable-logging",
                "--ssl-protocol=tlsv1.2",
                "--webdriver-loglevel=info"
        );

        if (!isBrowserShown())
        chromeOptions.addArguments(
                "--headless"
        );
        return chromeOptions;
    }

}

