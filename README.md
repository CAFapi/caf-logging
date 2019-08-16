# CAF Logging
## Logback Configuration JAR
This project configures Logback to meet the CAF Logging Standard.

- The log level is controllable via an environment variable.  
    - The `CAF_LOG_LEVEL` environment variable can be used to configure the required volume of logging.  
    - By default `INFO`-level logging is used.

- A consistent log message format is used.  
    - The header information is in square brackets.
    - Fixed size columns are used so that messages align vertically.
    - Missing or inapplicable header fields are replaced with a dash.

#### Pattern:
    [(UTC Time) #(Process Id).(Thread Id) (Log Level) (Tenant Id) (Correlation Id)] (Logger): (Log Message)

#### Example:
    [10:50:25.465Z #bff.009 DEBUG acme-corp    -   ] com.github.example.Logger: Example Log Message

To log the tenant and correlation ids the service using this configuration must use SLF4J's [MDC](https://www.slf4j.org/manual.html#mdc).  
Example commands:

    MDC.put("tenantId", "acme-corp");
    MDC.put("correlationId", "6afb775c-bf4d-11e9-9cb5-2a2ae2dbcce4");

If the tenant or correlation id are not supplied the logger will substitute them with a dash and pad the rest of their character allotment with spaces.  The logger will also pad any tenant id shorter than 12 characters to the 12 character limit.  This is to aide readability of the logs by aligning the log statements.

To use this logging configuration simply add the project as a runtime-scope dependency:

    <dependency>
        <groupId>com.github.cafapi</groupId>
        <artifactId>caf-logging</artifactId>
        <scope>runtime</scope>
    </dependency>
