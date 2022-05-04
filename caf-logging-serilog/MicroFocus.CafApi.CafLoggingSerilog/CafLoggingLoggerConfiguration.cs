/**
 * Copyright 2019-2022 Micro Focus or one of its affiliates.
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
using Serilog.Core;
using Serilog.Events;

namespace MicroFocus.CafApi.CafLoggingSerilog
{
    public static class CafLoggingLoggerConfiguration
    {
        private const string DefaultTemplate = "[{Timestamp:yyyy-MM-dd HH:mm:ss.fff zzz}{UTC} {ProcessId}.{ThreadId} {Level:u5} {tenantId} {correlationId:4}] {logger:30} {Message}{Exception}{NewLine}";

        public static LoggerConfiguration GetLoggerConfig(LoggingLevelSwitch levelSwitch)
        {
            return new LoggerConfiguration()
                .WriteTo.CustomConsoleConfiguration(writeTo => writeTo.Console(outputTemplate: DefaultTemplate), levelSwitch)
                .Enrich.FromLogContext()
                .Enrich.WithThreadId()
                .Enrich.WithThreadName()
                .Enrich.WithProcessId()
                .Enrich.WithProcessName()
                .MinimumLevel.Verbose()
                .Enrich.With(new SanitizingEnricher());
        }
        public static LoggerConfiguration GetLoggerConfig()
        {
            return new LoggerConfiguration()
                .WriteTo.CustomConsoleConfiguration(writeTo => writeTo.Console(outputTemplate: DefaultTemplate), null)
                .Enrich.FromLogContext()
                .Enrich.WithThreadId()
                .Enrich.WithThreadName()
                .Enrich.WithProcessId()
                .Enrich.WithProcessName()
                .MinimumLevel.Verbose()
                .Enrich.With(new SanitizingEnricher());
        }

    }
}
