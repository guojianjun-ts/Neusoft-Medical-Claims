package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientMedicalRequest;
import com.gjj.nmcbackend.model.vo.InpatientMedicalVO;
import com.gjj.nmcbackend.service.InpatientMedicalService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "住院医疗项目管理", value = "住院患者医疗项目相关操作")
@RestController
@RequestMapping("/inpatient/medical")
public class InpatientMedicalController {

    @Resource
    InpatientMedicalService inpatientMedicalService;

    @ApiOperation(value = "添加医疗项目记录", notes = "为住院患者添加新的医疗项目使用记录")
    @ApiImplicitParam(name = "request", value = "医疗项目添加请求参数", required = true, dataType = "AddInpatientMedicalRequest")
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Boolean> addMedical(@RequestBody AddInpatientMedicalRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = inpatientMedicalService.addMedical(request);
        return ResultUtils.success(result);
    }

    /**
     * 检查患者是否已添加该医疗项目
     */
    @ApiOperation(value = "检查医疗项目记录", notes = "检查患者是否已存在特定医疗项目使用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "medicalId", value = "医疗项目ID", required = true, paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
    @GetMapping("/check")
    public BaseResponse<Boolean> checkMedicalExists(
            @RequestParam Integer patientId,
            @RequestParam Integer medicalId) {
        boolean exists = inpatientMedicalService.checkMedicalExists(patientId, medicalId);
        ThrowUtils.throwIf(!exists, new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        return ResultUtils.success(exists);
    }

    @ApiOperation(value = "分页查询医疗项目", notes = "分页获取住院医疗项目使用记录，可选项按项目中文名称筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "chineseName", value = "医疗项目中文名称(可选)", paramType = "query", dataType = "String")
    })
    @UserAuthCheck
    @GetMapping("/list/page/vo")
    public BaseResponse<Page<InpatientMedicalVO>> listMedicalInfoByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String chineseName) {
        Page<InpatientMedicalVO> page = inpatientMedicalService.listInpatientMedicalByPage(current, size, chineseName);
        return ResultUtils.success(page);
    }

    /**
     * 删除医疗项目记录
     */
    @ApiOperation(value = "删除医疗项目记录", notes = "根据患者ID和医疗项目ID删除特定的医疗项目使用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "medicalId", value = "医疗项目ID", required = true, paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteMedical(
            @RequestParam Integer patientId,
            @RequestParam Integer medicalId) {
        ThrowUtils.throwIf(patientId == null || patientId <= 0, ErrorCode.PARAMS_ERROR, "患者ID不能为空且必须大于0");
        ThrowUtils.throwIf(medicalId == null || medicalId <= 0, ErrorCode.PARAMS_ERROR, "医疗项目ID不能为空且必须大于0");
        boolean result = inpatientMedicalService.deleteByPatientAndMedical(patientId, medicalId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除失败，记录不存在");
        return ResultUtils.success(result);
    }
}