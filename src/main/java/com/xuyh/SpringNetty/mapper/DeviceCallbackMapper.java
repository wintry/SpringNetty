package com.xuyh.SpringNetty.mapper;

import com.xuyh.SpringNetty.cache.model.DeviceItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;
@Mapper
public interface DeviceCallbackMapper {

    @Update({"<script>" +
            "<foreach item='device' collection='devices' index='index' open='' close='' separator=';'>" +
            " UPDATE DeviceCallback " +
            "SET modifyTime = NOW()," +
            "<if test='#{device.ip} != null'>IP = #{device.ip},</if>" +
            "<if test='#{device.isOnline} != null'>isOnline = #{device.isOnline},</if>" +
            "<if test='#{device.lastonlinetime} != null'>lastonlinetime = #{device.lastonlinetime},</if>" +
            "<if test='#{device.version} != null'>version = #{device.version},</if>" +
            " WHERE deviceID = #{device.deviceID} " +
            "</foreach>" +
            "</script>"})
    Integer updateDeviceCallbackList(@Param("devices") List<DeviceItem> deviceItemList);


    @Update({"update DeviceCallback SET modifyTime = NOW(),isonline=0 "})
    Integer ChangeDevStateWithOffline();

}
