package com.gjj.nmcbackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 患者医嘱药品信息表
 * @TableName inpatient_drug
 */
@TableName(value ="inpatient_drug")
@Data
public class InpatientDrug {
    /**
     * 患者医嘱药品信息 ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 起始时间
     */
    private Date startTime;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 医嘱内容
     */
    private String doctorOrder;

    /**
     * 医嘱频次（次/天）
     */
    private Integer orderNumber;

    /**
     * 用法
     */
    private String useMethod;

    /**
     * 药品表外键
     */
    private Integer drugId;

    /**
     * 患者表外键
     */
    private Integer patientId;

    /**
     * 医嘱状态：1-正常执行 0-作废 2-停止
     */
    private Integer status;
}