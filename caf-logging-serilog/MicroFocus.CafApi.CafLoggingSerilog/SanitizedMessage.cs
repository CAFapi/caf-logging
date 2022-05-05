using System;

namespace nuget_sample_caf_logging.MicroFocus.CafApi.CafLoggingSerilog
{
    class SanitizedMessage
    {
        public SanitizedMessage(string message)
        {
            this.message = message;
        }

        public string message;
    }
}
