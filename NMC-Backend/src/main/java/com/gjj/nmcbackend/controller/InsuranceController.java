package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.model.entity.DiseaseInfo;
import com.gjj.nmcbackend.model.entity.PatientRegistration;
import com.gjj.nmcbackend.model.vo.*;
import com.gjj.nmcbackend.service.*;
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Api(tags = "医保管理", value = "医保相关操作接口")
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

    @Resource
    private HospitalReimbursementService hospitalReimbursementService;

    @Resource
    private InsuranceCostService insuranceCostService;

    /**
     * 分页查询医保参保人员
     */
    @ApiOperation(value = "分页查询参保人员", notes = "分页查询医保参保人员信息，可按姓名筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "患者姓名(可选)", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
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
     */
    @ApiOperation(value = "更新参保人员信息", notes = "更新指定参保人员的基本信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "患者ID", required = true, paramType = "path", dataType = "Integer"),
            @ApiImplicitParam(name = "patient", value = "更新后的患者信息", required = true, dataType = "PatientRegistration")
    })
    @UserAuthCheck
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
    @ApiOperation(value = "查询患者药品费用", notes = "分页查询指定参保人员的药品使用及费用信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "5", paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
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
    @ApiOperation(value = "查询患者诊疗费用", notes = "分页查询指定参保人员的诊疗项目及费用信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "5", paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
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
    @ApiOperation(value = "查询患者医疗服务费用", notes = "分页查询指定参保人员的医疗服务及费用信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "5", paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
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
    @ApiOperation(value = "查询患者疾病诊断", notes = "分页查询指定参保人员的疾病诊断信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "5", paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
    @GetMapping("/disease/list")
    public BaseResponse<Page<PatientDiseaseVO>> listDiseaseByPatientId(
            @RequestParam Integer patientId,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "5") Integer size) {
        Page<PatientDiseaseVO> page = inpatientDiseaseService
                .listPatientDisease(patientId, current, size);
        return ResultUtils.success(page);
    }

    /**
     * 根据患者id的工作状态、医院类型进行分页查询
     */
    @ApiOperation(value = "查询医院报销比例", notes = "根据患者工作状态和医院等级查询报销比例")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "workStatus", value = "工作状态(在职/退休)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "hospitalLevel", value = "医院等级", defaultValue = "一级医院", paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "Integer")
    })
    @UserAuthCheck
    @GetMapping("/reimbursement/hospital/list")
    public BaseResponse<List<HospitalReimbursementVO>> getHospitalReimbursementList(
            @RequestParam Integer patientId,
            @RequestParam String workStatus,
            @RequestParam(defaultValue = "一级医院") String hospitalLevel,
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size) {
        String peopleTypeDesc = "在职人员".equals(workStatus) ? "在职人员" : "退休人员";
        List<HospitalReimbursementVO> list = hospitalReimbursementService.getVOByHospitalLevelAndPeopleType(hospitalLevel, peopleTypeDesc, current, size);
        return ResultUtils.success(list);
    }

    /**
     * 计算总报销费用
     */
    @ApiOperation(value = "计算总报销费用", notes = "根据患者ID、工作状态和医院等级计算总报销费用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "patientId", value = "患者ID", required = true, paramType = "query", dataType = "Integer"),
            @ApiImplicitParam(name = "workStatus", value = "工作状态(在职/退休)", required = true, paramType = "query", dataType = "String"),
            @ApiImplicitParam(name = "hospitalLevel", value = "医院等级", defaultValue = "一级医院", paramType = "query", dataType = "String")
    })
    @UserAuthCheck
    @GetMapping("/reimbursement/total")
    public BaseResponse<BigDecimal> calculateTotalReimbursement(
            @RequestParam Integer patientId,
            @RequestParam String workStatus,
            @RequestParam(defaultValue = "一级医院") String hospitalLevel) {
        BigDecimal totalReimbursement = insuranceCostService.calculateTotalReimbursement(patientId, workStatus, hospitalLevel);
        return ResultUtils.success(totalReimbursement);
    }
}