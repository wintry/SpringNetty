package com.xuyh.SpringNetty.netty.model;

import lombok.Data;

import java.util.Date;
@Data
public class DoorStatePacket extends Packet {

    private  Integer userPoliceIDSize;

    /// <summary>
    /// 警员编号 同步过来的User_id
    /// 个'\0'表示定盘  个0表示设备admin账户
    /// </summary>
    private String userPoliceID;

    /// <summary>
    /// 柜门号
    /// </summary>
    private int doorNumber;

    /// <summary>
    /// 开关门状态
    /// </summary>
    private int state;

    /// <summary>
    /// 时间
    /// </summary>
    private Date date ;


    public DoorStatePacket(byte[] bytes){
        super(bytes);
    }

    @Override
    public void dataLoad(byte[] bytes, int offset, int length) {
        userPoliceIDSize=(int)bytes[offset];
        userPoliceID =new String(bytes,offset+1,userPoliceIDSize);
        doorNumber =(int)bytes[offset+userPoliceIDSize+1];
        state=(int)bytes[offset+userPoliceIDSize+2];
        long time = 0;
        time += (bytes[offset+userPoliceIDSize+3] < 0 ? 256 + bytes[offset+userPoliceIDSize+3] :
                bytes[offset+userPoliceIDSize+3]) * 256 * 256 * 256;
        time += (bytes[offset+userPoliceIDSize+4] < 0 ? 256 + bytes[offset+userPoliceIDSize+4] :
                bytes[offset+userPoliceIDSize+4]) * 256 * 256;
        time += (bytes[offset+userPoliceIDSize+5] < 0 ? 256 + bytes[offset+userPoliceIDSize+5] :
                bytes[offset+userPoliceIDSize+5]) * 256;
        time += bytes[offset+userPoliceIDSize+6] < 0 ? 256 + bytes[offset+userPoliceIDSize+6] :
                bytes[offset+userPoliceIDSize+6];
        date =new Date(time * 1000);
    }
}
