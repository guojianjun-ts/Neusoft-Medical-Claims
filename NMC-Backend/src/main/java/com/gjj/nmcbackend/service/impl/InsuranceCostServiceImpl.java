package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gjj.nmcbackend.mapper.*;
import com.gjj.nmcbackend.model.entity.*;
import com.gjj.nmcbackend.service.InsuranceCostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class InsuranceCostServiceImpl implements InsuranceCostService {

    @Autowired
    private InpatientDrugMapper inpatientDrugMapper;

    @Autowired
    private InpatientDiagnosisMapper inpatientDiagnosisMapper;

    @Autowired
    private InpatientMedicalMapper inpatientMedicalMapper;

    @Autowired
    private DrugInfoMapper drugInfoMapper;

    @Autowired
    private DiagnosisTreatmentMapper diagnosisTreatmentMapper;

    @Autowired
    private MedicalServiceMapper medicalServiceMapper;

    @Override
    public BigDecimal calculateTotalCost(Integer patientId) {
        // 1. 计算药品总费用（只计算单价）
        BigDecimal drugCost = calculateDrugCost(patientId);

        // 2. 计算诊疗总费用（只计算单价）
        BigDecimal diagnosisCost = calculateDiagnosisCost(patientId);

        // 3. 计算服务总费用（只计算单价）
        BigDecimal serviceCost = calculateServiceCost(patientId);

        // 4. 汇总所有费用
        return drugCost.add(diagnosisCost).add(serviceCost);
    }



    @Override
    public Map<String, BigDecimal> calculateDrugCategoryCost(Integer patientId) {
        // 1. 初始化结果Map（保证顺序）
        Map<String, BigDecimal> result = new LinkedHashMap<>();
        result.put("甲类", BigDecimal.ZERO);
        result.put("乙类", BigDecimal.ZERO);
        result.put("丙类", BigDecimal.ZERO);

        // 2. 查询有效药品医嘱
        QueryWrapper<InpatientDrug> wrapper = new QueryWrapper<>();
        wrapper.eq("patientId", patientId)
                .eq("status", 1);

        List<InpatientDrug> drugs = inpatientDrugMapper.selectList(wrapper);
        if (drugs == null || drugs.isEmpty()) {
            return result;
        }

        // 3. 批量获取药品信息（使用驼峰字段）
        List<Integer> drugIds = drugs.stream()
                .map(InpatientDrug::getDrugId)
                .distinct()
                .collect(Collectors.toList());

        QueryWrapper<DrugInfo> drugQueryWrapper = new QueryWrapper<>();
        drugQueryWrapper.in("id", drugIds)
                .select("id", "drugPrice", "insuranceType"); // 驼峰字段
        List<DrugInfo> drugInfos = drugInfoMapper.selectList(drugQueryWrapper);

        // 4. 分类统计（无需校验分类）
        drugInfos.forEach(drug -> {
            BigDecimal price = drug.getDrugPrice() != null ?
                    drug.getDrugPrice() : BigDecimal.ZERO;
            result.put(drug.getInsuranceType(),
                    result.get(drug.getInsuranceType()).add(price));
        });

        return result;
    }



    @Override
    public Map<String, BigDecimal> calculateAllCategoryCost(Integer patientId) {
        // 1. 初始化结果Map（保证顺序）
        Map<String, BigDecimal> result = new LinkedHashMap<>();

        // 2. 复用已有方法计算各模块总费用
        BigDecimal drugCost = calculateDrugCost(patientId);
        BigDecimal diagnosisCost = calculateDiagnosisCost(patientId);
        BigDecimal serviceCost = calculateServiceCost(patientId);

        // 3. 按前端需要的分类名称填充结果
        result.put("保险药品", drugCost);
        result.put("诊疗项目", diagnosisCost);
        result.put("医疗服务", serviceCost);

        return result;
    }

    /**
     * 计算药品总费用（只计算单价）
     */
    private BigDecimal calculateDrugCost(Integer patientId) {
        QueryWrapper<InpatientDrug> wrapper = new QueryWrapper<>();
        wrapper.eq("patientId", patientId)
                .eq("status", 1); // 有效医嘱

        BigDecimal total = BigDecimal.ZERO;
        List<InpatientDrug> drugs = inpatientDrugMapper.selectList(wrapper);

        for (InpatientDrug drug : drugs) {
            DrugInfo drugInfo = drugInfoMapper.selectById(drug.getDrugId());
            if (drugInfo != null && drugInfo.getDrugPrice() != null) {
                total = total.add(drugInfo.getDrugPrice()); // 直接累加单价
            }
        }
        return total;
    }

    /**
     * 计算诊疗总费用（只计算单价）
     */
    private BigDecimal calculateDiagnosisCost(Integer patientId) {
        QueryWrapper<InpatientDiagnosis> wrapper = new QueryWrapper<>();
        wrapper.eq("patientId", patientId)
                .eq("status", 1); // 有效医嘱

        BigDecimal total = BigDecimal.ZERO;
        List<InpatientDiagnosis> diagnoses = inpatientDiagnosisMapper.selectList(wrapper);

        for (InpatientDiagnosis diagnosis : diagnoses) {
            DiagnosisTreatment treatment = diagnosisTreatmentMapper.selectById(diagnosis.getDiagnosisId());
            if (treatment != null && treatment.getTreatmentPrice() != null) {
                total = total.add(treatment.getTreatmentPrice()); // 直接累加单价
            }
        }
        return total;
    }

    /**
     * 计算服务总费用（只计算单价）
     */
    private BigDecimal calculateServiceCost(Integer patientId) {
        QueryWrapper<InpatientMedical> wrapper = new QueryWrapper<>();
        wrapper.eq("patientId", patientId)
                .eq("status", 1); // 有效医嘱

        BigDecimal total = BigDecimal.ZERO;
        List<InpatientMedical> services = inpatientMedicalMapper.selectList(wrapper);

        for (InpatientMedical service : services) {
            MedicalService medicalService = medicalServiceMapper.selectById(service.getMedicalId());
            if (medicalService != null && medicalService.getServicePrice() != null) {
                total = total.add(medicalService.getServicePrice()); // 直接累加单价
            }
        }
        return total;
    }
}