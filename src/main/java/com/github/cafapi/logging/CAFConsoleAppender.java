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

import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import com.hpe.caf.util.processidentifier.ProcessIdentifier;
import java.util.Locale;

public final class CAFConsoleAppender extends ConsoleAppender<LoggingEvent>
{
    private static final String PROCESS_ID;
    private final ThreadLocal<String> threadIds;

    static {
        PROCESS_ID = ProcessIdentifier.getProcessId().toString().substring(0, 3);
    }

    public CAFConsoleAppender()
    {
        this.threadIds = ThreadLocal.withInitial(CAFConsoleAppender::getThreadName);
    }

    @Override
    protected void subAppend(final LoggingEvent event)
    {
        event.setThreadName(threadIds.get());
        super.subAppend(event);
    }

    private static String getThreadName()
    {
        final long threadId = Thread.currentThread().getId();
        return String.format(Locale.ENGLISH, "#%s.%03d", PROCESS_ID, threadId);
    }
}