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

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonGenerator.Feature;
import java.io.IOException;
import org.apache.commons.text.StrBuilder;

public final class MaybeJsonMessageConverter extends ThrowableHandlingConverter
{
    private static final JsonFactory jsonFactory = createJsonFactory();
    private final ClassicConverter throwableConverter;

    public MaybeJsonMessageConverter()
    {
        this.throwableConverter = new RootCauseFirstThrowableProxyConverter();
    }

    @Override
    public void start()
    {
        throwableConverter.start();
        super.start();
    }

    @Override
    public void stop()
    {
        super.stop();
        throwableConverter.stop();
    }

    @Override
    public String convert(final ILoggingEvent event)
    {
        final IThrowableProxy throwableProxy = event.getThrowableProxy();
        final String message = event.getFormattedMessage();

        if (throwableProxy == null && isMessageSafeToLog(message)) {
            return message;
        } else {
            final StrBuilder sb = new StrBuilder();
            try (final JsonGenerator jsonBuilder = jsonFactory.createGenerator(sb.asWriter())) {
                jsonBuilder.writeStartObject();
                jsonBuilder.writeStringField("message", message);
                if (throwableProxy != null) {
                    jsonBuilder.writeStringField("exception", throwableConverter.convert(event));
                }
                jsonBuilder.writeEndObject();
            } catch (final IOException ex) {
                throw new UnexpectedIOException(ex);
            }

            return sb.toString();
        }
    }

    /**
     * Conservatively determines if the message is safe to log without being encoded.
     * <p>
     * Note: This method also returns false if the message starts with a curly bracket (to make it easy to determine if we're falling back
     * to JSON-encoding).
     */
    private static boolean isMessageSafeToLog(final String message)
    {
        if (message.isEmpty()) {
            return true;
        } else if (message.charAt(0) == '{') {
            return false;
        } else {
            return message.chars().allMatch(c -> c >= 0x20 && c < 0x7F);
        }
    }

    private static JsonFactory createJsonFactory()
    {
        final JsonFactory newJsonFactory = new JsonFactory();
        newJsonFactory.disable(Feature.AUTO_CLOSE_JSON_CONTENT);
        newJsonFactory.disable(Feature.AUTO_CLOSE_TARGET);
        newJsonFactory.disable(Feature.FLUSH_PASSED_TO_STREAM);

        return newJsonFactory;
    }

    private static final class UnexpectedIOException extends RuntimeException
    {
        public UnexpectedIOException(final IOException ex)
        {
        }
    }
}
