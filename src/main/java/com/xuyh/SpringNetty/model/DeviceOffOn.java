package com.xuyh.SpringNetty.model;

import lombok.Data;

@Data
public class DeviceOffOn extends  BaseEntity {
    private Integer deviceId;
    private String policeCode;
    private Integer userId;
    private Integer cellNumber;
    private Integer State;
    private Integer corpId;

}
