package com.gjj.nmcbackend.model.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DrugCostVO {

    private Long drugId;
    /**
     * 药品名称
     */
    private String drugName;

    /**
     * 规格
     */
    private String specification;


    /**
     * 生产厂家
     */
    private String manufacturer;

    /**
     * 单位
     */
    private String unit;

    /**
     * 价格(元)
     */
    private BigDecimal price;

}