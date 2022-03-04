using Serilog.Core;
using Serilog.Events;
using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;

namespace nuget_sample_caf_logging
{
    class SimpleClassEnricher : ILogEventEnricher
    {
        public void Enrich(LogEvent logEvent, ILogEventPropertyFactory propertyFactory)
        {
            //Console.WriteLine("from enricher: " + logEvent.MessageTemplate);
            //Console.WriteLine("context " + logEvent.Properties["SourceContext"]);
            //var tenantId = "tenant[-\\.testCustom";
            //logEvent.AddOrUpdateProperty(propertyFactory.CreateProperty("Message", "test-replace"));
            //string message = logEvent.RenderMessage();
            //Console.WriteLine("test "+message);
            //string sanitizedMessage = sanitizeMessage(message);
            //Console.WriteLine("test2 " + isMessageSafeToLog(message));
            //if (!isMessageSafeToLog(sanitizedMessage))
            //{
            //    logEvent.AddOrUpdateProperty(propertyFactory.CreateProperty("Error", "unsafe to log"));
            //}
            //logEvent.AddOrUpdateProperty(propertyFactory.CreateProperty("Message", "test-replace"));
            //logEvent.AddOrUpdateProperty(propertyFactory.CreateProperty("CorrelationId", "correlsssation-custom"));
        }

        


    }
}
