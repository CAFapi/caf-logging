using caf_logging_serilog;
using Serilog.Core;
using Serilog.Events;
using System.Collections.Generic;
using System.Linq;

namespace nuget_sample_caf_logging
{
    public class SanitizingEnricher : ILogEventEnricher
    {
        readonly string[] propertiesToCheck = new string[] { "tenantId", "correlationId", "logger" };

        public void Enrich(LogEvent logEvent, ILogEventPropertyFactory propertyFactory)
        {
            foreach (KeyValuePair<string, LogEventPropertyValue> kvp in logEvent.Properties)
            {
                if (propertiesToCheck.Any(kvp.Key.Contains))
                {
                    var value = kvp.Value.ToString();
                    var trimmedValue = value.Substring(1, value.Length - 2);

                    logEvent.AddOrUpdateProperty(propertyFactory.CreateProperty(kvp.Key, LogSanitizer.SanitizeMessage(trimmedValue)));
                }
            }
        }
    }
}
