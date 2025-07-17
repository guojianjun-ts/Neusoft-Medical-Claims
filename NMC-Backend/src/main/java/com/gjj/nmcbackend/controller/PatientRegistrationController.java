package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.entity.PatientRegistration;
import com.gjj.nmcbackend.model.vo.PatientVO;
import com.gjj.nmcbackend.service.PatientRegistrationService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "患者信息管理", value = "患者注册及相关信息操作")
@RestController
@RequestMapping("/patient")
public class PatientRegistrationController {

    @Resource
    private PatientRegistrationService patientRegistrationService;

    @ApiOperation(value = "添加患者信息", notes = "注册新的患者信息")
    @ApiImplicitParam(name = "patientRegistration", value = "患者信息实体", required = true, dataType = "PatientRegistration")
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Integer> addPatient(@RequestBody PatientRegistration patientRegistration) {
        ThrowUtils.throwIf(patientRegistration == null, ErrorCode.PARAMS_ERROR);
        patientRegistrationService.save(patientRegistration);
        return ResultUtils.success(patientRegistration.getId());
    }

    @ApiOperation(value = "更新患者信息", notes = "更新患者注册信息")
    @ApiImplicitParam(name = "patientRegistration", value = "患者信息实体(需包含ID)", required = true, dataType = "PatientRegistration")
    @UserAuthCheck
    @PostMapping("/update")
    public BaseResponse<Boolean> updatePatient(@RequestBody PatientRegistration patientRegistration) {
        ThrowUtils.throwIf(patientRegistration == null || patientRegistration.getId() == null, ErrorCode.PARAMS_ERROR);
        Boolean result = patientRegistrationService.updateById(patientRegistration);
        return ResultUtils.success(result);
    }

    /**
     * 分页获取患者信息列表
     */
    @ApiOperation(value = "分页查询患者信息", notes = "分页获取患者信息列表，可选项按患者姓名筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "patientName", value = "患者姓名(可选)", paramType = "query", dataType = "String")
    })
    @UserAuthCheck
    @GetMapping("/list/page/vo")
    public BaseResponse<Page<PatientVO>> listPatientByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String patientName) {
        Page<PatientVO> page = patientRegistrationService.getCurrentPatient(current, size, patientName);
        return ResultUtils.success(page);
    }

    /**
     * 获取当前患者信息
     */
    @ApiOperation(value = "查询患者详情", notes = "根据患者ID获取患者详细信息")
    @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer")
    @UserAuthCheck
    @GetMapping("/get")
    public BaseResponse<PatientRegistration> getCurrentPatient(@RequestParam Integer patientId) {
        ThrowUtils.throwIf(patientId == null, ErrorCode.PARAMS_ERROR);
        PatientRegistration patientRegistration = patientRegistrationService.getById(patientId);
        return ResultUtils.success(patientRegistration);
    }
}