package com.framework.qa.runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = "src/test/resources/features/base",
        glue = {"com.framework.qa.steps"},
        plugin = {"pretty", "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"},
        tags = "@Smoke or @Regression")
public class TestRunner extends AbstractTestNGCucumberTests {
    private static final int THREADS = Integer.getInteger("threads", 2);

    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        // If threads=1, avoid parallel execution overhead
        if (THREADS <= 1) {
            return new AbstractTestNGCucumberTests() {
                @Override
                @DataProvider(parallel = false)
                public Object[][] scenarios() {
                    return super.scenarios();
                }
            }.scenarios();
        }
        return super.scenarios();
    }
}


