/**
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
﻿using Newtonsoft.Json;
using nuget_sample_caf_logging;
using Serilog.Core;
using Serilog.Events;
using Serilog.Parsing;
using System;
using System.Collections.Generic;
using System.Linq;

namespace caf_logging_serilog
{
    public class ConsoleWrapper : ILogEventSink, IDisposable
    {
        readonly ILogEventSink _wrappedSink;
        readonly MessageTemplateParser parser = new MessageTemplateParser();

        public ConsoleWrapper(ILogEventSink wrappedSink)
        {
            _wrappedSink = wrappedSink;
        }

        public void Emit(LogEvent logEvent)
        {

            MessageTemplate newMessageTemplate;
            var messageReceived = logEvent.MessageTemplate.Text;

            if (null == logEvent.Exception && LogSanitizer.IsMessageSafeToLog(messageReceived))
            {
                newMessageTemplate = parser.Parse(messageReceived);
            }
            else
            {
                LogExceptionItem exceptionItem = new LogExceptionItem()
                {
                    message = messageReceived,
                    Exception = logEvent.Exception
                };
                var newMessage = JsonConvert.SerializeObject(exceptionItem);
                newMessageTemplate = parser.Parse(newMessage);
            }

            var newLogEvent = new LogEvent(
                logEvent.Timestamp,
                logEvent.Level,
                logEvent.Exception,
                newMessageTemplate,
                logEvent.Properties
                    .Select(kvp => new LogEventProperty(kvp.Key, kvp.Value))
            );

            _wrappedSink.Emit(newLogEvent);
        }



        public void Dispose()
        {
            (_wrappedSink as IDisposable)?.Dispose();
        }


    }
}
