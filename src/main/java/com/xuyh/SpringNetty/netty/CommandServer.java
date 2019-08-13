package com.xuyh.SpringNetty.netty;

import com.xuyh.SpringNetty.netty.command.*;
import com.xuyh.SpringNetty.netty.command.ClientReg;
import com.xuyh.SpringNetty.netty.command.KeepAlive;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;


public class CommandServer {
    @Autowired
    private ClientReg clientReg;


    private byte[] bytesCache = new byte[1024];

    private int offset = -1;//当前数组有值的那一位

    public void onMessageReceived(ChannelHandlerContext ctx, byte[] buffer) {
        System.arraycopy(buffer, 0, bytesCache, offset + 1, buffer.length);
        offset = offset + buffer.length;

        int headOffset = LockerProtocol.checkHead(bytesCache, bytesCache.length, 0);
        int temp = LockerProtocol.checkHead(bytesCache, bytesCache.length, headOffset + 1);
        if (temp != -1) {
            headOffset = temp;
        }
        int tailOffset = headOffset > 0 ? LockerProtocol.checkTail(bytesCache, bytesCache.length - headOffset, headOffset) : -1;
        if (headOffset > 0 && tailOffset > headOffset) {
            int tempLen = tailOffset - headOffset + 2;
            byte[] data = new byte[tempLen];
            System.arraycopy(bytesCache, headOffset - 1, data, 0, tempLen);
            data = LockerProtocol.unpack(data);

            //去掉解包成功的数据
            byte[] tempBytes = new byte[bytesCache.length];
            System.arraycopy(bytesCache, tailOffset + 1, tempBytes, 0, bytesCache.length - tailOffset - 1);
            offset = buffer.length - tailOffset - 2;
            bytesCache = tempBytes;


            int checkLen = LockerProtocol.checkLen(data);
            int checkBit = LockerProtocol.calcCheckBit(data);
            if (checkLen == LockerProtocol.LEN_MATCH && checkBit == data[data.length - 3]) {
                // 长度检测与校验位比对通过
                int type = (data[4] < 0 ? 256 + data[4] : data[4]) * 256
                        + (data[5] < 0 ? 256 + data[5] : data[5]);
                switch (type) {
                    case 0x1001:



                        new ClientReg().process(ctx,data);
                        break;
                    case 0x1003:

                        new KeepAlive().process(ctx,data);
                        break;
                    case 0x1004:



                }
            }


        }
    }

}
