package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiseaseRequest;
import com.gjj.nmcbackend.model.entity.DiseaseInfo;
import com.gjj.nmcbackend.model.entity.InpatientDisease;
import com.gjj.nmcbackend.model.vo.PatientDiseaseVO;

public interface InpatientDiseaseService extends IService<InpatientDisease> {
    Page<InpatientDisease> listDiseaseByPage(long current, long size, Integer patientId, Integer diseaseType);

    boolean addDisease(AddInpatientDiseaseRequest request);

    boolean checkDiseaseExists(Integer patientId, Integer diseaseId, Integer diseaseType);

    /**
     * 查询患者疾病信息（含诊断类型）
     */
    Page<PatientDiseaseVO> listPatientDisease(Integer patientId, Integer current, Integer size);
}