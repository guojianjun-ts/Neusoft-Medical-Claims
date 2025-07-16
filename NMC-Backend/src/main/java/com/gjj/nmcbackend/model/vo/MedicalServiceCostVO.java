package com.gjj.nmcbackend.model.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MedicalServiceCostVO {


    /**
     * 医疗服务ID
     */
    private Integer medicalId;

    /**
     * 医疗服务名称
     */
    private String serviceName;

    /**
     * 医疗服务编码
     */
    private String serviceCode;

    /**
     * 除外内容
     */
    private String excludeContent;

    /**
     * 计价单位
     */
    private String unit;

    /**
     * 价格(元)
     */
    private BigDecimal price;
}