/*
 * Copyright 2019-2020 Micro Focus or one of its affiliates.
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

import ch.qos.logback.core.pattern.CompositeConverter;

import com.github.cafapi.logging.common.LogMessageValidator;

public final class SanitizeConverter<E> extends CompositeConverter<E>
{

    @Override
    protected String transform(final E event, final String in)
    {
        return LogMessageValidator.sanitizeMessage(in);
    }
}
