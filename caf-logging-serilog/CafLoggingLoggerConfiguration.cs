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
using Serilog.Expressions;
using Serilog.Templates;
using System;

namespace MicroFocus.CafApi.CafLoggingSerilog
{
    public static class CafLoggingLoggerConfiguration
    {
        private const string DefaultTemplate =
            "[{UtcDateTime(@t):yyyy-MM-dd HH:mm:ss.fffZ} {FormatThreadAndProcessId(ProcessId,ThreadId)} {Log(@l):5} {Coalesce(Sanitize(tenantId, 12, 12), '-')} " +
            "{Coalesce(Sanitize(correlationId, 4, 4), '-')}] {Sanitize(logger, 30, 30)}: {ConvertMessageAndExceptionToJsonIfUnsafe(@m,@x)}\n";

        private static readonly StaticMemberNameResolver sanitizerFunctions = new(typeof(Sanitizer));
        private static readonly LoggingLevelSwitch defaultLoggingLevelSwitch = new(LogEventLevel.Information);
        private static readonly string Caf_log_level_env = Environment.GetEnvironmentVariable("CAF_LOG_LEVEL");

        public static LoggerConfiguration CreateLogConfiguration()
        {
            return new LoggerConfiguration()
                .WriteTo.Console(
                    new ExpressionTemplate(
                        DefaultTemplate,
                        nameResolver: sanitizerFunctions),              // References the functions used in the default template
                        standardErrorFromLevel: LogEventLevel.Verbose   // Redirects the logs to stderr
                    )
                .Enrich.FromLogContext()
                .Enrich.WithThreadId()
                .Enrich.WithProcessId()
                .MinimumLevel.ControlledBy(SetLog());
        }

        private static LoggingLevelSwitch SetLog()
        {
            try
            {
                var level = (LogEventLevel)Enum.Parse(typeof(LogEventLevel), Caf_log_level_env);
                return new LoggingLevelSwitch(level);
            }
            catch (ArgumentException) {
                return defaultLoggingLevelSwitch;
            }            
        }
    }
}
