package com.miro.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.miro.test.configs.ConfigManager.getDefaultBoard;
import static com.miro.test.pages.EditBoardPage.BOARD_HEADER;
import static com.miro.test.utils.Helpers.assertInLoop;
import static org.testng.Assert.assertTrue;

public class DashboardPage extends BasePage {

    private final WebDriver driver;

    private static final Logger LOGGER = LoggerFactory.getLogger(DashboardPage.class);

    public static final String CREATE_BOARD_LABEL = "//span[text()='Create a board']";
    public static final String OPEN_BOARD_CONTAINER = "//div[@data-testid='dashboard__grid__board']";


    public DashboardPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void switchToTeam(String teamName) {
        String teamAriaLabel = "//div[@aria-label='Switch to " + teamName + " team']";
        String boardXpath = "//span[text()='" + getDefaultBoard() + "']";
        WebElement image = waitForElementToBeClickable(10, By.xpath(teamAriaLabel + "//img"));

        assertInLoop(3, 5L, () -> {

            if (!waitUntilElementAppears(2, By.xpath(boardXpath))) {
                image.click();
            }
            assertTrue(waitUntilElementAppears(5, By.xpath(boardXpath)), "Team not switched!!");
            LOGGER.info("Switched to team {}", teamName);
        });
    }

    public void openBoardByName(String boardName) {
        switchToTeam("miroAssignment");

        waitForElementToBeClickable(10, By.xpath("//span[text()='" + boardName + "']")).click();

        assertInLoop(3, 5L, () -> {
            if (!getCurrentUrl().contains("/app/board")) {
                waitForElementToBeClickable(10, By.xpath("//span[text()='" + boardName + "']")).click();
            }
            assertTrue(waitUntilElementAppears(15,
                    By.xpath(BOARD_HEADER)), "Board Header not loaded!! " + boardName);
        });

        LOGGER.info("Board " + boardName + " opened successfully!");
    }
}
