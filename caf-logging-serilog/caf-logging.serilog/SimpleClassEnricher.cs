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
﻿using caf_logging_serilog;
using Serilog.Core;
using Serilog.Events;
using System.Collections.Generic;
using System.Linq;

namespace nuget_sample_caf_logging
{
    public class SanitizingEnricher : ILogEventEnricher
    {
        private readonly static string[] propertiesToCheck = new string[] { "tenantId", "correlationId", "logger" };

        public void Enrich(LogEvent logEvent, ILogEventPropertyFactory propertyFactory)
        {
            foreach (KeyValuePair<string, LogEventPropertyValue> kvp in logEvent.Properties)
            {
                if (propertiesToCheck.Any(kvp.Key.Contains))
                {
                    var value = kvp.Value.ToString();
                    var trimmedValue = value.Substring(1, value.Length - 2);

                    logEvent.AddOrUpdateProperty(propertyFactory.CreateProperty(kvp.Key, LogSanitizer.SanitizeMessage(trimmedValue)));
                }
            }
        }
    }
}
