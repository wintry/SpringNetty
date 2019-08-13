package com.xuyh.SpringNetty.cache.model;

import com.xuyh.SpringNetty.model.Device;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceItem {
    public DeviceItem (){}


    public DeviceItem(Device device){
        this.setDeviceCode(device.getCode());
        this.setDeviceID(device.getId());
        this.setLastModifyTime(device.getModifytime());
        this.setDeviceType(device.getType());
        this.setCorpType(device.getCorptype());
        this.setDeviceName(device.getDeviceName());

    }

    /// <summary>
    /// 机具ID
    /// </summary>
    private int DeviceID;

    /// <summary>
    /// 机具编号
    /// </summary>
    private String DeviceCode;

    /// <summary>
    /// 机具IP
    /// </summary>
    private String DeviceIP;
    /// <summary>
    /// 时间戳  用于时间同步用
    /// </summary>
    //public Date TimeStamp { set; get; }

    /// <summary>
    /// 同步时间周期 单位 分钟
    /// </summary>
    //public int SyncTime { set; get; }

    /// <summary>
    /// 最近上线时间
    /// </summary>
    private Date LastOnlineTime;

    /// <summary>
    /// 是否是有效缓存
    /// </summary>
    private boolean IsCached;


    /// <summary>
    /// 设备是否在线
    /// </summary>
    private boolean IsOnline;

    /// <summary>
    /// 设备类型 0-寄存柜  1-回转柜
    /// </summary>
    private int DeviceType;

    /// <summary>
    /// 所属单位类型
    /// </summary>
    private int CorpType ;

    /// <summary>
    /// 状态 是否是启用状态
    /// </summary>
    private int State ;

    /// <summary>
    /// 最新更新时间
    /// </summary>
    private Date LastModifyTime;
    /// 设备名称
    /// </summary>
    private String DeviceName ;

   /* public DevStateItem(int syctime)
    {
        TimeStamp = DateTime.Now;
        SyncTime = syctime;
    }*/

    private String Version;

}

