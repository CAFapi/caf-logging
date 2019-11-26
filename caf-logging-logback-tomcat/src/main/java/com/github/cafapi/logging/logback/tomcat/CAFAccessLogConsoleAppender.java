/*
 * Copyright 2019 Micro Focus or one of its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.cafapi.logging.logback.tomcat;

import ch.qos.logback.access.spi.AccessEvent;
import ch.qos.logback.core.ConsoleAppender;

import com.github.cafapi.logging.common.ProcessAndThreadIdProvider;

public final class CAFAccessLogConsoleAppender extends ConsoleAppender<AccessEvent>
{
    public CAFAccessLogConsoleAppender()
    {
    }

    @Override
    protected void subAppend(final AccessEvent event)
    {
        final StringBuilder logMsgBuilder = new StringBuilder(ProcessAndThreadIdProvider.getId());
        if(event.getStatusCode() >= 400 && event.getStatusCode() <=499){
            logMsgBuilder.append(" ").append("WARN ");
        }else if(event.getStatusCode() >= 500 && event.getStatusCode() <=599){
            logMsgBuilder.append(" ").append("ERROR");
        }else{
            logMsgBuilder.append(" ").append("INFO ");
        }
        event.setThreadName(logMsgBuilder.toString());
        super.subAppend(event);
    }
    
}
