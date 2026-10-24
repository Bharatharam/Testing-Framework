package com.framework.qa.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import org.testng.IAnnotationTransformer;
import org.testng.annotations.ITestAnnotation;

public class RetryTransformer implements IAnnotationTransformer {
    @Override
    public void transform(
            ITestAnnotation annotation, Class testClass, Constructor testConstructor, Method testMethod) {
        boolean enableRetry = Boolean.parseBoolean(ConfigReader.getProperty("ENABLE_RETRY", "false"));
        if (enableRetry) {
            annotation.setRetryAnalyzer(RetryAnalyzer.class);
        }
    }
}


