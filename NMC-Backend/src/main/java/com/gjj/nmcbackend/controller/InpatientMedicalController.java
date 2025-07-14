package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientMedicalRequest;
import com.gjj.nmcbackend.model.vo.InpatientMedicalVO;
import com.gjj.nmcbackend.service.InpatientMedicalService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inpatient/medical")
public class InpatientMedicalController {

    @Resource
    InpatientMedicalService inpatientMedicalService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addMedical(@RequestBody AddInpatientMedicalRequest request) {

        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);

        boolean result = inpatientMedicalService.addMedical(request);
        return ResultUtils.success(result);

    }


    /**
     * 检查患者是否已添加该疾病诊断
     */
    @GetMapping("/check")
    public BaseResponse<Boolean> checkMedicalExists(@RequestParam Integer patientId , @RequestParam Integer medicalId) {

        boolean exists = inpatientMedicalService.checkMedicalExists(patientId , medicalId);
        ThrowUtils.throwIf(!exists , new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        return ResultUtils.success(exists);
    }

    @GetMapping("/list/page/vo")
    public BaseResponse<Page<InpatientMedicalVO>> listMedicalInfoByPage(
            @RequestParam(defaultValue = "1") long current ,
            @RequestParam(defaultValue = "10") long size ,
            @RequestParam(required = false) String chineseName) {  // 添加可选搜索参数

        Page<InpatientMedicalVO> page = inpatientMedicalService.listInpatientMedicalByPage(current , size , chineseName);
        return ResultUtils.success(page);
    }


    /**
     * 删除诊断记录
     */
    @PostMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteMedical(@PathVariable Integer id) {
        boolean result = inpatientMedicalService.removeById(id);
        ThrowUtils.throwIf(!result , new BusinessException(ErrorCode.SYSTEM_ERROR));
        return ResultUtils.success(result);
    }
}
