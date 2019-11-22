package com.twh.common.grpc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;

@Slf4j
public class GrpcAuthConfiguration {
    @ConditionalOnMissingBean(ServerProperty.class)
    @Bean
    @ConfigurationProperties(prefix = "grpc")
    ServerProperty serverProperty() {
        return new ServerProperty();
    }

    @ConditionalOnMissingBean(GrpcBootstrap.class)
    @Bean
    GrpcBootstrap grpcBootstrap(ServerProperty serverProperty) {
        return new GrpcBootstrap(serverProperty);
    }
}
