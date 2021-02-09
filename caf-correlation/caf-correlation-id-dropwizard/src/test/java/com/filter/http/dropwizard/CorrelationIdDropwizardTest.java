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
package com.filter.http.dropwizard;

import static com.github.cafapi.http.filters.correlationid.CorrelationIdConfigurationConstants.headerName;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.cafapi.http.filters.correlationid.CorrelationIdBundle;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;

@ExtendWith(DropwizardExtensionsSupport.class)
class CorrelationIdDropwizardTest
{
    
    private static DropwizardAppExtension<Configuration> EXT = new DropwizardAppExtension<>(TestApp.class, new Configuration());
    
    @Test
    void testCorrelationIdIsAddedIfNotPresentInRequest()
    {
        Client client = EXT.client();
        
        Response response = client.target(
                String.format("http://localhost:%d/ping", EXT.getLocalPort()))
                .request()
                .get();
        
        Assertions.assertNotNull(response.getHeaders().get(headerName));
    }
    
    @Test
    void testRequestWithHeader()
    {
        Client client = EXT.client();
        
        Response response = client.target(
                String.format("http://localhost:%d/ping", EXT.getLocalPort()))
                .request()
                .header(headerName, "UUID1")
                .get();
        
        Assertions.assertEquals("UUID1", response.getHeaders().get(headerName).get(0));
        Assertions.assertEquals(1, response.getHeaders().get(headerName).size());
    }
    
    public static class TestApp extends Application<Configuration>
    {
        @Override
        public void initialize(Bootstrap<Configuration> bootstrap)
        {
            bootstrap.addBundle(new CorrelationIdBundle<>());
        }
        
        @Override
        public void run(Configuration configuration, Environment environment)
        {
            environment.jersey().register(this);
            environment.jersey().register(new PingResource());
        }
    }
    
    @Path("/ping")
    public static class PingResource
    {
        @GET
        public String ping()
        {
            return "pong";
        }
    }
    
}
