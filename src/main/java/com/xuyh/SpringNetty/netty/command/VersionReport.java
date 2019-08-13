package com.xuyh.SpringNetty.netty.command;

import com.xuyh.SpringNetty.cache.DeviceCache;
import com.xuyh.SpringNetty.cache.model.DeviceItem;
import com.xuyh.SpringNetty.netty.LockerProtocol;
import com.xuyh.SpringNetty.netty.model.VersionReportPacket;
import io.netty.channel.ChannelHandlerContext;

import java.util.Date;


public class VersionReport {
    public void process(ChannelHandlerContext ctx, byte[] data){
        VersionReportPacket versionReportPacket = new VersionReportPacket(data);
        if (versionReportPacket.isFrameError){
            return;
        }
        DeviceItem deviceItem = DeviceCache.getInstance().getDeviceInfo(versionReportPacket.getOriginalID());
        if (deviceItem!=null){
            deviceItem.setLastOnlineTime(new Date());
            deviceItem.setVersion(versionReportPacket.getVersion());


            byte[] versionReportAnswer = new byte[11];
            LockerProtocol.initMessage(versionReportAnswer);
            versionReportAnswer[4]=0x10;
            versionReportAnswer[5]=0x04;
            versionReportAnswer[7]=0x00;
            versionReportAnswer[8]=LockerProtocol.calcCheckBit(versionReportAnswer);
            versionReportAnswer= LockerProtocol.pack(versionReportAnswer);
            ctx.channel().writeAndFlush(versionReportAnswer);
        }


    }
}
