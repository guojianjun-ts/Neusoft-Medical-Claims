package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiseaseRequest;
import com.gjj.nmcbackend.model.entity.InpatientDisease;

public interface InpatientDiseaseService extends IService<InpatientDisease> {
    Page<InpatientDisease> listDiseaseByPage(long current, long size, Integer patientId, Integer diseaseType);

    boolean addDisease(AddInpatientDiseaseRequest request);

    boolean checkDiseaseExists(Integer patientId, Integer diseaseId, Integer diseaseType);
}