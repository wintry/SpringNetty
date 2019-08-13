package com.xuyh.SpringNetty.model;

import lombok.Data;

import java.util.Date;

@Data
public class DeviceCallback {

    private String ip;

    private String isOnline;

    private Date lastOnlineTime;

    private Date modifyTime;

    private String version;

    private int deviceId;

}
