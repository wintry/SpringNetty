package com.xuyh.SpringNetty.netty.model;
import lombok.Data;

@Data
public class TagSubmitItem {

    /// <summary>
    /// 档案编号
    /// </summary>
    private String Epc;

    /// <summary>
    /// 档案编号 0-离柜  1-在柜
    /// </summary>
    private int State;


}
