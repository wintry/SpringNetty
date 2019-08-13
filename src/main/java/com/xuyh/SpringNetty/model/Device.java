package com.xuyh.SpringNetty.model;

import lombok.Data;

@Data
public class Device extends BaseEntity {

    /// <summary>
    /// 设备编号
    /// </summary>
    private String code;

    /// <summary>
    /// 设备类型
    /// </summary>
    private int type;

    /// <summary>
    /// 所属单位 ID
    /// </summary>
    private int corpid;

    /// <summary>
    /// 所属单位类别 0-派出所 1-县局
    /// </summary>
    private int corptype;

    /// <summary>
    /// 状态
    /// </summary>
    private int state ;

    private boolean isOnline;


    private String deviceName;


}
