package com.gjj.nmcbackend.model.vo;

import lombok.Data;

@Data
public class PatientDiseaseVO {
    private Integer id;          // 患者疾病ID (inpatient_disease.id)
    private String diseaseName;  // 疾病名称 (disease_info.disease_name)
    private Integer diseaseType; // 诊断类型 (inpatient_disease.disease_type)
}