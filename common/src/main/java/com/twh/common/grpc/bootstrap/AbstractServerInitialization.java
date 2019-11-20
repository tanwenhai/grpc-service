package com.twh.common.grpc.bootstrap;

import io.grpc.*;
import io.grpc.protobuf.services.ProtoReflectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

/**
 * gPRC server 引导类
 */
public abstract class AbstractServerInitialization implements ApplicationContextAware {
    private Logger log = LoggerFactory.getLogger(AbstractServerInitialization.class);

    private Server server = null;

    protected ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * 配置ServerBuilder
     * @param serverBuilder
     */
    protected void configure(final ServerBuilder serverBuilder) {
    }

    protected Collection<BindableService> bindableServices() {
        Map<String, BindableService> beansOfType = applicationContext.getBeansOfType(BindableService.class);

        return beansOfType.values();
    }

    protected Collection<ServerServiceDefinition> serverServiceDefinitions() {
        Map<String, ServerServiceDefinition> beansOfType = applicationContext.getBeansOfType(ServerServiceDefinition.class);

        return beansOfType.values();
    }

    protected void addService(final ServerBuilder serverBuilder, final BindableService service) {
        serverBuilder.addService(service);
    }

    protected void addService(final ServerBuilder serverBuilder, final ServerServiceDefinition serverServiceDefinition) {
        serverBuilder.addService(serverServiceDefinition);
    }

    @PostConstruct
    public void start() {
        try {
            ServerProperty serverProperty = applicationContext.getBean(ServerProperty.class);
            final ServerBuilder serverBuilder = ServerBuilder.forPort(serverProperty.getPort());

            serverBuilder.addService(ProtoReflectionService.newInstance());
            Collection<BindableService> bindableServices = bindableServices();
            bindableServices.forEach((service)-> {
                this.addService(serverBuilder, service);
            });

            Collection<ServerServiceDefinition> serverServiceDefinitions = serverServiceDefinitions();
            serverServiceDefinitions.forEach((service)-> {
                this.addService(serverBuilder, service);
            });

            configure(serverBuilder);

            Server server = serverBuilder.build().start();
            log.info("server started at " +  server.getListenSockets());
            // 启动一个新的线程防止阻塞主线程
            Thread t = new Thread(()-> {
                try {
                    server.awaitTermination();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            t.setDaemon(false);
            t.setName("thread-grpc-server");
            t.start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    @PreDestroy
    public void stop() {
        log.info("server stop");
        if (server != null) {
            server.shutdown();
        }
    }
}
