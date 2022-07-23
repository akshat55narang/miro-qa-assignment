package com.miro.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.miro.test.pages.EditBoardPage.BOARD_HEADER;
import static com.miro.test.utils.Helpers.assertInLoop;
import static org.testng.Assert.assertTrue;

public class DashboardPage extends AbstractPage {

    private final WebDriver driver;

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardPage.class);

    public static final String CREATE_BOARD_LABEL = "//span[text()='Create a board']";
    public static final String OPEN_BOARD_CONTRAINER = "//div[@data-testid='dashboard__grid__board']";

    public DashboardPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void openBoardByName(String boardName) {
        WebElement board = waitForElement(20, By.xpath(OPEN_BOARD_CONTRAINER));

        assertInLoop(3, 5L, () -> {
            moveToElementAndClick(board);

            assertTrue(waitUntilElementAppears(10,
                    By.xpath("//canvas")), "Not able to open board " + boardName);
            assertTrue(waitUntilElementAppears(15,
                    By.xpath(BOARD_HEADER)), "Board Header not loaded!! " + boardName);
        });
        LOGGER.info("Board " + boardName + " opened successfully!");
    }
}
