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
    public static final String OPEN_BOARD_CONTAINER = "//div[@data-testid='dashboard__grid__board']";


    public DashboardPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void switchToTeam(String teamName) {
        String xpath = "//div[@aria-label='Switch to " + teamName + " team']";
        WebElement image = waitForElementToBeClickable(15, By.xpath(xpath + "//img"));
        if (waitUntilElementAppears(15, By.xpath(xpath))) {
            image.click();
        }
        assertTrue(waitUntilElementAppears(5, By.xpath(xpath)));
    }

    public void openBoardByName(String boardName) {

        switchToTeam("miroAssignment");

        WebElement board = waitForElement(20, By.xpath("//span[text()='" + boardName
                + "']/ancestor::div[@data-testid='dashboard__grid__board']"));

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
