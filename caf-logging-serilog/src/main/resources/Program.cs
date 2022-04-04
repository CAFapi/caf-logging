/**
 * Copyright 2019-2020 Micro Focus or one of its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
using Serilog;
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
