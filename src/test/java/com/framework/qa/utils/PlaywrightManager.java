package com.framework.qa.utils;

import com.microsoft.playwright.Playwright;

public abstract class PlaywrightManager {
    private static final ThreadLocal<Playwright> playwrightThread = new ThreadLocal<>();

    protected static Playwright getPlaywright() {
        if (playwrightThread.get() == null) {
            playwrightThread.set(Playwright.create());
        }
        return playwrightThread.get();
    }

    protected static void closePlaywright() {
        Playwright p = playwrightThread.get();
        if (p != null) {
            p.close();
            playwrightThread.remove();
        }
    }
}

