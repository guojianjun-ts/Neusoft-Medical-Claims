package com.gjj.nmcbackend.controller;

import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.service.InsuranceCostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/insurance/cost")
@RequiredArgsConstructor
public class InsuranceCostController {

    private final InsuranceCostService insuranceCostService;

    /**
     * 获取患者总费用（药品+诊疗+服务）
     */
    @GetMapping("/total/{patientId}")
    public BaseResponse<BigDecimal> getTotalCost(
            @PathVariable Integer patientId) {
        return ResultUtils.success(insuranceCostService.calculateTotalCost(patientId));
    }

    /**
     * 获取药品分类费用（甲/乙/丙类）
     */
    @GetMapping("/drug/category/{patientId}")
    public BaseResponse<Map<String, BigDecimal>> getDrugCategoryCost(
            @PathVariable Integer patientId) {
        return ResultUtils.success(insuranceCostService.calculateDrugCategoryCost(patientId));
    }

    /**
     * 获取全部分类费用（药品/诊疗/服务）
     */
    @GetMapping("/all/category/{patientId}")
    public BaseResponse<Map<String, BigDecimal>> getAllCategoryCost(
            @PathVariable Integer patientId) {
        return ResultUtils.success(insuranceCostService.calculateAllCategoryCost(patientId));
    }
}