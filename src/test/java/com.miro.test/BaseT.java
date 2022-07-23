package com.miro.test;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;

public class BaseT {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseT.class);
    private WebDriver driver;

    public BaseT(WebDriver driver) {
        this.driver = driver;
    }

    @BeforeMethod(alwaysRun = true)
    protected void setup(Method method) {
        LOGGER.info("********** Executing test ${method.name} **********\n\n");
    }

    @AfterMethod(alwaysRun = true)
    protected void tearDown(ITestResult testResult) {
        int status = testResult.getStatus();
        switch (status) {
            case 1: {
                LOGGER.info("********** Test : " + testResult.getName() + " completed with status : " + TestStatus.SUCCESS + " **********\n\n");
                break;
            }
            case 2:
                LOGGER.info("********** Test : " + testResult.getName() + " completed with status : " + TestStatus.FAILURE + " **********\n\n");

                LOGGER.info("****** Taking Screen shot for failed Scenario  ******");
                byte[] screenShot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Reporter.log(String.valueOf(screenShot), testResult.getStatus());

                break;
            case 3:
                LOGGER.info("********** Test : " + testResult.getName() + " completed with status : " + TestStatus.SKIPPED + " **********\n\n");
                break;
        }
    }

    enum TestStatus {
        SUCCESS,
        FAILURE,
        SKIPPED
    }
}
