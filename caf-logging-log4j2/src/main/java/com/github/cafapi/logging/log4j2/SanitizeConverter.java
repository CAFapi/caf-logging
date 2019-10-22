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
package com.github.cafapi.logging.log4j2;

import java.util.List;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.PatternFormatter;
import org.apache.logging.log4j.core.pattern.PatternParser;

import com.github.cafapi.logging.common.LogMessageValidator;

@Plugin(name = "SanitizeConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "sanitize" })
public final class SanitizeConverter extends LogEventPatternConverter
{
    private final List<PatternFormatter> formatters;

    private SanitizeConverter(final List<PatternFormatter> formatters) {
        super("SanitizeConverter", "sanitize");
        this.formatters = formatters;
    }

    public static SanitizeConverter newInstance(final Configuration config, final String[] options) {
        if (options.length != 1) {
            LOGGER.error("Incorrect number of options on sanitize. Expected 1 received " + options.length);
            return null;
        }
        if (options[0] == null) {
            LOGGER.error("No pattern supplied on replace");
            return null;
        }
        final PatternParser parser = PatternLayout.createPatternParser(config);
        final List<PatternFormatter> formatters = parser.parse(options[0]);
        return new SanitizeConverter(formatters);
    }

    @Override
    public void format(final LogEvent event, final StringBuilder toAppendTo) {
        final StringBuilder buf = new StringBuilder();
        for (final PatternFormatter formatter : formatters) {
            formatter.format(event, buf);
        }
        toAppendTo.append(LogMessageValidator.DISALLOWED_CHARACTERS.matcher(buf.toString()).replaceAll(LogMessageValidator.REPLACE_CHARACTER));
    }
}
