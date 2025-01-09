/*
 * Copyright 2019-2025 Open Text.
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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.github.cafapi.logging.common.JsonFactoryCreator;
import com.github.cafapi.logging.common.LogMessageValidator;
import com.github.cafapi.logging.common.UnexpectedIOException;
import java.io.IOException;
import org.apache.commons.text.StrBuilder;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.ThrowableProxy;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.PatternConverter;
import org.apache.logging.log4j.core.pattern.RootThrowablePatternConverter;
import org.apache.logging.log4j.core.pattern.ThrowablePatternConverter;

@Plugin(name = "MaybeJsonMessageConverter", category = PatternConverter.CATEGORY)
@ConverterKeys({"maybeJsonMsgAndEx"})
public final class MaybeJsonMessageConverter extends ThrowablePatternConverter
{
    private static final JsonFactory jsonFactory = JsonFactoryCreator.create();

    private final RootThrowablePatternConverter throwableConverter;

    private MaybeJsonMessageConverter(final Configuration configuration, final String[] options)
    {
        super("MaybeJsonMessageConverter", "throwable", options, configuration);
        this.throwableConverter = RootThrowablePatternConverter.newInstance(configuration, options);
    }

    @Override
    public void format(final LogEvent event, final StringBuilder buffer)
    {
        final ThrowableProxy throwableProxy = event.getThrownProxy();
        final String message = event.getMessage().getFormattedMessage();

        if (throwableProxy == null && LogMessageValidator.isMessageSafeToLog(message)) {
            buffer.append(message);
        } else {
            final StrBuilder sb = new StrBuilder();
            try (final JsonGenerator jsonBuilder = jsonFactory.createGenerator(sb.asWriter())) {
                jsonBuilder.writeStartObject();
                jsonBuilder.writeStringField("message", message);
                if (throwableProxy != null) {
                    final StringBuilder exceptionBuffer = new StringBuilder();
                    throwableConverter.format(event, exceptionBuffer);
                    jsonBuilder.writeStringField("exception", exceptionBuffer.toString());
                }
                jsonBuilder.writeEndObject();
            } catch (final IOException ex) {
                throw new UnexpectedIOException(ex);
            }
            buffer.append(sb.toString());
        }
    }

    public static MaybeJsonMessageConverter newInstance(
        final Configuration configuration,
        final String[] options
    )
    {
        return new MaybeJsonMessageConverter(configuration, options);
    }
}
