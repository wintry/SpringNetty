package com.xuyh.SpringNetty.netty.model;

import lombok.Data;

import java.util.Date;
@Data
public class ErrorPacket  extends Packet{

    public ErrorPacket(byte[] bytes){
        super(bytes);
    }


    /// <summary>
    /// 柜门号
    /// </summary>
    private int DoorNumber;

    /// <summary>
    /// 时间
    /// </summary>
    private Date Time;

    /// <summary>
    /// 错误代码
    /// </summary>
    private Integer ErrorCode;



    @Override
    public void dataLoad(byte[] bytes, int offset, int length) {
        DoorNumber = bytes[offset];


        //时间
        long time = 0;
        time += (bytes[offset+1] < 0 ? 256 + bytes[offset+1] : bytes[offset+1]) * 256 * 256 * 256;
        time += (bytes[offset+2] < 0 ? 256 + bytes[offset+2] :
                bytes[offset+2]) * 256 * 256;
        time += (bytes[offset+3] < 0 ? 256 + bytes[offset+3] : bytes[offset+3]) * 256;
        time += bytes[offset+4] < 0 ? 256 + bytes[offset+4] :
                bytes[offset+4];
        Time =new Date(time * 1000);

        //错误编号
        ErrorCode = (int)(((bytes[offset + 5] & 0x000000FF) << 24) | ((bytes[offset + 6] & 0x000000FF) << 16)
                | ((bytes[offset + 7] & 0x000000FF) << 8) | (bytes[offset + 8] & 0x000000FF));

    }
}

