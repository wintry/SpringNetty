package com.xuyh.SpringNetty.netty.model;

import lombok.Data;

@Data
public class VersionReportPacket extends Packet {
    public VersionReportPacket(byte[] bytes){
        super(bytes);
    }

    private String version;

    @Override
    public void dataLoad(byte[] bytes, int offset, int length) {
        int versionnum = (int)bytes[offset ] * 256 + (int)bytes[offset + 1];
        int hundreds =(int) ((float)versionnum / 100);
        int ten = (int)((float)((versionnum % 100) / 10));
        int single = versionnum % 10;
        version = "V" + String.valueOf(hundreds)+ "." + String.valueOf(ten) + "." + String.valueOf(single);
    }

    @Override
    public boolean CheckFrameLength(byte[] arrContent, int offset, int length) {
        return true;
    }
}
