/*
 * Copyright 2019-2020 Micro Focus or one of its affiliates.
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
package com.github.cafapi.http.filters.correlationid;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class CorrelationIdBundle<C extends Configuration> implements ConfiguredBundle<C> {
    
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    
    }
    
    @Override
    public void run(C configuration, Environment environment) {
        environment.servlets()
                .addFilter("correlation-id-servlet-filter", new CorrelationIdFilter())
                .addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false, "*");
    }
}
