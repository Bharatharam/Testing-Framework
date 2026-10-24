package com.framework.qa.utils;

import com.framework.qa.context.TestContext;
import com.microsoft.playwright.Page;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PageFactory {
    private final Page page;
    private final Map<Class<?>, Object> cache = new ConcurrentHashMap<>();
    private TestContext testContext;

    public PageFactory(Page page) {
        this.page = page;
    }

    public <T> T getPage(Class<T> clazz) {
        return clazz.cast(
                cache.computeIfAbsent(
                        clazz,
                        c -> {
                            try {
                                return c.getConstructor(Page.class, TestContext.class)
                                        .newInstance(page, testContext);
                            } catch (InstantiationException
                                     | IllegalAccessException
                                     | InvocationTargetException
                                     | NoSuchMethodException e) {
                                throw new RuntimeException("Cannot create page instance: " + c.getName(), e);
                            }
                        }));
    }

    public void setTestContext(TestContext ctx) {
        this.testContext = ctx;
    }

    public TestContext getTestContext() {
        return testContext;
    }
}