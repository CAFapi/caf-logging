/*
 * Copyright 2019-2023 Open Text.
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
package com.github.cafapi.logging.common;

import com.hpe.caf.util.processidentifier.ProcessIdentifier;
import java.util.Locale;

public final class ProcessAndThreadIdProvider
{
    private static final String PROCESS_ID;
    private static final ThreadLocal<String> threadIds;

    static {
        PROCESS_ID = ProcessIdentifier.getProcessId().toString().substring(0, 3);
        threadIds = ThreadLocal.withInitial(ProcessAndThreadIdProvider::getThreadName);
    }

    private ProcessAndThreadIdProvider()
    {
    }

    private static String getThreadName()
    {
        final long threadId = Thread.currentThread().getId();
        return String.format(Locale.ENGLISH, "#%s.%03d", PROCESS_ID, threadId);
    }

    public static String getId()
    {
        return threadIds.get();
    }
}
