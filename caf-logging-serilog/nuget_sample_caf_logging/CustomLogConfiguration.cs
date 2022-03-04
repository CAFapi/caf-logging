using Serilog;
using System;

namespace nuget_sample_caf_logging
{
    //public class CustomLogConfiguration: ILogger
    //{
    //    private const string DefaultTemplate = "[{Timestamp:yyyy-MM-dd HH:mm:ss} {ProcessId}.{ThreadId} {Level:u4} {TenantId} {CorrelationId}] [{SourceContext}] {Message}{NewLine}{Exception}";

    //    public Serilog.Core.Logger getLogger() {

    //        Serilog.Core.Logger logger = new LoggerConfiguration()
    //           .WriteTo.Console(outputTemplate: DefaultTemplate)
    //           .CreateLogger();
    //        Console.WriteLine("created logger");
    //        return logger;
    //    }

    //}
}
