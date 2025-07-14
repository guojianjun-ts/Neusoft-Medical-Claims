package com.gjj.nmcbackend.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InpatientMedicalVO {

    /**
     * 医疗服务id
     */
    private Integer id;

    /**
     * 医疗服务中文名称
     */
    private String serviceName;

    /**
     * 项目编码
     */
    private String serviceNumber;

    /**
     * 国家编码
     */
    private String countryNumber;

    /**
     * 项目说明
     */
    private String serviceInfo;

    /**
     * 除外内容
     */
    private String serviceExclude;

    /**
     * 计价单位
     */
    private String serviceUnit;

    /**
     * 价格
     */
    private BigDecimal servicePrice;


}
