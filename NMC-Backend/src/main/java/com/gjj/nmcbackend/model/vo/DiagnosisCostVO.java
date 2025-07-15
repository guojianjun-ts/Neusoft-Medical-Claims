package com.gjj.nmcbackend.model.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DiagnosisCostVO {

    /**
     * 诊疗项目名称
     */
    private String diagnosisName;

    /**
     * 诊疗项目编码
     */
    private String diagnosisCode;

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