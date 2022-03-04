using Serilog;
using nuget_sample_caf_logging;
using Serilog.Core;

namespace SerilogExample
{
    public class Program
    {
        
        static void Main()
        {
            LoggerConfiguration config = CustomLogger.GetLoggerConfig();

            Logger logger = config
                //.MinimumLevel.Debug()
                //.Enrich.FromLogContext()
                //.Enrich.WithThreadId()
                //.Enrich.WithThreadName()
                //.Enrich.WithProcessId()
                //.Enrich.WithProcessName()
                .CreateLogger();


            logger.Information("{testInfo ");
            logger.Error("{testErrorUnsafe1 ");
            logger.Error("te^stErrorUnsafe2 ");
            logger.Error("test[ErrorUnsafe3 ");
            logger.Error("testErrorU]nsafe3 ");
            logger.Error("testErrorSafe ");

        }
    }
}
