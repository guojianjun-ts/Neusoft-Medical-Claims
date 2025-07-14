package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.entity.PatientRegistration;
import com.gjj.nmcbackend.model.vo.PatientVO;
import com.gjj.nmcbackend.service.PatientRegistrationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/patient")
public class PatientRegistrationController {


    @Resource
    private PatientRegistrationService patientRegistrationService;

    @PostMapping("/add")
    public BaseResponse<Integer> addPatient(@RequestBody PatientRegistration patientRegistration) {
        ThrowUtils.throwIf(patientRegistration == null , ErrorCode.PARAMS_ERROR);
        patientRegistrationService.save(patientRegistration);
        return ResultUtils.success(patientRegistration.getId());
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updatePatient(@RequestBody PatientRegistration patientRegistration) {
        ThrowUtils.throwIf(patientRegistration == null || patientRegistration.getId() == null , ErrorCode.PARAMS_ERROR);
        Boolean result = patientRegistrationService.updateById(patientRegistration);
        return ResultUtils.success(result);
    }

    @GetMapping("/list/page/vo")
    public BaseResponse<Page<PatientVO>> listPatientVOByPage(
            @RequestParam(defaultValue = "1") long current ,
            @RequestParam(defaultValue = "10") long size ,
            @RequestParam(required = false) String patientName) {  // 添加可选搜索参数

        Page<PatientVO> page = patientRegistrationService.listPatientInfoByPage(current , size , patientName);
        return ResultUtils.success(page);
    }

    @GetMapping("/get")
    public BaseResponse<PatientRegistration> listPatientByPage(@RequestParam Integer patientId) {
        ThrowUtils.throwIf(patientId == null , ErrorCode.PARAMS_ERROR);
        PatientRegistration patientRegistration = patientRegistrationService.getById(patientId);
        return ResultUtils.success(patientRegistration);
    }

}
