using Serilog.Core;
using Serilog.Events;
using System;
using System.Collections.Generic;
using System.Text;
using System.Text.RegularExpressions;

namespace caf_logging_serilog
{
    class SimpleClassEnricher : ILogEventEnricher
    {
        public void Enrich(LogEvent logEvent, ILogEventPropertyFactory propertyFactory)
        {
            //logEvent.AddOrUpdateProperty(propertyFactory.CreateProperty("CorrelationId", "correlsssation-custom"));
        }

        


    }
}
