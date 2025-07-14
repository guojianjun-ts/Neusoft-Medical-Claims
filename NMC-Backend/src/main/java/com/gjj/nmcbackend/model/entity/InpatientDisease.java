package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 患者诊断疾病表
 * @TableName inpatient_disease
 */
@TableName(value ="inpatient_disease")
@Data
public class InpatientDisease {
    /**
     * 患者疾病 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 住院患者表外键
     */
    private Integer patientId;

    /**
     * 疾病表外键
     */
    private Integer diseaseId;

    /**
     * 疾病诊断时间
     */
    private Date orderTime;

    /**
     * 诊断类型（1、入院诊断，2、主要诊断，3、其他诊断）
     */
    private Integer diseaseType;
}