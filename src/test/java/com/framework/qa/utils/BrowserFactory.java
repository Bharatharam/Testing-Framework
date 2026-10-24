package com.framework.qa.utils;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Playwright;
import java.util.Collections;

public class BrowserFactory {
    private static final String HEADLESS = "HEADLESS";
    private static final String BROWSER_NAME = "BROWSER_NAME";
    private static final String DESKTOP_BROWSER = "DESKTOP_BROWSER";

    public static Browser createBrowser(Playwright playwright) {
        boolean headless = Boolean.parseBoolean(ConfigReader.getProperty(HEADLESS, "true"));
        String browserName = ConfigReader.getProperty(BROWSER_NAME, "chromium").toLowerCase();
        boolean desktopBrowser =
                Boolean.parseBoolean(ConfigReader.getProperty(DESKTOP_BROWSER, "false"));
        BrowserType.LaunchOptions launchOptions =
                new BrowserType.LaunchOptions()
                        .setHeadless(headless)
                        .setArgs(Collections.singletonList("--start-maximized"));
        String channel = desktopBrowser ? getChannel(browserName) : null;
        switch (browserName) {
            case "firefox":
                return playwright
                        .firefox()
                        .launch(channel != null ? launchOptions.setChannel(channel) : launchOptions);
            case "webkit":
                return playwright.webkit().launch(launchOptions);
            case "edge":
                return playwright.chromium().launch(launchOptions.setChannel("msedge"));
            default:
                return playwright
                        .chromium()
                        .launch(channel != null ? launchOptions.setChannel(channel) : launchOptions);
        }
    }

    private static String getChannel(String browserName) {
        switch (browserName) {
            case "firefox":
                return "firefox";
            case "edge":
                return "msedge";
            case "chromium":
                return "chrome";
            default:
                return null;
        }
    }
}


