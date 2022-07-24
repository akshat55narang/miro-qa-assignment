package com.miro.test.configs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static com.miro.test.utils.Helpers.getParameter;

public class ConfigManager {

    private static final String BASE_URL = "base.url";
    private static final String IS_BROWSER_SHOWN = "is.browser.shown";
    private static final String DEFAULT_ACCESS_TOKEN = "default.access.token";
    private static final String DEFAULT_TEAM_ID = "default.team.id";
    private static final String DEFAULT_BOARD = "default.board";

    private ConfigManager() {
    }

    private static Properties loadProperties() {
        Properties properties = null;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("default.properties"))) {
            properties = new Properties();
            properties.load(bufferedReader);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }

    public static String getConfig(String variableName) {
        if (!loadProperties().containsKey(variableName)) {
            throw new RuntimeException("Configuration option " + variableName + " not found");
        }
        return loadProperties().getProperty(variableName);
    }

    public static String getBaseUrl() {
        return getParameter("baseUrl", getConfig(BASE_URL));
    }

    public static boolean isBrowserHeadless() {
        String isBrowserShown = getParameter("isBrowserShown", getConfig(IS_BROWSER_SHOWN));
        return Boolean.parseBoolean(isBrowserShown);
    }

    public static String getDefaultAccessToken() {
        return getParameter("defaultAccessToken", getConfig(DEFAULT_ACCESS_TOKEN));
    }

    public static String getDefaultTeamId() {
        return getParameter("defaultTeamId", getConfig(DEFAULT_TEAM_ID));
    }

    public static String getDefaultBoard() {
        return getParameter("defaultBoard", getConfig(DEFAULT_BOARD));
    }


}
