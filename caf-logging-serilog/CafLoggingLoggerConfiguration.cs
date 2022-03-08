
using nuget_sample_caf_logging;
using Serilog;

namespace caf_logging_serilog
{
    public static class CafLoggingLoggerConfiguration
    {
        private const string DefaultTemplate = "[{Timestamp:yyyy-MM-dd HH:mm:ss} {ProcessId}.{ThreadId} {Level:u5} {tenantId} {correlationId:4}] {logger:30} / {Message}{Exception}";

        public static LoggerConfiguration GetLoggerConfig()
        {
            return new LoggerConfiguration()
                .WriteTo.CustomConsoleConfiguration(writeTo => writeTo.Console(outputTemplate: DefaultTemplate))
                .Enrich.FromLogContext()
                .Enrich.WithThreadId()
                .Enrich.WithThreadName()
                .Enrich.WithProcessId()
                .Enrich.WithProcessName()
                .Enrich.WithCorrelationId()
                .Enrich.With(new SanitizingEnricher())
                ;
        }
    }
}
