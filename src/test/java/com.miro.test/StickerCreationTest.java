package com.miro.test;

import com.miro.test.api.BoardApi;
import com.miro.test.pages.DashboardPage;
import com.miro.test.pages.EditBoardPage;
import com.miro.test.pages.LoginPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.miro.test.configs.ConfigManager.getDefaultBoard;

public class StickerCreationTest extends Base {
    private static final Logger LOGGER = LoggerFactory.getLogger(StickerCreationTest.class);

    public static final String BOARD = getDefaultBoard();
    private static final String STICKER_TEXT = "HelloWorld";
    private static final String USER1_EMAIL = "vejoc34999@leupus.com";
    private static final String USER2_EMAIL = "relegat286@leupus.com";
    private static final String DEFAULT_PASSWORD = "testen#123";

    private BoardApi boardApi;
    private LoginPage loginPage;
    private DashboardPage dashboardPage;
    private EditBoardPage editBoardPage;

    @BeforeClass(alwaysRun = true)
    public void initialize() {
        boardApi = new BoardApi();
        loginPage = new LoginPage(driver);
        dashboardPage = new DashboardPage(driver);
        editBoardPage = new EditBoardPage(driver);
    }

    @BeforeMethod
    public void setup() {
        if (!boardApi.isBoardPresent(BOARD)) {
            LOGGER.info("Board with name {} does not exist. Creating via API!", BOARD);
            String boardId = boardApi.createBoard(BOARD);
            LOGGER.info("Created board with id {}", boardId);
        }
    }

    @Test
    public void shouldBeAbleToShareStickyNote() {
        loginPage.loginWithEmail(USER1_EMAIL, DEFAULT_PASSWORD);
        dashboardPage.openBoardByName(BOARD);
        editBoardPage.addStickerWithText(STICKER_TEXT);

        editBoardPage.inviteUserByEmail(USER2_EMAIL);

        loginPage.clearCookies();
        loginPage.loginWithEmail(USER2_EMAIL, DEFAULT_PASSWORD);
        dashboardPage.openBoardByName(BOARD);
        editBoardPage.verifySticker();

    }

}
