using Serilog;
using Serilog.Configuration;
using Serilog.Events;
using System;

namespace nuget_sample_caf_logging
{
    static class LoggerSinkConfigurationExtensions
    {
        
        public static LoggerConfiguration CustomConsoleConfiguration(
            this LoggerSinkConfiguration lsc,
            Action<LoggerSinkConfiguration> writeTo)
        {
            return LoggerSinkConfiguration.Wrap(
                lsc,
                wrapped => new ConsoleWrapper(wrapped),
                writeTo,
                LogEventLevel.Error,
                null);
        }
    }
}
