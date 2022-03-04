
using Serilog.Core;
using Serilog.Events;
using System;
using System.Text.RegularExpressions;

namespace nuget_sample_caf_logging
{

    public class SerilogCustomSink : ILogEventSink
    {
        //private static int i = 0;
        private const String DefaultTemplate = "[{Timestamp:yyyy-MM-dd HH:mm:ss} {ProcessId}.{ThreadId} {Level:u4} {TenantId} {CorrelationId}] [{SourceContext}] {Message}{NewLine}{Exception}";

        private readonly IFormatProvider _formatProvider;

        public object Enrich { get; internal set; }

        public SerilogCustomSink(IFormatProvider formatProvider)
        {
            _formatProvider = formatProvider;
        }

        public void Emit(LogEvent logEvent)
        {

            //Console.WriteLine(DefaultTemplate, "eventw: " + logEvent.Level);
            //Console.WriteLine("eventw: " + logEvent.MessageTemplate);
            // LogEventPropertyValue valu1 = new LogEventPropertyValue();
            //LogEventProperty prop1 = new LogEventProperty("outputTemplate", null);
            //logEvent.AddOrUpdateProperty(prop1);
            //var message = logEvent.RenderMessage(_formatProvider);
            //Console.WriteLine(DateTimeOffset.Now.ToString() + " " + message);
            //Console.WriteLine("outputTemplate", DefaultTemplate);
            //var processId = "processId-test";
            //Console.ForegroundColor = ConsoleColor.Black;
            // string message = logEvent.RenderMessage(_formatProvider);
            // Console.WriteLine("gg" + String.Format(DefaultTemplate, logEvent.RenderMessage(_formatProvider)));
            //Console.ForegroundColor = ConsoleColor.Black;
            //string message = logEvent.RenderMessage(_formatProvider);
            String formatted = logEvent.RenderMessage(_formatProvider);
            //Decimal pricePerOunce = 17.36m;
            string name = "";
            var ProcessId = "";
            var ThreadId = "";
            Console.WriteLine($"Hello, {name}! Today is {ProcessId}.{ThreadId}");
            Console.ResetColor();
            //Console.ResetColor();
            //Console.Error.WriteLine(String.Format(DefaultTemplate, message));
        }

        //public void Enrich(LogEvent logEvent, ILogEventPropertyFactory propertyFactory)
        //{
        //    var tenantId = "tenantTest";
        //    var correlationId = "Correlation test";

        //    logEvent.AddOrUpdateProperty(propertyFactory.CreateProperty("TenantId", "dsdfsfgf"));
        //    logEvent.AddOrUpdateProperty(propertyFactory.CreateProperty("CorrelationId", "dss"));
        //}

        public string sanitizeMessage(string message)
        {
            Regex pattern = new Regex("[^\\w-.]");
            return pattern.Replace(message, "");
        }


        public bool isMessageSafeToLog(string message) {

            if (string.IsNullOrWhiteSpace(message)){
                return true;
            }
            if (message.StartsWith("{")) {
                return false;
            }

            // Makes sure that each character is ASCII printable
            // http://www.csc.villanova.edu/~tway/resources/ascii-table.html
            foreach (char c in message)
            {
                if (!Char.IsControl(c)) return false;
            }
            return true;
        }
    }
}

