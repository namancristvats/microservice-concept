package com.ncv.microservices.api_gateway.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthenticationGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthenticationGatewayFilterFactory.Config> {
    public AuthenticationGatewayFilterFactory() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange,chain)->{
            String authHeader=exchange.getRequest().getHeaders().getFirst("Authorization");
            String userId=authHeader.split("Bearer ")[1];
            log.info("Authentication Filter is added {}",userId);
            return chain.filter(exchange);
        };
    }

    @Data
    public static class Config{
        private boolean isEnabled;
    }
}
