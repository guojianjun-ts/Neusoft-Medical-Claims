package com.gjj.nmcbackend.controller;

import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.service.InsuranceCostService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@Api(tags = "医保费用计算", value = "医保费用相关计算接口")
@RestController
@RequestMapping("/api/insurance/cost")
@RequiredArgsConstructor
public class InsuranceCostController {

    private final InsuranceCostService insuranceCostService;

    /**
     * 获取患者总费用（药品+诊疗+服务）
     */
    @ApiOperation(value = "计算患者总费用", notes = "计算患者药品、诊疗和服务项目的总费用")
    @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "path", dataType = "Integer")
    @UserAuthCheck
    @GetMapping("/total/{patientId}")
    public BaseResponse<BigDecimal> getTotalCost(
            @PathVariable Integer patientId) {
        return ResultUtils.success(insuranceCostService.calculateTotalCost(patientId));
    }

    /**
     * 获取药品分类费用（甲/乙/丙类）
     */
    @ApiOperation(value = "计算药品分类费用", notes = "按药品分类（甲/乙/丙类）计算患者药品费用")
    @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "path", dataType = "Integer")
    @UserAuthCheck
    @GetMapping("/drug/category/{patientId}")
    public BaseResponse<Map<String, BigDecimal>> getDrugCategoryCost(
            @PathVariable Integer patientId) {
        return ResultUtils.success(insuranceCostService.calculateDrugCategoryCost(patientId));
    }

    /**
     * 获取全部分类费用（药品/诊疗/服务）
     */
    @ApiOperation(value = "计算全部分类费用", notes = "按药品、诊疗和服务分类计算患者各项费用")
    @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "path", dataType = "Integer")
    @UserAuthCheck
    @GetMapping("/all/category/{patientId}")
    public BaseResponse<Map<String, BigDecimal>> getAllCategoryCost(
            @PathVariable Integer patientId) {
        return ResultUtils.success(insuranceCostService.calculateAllCategoryCost(patientId));
    }
}