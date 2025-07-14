package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 患者医嘱诊疗项目信息表
 * @TableName inpatient_diagnosis
 */
@TableName(value ="inpatient_diagnosis")
@Data
public class InpatientDiagnosis {
    /**
     * 患者医嘱诊疗项目 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 开立时间
     */
    private Date orderTime;

    /**
     * 医生医嘱诊疗项目信息
     */
    private String doctorOrder;

    /**
     * 用法详情说明
     */
    private String useMethod;

    /**
     * 诊疗项目表外键
     */
    private Integer diagnosisId;

    /**
     * 医嘱状态：1-正常执行 0-作废 2-停止
     */
    private Integer status;

    /**
     * 患者表外键
     */
    private Integer patientId;
}