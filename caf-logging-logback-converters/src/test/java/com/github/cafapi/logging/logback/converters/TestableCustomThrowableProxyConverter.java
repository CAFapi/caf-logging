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

import ch.qos.logback.classic.spi.IThrowableProxy;

class TestableCustomThrowableProxyConverter extends CustomThrowableProxyConverter {

    public TestableCustomThrowableProxyConverter(final String packages, final String maxLines) {
        super(packages, maxLines);
    }

    public String throwableProxyToStringPublic(final IThrowableProxy tp) {
        return throwableProxyToString(tp);
    }
}
