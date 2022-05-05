using System;

namespace nuget_sample_caf_logging.MicroFocus.CafApi.CafLoggingSerilog
{
    class SanitizedException
    {
        public SanitizedException(Exception exception, string message)
        {
            this.exception = exception;
            this.message = message;
        }

        public string message;
        public Exception exception;
    }
}
