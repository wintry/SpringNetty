package com.xuyh.SpringNetty.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;


public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        // 解码编码
//        socketChannel.pipeline().addLast(new StringDecoder(CharsetUtil.UTF_8));
//        socketChannel.pipeline().addLast(new StringEncoder(CharsetUtil.UTF_8));
        socketChannel.pipeline().addLast(new ByteArrayEncoder());
        socketChannel.pipeline().addLast(new ByteArrayDecoder());
        socketChannel.pipeline().addLast(new ServerHandler());
    }
}