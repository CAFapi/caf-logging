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

import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxyUtil;
import ch.qos.logback.core.CoreConstants;

import java.util.Arrays;
import java.util.List;

public class CustomThrowableProxyConverter extends RootCauseFirstThrowableProxyConverter {
    private List<String> filteredPackages;
    private int maxStackTraceSize;

    public CustomThrowableProxyConverter(final String packages, final String maxLines) {
        if (null != packages && !packages.isEmpty()) {
            filteredPackages = Arrays.asList(packages.split(";"));
        }
        try {
            maxStackTraceSize = Integer.parseInt(maxLines);
        } catch (final NumberFormatException e) {
            maxStackTraceSize = Integer.MAX_VALUE; // default behavior to print all lines if parsing fails
        }
    }

    @Override
    protected void subjoinSTEPArray(final StringBuilder buf, final int indent, final IThrowableProxy tp) {
        final StackTraceElementProxy[] stepArray = tp.getStackTraceElementProxyArray();
        int stackTracePrinted = 0;

        for (int i = 0; i < stepArray.length && stackTracePrinted < maxStackTraceSize; i++) {
            final StackTraceElementProxy step = stepArray[i];
            final StackTraceElement ste = step.getStackTraceElement();

            if (isIncludedPackage(ste.getClassName())) {
                if (0 < stackTracePrinted) {
                    buf.append(CoreConstants.LINE_SEPARATOR);
                }
                ThrowableProxyUtil.indent(buf, indent);
                buf.append(step);
                stackTracePrinted++;
            }
        }

        if (0 < tp.getCommonFrames() && stackTracePrinted < maxStackTraceSize) {
            ThrowableProxyUtil.indent(buf, indent);
            buf.append("... ").append(tp.getCommonFrames()).append(" more");
            buf.append(CoreConstants.LINE_SEPARATOR);
        }
    }

    private boolean isIncludedPackage(final String className) {
        if (null == filteredPackages || filteredPackages.isEmpty()) {
            return true; // No filtering applied, include all packages
        }
        return filteredPackages.stream().anyMatch(className::startsWith);
    }
}
