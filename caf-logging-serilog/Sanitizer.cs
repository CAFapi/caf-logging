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
﻿using Newtonsoft.Json;
using Serilog.Events;
using System;
using System.Text.RegularExpressions;

namespace MicroFocus.CafApi.CafLoggingSerilog
{
    static class Sanitizer
    {
        private const string ForbiddenPattern = "[^\\w\\s\\-.]*";
        private readonly static Regex rgx = new(ForbiddenPattern);

        public static LogEventPropertyValue? Log(LogEventPropertyValue? value)
        {
            if (value is ScalarValue)
            {
                var level = value.ToString();
                switch (level)
                {
                    case "Information":
                    case "Warning":
                        return new ScalarValue(level.ToUpper().Substring(0, 4).PadRight(5, ' '));
                    default:
                        return new ScalarValue(level.ToUpper().Substring(0, 5));
                }
            }

            return null;
        }

        public static LogEventPropertyValue? Tid(LogEventPropertyValue? processId, LogEventPropertyValue? ThreadId)
        {
            if (processId is ScalarValue && ThreadId is ScalarValue)
            {
                var threadId = ThreadId.ToString();
                return new ScalarValue("#" + processId.ToString().Substring(0, 3) + "." + threadId.PadLeft(3, '0'));
            }

            return null;
        }

        public static LogEventPropertyValue? Sanitize(LogEventPropertyValue? value, LogEventPropertyValue length, LogEventPropertyValue padding)
        {

            if (value is ScalarValue)
            {
                int valueLength, valuePadding;
                try
                {
                    valueLength = Convert.ToInt32(length.ToString());
                    valuePadding = Convert.ToInt32(padding.ToString());
                }
                catch (FormatException)
                {
                    valueLength = 4;
                    valuePadding = 4;
                }
                var newValue = rgx.Replace(value.ToString(), "");
                if (valueLength <= newValue.Length)
                {
                    newValue = newValue.Substring(0, valueLength);
                }
                newValue = newValue.PadRight(valuePadding, ' ');
                return new ScalarValue(newValue);
            }

            return null;
        }

        public static LogEventPropertyValue? MaybeJsonMsgAndEx(LogEventPropertyValue? messageValue, LogEventPropertyValue? exceptionValue)
        {

            if (messageValue is ScalarValue)
            {
                var message = messageValue.ToString();
                if (IsMessageSafeToLog(message) && null == exceptionValue)
                {
                    return new ScalarValue(message);
                }
                else 
                {
                    if (null != exceptionValue)
                    {
                        var exception = exceptionValue.ToString();
                        return new ScalarValue(JsonConvert.SerializeObject(new SanitizedMessage(message, exception)));
                    } 
                    else
                    {
                        var jsonData = JsonConvert.SerializeObject(new SanitizedMessage("test"));
                        return new ScalarValue(jsonData);
                    }
                }
            }
            return null;
        }

        private static bool IsMessageSafeToLog(string message)
        {
            if (string.IsNullOrWhiteSpace(message))
            {
                return true;
            }

            if (message.StartsWith("{") || message.StartsWith("\"{"))
            {
                return false;
            }

            return containSafeCharacters(message);
        }

        private static bool containSafeCharacters(string message)
        {
            int min = Convert.ToInt32("0x20", 16);
            int max = Convert.ToInt32("0x7F", 16);

            foreach (char c in message)
            {
                int x = c;
                if (x < min && x >= max)
                {
                    return false;
                }
            }

            return true;
        }
    }
}
