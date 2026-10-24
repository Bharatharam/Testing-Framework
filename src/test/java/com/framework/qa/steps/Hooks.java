package com.framework.qa.steps;

import com.framework.qa.context.TestContext;
import com.framework.qa.utils.BrowserFactory;
import com.framework.qa.utils.ConfigReader;
import com.framework.qa.utils.PageFactory;
import com.framework.qa.utils.PlaywrightManager;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Tracing;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.assertj.core.api.SoftAssertions;

public class Hooks extends PlaywrightManager {
    private final TestContext testContext;
    private String scenarioName;

    public Hooks(TestContext testContext) {
        this.testContext = testContext;
    }

    @Before(order = 0)
    public void setup(Scenario scenario) {
        scenarioName = scenario.getName().replaceAll("\\s+", "_");
        System.out.println(
                "[Hooks] Starting scenario: " + scenarioName + " on env=" + ConfigReader.getEnvironment());
        testContext.setPlaywright(getPlaywright());
        Browser browser = BrowserFactory.createBrowser(testContext.getPlaywright());
        testContext.setBrowser(browser);
        testContext.setContext(
                browser.newContext(new Browser.NewContextOptions().setViewportSize(null)));
        testContext
                .getContext()
                .tracing()
                .start(new Tracing.StartOptions().setScreenshots(true).setSnapshots(true).setSources(true));
        testContext.setPage(testContext.getContext().newPage());
        testContext
                .getPage()
                .setDefaultTimeout(
                        Double.parseDouble(ConfigReader.getProperty("DEFAULT_TIMEOUT", "30000")));
        testContext.setSoftly(new SoftAssertions());
        PageFactory pf = new PageFactory(testContext.getPage());
        pf.setTestContext(testContext);
        testContext.setPageFactory(pf);
    }

    @After
    public void teardown(Scenario scenario) {
        if (testContext.getContext() == null) return;
        Path tracePath = Paths.get("traces", scenarioName + "_trace.zip");
        testContext.getContext().tracing().stop(new Tracing.StopOptions().setPath(tracePath));
        if (scenario.isFailed()) {
            byte[] screenshot = testContext.getPage().screenshot();
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
            try {
                byte[] traceBytes = Files.readAllBytes(tracePath);
                scenario.attach(traceBytes, "application/zip", "Playwright Trace");
            } catch (IOException ignored) {
            }
        }
        try {
            testContext.getSoftly().assertAll();
        } catch (AssertionError e) {
            scenario.attach(e.getMessage().getBytes(), "text/plain", "Soft Assertions Failures");
        }
        testContext.getPage().close();
        testContext.getContext().close();
        testContext.getBrowser().close();
        closePlaywright();
    }
}
