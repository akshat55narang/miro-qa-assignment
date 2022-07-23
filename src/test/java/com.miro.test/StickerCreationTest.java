package com.miro.test;

import com.miro.test.pages.DashboardPage;
import com.miro.test.pages.EditBoardPage;
import com.miro.test.pages.LoginPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;

import static com.miro.test.pages.EditBoardPage.STICKER_PARAGRAPH;
import static com.miro.test.utils.Helpers.areScreenshotsMatching;
import static org.testng.Assert.assertFalse;

public class StickerCreationTest extends BaseTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(StickerCreationTest.class);

    private static final String BOARD = "TestBoard";
    private static final String STICKER_TEXT = "Hello World";
    private static final String USER1_EMAIL = "vejoc34999@leupus.com";
    private static final String USER2_EMAIL = "relegat286@leupus.com";
    private static final String DEFAULT_PASSWORD = "testen#123";

    private final WebDriver driver = getDriverInstance();
    private final LoginPage loginPage = new LoginPage(driver);
    private final DashboardPage dashboardPage = new DashboardPage(driver);
    private final EditBoardPage editBoardPage = new EditBoardPage(driver);


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

        assertFalse(areScreenshotsMatching(before, after), "Stickers observed by " + USER1_EMAIL + "and "
              + USER2_EMAIL  + " are different");
    }

}
