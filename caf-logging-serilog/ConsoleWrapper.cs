using Newtonsoft.Json;
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
            string messageReceived = logEvent.MessageTemplate.Text;

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
                string newMessage = JsonConvert.SerializeObject(exceptionItem);
                newMessageTemplate = parser.Parse(newMessage);
            }

            LogEvent newLogEvent = new LogEvent(
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
