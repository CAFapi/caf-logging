using Newtonsoft.Json;
using nuget_sample_caf_logging.MicroFocus.CafApi.CafLoggingSerilog;
using Serilog.Events;
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
                switch (level) {
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
                return new ScalarValue("#" + processId + "." + threadId.PadLeft(3, '0'));
            }

            return null;
        }

        public static LogEventPropertyValue? Sanitize(LogEventPropertyValue? value)
        {

            if (value is ScalarValue)
            {
                var newValue = rgx.Replace(value.ToString(), "").PadRight(4, ' ');
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
                else {
                    if (null != exceptionValue)
                    {
                        var exception = exceptionValue.ToString();
                        return new ScalarValue(JsonConvert.SerializeObject(new SanitizedMessage(message, exception)));
                    } else
                    {
                        return new ScalarValue(JsonConvert.SerializeObject(new SanitizedMessage(message)));
                    }
                }
            }
            return null;
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
                if (char.IsControl(c))
                {
                    return true;
                }
            }
            return true;
        }

    }
}
