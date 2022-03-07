
using Serilog;

namespace caf_logging_serilog
{
    public static class CafLoggingLoggerConfiguration
    {
        private const string DefaultTemplate = "[{Timestamp:yyyy-MM-dd HH:mm:ss} {ProcessId}.{ThreadId} {Level:u5} {TenantId} {CorrelationId}] [{SourceContext}] {Error} / {Message}{NewLine}{Exception}";

        public static LoggerConfiguration GetLoggerConfig()
        {
            return new LoggerConfiguration()
                .WriteTo.CustomConsoleConfiguration(writeTo => writeTo.Console(outputTemplate: DefaultTemplate))
                //.Enrich.With(new SimpleClassEnricher())
                .Enrich.FromLogContext()
                .Enrich.WithThreadId()
                .Enrich.WithThreadName()
                .Enrich.WithProcessId()
                .Enrich.WithProcessName()
                .Enrich.WithCorrelationId()
                ;
        }
    }
}
