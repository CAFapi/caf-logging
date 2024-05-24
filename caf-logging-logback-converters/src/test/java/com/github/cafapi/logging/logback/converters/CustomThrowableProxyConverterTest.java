/*
 * Copyright 2019-2024 Open Text.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cafapi.logging.logback.converters;

import java.util.Arrays;

import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.CoreConstants;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CustomThrowableProxyConverterTest
{
    private TestableCustomThrowableProxyConverter converter;

    @Before
    public void setUp()
    {
        // Reset the converter before each test
        converter = null;
    }

    @Test
    public void testNoFilteringNoLimitation()
    {
        converter = new TestableCustomThrowableProxyConverter(null, null);
        final Exception exception = new Exception("Test1 exception");
        final IThrowableProxy tp = new ThrowableProxy(exception);

        final String result = converter.throwableProxyToStringPublic(tp);

        Assert.assertTrue(result.contains("Test1 exception"));
        Assert.assertTrue(result.contains("CustomThrowableProxyConverterTest.testNoFilteringNoLimitation"));
    }

    @Test
    public void testPackageFiltering()
    {
        converter = new TestableCustomThrowableProxyConverter("org.junit", "10");
        final Throwable throwable = new FixedStackTraceThrowable("Test exception");
        final IThrowableProxy tp = new ThrowableProxy(throwable);

        final String result = converter.throwableProxyToStringPublic(tp);

        Assert.assertTrue(result.contains("Test exception"));

        final String[] lines = result.split(CoreConstants.LINE_SEPARATOR);
        Assert.assertFalse(Arrays.stream(lines)
            .skip(1) // Skip the first line as it contains the exception message
            .anyMatch(line -> line.contains("java.lang")));
        Assert.assertTrue(Arrays.stream(lines)
            .skip(1) // Skip the first line as it contains the exception message
            .allMatch(line -> line.contains("org.junit")));
    }

    @Test
    public void testMaxStackTraceSize()
    {
        converter = new TestableCustomThrowableProxyConverter(null, "2");
        final Throwable throwable = new FixedStackTraceThrowable("Test exception");
        final IThrowableProxy tp = new ThrowableProxy(throwable);

        final String result = converter.throwableProxyToStringPublic(tp);

        Assert.assertTrue(result.contains("Test exception"));
        // We expect only 2 stack trace lines
        final int stackTraceLineCount = (int) Arrays.stream(result.split("\\R")).filter(line -> line.contains("at")).count();
        Assert.assertEquals(2, stackTraceLineCount);
    }

    @Test
    public void testPackageFilteringAndMaxStackTraceSize()
    {
        converter = new TestableCustomThrowableProxyConverter("com.example", "1");
        final Throwable throwable = new FixedStackTraceThrowable("Test exception");
        final IThrowableProxy tp = new ThrowableProxy(throwable);

        final String result = converter.throwableProxyToStringPublic(tp);

        Assert.assertTrue(result.contains("Test exception"));
        // We expect only 2 stack trace lines
        final int stackTraceLineCount = (int) Arrays.stream(result.split("\\R")).filter(line -> line.contains("at")).count();
        Assert.assertEquals(1, stackTraceLineCount);
    }

    @Test
    public void testPackageFilteringAndNullMaxStackTraceSize()
    {
        converter = new TestableCustomThrowableProxyConverter("com.example;java.lang", null);
        final Throwable throwable = new FixedStackTraceThrowable("Test exception");
        final IThrowableProxy tp = new ThrowableProxy(throwable);

        final String result = converter.throwableProxyToStringPublic(tp);

        Assert.assertTrue(result.contains("Test exception"));
        // We expect only 2 stack trace lines
        final int stackTraceLineCount = (int) Arrays.stream(result.split("\\R")).filter(line -> line.contains("at")).count();
        //2 entries from com.example
        //1 entry from java.lang
        Assert.assertEquals(3, stackTraceLineCount);
    }

    @Test
    public void testInvalidMaxLinesParameter()
    {
        converter = new TestableCustomThrowableProxyConverter(null, "invalid");
        final Throwable throwable = new FixedStackTraceThrowable("Test exception");
        final IThrowableProxy tp = new ThrowableProxy(throwable);

        final String result = converter.throwableProxyToStringPublic(tp);

        Assert.assertTrue(result.contains("Test exception"));
        // We expect all stack trace lines because the maxLines parameter is invalid
        final int stackTraceLineCount = (int) Arrays.stream(result.split("\\R")).filter(line -> line.contains("at")).count();
        Assert.assertTrue(2 < stackTraceLineCount); // assuming the stack trace contains more than 2 lines
    }
}
