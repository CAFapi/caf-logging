# caf-logging  

This repository provides the logging configuration needed to force logback to log output in the pattern shown in the example below.  

**Pattern:**  
```  
 [(Time) (Thread Name) (Log Level) (Tenant Id (padded to 30 characters)) (CAF Correlation Id (first 8 characters only)) ] (Logger) (Log Message)  
```  

**Example:**
```  
[10:50:25.465 http-nio-8080-exec-9 DEBUG acme-corp                      -       ] com.github.example.Logger Example Log Message  
```

To Enable this configuration to log the tenant or CAF correlation id's the service using this configuration must add the tenant id and the correlation id to the Mapped Diagnostic Context using the following commands from with the Java code.  
```  
MDC.put("tenantId", acme-corp);  
MDC.put("correlationId", 6afb775c-bf4d-11e9-9cb5-2a2ae2dbcce4);  
```  

If tenant or CAF correlation id are not supplied the logger will substitute them with a `-` and pad the rest of their character allotment with spaces. The logger will also pad any tenant id shorter than 30 characters to the 30 character limit, this is to aide readability of the logs by aligning the log statements.  

To consume this library add the maven dependency to your project.  
```  
  <dependency>  
    <groupId>com.github.cafapi</groupId>  
    <artifactId>caf-logging</artifactId>  
    <version>1.0.0-SNAPSHOT</version>  
  </dependency>  
```  
