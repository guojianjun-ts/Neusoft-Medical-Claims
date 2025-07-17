package com.gjj.nmcbackend.service;

import java.math.BigDecimal;
import java.util.Map;

public interface InsuranceCostService {

    /**
     * 计算患者的总诊疗费用
     * @param patientId 患者ID
     * @return 总费用
     */
    BigDecimal calculateTotalCost(Integer patientId);

    /**
     * 按药品类别计算患者的诊疗费用
     * @param patientId 患者ID
     * @return 各药品类别及其对应费用
     */
    Map<String, BigDecimal> calculateDrugCategoryCost(Integer patientId);

    /**
     * 按所有类别计算患者的诊疗费用
     * @param patientId 患者ID
     * @return 各诊疗类别及其对应费用
     */
    Map<String, BigDecimal> calculateAllCategoryCost(Integer patientId);


    /**
     * 计算总报销费用
     */
    BigDecimal calculateTotalReimbursement(Integer patientId, String workStatus, String hospitalLevel);

}