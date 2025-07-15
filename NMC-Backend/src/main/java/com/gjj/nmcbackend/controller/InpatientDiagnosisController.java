package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiagnosisRequest;
import com.gjj.nmcbackend.model.vo.InpatientDiagnosisVO;
import com.gjj.nmcbackend.service.InpatientDiagnosisService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inpatient/diagnosis")
public class InpatientDiagnosisController {

    @Resource
    InpatientDiagnosisService inpatientDiagnosisService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addDiagnosis(@RequestBody AddInpatientDiagnosisRequest request) {

        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);

        boolean result = inpatientDiagnosisService.addDiagnosis(request);
        return ResultUtils.success(result);

    }


    /**
     * 检查患者是否已添加该疾病诊断
     */
    @GetMapping("/check")
    public BaseResponse<Boolean> checkDiagnosisExists(@RequestParam Integer patientId , @RequestParam Integer diagnosisId) {

        boolean exists = inpatientDiagnosisService.checkDiagnosisExists(patientId , diagnosisId);
        ThrowUtils.throwIf(!exists , new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        return ResultUtils.success(exists);
    }

    @GetMapping("/list/page/vo")
    public BaseResponse<Page<InpatientDiagnosisVO>> listDiagnosisInfoByPage(
            @RequestParam(defaultValue = "1") long current ,
            @RequestParam(defaultValue = "10") long size ,
            @RequestParam(required = false) String treatmentName) {  // 添加可选搜索参数

        Page<InpatientDiagnosisVO> page = inpatientDiagnosisService.listInpatientDiagnosisByPage(current , size , treatmentName);
        return ResultUtils.success(page);
    }

    /**
     * 删除诊断记录
     */
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDiagnosis(
            @RequestParam Integer patientId,
            @RequestParam Integer diagnosisId) {

        ThrowUtils.throwIf(patientId == null || patientId <= 0, ErrorCode.PARAMS_ERROR, "患者ID不能为空且必须大于0");
        ThrowUtils.throwIf(diagnosisId == null || diagnosisId <= 0, ErrorCode.PARAMS_ERROR, "诊断ID不能为空且必须大于0");

        boolean result = inpatientDiagnosisService.deleteByPatientAndDiagnosis(patientId, diagnosisId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除失败，记录不存在");
        return ResultUtils.success(result);
    }
}
