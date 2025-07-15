package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiagnosisRequest;
import com.gjj.nmcbackend.model.entity.DiagnosisTreatment;
import com.gjj.nmcbackend.model.entity.InpatientDiagnosis;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjj.nmcbackend.model.vo.DiagnosisCostVO;
import com.gjj.nmcbackend.model.vo.InpatientDiagnosisVO;

/**
 * @author 78568
 * @description 针对表【inpatient_diagnosis(患者医嘱药品信息表)】的数据库操作Service
 * @createDate 2025-07-14 14:19:28
 */
public interface InpatientDiagnosisService extends IService<InpatientDiagnosis> {

    boolean addDiagnosis(AddInpatientDiagnosisRequest request);


    boolean checkDiagnosisExists(Integer patientId, Integer diagnosisId);

    Page<InpatientDiagnosisVO> listInpatientDiagnosisByPage(long current, long size, String diagnosisName);

    boolean deleteByPatientAndDiagnosis(Integer patientId , Integer diagnosisId);

    /**
     * 根据患者ID分页查询诊疗项目费用
     */
    Page<DiagnosisCostVO> listDiagnosisCostByPatientId(Integer patientId, Integer current, Integer size);
}
