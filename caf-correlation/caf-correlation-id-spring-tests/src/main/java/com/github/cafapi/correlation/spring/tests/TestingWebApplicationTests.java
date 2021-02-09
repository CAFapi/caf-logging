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
package com.github.cafapi.correlation.spring.tests;

import static com.github.cafapi.correlation.constants.CorrelationIdConfigurationConstants.HEADER_NAME;
import static com.github.cafapi.correlation.constants.CorrelationIdConfigurationConstants.MDC_KEY;
import com.github.cafapi.correlation.jaxrs.CorrelationIdClientFilter;
import java.util.UUID;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestingWebApplicationTests
{
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    public void contextLoads()
    {
    }
    
    @Test
    public void testInterceptor()
    {
        //prepare
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HEADER_NAME, "UUID1");
        HttpEntity<?> request = new HttpEntity<>(httpHeaders);
        
        //act
        HttpEntity<String> responseEntity = this.restTemplate.exchange("http://localhost:" + port + "/greeting", HttpMethod.GET, request, String.class);
        
        //assert
        String correlationID = responseEntity.getHeaders().get(HEADER_NAME).get(0);
        Assertions.assertThat(correlationID).isNotNull();
        Assertions.assertThat(correlationID).isEqualTo("UUID1");
    }
    
    @Test
    public void testClientFilter()
    {
        //prepare
        final Client client = ClientBuilder.newClient();
        String UUIDValue = UUID.randomUUID().toString();
        MDC.put(MDC_KEY, UUIDValue);
        client.register(new CorrelationIdClientFilter());
        
        //act
        Response response = client
                .target("http://localhost:" + port + "/greeting")
                .request()
                .get();
        
        //assert
        Assertions.assertThat(response.getHeaders().get(HEADER_NAME)).isNotNull();
        Assertions.assertThat(response.getHeaders().get(HEADER_NAME).get(0)).isEqualTo(UUIDValue);
        Assertions.assertThat(response.getHeaders().get(HEADER_NAME).size()).isEqualTo(1);
    }
    
}