package com.xuyh.SpringNetty.impl;

import com.xuyh.SpringNetty.cache.model.DeviceItem;
import com.xuyh.SpringNetty.mapper.DeviceCallbackMapper;
import com.xuyh.SpringNetty.service.DeviceCallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DeviceCallbackServiceImpl implements DeviceCallbackService {

    @Autowired
    private DeviceCallbackMapper deviceCallbackMapper;

    @Override
    public int updateDeviceCallBackList(List<DeviceItem> lstDevStates) {
        return deviceCallbackMapper.updateDeviceCallbackList(lstDevStates);
    }

    @Override
    public int ChangeDevStateWithOffline() {
        return deviceCallbackMapper.ChangeDevStateWithOffline();
    }
}
