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

namespace nuget_sample_caf_logging.MicroFocus.CafApi.CafLoggingSerilog
{
    internal class SanitizedMessage
    {
        public string message;
        public string exception;

        public SanitizedMessage(string message)
        {
            this.message = message;
        }

        public SanitizedMessage(string message, string exception)
        {
            this.message = message;
            this.exception = exception;
        }

    }
}
