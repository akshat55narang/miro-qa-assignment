package com.miro.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.testng.Assert.assertTrue;

public class LoginPage extends BasePage {
    private final WebDriver driver;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginPage.class);

    public static final String LOGIN_PAGE_URL = "/login";
    public static final String LOGIN_INPUT = "//input[@id='email']";
    public static final String PASSWORD_INPUT = "//input[@id='password']";
    public static final String SIGN_IN_BUTTON = "//button[text()='Sign in']";

    public LoginPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void open() {
        get(LOGIN_PAGE_URL);
        assertTrue(waitUntilElementAppears(10, By.xpath(SIGN_IN_BUTTON)),
                "Login page not loaded!!");
        LOGGER.info("Login page loaded successfully!!");
    }


    public void loginWithEmail(String email, String password) {
        open();
        sendTextToFieldBy(email, By.xpath(LOGIN_INPUT));
        sendTextToFieldBy(password, By.xpath(PASSWORD_INPUT));
        waitForElementToBeClickable(By.xpath(SIGN_IN_BUTTON)).click();

        assertTrue(waitUntilElementDisappears(30, By.xpath(SIGN_IN_BUTTON)),
                "Not able to login with user !!" + email);
        LOGGER.info("Login with user " + email + " successful!!");
    }


}
