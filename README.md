# CAF Logging
This project configures Logback and Log4j2 to meet the CAF Logging Standard.

- The log level is controllable via an environment variable.  
    - The `CAF_LOG_LEVEL` environment variable can be used to configure the required volume of logging.  
    - By default `INFO`-level logging is used.

- A consistent log message format is used.  
    - The header information is in square brackets.
    - Fixed size columns are used so that messages align vertically.
    - Missing or inapplicable header fields are replaced with a dash.

- Log Injection attacks are prevented.  
    - Any unsafe characters found in the Tenant Id, Correlation Id, or Logger are removed.
    - The log message is JSON-encoded if it contains any control characters (including newlines) or any non-ASCII characters.
    - Exception stack traces are JSON-encoded so that they do not span multiple lines.

#### Pattern:
    [(UTC Time) #(Process Id).(Thread Id) (Log Level) (Tenant Id) (Correlation Id)] (Logger): (Log Message)

#### Example:
    [10:50:25.465Z #bff.009 DEBUG acme-corp    -   ] com.github.example.Logger: Example Log Message

To log the tenant and correlation ids the service using this configuration must use SLF4J's [MDC](https://www.slf4j.org/manual.html#mdc).  
Example commands:

    MDC.put("tenantId", "acme-corp");
    MDC.put("correlationId", "6afb775c-bf4d-11e9-9cb5-2a2ae2dbcce4");

If the tenant or correlation id are not supplied the logger will substitute them with a dash and pad the rest of their character allotment with spaces.  The logger will also pad any tenant id shorter than 12 characters to the 12 character limit.  This is to aide readability of the logs by aligning the log statements.

To use this logging configuration simply add the appropriate project as a runtime-scope dependency, either:

    <dependency>
        <groupId>com.github.cafapi.logging</groupId>
        <artifactId>caf-logging-logback</artifactId>
        <scope>runtime</scope>
    </dependency>

or

    <dependency>
        <groupId>com.github.cafapi.logging</groupId>
        <artifactId>caf-logging-log4j2</artifactId>
        <scope>runtime</scope>
    </dependency>
