package com.framework.qa.utils;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.assertions.LocatorAssertions;
import org.assertj.core.api.SoftAssertions;

public class CustomAssertions {
    private final LocatorAssertions.IsVisibleOptions visibleOptions =
            new LocatorAssertions.IsVisibleOptions().setTimeout(10000);
    private final LocatorAssertions.IsHiddenOptions hiddenOptions =
            new LocatorAssertions.IsHiddenOptions().setTimeout(10000);
    private final SoftAssertions softly;

    public CustomAssertions(SoftAssertions softly) {
        this.softly = softly;
    }

    public void assertVisible(Locator locator) {
        assertThat(locator).isVisible(visibleOptions);
    }

    public void assertHidden(Locator locator) {
        assertThat(locator).isHidden(hiddenOptions);
    }

    public void assertTextEquals(Locator locator, String expected) {
        String actual = locator.textContent() == null ? "" : locator.textContent().trim();
        if (!expected.equals(actual)) {
            softly.fail("Expected text '" + expected + "' but found '" + actual + "'");
        }
    }

    public void flush() {
        softly.assertAll();
    }
}

