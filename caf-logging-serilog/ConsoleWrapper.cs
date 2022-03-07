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

        public ConsoleWrapper(ILogEventSink wrappedSink)
        {
            _wrappedSink = wrappedSink;
        }

        public void Emit(LogEvent logEvent)
        {
            MessageTemplateParser parser = new MessageTemplateParser();
            MessageTemplate newMessageTemplate;
            string messageReceived = logEvent.MessageTemplate.Text;
            if (null == logEvent.Exception && LogSanitizer.IsMessageSafeToLog(messageReceived))
            {
                Console.WriteLine("safe to log " +LogSanitizer.IsMessageSafeToLog(messageReceived) + messageReceived ) ;
                newMessageTemplate = parser.Parse(messageReceived);
            }
            else {
                LogExceptionItem exceptionItem = new LogExceptionItem()
                {
                    message = messageReceived,
                    Exception = logEvent.Exception
                };
                string newMessage = JsonConvert.SerializeObject(exceptionItem);
                newMessageTemplate = parser.Parse(newMessage);
                Console.WriteLine("newMessagfe " + newMessage);
            }
            // Console.WriteLine("Message received: " + messageReceived);
           // string sanitizedMessage = LogSanitizer.SanitizeMessage(logEvent.MessageTemplate.Text);
            // Console.WriteLine("Message sanitized: " + sanitizedMessage);
            
            //Console.WriteLine(messageReceived);
            //Console.WriteLine("type " + logEvent.GetType());
            //Console.WriteLine("exception " + logEvent.Exception);
            //Console.WriteLine("level " + logEvent.Level);


            //if (!LogSanitizer.IsMessageSafeToLog(sanitizedMessage))
            //{
            //    newMessageTemplate = parser.Parse("Unsafe messages");
            //}
            //else { 
            //    newMessageTemplate = parser.Parse(sanitizedMessage);
            //}
            //IReadOnlyDictionary<string, LogEventPropertyValue> properties = logEvent.Properties;

            foreach (KeyValuePair<string, LogEventPropertyValue> kvp in logEvent.Properties)
            {
                Console.WriteLine("Key = {0}, Value = {1}", kvp.Key, kvp.Value);
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
