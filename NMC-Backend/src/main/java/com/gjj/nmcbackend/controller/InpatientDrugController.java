package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDrugRequest;
import com.gjj.nmcbackend.model.vo.InpatientDrugVO;
import com.gjj.nmcbackend.service.InpatientDrugService;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api(tags = "住院药品管理", value = "住院患者药品相关操作")
@RestController
@RequestMapping("/inpatient/drug")
public class InpatientDrugController {

    @Resource
    InpatientDrugService inpatientDrugService;

    @ApiOperation(value = "添加住院药品", notes = "为住院患者添加药品使用记录")
    @ApiImplicitParam(name = "request", value = "药品添加请求参数", required = true, dataType = "AddInpatientDrugRequest")
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Boolean> addDrug(@RequestBody AddInpatientDrugRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = inpatientDrugService.addDrug(request);
        return ResultUtils.success(result);
    }

    /**
     * 检查患者是否已添加该药品
     */
    @ApiOperation(value = "检查药品记录", notes = "检查患者是否已存在特定药品使用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "drugId", value = "药品ID", required = true, paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
    @GetMapping("/check")
    public BaseResponse<Boolean> checkDrugExists(
            @RequestParam Integer patientId,
            @RequestParam Integer drugId) {
        boolean exists = inpatientDrugService.checkDrugExists(patientId, drugId);
        ThrowUtils.throwIf(!exists, new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        return ResultUtils.success(exists);
    }

    @ApiOperation(value = "分页查询住院药品", notes = "分页获取住院药品使用记录，可选项按药品中文名称筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "chineseName", value = "药品中文名称(可选)", paramType = "query", dataType = "String")
    })
    @UserAuthCheck
    @GetMapping("/list/page/vo")
    public BaseResponse<Page<InpatientDrugVO>> listDrugInfoByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String chineseName) {
        Page<InpatientDrugVO> page = inpatientDrugService.listInpatientDrugByPage(current, size, chineseName);
        return ResultUtils.success(page);
    }

    /**
     * 删除患者药品
     */
    @ApiOperation(value = "删除药品记录", notes = "根据患者ID和药品ID删除特定的药品使用记录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "drugId", value = "药品ID", required = true, paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteDrug(
            @RequestParam Integer patientId,
            @RequestParam Integer drugId) {
        boolean result = inpatientDrugService.deleteByPatientAndDrug(patientId, drugId);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR, "删除失败，记录不存在");
        return ResultUtils.success(result);
    }
}