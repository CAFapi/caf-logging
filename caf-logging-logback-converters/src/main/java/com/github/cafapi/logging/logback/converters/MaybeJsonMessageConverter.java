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
package com.github.cafapi.logging.logback.converters;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.github.cafapi.logging.common.JsonFactoryCreator;
import com.github.cafapi.logging.common.LogMessageValidator;
import com.github.cafapi.logging.common.UnexpectedIOException;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.text.StrBuilder;

public final class MaybeJsonMessageConverter extends ThrowableHandlingConverter
{
    private static final JsonFactory jsonFactory = JsonFactoryCreator.create();
    public static final String HYPHEN = "-";
    private ClassicConverter throwableConverter;

    public MaybeJsonMessageConverter()
    {

    }

    @Override
    public void start()
    {
        final List<String> optionList = getOptionList();
        final String packages = getPackages(optionList);
        final String maxLines = getStacktraceLineLimit(optionList);

        if (null != packages || null != maxLines) {
            throwableConverter = new CustomThrowableProxyConverter(packages, maxLines);
        } else
        {
            throwableConverter = new RootCauseFirstThrowableProxyConverter();
        }
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

        if (throwableProxy == null && LogMessageValidator.isMessageSafeToLog(message)) {
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
    private String getStacktraceLineLimit(final List<String> optionList)
    {
        if (Objects.nonNull(optionList) && 1 < optionList.size()) {
            final String s = optionList.get(1);
            if (s.equals(HYPHEN)) {
                return null;
            }
        }
        return null;
    }

    private static String getPackages(final List<String> optionList)
    {
        if (Objects.nonNull(optionList) && !optionList.isEmpty()) {
            final String s = optionList.get(0);
            if (s.equals(HYPHEN)) {
                return null;
            }
            return s;
        }
        return null;
    }

}
