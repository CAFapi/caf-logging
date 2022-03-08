using System;
using System.Linq;

namespace caf_logging_serilog
{
    public static class LogSanitizer
    {
        readonly static Char[] forbiddenCharacters = new char[] { '[', '^', '\\', 'w', ' ', '-', '.', ']' };

        public static string SanitizeMessage(string message)
        {
            return new string(message
              .Where(x => !forbiddenCharacters.Contains(x))
              .ToArray());
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
                Console.WriteLine("wrong character " + c);
                if (Char.IsControl(c))
                {
                    return false;
                }
            }
            return true;
        }
    }
}
