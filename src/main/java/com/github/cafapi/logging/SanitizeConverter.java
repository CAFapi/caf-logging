/*
 * Copyright 2019 Micro Focus or one of its affiliates.
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
package com.github.cafapi.logging;

import ch.qos.logback.core.pattern.CompositeConverter;
import java.util.regex.Pattern;

public final class SanitizeConverter<E> extends CompositeConverter<E>
{
    private static final Pattern DISALLOWED_CHARACTERS;

    static {
        // Matches all characters except for alphanumeric characters, underscores, dashes, and periods.
        DISALLOWED_CHARACTERS = Pattern.compile("[^\\w-.]");
    }

    @Override
    protected String transform(final E event, final String in)
    {
        return DISALLOWED_CHARACTERS.matcher(in).replaceAll("");
    }
}
