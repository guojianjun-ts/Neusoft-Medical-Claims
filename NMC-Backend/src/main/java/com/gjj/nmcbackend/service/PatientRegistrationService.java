package com.gjj.nmcbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.model.entity.PatientRegistration;
import com.baomidou.mybatisplus.extension.service.IService;
import com.gjj.nmcbackend.model.vo.PatientVO;

/**
* @author 78568
* @description 针对表【patient_registration(患者信息表)】的数据库操作Service
* @createDate 2025-07-14 09:16:50
*/
public interface PatientRegistrationService extends IService<PatientRegistration> {

    Page<PatientVO> getCurrentPatient(long current, long size, String patientName);

    Page<PatientRegistration> listInsuredPatients(String name , Integer current , Integer size);

}
