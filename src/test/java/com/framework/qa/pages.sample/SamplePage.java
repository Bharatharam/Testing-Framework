package com.framework.qa.pages.sample;

import com.framework.qa.base.BasePage;
import com.framework.qa.context.TestContext;
import com.microsoft.playwright.Page;
import io.qameta.allure.Step;


public class SamplePage extends BasePage {
    public SamplePage(Page page, TestContext testContext) {
        super(page, testContext);
    }

    @Step("Open Sample Page")
    public void open() {
        navigateToBase();
    }

    @Step("Validate sample page loaded")
    public void validateLoaded() {
        testContext.getSoftly().assertThat(page.url()).as("URL").isEqualTo(baseUrl);
    }
}
