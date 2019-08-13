package com.xuyh.SpringNetty.cache;

import com.xuyh.SpringNetty.service.DeviceCallbackService;
import com.xuyh.SpringNetty.tool.TimeTool;
import com.xuyh.SpringNetty.cache.model.DeviceItem;
import com.xuyh.SpringNetty.model.Device;
import com.xuyh.SpringNetty.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.*;

@Component
public class DeviceCache {
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private DeviceCallbackService deviceCallbackService;

    private HashMap<String, DeviceItem> deviceHashMap = new HashMap<String, DeviceItem>();

    private DeviceCache() {
    }

    private static DeviceCache deviceCache = new DeviceCache();

    public static DeviceCache getInstance() {
        return deviceCache;
    }

    private Date lastFlushTime;

    private int timeout_seconds = 20;



    @PostConstruct
    public void init() {
        deviceCache = this;
        deviceCache.deviceService = this.deviceService;
        deviceCache.deviceCallbackService=this.deviceCallbackService;
    }

    /**
     * 启动
     */
    public void startDeviceCache() {
        initCache();
    }

    /**
     * 初始化缓存
     */
    private void initCache() {

        List<Device> deviceList = deviceService.queryAllDevice();
        for (Device device : deviceList) {
            if (device.getModifytime() != null && device.getModifytime().after(lastFlushTime)) {
                lastFlushTime = (Date) device.getModifytime();
            }


            device.setOnline(false);
            if (!deviceHashMap.containsKey(device.getCode())) {
                DeviceItem deviceItem = new DeviceItem(device);

                deviceItem.setIsOnline(false);
                deviceItem.setIsCached(false);

                deviceHashMap.put(device.getCode(), deviceItem);
            } else {
                //打印日志
            }

        }
        deviceCallbackService.ChangeDevStateWithOffline();
    }

    @Scheduled(fixedRate = 5000)
    public void autoRefresh() {
        if (deviceHashMap.isEmpty()==false) {
            int reCheckCount = 0;

            reCheckCount++;

            // 先进行缓存刷新处理
            CacheFlush();        //超时

            // 进行库中信息读取
            UpdateCache();       //更新设备信息表到内存中

            if (reCheckCount >= 30) {
                // 检查被删除的设备
                reCheckCount = 0;
                ReCheckCache();
            }

        }
    }

    /***
     * 离线检查
     */
    public void CacheFlush() {
        List<DeviceItem> lstDevStates = new ArrayList<>();
        try {
            lstDevStates.clear();
            for (Map.Entry<String, DeviceItem> entry : deviceHashMap.entrySet()) {
                if (entry.getValue().isIsCached() == true) {
                    if (entry.getValue().isIsOnline() == true && entry.getValue().getLastOnlineTime() != null && TimeTool.getDateDifference(new Date(),
                            entry.getValue().getLastOnlineTime()) >= 20) {
                        entry.getValue().setIsCached(false);
                        entry.getValue().setIsOnline(false);
                        lstDevStates.add(entry.getValue());
                    } else {
                        lstDevStates.add(entry.getValue());
                    }
                }
            }
            if(lstDevStates.size()!=0){
            deviceCallbackService.updateDeviceCallBackList(lstDevStates);}
        } catch (Exception e) {

        }
    }

    public void UpdateCache(){
        try{
            Date checkTime = lastFlushTime;
            List<DeviceItem> deviceItemList=null;
            List<Device> deviceList =deviceService.queryListWithNewModify(checkTime);
            for (Device device:
                    deviceList) {
               if(device.getModifytime()!=null&&device.getModifytime().after(lastFlushTime)){
                   lastFlushTime=device.getModifytime();
               }
               if (device.getState()==1){
                   DeviceItem deviceItem= deviceHashMap.get(device.getCode());
                   if(deviceItem!=null){
                       deviceItem.setDeviceID(device.getId());
                       deviceItem.setLastModifyTime(device.getModifytime());
                       deviceItem.setDeviceType(device.getType());
                       deviceItem.setCorpType(device.getCorptype());
                   }
                   else {
                       deviceItem = new DeviceItem(device);
                       deviceItem.setIsOnline(false);
                       deviceItem.setIsCached(false);
                       deviceHashMap.put(device.getCode(),deviceItem);
                   }
               }else if (device.getState()==0){
                   deviceHashMap.remove(device.getCode());
               }
            }


        }catch (Exception e){

        }
    }

    void ReCheckCache(){
        for (Map.Entry<String, DeviceItem> entry : deviceHashMap.entrySet()) {
            if(deviceService.queryDeviceByDeviceCode(entry.getKey())==null){
                deviceHashMap.remove(entry.getKey());
            }
        }
    }

    /**
     *获得设备信息
     * @param deviceCode
     * @return
     */
    public DeviceItem getDeviceInfo(String deviceCode) {
        return deviceHashMap.get(deviceCode);
    }
}
