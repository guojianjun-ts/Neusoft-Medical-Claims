package com.gjj.nmcbackend.model.dto.Inpatient;

import lombok.Data;

import java.util.Date;

@Data
public class AddInpatientMedicalRequest {

    /**
     * 患者id
     */
    private Integer patientId;

    /**
     * 医疗服务id
     */
    private Integer medicalId;

    /**
     * 医嘱内容
     */
    private String doctorOrder;

    /**
     * 医疗服务用法用法
     */
    private String useMethod;

    /**
     * 用药起始时间
     */
    private Date orderTime;

    /**
     * 用药频率
     */
    private Integer orderNumber;


}
