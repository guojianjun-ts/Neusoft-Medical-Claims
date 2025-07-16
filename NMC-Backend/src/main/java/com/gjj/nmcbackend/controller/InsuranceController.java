package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.model.entity.DiseaseInfo;
import com.gjj.nmcbackend.model.entity.PatientRegistration;
import com.gjj.nmcbackend.model.vo.DiagnosisCostVO;
import com.gjj.nmcbackend.model.vo.DrugCostVO;
import com.gjj.nmcbackend.model.vo.MedicalServiceCostVO;
import com.gjj.nmcbackend.model.vo.PatientDiseaseVO;
import com.gjj.nmcbackend.service.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/insurance")
public class InsuranceController {

    @Resource
    private PatientRegistrationService patientRegistrationService;

    @Resource
    private InpatientDrugService inpatientDrugService;

    @Resource
    private InpatientDiagnosisService inpatientDiagnosisService;

    @Resource
    private InpatientMedicalService inpatientMedicalService;

    @Resource
    private InpatientDiseaseService inpatientDiseaseService;


    /**
     * 分页查询医保参保人员
     * @param name 患者姓名（可选）
     * @param current 当前页
     * @param size 每页大小
     * @return 分页的参保人员列表
     */
    @GetMapping("/patient/list")
    public BaseResponse<Page<PatientRegistration>> listInsuredPatients(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<PatientRegistration> page = patientRegistrationService
                .listInsuredPatients(name, current, size);
        return ResultUtils.success(page);
    }

    /**
     * 更新参保人员信息
     * @param id 患者ID
     * @param patient 更新后的患者信息
     * @return 操作结果
     */
    @PutMapping("/patient/{id}")
    public BaseResponse<Boolean> updatePatientInfo(
            @PathVariable Integer id,
            @RequestBody PatientRegistration patient) {

        patient.setId(id);
        boolean result = patientRegistrationService.updateById(patient);
        return ResultUtils.success(result);
    }

    /**
     * 根据患者id分页查询参保人员药品
     */
    @GetMapping("/cost/drug/list")
    public BaseResponse<Page<DrugCostVO>> listDrugCostByPatientId(
            @RequestParam Integer patientId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "5") Integer size) {
        return ResultUtils.success(inpatientDrugService.listDrugCostByPatientId(patientId, current, size));
    }

    /**
     * 根据患者id分页查询参保人员诊疗项目
     */
    @GetMapping("/cost/diagnosis/list")
    public BaseResponse<Page<DiagnosisCostVO>> listDiagnosisCostByPatientId(
            @RequestParam Integer patientId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "5") Integer size) {
        return ResultUtils.success(inpatientDiagnosisService.listDiagnosisCostByPatientId(patientId, current, size));
    }

    /**
     * 根据患者id分页查询参保人员医疗服务
     */
    @GetMapping("/cost/medical/list")
    public BaseResponse<Page<MedicalServiceCostVO>> listServiceCostByPatientId(
            @RequestParam Integer patientId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "5") Integer size) {
        return ResultUtils.success(inpatientMedicalService.listMedicalServiceCostByPatientId(patientId, current, size));
    }

    /**
     * 根据患者ID分页查询疾病诊断信息
     */
    @GetMapping("/disease/list")
    public BaseResponse<Page<PatientDiseaseVO>> listDiseaseByPatientId(
            @RequestParam Integer patientId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "5") Integer size) {

        Page<PatientDiseaseVO> page = inpatientDiseaseService
                .listPatientDisease(patientId, current, size);
        return ResultUtils.success(page);
    }

}