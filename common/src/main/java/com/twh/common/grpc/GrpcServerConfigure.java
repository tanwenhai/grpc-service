package com.twh.common.grpc;

import io.grpc.BindableService;
import io.grpc.ServerBuilder;
import io.grpc.ServerServiceDefinition;

import java.util.Collection;

public interface GrpcServerConfigure {
    void configure(final ServerBuilder serverBuilder);

    Collection<BindableService> bindableServices();

    Collection<ServerServiceDefinition> serverServiceDefinitions();

    void addService(final ServerBuilder serverBuilder, final BindableService service);
}
