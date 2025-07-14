package com.gjj.nmcbackend.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InpatientDrugVO {

    /**
     * 药品id
     */
    private Long id;

    /**
     * 药品中文名称
     */
    private String chinaName;

    /**
     * 规格
     */
    private String specifications;

    /**
     * 生产企业
     */
    private String drugManufacturer;

    /**
     * 价格
     */
    private BigDecimal drugPrice;
}
