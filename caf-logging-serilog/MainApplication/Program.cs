using Serilog;
using nuget_sample_caf_logging;
using Serilog.Core;

namespace SerilogExample
{
    public class Program
    {
        private const string DefaultTemplate = "{Timestamp:yyyy-MM-dd HH:mm:ss} [{Level}] [{SourceContext}] {Message}{NewLine}{Exception}";

        static void Main()
        {
            LoggerConfiguration config = CafLoggingLoggerConfiguration.GetLoggerConfig();

            Logger logger = config
                .MinimumLevel.Debug()
                .Enrich.FromLogContext()
                .Enrich.WithThreadId()
                .Enrich.WithThreadName()
                .Enrich.WithProcessId()
                .Enrich.WithProcessName()
                .WriteTo.Console(outputTemplate: DefaultTemplate)
                .Enrich.FromLogContext()
                .CreateLogger();


            logger.Information("{testInfo ");
            logger.Error("{testErrorUnsafe1 ");
            //logger.Error("te^stErrorUnsafe2 ");
            //logger.Error("test[ErrorUnsafe3 ");
            //logger.Error("testErrorU]nsafe3 ");
            logger.Error("testErrorSafe ");

        }
    }
}
