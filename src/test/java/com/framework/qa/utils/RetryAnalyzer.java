package com.framework.qa.utils;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int retryCount = 0;
    private static final int MAX = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess() && retryCount < MAX) {
            retryCount++;
            System.out.println("[Retry] Attempt " + retryCount + " for test: " + result.getName());
            return true;
        }
        return false;
    }
}