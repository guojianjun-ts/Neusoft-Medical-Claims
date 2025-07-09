package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import lombok.Data;

/**
 * 医疗服务设施表
 * @TableName medical_service
 */
@TableName(value ="medical_service")
@Data
public class MedicalService {
    /**
     * 医疗服务 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 医疗服务类别
     */
    private String serviceType;

    /**
     * 医疗服务编码
     */
    private String serviceNumber;

    /**
     * 医疗服务国家编码
     */
    private String countryNumber;

    /**
     * 医疗服务名称
     */
    private String serviceName;

    /**
     * 医疗服务说明
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

    /**
     * 备注
     */
    private String remark;
}