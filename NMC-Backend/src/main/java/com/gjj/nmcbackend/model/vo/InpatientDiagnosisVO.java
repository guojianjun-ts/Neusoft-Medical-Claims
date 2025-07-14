package com.gjj.nmcbackend.model.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class InpatientDiagnosisVO {

    /**
     * 诊疗项目 id
     */
    private Integer id;

    /**
     * 诊疗项目中文名称
     */
    private String treatmentName;

    /**
     * 项目编码
     */
    private String treatmentNumber;

    /**
     * 国家编码
     */
    private String countryNumber;

    /**
     * 项目说明
     */
    private String treatmentInfo;

    /**
     * 除外内容
     */
    private String treatmentExclude;

    /**
     * 计价单位
     */
    private String treatmentUnit;

    /**
     * 价格
     */
    private BigDecimal treatmentPrice;
}
