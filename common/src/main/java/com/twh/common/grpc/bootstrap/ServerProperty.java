package com.twh.common.grpc.bootstrap;

import lombok.Data;

import java.net.InetAddress;

@Data
public class ServerProperty {
    private InetAddress address;

    private Integer port;
}
