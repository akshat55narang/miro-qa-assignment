package com.miro.test;

import com.miro.test.pages.DashboardPage;
import com.miro.test.pages.EditBoardPage;
import com.miro.test.pages.LoginPage;
import com.miro.test.utils.DriverFactory;
import com.miro.test.utils.Helpers;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.miro.test.pages.EditBoardPage.STICKER_PARAGRAPH;
import static org.testng.Assert.assertFalse;

public class StickerCreationTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(StickerCreationTest.class);

    private static final String BOARD = "TestBoard";
    private static final String STICKER_TEXT = "Hello World";
    private static final String USER1_EMAIL = "vejoc34999@leupus.com";
    private static final String USER2_EMAIL = "relegat286@leupus.com";
    private static final String DEFAULT_PASSWORD = "testen#123";

    private WebDriver driver = DriverFactory.getDriver(DriverFactory.Browser.CHROME);
    private LoginPage loginPage = new LoginPage(driver);
    private DashboardPage dashboardPage = new DashboardPage(driver);
    private EditBoardPage editBoardPage = new EditBoardPage(driver);

//    public StickerCreationTest(WebDriver driver) {
//        super(driver);
//
//    }


    @BeforeMethod(alwaysRun = true)
    public void setup() {
        driver.manage().window().maximize();
    }

    @Test
    public void shouldBeAbleToShareStickyNote() {
        loginPage.loginWithEmail(USER1_EMAIL, DEFAULT_PASSWORD);
        dashboardPage.openBoardByName(BOARD);
        editBoardPage.addStickerWithText(STICKER_TEXT);

        Screenshot before = new AShot()
                .takeScreenshot(driver, driver.findElement(By.xpath(STICKER_PARAGRAPH)));

        editBoardPage.inviteUserByEmail(USER2_EMAIL);

        loginPage.clearCookies();
        loginPage.loginWithEmail(USER2_EMAIL, DEFAULT_PASSWORD);
        dashboardPage.openBoardByName(BOARD);
        editBoardPage.verifySticker();

        Screenshot after = new AShot()
                .takeScreenshot(driver, driver.findElement(By.xpath(STICKER_PARAGRAPH)));

        assertFalse(new Helpers().isScreenShotDifference(before, after), "Screenshots are different");
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult testResult) {
        int status = testResult.getStatus();
        switch (status) {
            case 1: {
                LOGGER.info("********** Test : " + testResult.getName() + " completed with status : " + BaseT.TestStatus.SUCCESS + " **********\n\n");
                break;
            }
            case 2:
                LOGGER.info("********** Test : " + testResult.getName() + " completed with status : " + BaseT.TestStatus.FAILURE + " **********\n\n");

                LOGGER.info("****** Taking Screen shot for failed Scenario  ******");
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat formater = new SimpleDateFormat("dd_MM_yyyy_hh_mm_ss");
                String methodName = testResult.getName();
                File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                try {
                    String reportDirectory = new File(System.getProperty("user.dir")).getAbsolutePath() + "/target/surefire-reports";
                    File destFile = new File((String) reportDirectory + "/failure_screenshots/" + methodName + "_" + formater.format(calendar.getTime()) + ".png");
                    FileUtils.copyFile(scrFile, destFile);
                    Reporter.log("<a href='" + destFile.getAbsolutePath() + "'> <img src='" + destFile.getAbsolutePath() + "' height='100' width='100'/> </a>");
                } catch (IOException e) {
                    e.printStackTrace();
                }

                break;
            case 3:
                LOGGER.info("********** Test : " + testResult.getName() + " completed with status : " + BaseT.TestStatus.SKIPPED + " **********\n\n");
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
