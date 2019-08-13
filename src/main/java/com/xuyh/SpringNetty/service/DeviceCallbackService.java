package com.xuyh.SpringNetty.service;

import com.xuyh.SpringNetty.cache.model.DeviceItem;

import java.util.List;

public interface DeviceCallbackService {
    public int updateDeviceCallBackList(List<DeviceItem> lstDevStates);

    public int ChangeDevStateWithOffline();
}
