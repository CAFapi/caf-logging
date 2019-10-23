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
package com.github.cafapi.logging.common;

import java.util.regex.Pattern;

public final class LogMessageValidator
{
    public static final String PROCESS_ID_FORMAT = "#%s.%03d";
    private static final Pattern DISALLOWED_CHARACTERS;

    static {
        // Matches all characters except for alphanumeric characters, underscores, dashes, and periods.
        DISALLOWED_CHARACTERS = Pattern.compile("[^\\w-.]");
    }

    private LogMessageValidator()
    {
    }

    /**
     * Conservatively determines if the message is safe to log without being encoded.
     * <p>
     * Note: This method also returns false if the message starts with a curly bracket (to make it easy to determine if we're falling back
     * to JSON-encoding).
     * @param message Message to validate
     * @return true if the message has only the allowed characters
     */
    public static boolean isMessageSafeToLog(final String message)
    {
        if (message.isEmpty()) {
            return true;
        } else if (message.charAt(0) == '{') {
            return false;
        } else {
            return message.chars().allMatch(c -> c >= 0x20 && c < 0x7F);
        }
    }

    public static String sanitizeMessage(final String message)
    {
        return LogMessageValidator.DISALLOWED_CHARACTERS.matcher(message).replaceAll("");
    }
}
