package com.gjj.nmcbackend.model.dto.Inpatient;

import lombok.Data;

import java.util.Date;

@Data
public class AddInpatientDrugRequest {

    /**
     * 患者id
     */
    private Integer patientId;

    /**
     * 药品id
     */
    private Integer drugId;


    /**
     * 医嘱内容
     */
    private String doctorOrder;

    /**
     * 药品用法
     */
    private String useMethod;


    /**
     * 用药频率
     */
    private Integer orderNumber;

    /**
     * 用药起始时间
     */
    private Date startTime;

    /**
     * 用药结束时间
     */
    private Date endTime;

}
