package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 药品信息表
 * @TableName drug_info
 */
@TableName(value ="drug_info")
@Data
public class DrugInfo implements Serializable {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 药品类型
     */
    private String insuranceType;

    /**
     * 药品名称
     */
    private String chinaName;

    /**
     * 品名
     */
    private String goodsName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 单位
     */
    private String drugUnit;

    /**
     * 生产厂家
     */
    private String drugManufacturer;

    /**
     * 价格
     */
    private BigDecimal drugPrice;

    /**
     * 备注
     */
    private String remarks;

    private static final long serialVersionUID = 7014052787054974740L;

}