package com.miro.test;

import com.miro.test.utils.DriverFactory;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseTest.class);
    protected final WebDriver driver = getDriverInstance();

    public WebDriver getDriverInstance() {
        return (driver == null)? DriverFactory.getDriver(DriverFactory.Browser.CHROME) : driver;
    }

    @BeforeMethod(alwaysRun = true)
    protected void setup(Method method) {
        LOGGER.info("********** Executing test ${method.name} **********\n\n");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult testResult) {
        int status = testResult.getStatus();
        switch (status) {
            case 1: {
                LOGGER.info("********** Test : " + testResult.getName()
                        + " completed with status : " + BaseTest.TestStatus.SUCCESS + " **********\n\n");
                break;
            }

            case 2:
                LOGGER.info("********** Test : " + testResult.getName()
                        + " completed with status : " + BaseTest.TestStatus.FAILURE + " **********\n\n");

                String methodName = testResult.getName();
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                    String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath()
                            + "/target/surefire-reports";
                    File destFile = new File(reportDirectory + "/failure_screenshots/" + methodName + ".png");
                    FileUtils.copyFile(scrFile, destFile);
                    Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='"
                            + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case 3:
                LOGGER.info("********** Test : " + testResult.getName() + " completed with status : " + TestStatus.SKIPPED + " **********\n\n");
                break;
        }
        if (driver != null) {
            driver.quit();
        }
    }

    enum TestStatus {
        SUCCESS,
        FAILURE,
        SKIPPED
    }
}
