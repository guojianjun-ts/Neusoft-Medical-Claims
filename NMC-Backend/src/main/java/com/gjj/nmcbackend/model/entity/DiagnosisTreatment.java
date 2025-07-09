package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 诊疗项目表
 * @TableName diagnosis_treatment
 */
@TableName(value ="diagnosis_treatment")
@Data
public class DiagnosisTreatment {
    /**
     * 诊疗项目 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 诊疗项目类别
     */
    private String treatmentType;

    /**
     * 诊疗项目编码
     */
    private String treatmentNumber;

    /**
     * 诊疗项目国家编码
     */
    private String countryNumber;

    /**
     * 诊疗项目名称
     */
    private String treatmentName;

    /**
     * 诊疗项目说明
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

    /**
     * 备注
     */
    private String remark;
}