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
package com.filter.http.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.github.cafapi.http.interceptors.CorrelationIdInterceptor;

@SpringBootApplication(scanBasePackages = {"com.filter.http.spring"})
public class TestingWebApplication implements WebMvcConfigurer
{
    public static void main(String[] args)
    {
        SpringApplication.run(TestingWebApplication.class, args);
    }
    
    @Override
    public void addInterceptors(final InterceptorRegistry registry)
    {
        registry.addInterceptor(new CorrelationIdInterceptor());
    }
}
