package com.xuyh.SpringNetty.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;



public class ServerHandler extends ChannelInboundHandlerAdapter {

    private CommandServer commandServer = new CommandServer();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        /*ByteBuf buf=(ByteBuf) msg;
        System.out.println(buf.readableBytes());
        byte[] butfs = new byte[buf.readableBytes()];
       buf.readBytes(butfs);


        commandServer.onMessageReceived(butfs);*/
        byte[] bytes=(byte[]) msg;
        commandServer.onMessageReceived(ctx,bytes);

        System.out.println("server receive message :"+ msg);
        /*ctx.channel().writeAndFlush("yes server already accept your message" + msg);*/
        //ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channelActive>>>>>>>>");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        //……
        if(channel.isActive())ctx.close();
    }



}
