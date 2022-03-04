using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace nuget_sample_caf_logging
{
    static class LogSanitizer
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
                if (Char.IsControl(c) && !Char.IsWhiteSpace(c))
                {
                    Console.WriteLine("wrong character " + c);
                    return false;
                }
            }
            return true;
        }
    }
}
