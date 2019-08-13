package com.xuyh.SpringNetty.netty.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Data;

@Data
public class TagDataPacket extends Packet {
    private int userPoliceIDLong;
    private String userPoliceID;
     private  int doorNumber;
    private  int reserved;
     private int state;
    private Date date;
    private List<TagSubmitItem> tagSubmitItemList;



    public TagDataPacket(byte[] bytes){
        super(bytes);
    }

    @Override
    public void dataLoad(byte[] bytes, int offset, int length){
        userPoliceIDLong=(int)bytes[offset];
       userPoliceID=new String(bytes,offset+1,userPoliceIDLong);
        doorNumber=bytes[offset+1+userPoliceIDLong];
        reserved=bytes[offset+2+userPoliceIDLong];
        //时间
        long time = 0;
        time += (bytes[offset+3+userPoliceIDLong] < 0 ? 256 + bytes[offset+3+userPoliceIDLong] : bytes[offset+3+userPoliceIDLong]) * 256 * 256 * 256;
        time += (bytes[offset+4+userPoliceIDLong] < 0 ? 256 + bytes[offset+4+userPoliceIDLong] :
                bytes[offset+4+userPoliceIDLong]) * 256 * 256;
        time += (bytes[offset+5+userPoliceIDLong] < 0 ? 256 + bytes[offset+5+userPoliceIDLong] : bytes[offset+5+userPoliceIDLong]) * 256;
        time += bytes[offset+6+userPoliceIDLong] < 0 ? 256 + bytes[offset+6+userPoliceIDLong] :
                bytes[offset+6+userPoliceIDLong];
        date =new Date(time * 1000);

        tagSubmitItemList = new ArrayList<TagSubmitItem>();
        int totalCount = bytes[offset + 7+userPoliceIDLong];
        int archivesIndex = offset + 8+userPoliceIDLong;
        for (int i = 0; i < totalCount; i++)
        {
            TagSubmitItem tagSubmitItem = new TagSubmitItem();
            int epclength = bytes[archivesIndex];
            tagSubmitItem.setState( bytes[archivesIndex + 1]);
            tagSubmitItem.setEpc("");
            for (int j = 0; j < epclength; j++)
            {
                tagSubmitItem.setEpc( tagSubmitItem.getEpc()+ (bytes[archivesIndex + 2 + j]&0xFF));
            }
            tagSubmitItemList.add(tagSubmitItem);
            archivesIndex += epclength + 2;
        }



    }

    @Override
    public boolean CheckFrameLength(byte[] arrContent, int offset, int length) {
        return true;
    }
}
