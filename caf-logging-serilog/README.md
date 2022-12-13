# CAF Logging Serilog
This project configures Serilog to meet the CAF Logging Standard.

The log level is controllable via an environment variable.
- The `CAF_LOG_LEVEL` environment variable can be used to configure the required volume of logging.
- By default `INFO`-level logging is used.

All logs are being redirected to `stderr`.
A consistent log message format is used.

The header information is in square brackets.
Fixed size columns are used so that messages align vertically.
Missing or inapplicable header fields are replaced with a dash.
Log Injection attacks are prevented.

Any unsafe characters found in the Tenant Id, Correlation Id, or Logger are removed.
The log message is JSON-encoded if it contains any control characters (including newlines) or any non-ASCII characters.
Exception stack traces are JSON-encoded so that they do not span multiple lines.

## Dependency

Add the nuget package `MicroFocus.CafApi.CafLoggingSerilog`

## Pattern

`[{@t:yyyy-MM-dd HH:mm:ss.fffZ} {Tid(ProcessId,ThreadId)} {Log(@l):5} {Sanitize(tenantId, 12, 12)} {Sanitize(correlationId, 4, 4)}] {Sanitize(logger, 30, 30)}: {MaybeJsonMsgAndEx(@m,@x)}\n`

## Examples

`[2022-05-10 13:29:45.444Z #217.001 WARN  john-tenant  Cidi] c.m.d.f.f.f.FieldFullTextFixer: "Greek test: κόσμε"`

To log the tenant and correlation ids the service using this configuration must use Serilog's MDC.

Example commands:

    using (LogContext.PushProperty("tenantId", "sample-tenant"))
    using (LogContext.PushProperty("correlationId", "Abc123"))

If the tenant or correlation id are not supplied the logger will substitute them with a dash and pad the rest of their character allotment
with spaces. The logger will also pad any tenant id shorter than 12 characters to the 12 characters limit.  
This is to aid readability of the logs by aligning the log statements.

To use this logging configuration simply add the following nuget package:

    `MicroFocus.CafApi.CafLoggingSerilog`

### Basic
    Log.Logger = CafLoggingLoggerConfiguration
      .GetLoggerConfig()
      .CreateLogger();

### With switchLevel ( minimum logging level )

    var levelSwitch = new LoggingLevelSwitch();
    levelSwitch.MinimumLevel = LogEventLevel.Verbose;
    Log.Logger = CafLoggingLoggerConfiguration
      .GetLoggerConfig(levelSwitch)
      .CreateLogger();

### With configuration file

    var config_path = "path\\to\\file\\appsettings.json";
    var configuration = new ConfigurationBuilder()
         .AddJsonFile(config_path)
         .Build();
    Log.Logger = CafLoggingLoggerConfiguration
         .GetLoggerConfig()
         .ReadFrom.Configuration(configuration)
         .CreateLogger();
