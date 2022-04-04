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
using System;
using System.Linq;
using System.Text.RegularExpressions;

namespace MicroFocus.CafApi.CafLoggingSerilog
{
    public static class LogSanitizer
    {
        private const string ForbiddenPattern = "[^\\w\\s\\-.]*";
        private readonly static Regex rgx = new Regex(ForbiddenPattern);

        public static string SanitizeMessage(string message)
        {
            return rgx.Replace(message, "");
        }


        public static bool IsMessageSafeToLog(string message)
        {
            if (string.IsNullOrWhiteSpace(message))
            {
                return true;
            }

            if (message.StartsWith("{"))
            {
                return false;
            }

            // Makes sure that each character is ASCII printable
            // http://www.csc.villanova.edu/~tway/resources/ascii-table.html
            foreach (char c in message)
            {
                if (Char.IsControl(c))
                {
                    return false;
                }
            }
            return true;
        }
    }
}
