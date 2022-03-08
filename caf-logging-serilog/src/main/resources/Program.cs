using Serilog;
using caf_logging_serilog;
using Serilog.Context;
using Microsoft.VisualStudio.TestTools.UnitTesting;


/* This file is for testing purpose only and should be removed. In order to use it, it's required to create a new
console Application alongside the caf-logging-serilog library with a dependency on it, then use the present content inside a Program.cs
class. That console App project must be used as a startup project. */

namespace SerilogExample
{
    public class Program
    {
        public static void Main()
        {
            //Log.Logger = new LoggerConfiguration().WriteTo.Console().CreateLogger();
            Log.Logger = CafLoggingLoggerConfiguration.GetLoggerConfig().CreateLogger();


            MainFunction("acme-corp", "abcdef");
            Testing();
        }

        private static void MainFunction(string tenantId, string correlationId)
        {
            using (LogContext.PushProperty("tenantId", tenantId))
            using (LogContext.PushProperty("correlationId", correlationId))
            {
                MainLogic();
            }
        }

        private static void MainLogic()
        {
            Log.Error("Hello World!");
        }

        private static void Testing()
        {
            var message = "[a^\\cme-co_$%&r.p]";
            Assert.AreEqual("acme-co_r.p", LogSanitizer.SanitizeMessage(message));

            Assert.IsFalse(LogSanitizer.IsMessageSafeToLog("{"));
            Assert.IsFalse(LogSanitizer.IsMessageSafeToLog("test\ntest"));

            Assert.IsTrue(LogSanitizer.IsMessageSafeToLog(""));
            Assert.IsTrue(LogSanitizer.IsMessageSafeToLog(null));
        }
    }
}
