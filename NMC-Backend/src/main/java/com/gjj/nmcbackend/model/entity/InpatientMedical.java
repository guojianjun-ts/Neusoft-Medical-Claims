package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 患者医嘱医疗服务项目信息表
 * @TableName inpatient_medical
 */
@TableName(value ="inpatient_medical")
@Data
public class InpatientMedical {
    /**
     * ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 开立时间
     */
    private Date orderTime;

    /**
     * 医嘱内容
     */
    private String doctorOrder;

    /**
     * 用法详情说明
     */
    private String useMethod;

    /**
     * 医疗服务表外键
     */
    private Integer medicalId;

    /**
     * 医嘱状态：1-正常执行 0-作废 2-停止
     */
    private Integer status;

    /**
     * 患者id
     */
    private Integer patientId;
}