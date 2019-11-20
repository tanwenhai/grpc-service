package com.twh.base.service;

import com.twh.grpc.base.OrderStatus;
import com.twh.grpc.service.Order;
import com.twh.grpc.service.OrderFilter;
import com.twh.grpc.service.OrderServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl extends OrderServiceGrpc.OrderServiceImplBase {
    @Override
    public void query(OrderFilter request, StreamObserver<Order> responseObserver) {
        for (int i = 0; i < 100; i++) {
            responseObserver.onNext(Order.newBuilder().setStatus(OrderStatus.RUNNING).build());
        }
        responseObserver.onCompleted();
    }
}
