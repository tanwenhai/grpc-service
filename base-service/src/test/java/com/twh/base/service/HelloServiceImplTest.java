package com.twh.base.service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.helloworld.GreeterGrpc;
import io.grpc.examples.helloworld.HelloReply;
import io.grpc.examples.helloworld.HelloRequest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class HelloServiceImplTest {
    @Test
    public void sayHello() {

    }

    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("127.0.0.1", 9999).usePlaintext().build();
        HelloRequest.Builder builder = HelloRequest.newBuilder();
        builder.setName("twh");
        GreeterGrpc.GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel);
        HelloReply helloReply = stub.sayHello(builder.build());
        System.out.println(helloReply.getMessage());
    }
}
