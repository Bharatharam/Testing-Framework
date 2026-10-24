package com.framework.qa.base;

import com.framework.qa.context.TestContext;
import com.framework.qa.utils.ConfigReader;
import com.framework.qa.utils.CustomAssertions;
import com.microsoft.playwright.Page;

public class BasePage extends CustomAssertions {
    protected final Page page;
    protected final TestContext testContext;
    protected final String baseUrl;

    public BasePage(Page page, TestContext testContext) {
        super(testContext.getSoftly());
        this.page = page;
        this.testContext = testContext;
        this.baseUrl = ConfigReader.getProperty("BASE_URL");
    }

    protected void navigate(String path) {
        page.navigate(path);
    }

    protected void navigateToBase() {
        page.navigate(baseUrl);
    }
}