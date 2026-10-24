package com.framework.qa.steps.sample;

import com.framework.qa.context.TestContext;
import com.framework.qa.pages.sample.SamplePage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class SampleSteps {
    private final SamplePage samplePage;

    public SampleSteps(TestContext testContext) {
        this.samplePage = testContext.getClassPage(SamplePage.class);
    }

    @Given("the framework is initialized")
    public void framework_initialized() {
        /* initialization handled in Hooks */
    }

    @When("I open the sample page")
    public void i_open_sample_page() {
        samplePage.open();
    }

    @Then("I see the sample page loaded")
    public void i_see_sample_page_loaded() {
        samplePage.validateLoaded();
    }
}

