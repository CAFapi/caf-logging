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
using Serilog.Expressions;
using Serilog.Templates;

namespace MicroFocus.CafApi.CafLoggingSerilog
{
    public static class CafLoggingLoggerConfiguration
    {
        private const string DefaultTemplate = "[{@t:yyyy-MM-dd HH:mm:ss.fffZ} {TId(ProcessId,ThreadId)} {Log(@l):5} {Sanitize(tenantId)} {Sanitize(correlationId)}] {logger:30} {MaybeJsonMsgAndEx(@m,@x)}\n";
        private static readonly StaticMemberNameResolver sanitizerFunctions = new(typeof(Sanitizer));
        public static LoggerConfiguration GetLoggerConfig(LoggingLevelSwitch levelSwitch)
        {
            
            return new LoggerConfiguration()
                .WriteTo.Console(new ExpressionTemplate(
        DefaultTemplate, nameResolver: sanitizerFunctions), levelSwitch: levelSwitch)
                .Enrich.FromLogContext()
                .Enrich.WithThreadId()
                .Enrich.WithProcessId()
                .MinimumLevel.Verbose()
                ;
        }
        public static LoggerConfiguration GetLoggerConfig()
        {
            return new LoggerConfiguration()
                .WriteTo.Console(new ExpressionTemplate(
        DefaultTemplate, nameResolver: sanitizerFunctions))
                .Enrich.FromLogContext()
                .Enrich.WithThreadId()
                .Enrich.WithProcessId()
                .MinimumLevel.Verbose()
                ;
        }

    }
}
