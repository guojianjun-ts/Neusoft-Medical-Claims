package com.gjj.nmcbackend.model.dto.Inpatient;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class AddInpatientDiagnosisRequest {


    /**
     * 患者表外键
     */
    private Integer patientId;

    /**
     * 诊疗项目表外键
     */
    private Integer diagnosisId;

    /**
     * 医生医嘱诊疗项目信息
     */
    private String doctorOrder;

    /**
     * 用法详情说明
     */
    private String useMethod;

    /**
     * 开立时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date orderTime;
}