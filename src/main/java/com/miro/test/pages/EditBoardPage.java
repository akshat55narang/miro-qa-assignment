package com.miro.test.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.miro.test.utils.Helpers.assertInLoop;
import static org.testng.Assert.assertTrue;


public class EditBoardPage extends BasePage {
    private final WebDriver driver;
    private static final Logger LOGGER = LoggerFactory.getLogger(EditBoardPage.class);

    public static final String BASE_CANVAS = "//div[@id='pixiCanvasContainer']//canvas[1]";
    public static final String CREATE_STICKER_BUTTON = "//div[@data-plugin-id='STICKERS']";
    public static final String STICKER_EDITOR_BOX = "//div[@class='editor-box']";
    public static final String NEW_STICKER_INPUT = "//div[contains(@class,'ql-editor')]";
    public static final String STICKER_PARAGRAPH = NEW_STICKER_INPUT + "/p";
    public static final String SHARE_BUTTON_XPATH = "//button[@data-testid='share-board-button']";
    public static final String EMAIL_EDITOR_LABEL = "//label[@class='share-content__emails-editor-interactive-placeholder']";
    public static final String EMAIL_INPUT = EMAIL_EDITOR_LABEL + "/span[1]";
    public static final String SEND_EMAIL_BUTTON = "//button[@data-testid='shareMdButtonSend']";
    public static final String BOARD_HEADER = "//div[@data-testid='board-header__logo']";


    public EditBoardPage(WebDriver driver) {
        super(driver);
        this.driver = driver;
    }

    public void addStickerWithText(String stickerText) {
        LOGGER.info("Add sticker to Board!!");
        Actions actions = new Actions(driver);
        assertInLoop(3, 5L, () -> {

            waitForElementToBeClickable(By.xpath(CREATE_STICKER_BUTTON)).click();

            actions.moveToElement(waitForElementToBeClickable(By.xpath(BASE_CANVAS)), 0, 0)
                    .moveByOffset(30, 30)
                    .click()
                    .build().perform();

            assertTrue(waitUntilElementAppears(By.xpath(NEW_STICKER_INPUT)), "New sticker was not added ");

            addTextToSticker(stickerText);
        });
        LOGGER.info("Sticker with text " + stickerText + " added to Board!!");
    }

    public void addTextToSticker(String stickerText) {
        sendTextToFieldBy(stickerText, By.xpath(STICKER_PARAGRAPH));
        assertTrue(waitUntilElementAppears(2,
                By.xpath(STICKER_PARAGRAPH + "[text()='" + stickerText + "']")));

        LOGGER.info("Text added to Sticker successfully!!");
    }

    public void clickShareButton() {
        WebElement shareButton = waitForElementInDom(3, By.xpath(SHARE_BUTTON_XPATH));
        executeScript("arguments[0].click()", shareButton);
        assertInLoop(3, 5L, () -> {
            assertTrue(waitUntilElementAppears(5, By.xpath(EMAIL_EDITOR_LABEL)),
                    "Share button not opened!!");
        });

        LOGGER.info("Invite shared successfully!!");
    }

    public void sendEmail() {
        WebElement sendEmailButton = waitForElementToBeClickable(5, By.xpath(SEND_EMAIL_BUTTON));
        executeScript("arguments[0].click()", sendEmailButton);

        assertInLoop(3, 5L, () -> {
            if (waitUntilElementAppears(3, By.xpath("//span[contains(text(), 'invalid email address')]"))) {
                clickElementWithActionsClass(waitForElementToBeClickable(2,
                        By.xpath("//button[@class='chip__deleteButton']")));
                executeScript("arguments[0].click()", sendEmailButton);
            }
            assertTrue(waitUntilElementDisappears(3, By.xpath(SEND_EMAIL_BUTTON)),
                    "Email not sent!!");
        });

        LOGGER.info("Email sent successfully!!");
    }

    public void inviteUserByEmail(String email) {
        clickShareButton();
        assertInLoop(3, 2L, () -> {
            sendTextToFieldUsingActionsClass(email, By.xpath(EMAIL_INPUT));
            assertTrue(waitUntilElementAppears(By.xpath(SEND_EMAIL_BUTTON)), "Send Email button not loaded!!");
        });
        sendEmail();

        LOGGER.info("Invite successfully sent to email " + email);
    }

    public void verifySticker() {
        Actions actions = new Actions(driver);
        assertInLoop(3, 5L, () -> {
            refreshPage();
            assertTrue(waitUntilElementAppears(10, By.xpath(BOARD_HEADER)), "Canvas not loaded!");

            actions.moveToElement(waitForElementToBeClickable(By.xpath(BASE_CANVAS)), 0, 0)
                    .moveByOffset(30, 30)
                    .doubleClick()
                    .build().perform();

            assertTrue(waitUntilElementAppears(5, By.xpath(NEW_STICKER_INPUT)), "Sticker not loaded");

            WebElement stickerInput = driver.findElement(By.xpath(NEW_STICKER_INPUT));
            selectAllText(stickerInput);
        });

        LOGGER.info("Sticker visible to other users!!");
        verifyScreenshot(driver.findElement(By.xpath(STICKER_EDITOR_BOX)));
    }
}
