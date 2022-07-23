package com.miro.test.utils;

import org.openqa.selenium.WebDriverException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.comparison.ImageDiff;
import ru.yandex.qatools.ashot.comparison.ImageDiffer;

import java.time.Duration;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Helpers {

    private static final Logger LOGGER = LoggerFactory.getLogger(Helpers.class);

    public static void assertInLoop(int attempts, long timeoutInSeconds, Runnable assertion) {
        for (int i = 0; i < attempts; i++) {
            try {
                assertion.run();
                return;
            } catch (WebDriverException | AssertionError exception) {
                try {
                    LOGGER.info("Assertion attempt = " + i + "failed, with exception " + exception.getMessage());
                    Thread.sleep(Duration.ofSeconds(timeoutInSeconds).toMillis());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        assertion.run();
    }

    public static String getParameter(String variableName, String defaultValue) {
        String env = System.getenv(variableName);
        if (env != null) {
            defaultValue = env;
        }
        String result = System.getProperty(variableName, defaultValue);
        if (isBlank(result)) {
            result = defaultValue;
        }

        return result;
    }

    public static boolean areScreenshotsMatching(Screenshot before, Screenshot after) {
        ImageDiff diff = new ImageDiffer().makeDiff(before, after);
        return diff.hasDiff();
    }
}
