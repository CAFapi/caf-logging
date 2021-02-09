/*
 * Copyright 2021 Micro Focus or one of its affiliates.
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
package com.github.cafapi.correlation.jaxrs;

import static com.github.cafapi.correlation.constants.CorrelationIdConfigurationConstants.HEADER_NAME;
import static com.github.cafapi.correlation.constants.CorrelationIdConfigurationConstants.MDC_KEY;
import java.util.Optional;
import java.util.UUID;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;
import org.slf4j.MDC;

@Provider
public class CorrelationIdClientFilter implements ClientRequestFilter
{
    @Override
    public void filter(final ClientRequestContext requestContext)
    {
        final String correlationId = Optional.ofNullable(MDC.get(MDC_KEY)).orElseGet(() -> UUID.randomUUID().toString());
        requestContext.getHeaders().add(HEADER_NAME, correlationId);
    }
}
