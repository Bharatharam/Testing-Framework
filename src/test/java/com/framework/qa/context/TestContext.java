package com.framework.qa.context;

import com.framework.qa.utils.CsvDataManager;
import com.framework.qa.utils.PageFactory;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import java.util.Map;
import org.assertj.core.api.SoftAssertions;

public class TestContext {
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private PageFactory pageFactory;
    private SoftAssertions softly;

    // Optional test data map (CSV loading can be added later)
    private final CsvDataManager csvDataManager;
    private Map<String, String> testData;

    public TestContext() {
        this.csvDataManager = new CsvDataManager();
    }

    public <T> T getClassPage(Class<T> pageClass) {
        return getPageFactory().getPage(pageClass);
    }

    public PageFactory getPageFactory() {
        return pageFactory;
    }

    public void setPageFactory(PageFactory pageFactory) {
        this.pageFactory = pageFactory;
    }

    public Playwright getPlaywright() {
        return playwright;
    }

    public void setPlaywright(Playwright playwright) {
        this.playwright = playwright;
    }

    public Browser getBrowser() {
        return browser;
    }

    public void setBrowser(Browser browser) {
        this.browser = browser;
    }

    public BrowserContext getContext() {
        return context;
    }

    public void setContext(BrowserContext context) {
        this.context = context;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public SoftAssertions getSoftly() {
        return softly;
    }

    public void setSoftly(SoftAssertions softly) {
        this.softly = softly;
    }

    public CsvDataManager getCsvDataManager() {
        return csvDataManager;
    }

    public Map<String, String> getTestData() {
        return testData;
    }

    public void setTestData(Map<String, String> testData) {
        this.testData = testData;
    }
}