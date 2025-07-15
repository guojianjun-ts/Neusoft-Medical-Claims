package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientMedicalRequest;
import com.gjj.nmcbackend.model.entity.InpatientMedical;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjj.nmcbackend.model.vo.DiagnosisCostVO;
import com.gjj.nmcbackend.model.vo.InpatientMedicalVO;
import com.gjj.nmcbackend.model.vo.MedicalServiceCostVO;

/**
 * @author 78568
 * @description 针对表【inpatient_medical(患者医嘱药品信息表)】的数据库操作Service
 * @createDate 2025-07-14 14:19:28
 */
public interface InpatientMedicalService extends IService<InpatientMedical> {

    boolean addMedical(AddInpatientMedicalRequest request);


    boolean checkMedicalExists(Integer patientId, Integer medicalId);

    Page<InpatientMedicalVO> listInpatientMedicalByPage(long current, long size, String medicalName);

    boolean deleteByPatientAndMedical(Integer patientId , Integer medicalId);

    Page<MedicalServiceCostVO> listMedicalServiceCostByPatientId(Integer patientId, Integer current, Integer size);


}
