package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiagnosisRequest;
import com.gjj.nmcbackend.model.vo.InpatientDiagnosisVO;
import com.gjj.nmcbackend.service.InpatientDiagnosisService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "住院诊断管理", value = "住院患者诊断相关操作")
@RestController
@RequestMapping("/inpatient/diagnosis")
public class InpatientDiagnosisController {

    @Resource
    InpatientDiagnosisService inpatientDiagnosisService;

    @ApiOperation(value = "添加住院诊断", notes = "为住院患者添加新的诊断记录")
    @ApiImplicitParam(name = "request", value = "住院诊断添加请求参数", required = true, dataType = "AddInpatientDiagnosisRequest")
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Boolean> addDiagnosis(@RequestBody AddInpatientDiagnosisRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = inpatientDiagnosisService.addDiagnosis(request);
        return ResultUtils.success(result);
    }

    /**
     * 检查患者是否已添加该疾病诊断
     */
    @ApiOperation(value = "检查诊断记录是否存在", notes = "检查指定患者是否已存在特定诊断记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "diagnosisId", value = "诊断ID", required = true, paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
    @GetMapping("/check")
    public BaseResponse<Boolean> checkDiagnosisExists(
            @RequestParam Integer patientId,
            @RequestParam Integer diagnosisId) {
        boolean exists = inpatientDiagnosisService.checkDiagnosisExists(patientId, diagnosisId);
        ThrowUtils.throwIf(!exists, new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        return ResultUtils.success(exists);
    }

    @ApiOperation(value = "分页查询住院诊断", notes = "分页获取住院诊断信息，可选项按治疗名称筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "treatmentName", value = "治疗名称(可选)", paramType = "query", dataType = "String")
    })
    @UserAuthCheck
    @GetMapping("/list/page/vo")
    public BaseResponse<Page<InpatientDiagnosisVO>> listDiagnosisInfoByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String treatmentName) {
        Page<InpatientDiagnosisVO> page = inpatientDiagnosisService.listInpatientDiagnosisByPage(current, size, treatmentName);
        return ResultUtils.success(page);
    }

    /**
     * 删除诊断记录
     */
    @ApiOperation(value = "删除诊断记录", notes = "根据患者ID和诊断ID删除特定的诊断记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "diagnosisId", value = "诊断ID", required = true, paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
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