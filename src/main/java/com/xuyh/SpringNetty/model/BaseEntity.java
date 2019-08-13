package com.xuyh.SpringNetty.model;

import lombok.Data;

import java.util.Date;
@Data
public class BaseEntity {

    private int id ;

    /// <summary>
    /// 创建人
    /// </summary>
    private int creator;

    /// <summary>
    /// 创建时间
    /// </summary>
    private Date createtime;

    /// <summary>
    /// 修改者
    /// </summary>
    private int modifier;

    /// <summary>
    /// 修改时间
    /// </summary>
    private Date modifytime;

    /// <summary>
    /// 备注
    /// </summary>
    private String mark;

}
