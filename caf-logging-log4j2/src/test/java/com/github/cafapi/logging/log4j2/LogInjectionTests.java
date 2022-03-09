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
package com.github.cafapi.logging.log4j2;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import org.junit.Assert;
import org.junit.Test;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class LogInjectionTests
{
    //@Test
    public void testLogMessageWithNewline() throws Exception
    {
        // Use tenant and correlation ids that have newlines in them
        ThreadContext.put("tenantId", "acme-\ncorp");
        ThreadContext.put("correlationId", "6\nafb775c-bf4d-11e9-9cb5-2a2ae2dbcce4");

        // Messages to be logged
        final String messageLine1 = "This is the first line of the message";
        final String messageLine2 = "This is the second line of the message";
        final String combinedMessageLine = messageLine1 + "\n" + messageLine2;

        // Log the messages
        final ByteArrayOutputStream logStream = new ByteArrayOutputStream();
        try (final StdErrRedirector redirector = new StdErrRedirector(logStream)) {
            final Logger logger = LogManager.getLogger("LogInjection\nTests");
            logger.info(combinedMessageLine);
            logger.info("There are {} green bottles sitting on the wall.", 10);
        }

        // Read the log into a string
        final String log = utf8Decode(logStream);
        final long logLineCount = countLines(log);

        // Assert that the message lines are in the log
        Assert.assertThat(log, containsString(messageLine1));
        Assert.assertThat(log, containsString(messageLine2));

        // Assert that the combined message line isn't in the log
        // (because that would mean that the newline hasn't been removed or encoded)
        Assert.assertThat(log, not(containsString(combinedMessageLine)));

        // Assert that the format arguments have been inserted as expected
        Assert.assertThat(log, containsString("10 green bottles"));

        // Assert that there has been 1 line logged for each log message
        Assert.assertThat(logLineCount, is(2L));
    }

    @Test
    public void testLogExceptionMessage() throws Exception
    {
        // Use tenant and correlation ids that have newlines in them
        ThreadContext.put("tenantId", "acme-corp");
        ThreadContext.put("correlationId", "ca480711-46d7-4d8b-8e74-4f98e2bf4a0b");

        // Exception message to be logged
        final String exceptionMsg = "Division by zero has no meaning";

        // Log the exception
        final ByteArrayOutputStream logStream = new ByteArrayOutputStream();
        try (final StdErrRedirector redirector = new StdErrRedirector(logStream)) {
            final Logger logger = LogManager.getLogger("LogInjectionTests");

            try {
                final int fiveDivideByZero = 5 / 0;
            } catch (final ArithmeticException ex) {
                logger.error(exceptionMsg, ex);
            }
        }

        // Read the log into a string
        final String log = utf8Decode(logStream);
        final long logLineCount = countLines(log);

        // Assert that the exception message is in the log
        Assert.assertThat(log, containsString(exceptionMsg));

        // Assert that the function name is in the log (as that probably means that the stack trace is present)
        Assert.assertThat(log, containsString("testLogExceptionMessage"));

        // Assert that there has been just 1 line logged
        Assert.assertThat(logLineCount, is(1L));
    }

    private static final class StdErrRedirector implements AutoCloseable
    {
        private final PrintStream originalStdErr;

        public StdErrRedirector(final OutputStream out)
        {
            this.originalStdErr = System.err;
            System.setErr(new PrintStream(out));
        }

        @Override
        public void close()
        {
            System.err.flush();
            System.setErr(originalStdErr);
        }
    }

    private static String utf8Decode(final ByteArrayOutputStream stream) throws CharacterCodingException
    {
        // 'final String log = logStream.toString("UTF-8");' would be simplier but I want an exception to be thrown if there are any
        //  invalid UTF-8 sequences
        return StandardCharsets.UTF_8.newDecoder().decode(ByteBuffer.wrap(stream.toByteArray())).toString();
    }

    private static long countLines(final String str)
    {
        return str.chars().filter(c -> c == '\n').count();
    }
}
