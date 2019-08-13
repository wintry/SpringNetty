package com.xuyh.SpringNetty;


import com.xuyh.SpringNetty.cache.DeviceCache;
import com.xuyh.SpringNetty.cache.model.DeviceItem;
import com.xuyh.SpringNetty.netty.NettyServer;
import io.netty.channel.ChannelFuture;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetSocketAddress;

@SpringBootApplication
@MapperScan("com.xuyh.SpringNetty.mapper")
@EnableScheduling
public class Application implements ApplicationRunner {

    @Value("${netty.port}")
    private int port;

    @Value("${netty.url}")
    private String url;

    @Autowired
    private NettyServer socketServer;


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {

        DeviceCache.getInstance().startDeviceCache();
        DeviceItem device=DeviceCache.getInstance().getDeviceInfo("001");

        //开启netty服务
        InetSocketAddress address = new InetSocketAddress(url, port);
        ChannelFuture future = socketServer.run(address);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                socketServer.destroy();
            }
        });
        future.channel().closeFuture().syncUninterruptibly();
    }
}