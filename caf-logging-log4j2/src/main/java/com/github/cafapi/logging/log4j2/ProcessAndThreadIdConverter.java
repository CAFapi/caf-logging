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
package com.github.cafapi.logging.log4j2;

import com.github.cafapi.logging.common.ProcessAndThreadIdProvider;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

@Plugin(name = "ProcessAndThreadIdConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"pidtid"})
public final class ProcessAndThreadIdConverter extends LogEventPatternConverter
{
    private ProcessAndThreadIdConverter()
    {
        super("ProcessAndThreadIdConverter", "pidtid");
    }

    public static ProcessAndThreadIdConverter newInstance(final String[] options)
    {
        return new ProcessAndThreadIdConverter();
    }

    @Override
    public void format(final LogEvent event, final StringBuilder buffer)
    {
        buffer.append(ProcessAndThreadIdProvider.getId());
    }
}
