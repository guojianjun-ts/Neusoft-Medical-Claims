package com.gjj.nmcbackend.model.dto.Inpatient;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 用药结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

}
