using Serilog.Core;
using Serilog.Events;
using Serilog.Parsing;
using System;
using System.Linq;

namespace caf_logging_serilog
{ 
    public class ConsoleWrapper : ILogEventSink, IDisposable
    {
        readonly ILogEventSink _wrappedSink;

        public ConsoleWrapper(ILogEventSink wrappedSink)
        {
            _wrappedSink = wrappedSink;
        }

        public void Emit(LogEvent logEvent)
        {
            string messageReceived = logEvent.MessageTemplate.Text;
            // Console.WriteLine("Message received: " + messageReceived);
            string sanitizedMessage = LogSanitizer.SanitizeMessage(logEvent.MessageTemplate.Text);
            // Console.WriteLine("Message sanitized: " + sanitizedMessage);
            MessageTemplateParser parser = new MessageTemplateParser();
            MessageTemplate newMessageTemplate;

            if (!LogSanitizer.IsMessageSafeToLog(sanitizedMessage))
            {
               newMessageTemplate = parser.Parse("Unsafe message");
            }
            else { 
                newMessageTemplate = parser.Parse(sanitizedMessage);
            }

            LogEvent newLogEvent = new LogEvent(
                logEvent.Timestamp,
                logEvent.Level,
                logEvent.Exception,
                newMessageTemplate,
                logEvent.Properties
                    .Select(kvp => new LogEventProperty(kvp.Key, kvp.Value)));

            _wrappedSink.Emit(newLogEvent);
        }

        public void Dispose()
        {
            (_wrappedSink as IDisposable)?.Dispose();
        }

       
    } 
}
