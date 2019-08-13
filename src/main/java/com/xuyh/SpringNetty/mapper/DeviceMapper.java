package com.xuyh.SpringNetty.mapper;

import com.xuyh.SpringNetty.cache.model.DeviceItem;
import com.xuyh.SpringNetty.model.Device;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface DeviceMapper {
    @Select("select * from device where state =1")
    List<Device> getAllDevice();


    @Select("select * from device where ModifyTime>= #{lastFlushTime}")
    List<Device> queryListWithNewModify(@Param("lastFlushTime")Date date);
    @Select("select * from device where code = #{deviceCode}")
    Device queryDeviceByDeviceCode(@Param("deviceCode") String deviceCode);

    @Update({"<script>" +
            "<foreach item='deice' collection='devices' index='index' open='' close='' separator=';'>" +
            " UPDATE deice " +
            "SET update_time = NOW()," +
            "<if test='#{totalPvuv.countPv} != null'>count_pv = #{totalPvuv.countPv},</if>" +
            "<if test='#{totalPvuv.sumAuv} != null'>sum_auv = #{totalPvuv.sumAuv},</if>" +
            "<if test='#{totalPvuv.sumSuv} != null'>sum_suv = #{totalPvuv.sumSuv}</if>" +
            " WHERE app_id = #{totalPvuv.appId} " +
            "</foreach>" +
            "</script>"})
    Integer updateDeviceList(@Param("devices")List<DeviceItem> deviceItemList);
}
