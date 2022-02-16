   using System;
   using Serilog;

   namespace SerilogExample
   {
       class Program
       {
           // [(UTC Date) (UTC Time) #(Process Id).(Thread Id) (Log Level) (Tenant Id) (Correlation Id)] (Logger): (Log Message)
           // [2022-02-15 12:45:08.704Z #5ba.026 INFO 1 - ] AccessLog: Message - "
           private const string DefaultTemplate = "[{Timestamp:yyyy-MM-dd HH:mm:ss} {ProcessId}.{ThreadId} {Level:u4} {TenantId} {CorrelationId}] [{SourceContext}] {Message}{NewLine}{Exception}";

           static void Main()
           {
               Log.Logger = new LoggerConfiguration()
                   .MinimumLevel.Debug()
                   .Enrich.WithThreadId()
                   .Enrich.WithThreadName()
                   .Enrich.WithProcessId()
                   .Enrich.WithProcessName()
                   .WriteTo.Console(outputTemplate: DefaultTemplate)
                   .CreateLogger();

               Log.Information("Hello, world!");

               //int a = 10, b = 0;
               //try
               //{
               //    Log.Debug("Dividing {A} by {B}", a, b);
               //    Console.WriteLine(a / b);
               //}
               //catch (Exception ex)
               //{
               //    Log.Error(ex, "Something went wrong");
               //}
               //finally
               //{
               //    Log.CloseAndFlush();
               //}
           }
       }
   }
