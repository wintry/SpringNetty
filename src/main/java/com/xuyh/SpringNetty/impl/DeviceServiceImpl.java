package com.xuyh.SpringNetty.impl;

import com.xuyh.SpringNetty.cache.model.DeviceItem;
import com.xuyh.SpringNetty.mapper.DeviceMapper;
import com.xuyh.SpringNetty.model.Device;
import com.xuyh.SpringNetty.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
@Service
public class DeviceServiceImpl implements DeviceService {
    @Autowired
    private DeviceMapper deviceMapper;


    public List<Device> queryAllDevice(){
        return deviceMapper.getAllDevice();
    }



    @Override
    public List<Device> queryListWithNewModify(Date lastFlushTime) {
        return deviceMapper.queryListWithNewModify(lastFlushTime);
    }

    @Override
    public Device queryDeviceByDeviceCode(String deviceCode) {
        return deviceMapper.queryDeviceByDeviceCode(deviceCode);
    }

    public  int updateDeviceList(List<DeviceItem> deviceItems){
        return  0;
    }
}
