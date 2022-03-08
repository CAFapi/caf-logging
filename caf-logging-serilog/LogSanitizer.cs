using System;
using System.Linq;
using System.Text.RegularExpressions;

namespace caf_logging_serilog
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
