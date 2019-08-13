package com.xuyh.SpringNetty.netty.command;

import com.xuyh.SpringNetty.cache.DeviceCache;
import com.xuyh.SpringNetty.cache.model.DeviceItem;
import com.xuyh.SpringNetty.netty.LockerProtocol;
import com.xuyh.SpringNetty.netty.model.KeepAlivePacket;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Date;

public class KeepAlive {
    public void process(ChannelHandlerContext ctx, byte[] data){
        KeepAlivePacket keepAlivePacket = new KeepAlivePacket(data);

        if (keepAlivePacket.isFrameError)
        {
            return;
        }
        DeviceItem deviceItem = DeviceCache.getInstance().getDeviceInfo(keepAlivePacket.getOriginalID());
        if(deviceItem!=null){
            byte[] keepAliveReq = new byte[10];
            LockerProtocol.initMessage(keepAliveReq);
            keepAliveReq[4]= 0x10;
            keepAliveReq[5]= 0x03;
            keepAliveReq[7]= LockerProtocol.calcCheckBit(keepAliveReq);
            keepAliveReq = LockerProtocol.pack(keepAliveReq);
            ctx.channel().writeAndFlush(keepAliveReq);

            deviceItem.setIsOnline(true);
            deviceItem.setLastOnlineTime(new Date());
            //获取ip
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
                    .remoteAddress();
            String clientIP = insocket.getAddress().toString();
            deviceItem.setDeviceIP(clientIP);
        }


    }
}
