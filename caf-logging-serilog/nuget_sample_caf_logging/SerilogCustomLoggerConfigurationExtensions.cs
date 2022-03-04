using Serilog;
using Serilog.Configuration;
using Serilog.Events;
using System;

namespace nuget_sample_caf_logging
{
    public static class SerilogCounterLoggerConfigurationExtensions
    {
        public static LoggerConfiguration SerilogCounterSink(
            this LoggerSinkConfiguration loggerConfiguration,
                  IFormatProvider formatProvider = null)
        {
            //LoggerConfiguration configuration = loggerConfiguration.Sink(new SerilogCustomSink(formatProvider),
            //    LogEventLevel.Verbose);

            return loggerConfiguration.Sink(new SerilogCustomSink(formatProvider))
                .ReadFrom.AppSettings()
                .Enrich.FromLogContext()
                .MinimumLevel.Debug()
                .Enrich.WithProcessId()
                .Enrich.WithThreadId()
                .Enrich.With(new SimpleClassEnricher())
                ;
            //return loggerConfiguration.Sink(new SerilogCustomSink(formatProvider));
        }
    }
}