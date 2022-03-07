using Serilog;
using Serilog.Configuration;
using Serilog.Events;
using System;

namespace caf_logging_serilog
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
