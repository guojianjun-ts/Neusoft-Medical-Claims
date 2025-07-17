package com.gjj.nmcbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gjj.nmcbackend.mapper.*;
import com.gjj.nmcbackend.model.entity.*;
import com.gjj.nmcbackend.service.HospitalReimbursementService;
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

    @Autowired
    private DrugReimbursementMapper drugReimbursementMapper;

    @Autowired
    private HospitalReimbursementService hospitalReimbursementService;

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

    @Override
    public BigDecimal calculateTotalReimbursement(Integer patientId, String workStatus, String hospitalLevel) {
        // 获取各类药品费用
        Map<String, BigDecimal> drugCategoryCost = calculateDrugCategoryCost(patientId);
        // 获取总费用
        BigDecimal totalCost = calculateTotalCost(patientId);
        // 获取其他费用（诊疗项目和医疗服务费用）
        Map<String, BigDecimal> allCategoryCost = calculateAllCategoryCost(patientId);
        BigDecimal otherCost = allCategoryCost.get("诊疗项目").add(allCategoryCost.get("医疗服务"));

        // 获取药品报销比例
        List<DrugReimbursement> drugReimbursements = drugReimbursementMapper.selectList(null);
        Map<String, Integer> drugReimbursementMap = drugReimbursements.stream()
                .collect(java.util.stream.Collectors.toMap(DrugReimbursement::getDrugReimbursementType, DrugReimbursement::getDrugReimbursementProportion));

        // 计算各类药品报销费用
        BigDecimal drugReimbursementTotal = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : drugCategoryCost.entrySet()) {
            String drugType = entry.getKey();
            BigDecimal cost = entry.getValue();
            Integer proportion = drugReimbursementMap.get(drugType);
            drugReimbursementTotal = drugReimbursementTotal.add(cost.multiply(BigDecimal.valueOf(proportion)).divide(BigDecimal.valueOf(100)));
        }

        // 获取医院报销信息
        Integer peopleType = "在职人员".equals(workStatus) ? 1 : 0;
        List<HospitalReimbursement> hospitalReimbursements = hospitalReimbursementService.list(new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<HospitalReimbursement>()
                .eq(HospitalReimbursement::getHospitalLevel, hospitalLevel)
                .eq(HospitalReimbursement::getPeopleType, peopleType)
                .orderByAsc(HospitalReimbursement::getMinPayLevel));

        // 找到最近的起付线和报销比例
        HospitalReimbursement selectedReimbursement = null;
        for (HospitalReimbursement reimbursement : hospitalReimbursements) {
            if (totalCost.compareTo(BigDecimal.valueOf(reimbursement.getMinPayLevel())) >= 0) {
                selectedReimbursement = reimbursement;
            } else {
                break;
            }
        }

        if (selectedReimbursement == null) {
            return BigDecimal.ZERO;
        }

        // 计算总报销费用
        BigDecimal deductible = BigDecimal.valueOf(selectedReimbursement.getMinPayLevel());
        BigDecimal reimbursementProportion = BigDecimal.valueOf(selectedReimbursement.getPayProportion()).divide(BigDecimal.valueOf(100));
        BigDecimal totalReimbursement = drugReimbursementTotal.add(otherCost).subtract(deductible).multiply(reimbursementProportion);

        return totalReimbursement.max(BigDecimal.ZERO);
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