package com.xuyh.SpringNetty.netty.command;


import com.xuyh.SpringNetty.cache.DeviceCache;
import com.xuyh.SpringNetty.cache.model.DeviceItem;
import com.xuyh.SpringNetty.netty.LockerProtocol;
import com.xuyh.SpringNetty.netty.model.ClientRegPacket;
import com.xuyh.SpringNetty.tool.TimeTool;
import io.netty.channel.ChannelHandlerContext;

import java.net.InetSocketAddress;
import java.util.Date;

public class ClientReg {

    public void process(ChannelHandlerContext ctx,byte[] data){
        ClientRegPacket clientRegPacket =new ClientRegPacket(data);

        if (clientRegPacket.isFrameError)
        {
            return;
        }
        /*byte[] regAnswer = new byte[11];
        LockerProtocol.initMessage(regAnswer);
        regAnswer[4]=0x10;
        regAnswer[5]=0x01;
        regAnswer[7]=0x00;
        regAnswer[8]= LockerProtocol.calcCheckBit(regAnswer);
        regAnswer=LockerProtocol.pack(regAnswer);
        ctx.channel().writeAndFlush(regAnswer);*/

        DeviceItem deviceItem = DeviceCache.getInstance().getDeviceInfo(clientRegPacket.getOriginalID());
        if(deviceItem!=null){
            byte[] regAnswer = new byte[11];
            LockerProtocol.initMessage(regAnswer);
            regAnswer[4]=0x10;
            regAnswer[5]=0x01;
            regAnswer[7]=0x00;
            regAnswer[8]= LockerProtocol.calcCheckBit(regAnswer);
            regAnswer=LockerProtocol.pack(regAnswer);
            ctx.channel().writeAndFlush(regAnswer);


            int timeint = TimeTool.DateToTimestamp(new Date());
            byte[] timeCommand = new byte[14];
            LockerProtocol.initMessage(timeCommand);
            timeCommand[4]=0x10;
            timeCommand[5]=0x02;
            timeCommand[7]=(byte)((timeint >> 24) & 0x00ff);
            timeCommand[8]=(byte)((timeint >> 16) & 0x00ff);
            timeCommand[9]=(byte)((timeint >> 8) & 0x00ff);
            timeCommand[10]=(byte)((timeint ) & 0x00ff);
            timeCommand=LockerProtocol.pack(timeCommand);
            ctx.channel().writeAndFlush(timeCommand);


            //获取ip
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel()
                    .remoteAddress();
            String clientIP = insocket.getAddress().toString();

            deviceItem.setDeviceIP(clientIP);
            deviceItem.setLastOnlineTime(new Date());
            deviceItem.setIsOnline(true);
        }else{
            byte[] regAnswer = new byte[11];
            LockerProtocol.initMessage(regAnswer);
            regAnswer[4]=0x10;
            regAnswer[5]=0x01;
            regAnswer[7]=0x01;
            regAnswer[8]= LockerProtocol.calcCheckBit(regAnswer);
            regAnswer=LockerProtocol.pack(regAnswer);
            ctx.channel().writeAndFlush(regAnswer);
        }




    }

}
