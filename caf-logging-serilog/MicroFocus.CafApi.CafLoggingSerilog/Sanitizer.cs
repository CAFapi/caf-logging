using Serilog.Events;
using System;

namespace MicroFocus.CafApi.CafLoggingSerilog
{
    static class Sanitizer
    {
        public static LogEventPropertyValue? Log(LogEventPropertyValue? value)
        {
            if (value is ScalarValue scalar)
            {
                var level = scalar.ToString();
                if ("Information".Equals(level))
                {
                    level = "INFO ";
                }
                return new ScalarValue(level.ToUpper());
            }

            return null;
        }

        public static LogEventPropertyValue? Sanitize(LogEventPropertyValue? value)
        {
            if (value is ScalarValue scalar)
            {
                return new ScalarValue("TODO Sanitize");
            }

            return null;
        }


    }
}
