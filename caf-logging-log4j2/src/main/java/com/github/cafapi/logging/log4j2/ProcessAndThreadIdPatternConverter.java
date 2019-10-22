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

import com.github.cafapi.logging.common.LogMessageValidator;
import com.hpe.caf.util.processidentifier.ProcessIdentifier;
import java.util.Locale;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;
import org.apache.logging.log4j.core.pattern.PatternConverter;

@Plugin(name = "ProcessAndThreadIdPatternConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({ "aProcessId" })
public final class ProcessAndThreadIdPatternConverter extends LogEventPatternConverter
{

    private static final String PROCESS_ID;
    private final ThreadLocal<String> threadIds;

    static {
        PROCESS_ID = ProcessIdentifier.getProcessId().toString().substring(0, 3);
    }

    private ProcessAndThreadIdPatternConverter() {
        super("ProcessAndThreadIdPatternConverter", "aProcessId");
        this.threadIds = ThreadLocal.withInitial(ProcessAndThreadIdPatternConverter::getThreadName);
    }

    private static String getThreadName()
    {
        final long threadId = Thread.currentThread().getId();
        return String.format(Locale.ENGLISH, LogMessageValidator.PROCESS_ID_FORMAT, PROCESS_ID, threadId);
    }

    public static ProcessAndThreadIdPatternConverter newInstance(final String[] options) {
        return new ProcessAndThreadIdPatternConverter();
    }

    @Override
    public void format(final LogEvent event, final StringBuilder buffer) {
        buffer.append(threadIds.get());
    }
}
