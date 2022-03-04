using Microsoft.VisualStudio.TestTools.UnitTesting;
using nuget_sample_caf_logging;
using Serilog;
using Serilog.Core;
using Serilog.Sinks.TestCorrelator;
using System;
using FluentAssertions;

namespace caf_logging_serilog_Tests
{
    [TestClass]
    public class CustomLoggerTest
    {
        LoggerConfiguration config = CafLoggingLoggerConfiguration.GetLoggerConfig();
        static Logger? logger;

        [AssemblyInitialize]
        public static void ConfigureGlobalLogger(TestContext testContext)
        {
            LoggerConfiguration config = CafLoggingLoggerConfiguration.GetLoggerConfig();
            logger = config
                .WriteTo.TestCorrelator()
                .MinimumLevel.Debug()
                .Enrich.FromLogContext()
                .Enrich.WithThreadId()
                .Enrich.WithThreadName()
                .Enrich.WithProcessId()
                .Enrich.WithProcessName()
                .CreateLogger();
        }


        [TestMethod]
        public void ShouldNotLogInformation() {
            LoggerConfiguration config = CafLoggingLoggerConfiguration.GetLoggerConfig();
            Log.Logger = config.WriteTo.TestCorrelator().CreateLogger();

            using (TestCorrelator.CreateContext())
            {
                Log.Information("My log message!");

                TestCorrelator.GetLogEventsFromCurrentContext()
                    .Should().ContainSingle()
                    .Which.MessageTemplate.Text
                    .Should().Be("My log message!");
            }
        }
    }
}
