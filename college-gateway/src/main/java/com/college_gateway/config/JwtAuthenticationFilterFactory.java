package com.college_gateway.config;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.GatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationFilterFactory implements GatewayFilterFactory<Object> {

    @Override
    public GatewayFilter apply(Object config) {
        return new JwtAuthenticationFilter();
    }

    @Override
    public Class<Object> getConfigClass() {
        return Object.class;
    }
}

