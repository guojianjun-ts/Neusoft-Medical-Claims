package com.gjj.nmcbackend.model.dto.Inpatient;

import lombok.Data;

@Data
public class AddInpatientDiseaseRequest {

    /**
     * 病人id
     */
    private Integer patientId;

    /**
     * 疾病id
     */
    private Integer diseaseId;

    /**
     * 疾病类型（入院/主要/其它）
     */
    private Integer diseaseType;
}