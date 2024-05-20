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

public class FixedStackTraceThrowable extends Throwable {
    private static final StackTraceElement[] STACK_TRACE = new StackTraceElement[] {
        new StackTraceElement("org.junit.TestClass", "testMethod", "TestClass.java", 10),
        new StackTraceElement("com.example.MyClass", "myMethod", "MyClass.java", 20),
        new StackTraceElement("com.example.MyClass2", "myMethod2", "MyClass2.java", 21),
        new StackTraceElement("java.lang.Thread", "getStackTrace", "Thread.java", 30)
    };

    public FixedStackTraceThrowable(String message) {
        super(message);
        setStackTrace(STACK_TRACE);
    }
}
