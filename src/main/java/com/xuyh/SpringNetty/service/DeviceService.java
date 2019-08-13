package com.xuyh.SpringNetty.service;

import com.xuyh.SpringNetty.cache.model.DeviceItem;
import com.xuyh.SpringNetty.model.Device;

import java.util.Date;
import java.util.List;

public interface DeviceService {
    public List<Device> queryAllDevice();

    public int updateDeviceList(List<DeviceItem> deviceItems);

    public  List<Device> queryListWithNewModify(Date lastFlushTime);

    public Device queryDeviceByDeviceCode(String deviceCode);
}
