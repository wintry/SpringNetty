package com.xuyh.SpringNetty.mapper;

import com.xuyh.SpringNetty.model.DeviceOffOn;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DeviceOffOnMapper {

    @Insert("INSERT INTO DeviceOffOn(DeviceID, PoliceCode,UserID,CellNumber,State,CorpID,Creator,Modifier,CreateTime,ModifyTime" +
            ") VALUES" +
            "(#{DeviceID}, #{PoliceCode}, #{UserID}, #{CellNumber}, #{State}, #{CorpID}, #{Creator}, #{Modifier}, #{CreateTime}, #{ModifyTime})")
    Integer insert(DeviceOffOn deviceOffOn);
}
