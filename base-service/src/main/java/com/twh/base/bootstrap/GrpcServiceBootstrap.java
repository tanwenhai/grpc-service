package com.twh.base.bootstrap;

import com.twh.common.grpc.bootstrap.AbstractServerInitialization;
import com.twh.common.grpc.bootstrap.ServerProperty;
import io.grpc.Attributes;
import io.grpc.ServerBuilder;
import io.grpc.ServerTransportFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GrpcServiceBootstrap extends AbstractServerInitialization {

    @Autowired
    ServerProperty serverProperty;

    @Bean
    @ConfigurationProperties(prefix = "grpc")
    ServerProperty serverProperty() {
        return new ServerProperty();
    }

    @Override
    protected void configure(ServerBuilder serverBuilder) {
        serverBuilder.addTransportFilter(new ServerTransportFilter() {
            @Override
            public Attributes transportReady(Attributes transportAttrs) {
                return super.transportReady(transportAttrs);
            }

            @Override
            public void transportTerminated(Attributes transportAttrs) {
                super.transportTerminated(transportAttrs);
            }
        });

    }
}
