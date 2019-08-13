package com.xuyh.SpringNetty.netty.model;
import lombok.Data;
@Data
public class Packet {
    //设备

    private  int OriginalLong;

    private String OriginalID;

    /// <summary>
    /// 用于外层判断数据帧是否正确
    /// </summary>

    public boolean isFrameError;






    public Packet(byte[] arrContent){
        int frameLength = (((arrContent[2] & 0x00FF) << 8) | (arrContent[3] & 0x00FF));
        if (arrContent != null && CheckFrameLength(arrContent, 12, frameLength))
        {
            isFrameError = false;

            OriginalLong = arrContent[7];
            // 设备号
            OriginalID=new String(arrContent,8,OriginalLong);
            dataLoad(arrContent, 8+ OriginalLong, frameLength - (OriginalLong+7));
        }
        else
        {
            isFrameError = true;
           // FrameErrorLog();
        }
    }

    public void dataLoad(byte[] bytes, int offset, int length){

    }

    public  boolean CheckFrameLength(byte[] arrContent, int offset, int length)
    {
        return length == 14;
    }
}
