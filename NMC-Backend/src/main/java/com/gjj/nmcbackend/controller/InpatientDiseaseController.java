package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDiseaseRequest;
import com.gjj.nmcbackend.model.entity.DiseaseInfo;
import com.gjj.nmcbackend.model.entity.InpatientDisease;
import com.gjj.nmcbackend.service.DiseaseInfoService;
import com.gjj.nmcbackend.service.InpatientDiseaseService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inpatient/disease")
public class InpatientDiseaseController {

    @Resource
    private InpatientDiseaseService inpatientDiseaseService;

    @Resource
    private DiseaseInfoService diseaseInfoService;
    /**
     * 添加诊断记录
     */
    @PostMapping("/add")
    public BaseResponse<Boolean> addDisease(@RequestBody AddInpatientDiseaseRequest request) {
        boolean result = inpatientDiseaseService.addDisease(request);
        return ResultUtils.success(result);
    }

    /**
     * 删除诊断记录
     */
    @PostMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteDisease(@PathVariable Integer id) {
        boolean result = inpatientDiseaseService.removeById(id);
        return ResultUtils.success(result);
    }

    @GetMapping("/list/page/vo")
    public BaseResponse<Page<DiseaseInfo>> listDiseaseInfoByPage(
            @RequestParam(defaultValue = "1") long current ,
            @RequestParam(defaultValue = "10") long size ,
            @RequestParam(required = false) String diseaseName) {  // 添加可选搜索参数

        Page<DiseaseInfo> page = diseaseInfoService.listDiseaseInfoByPage(current , size , diseaseName);
        return ResultUtils.success(page);
    }
    /**
     * 检查患者是否已添加该疾病诊断
     */
    @GetMapping("/check")
    public BaseResponse<Boolean> checkDiseaseExists(
            @RequestParam Integer patientId,
            @RequestParam Integer diseaseId,
            @RequestParam(required = false) Integer diseaseType) {

        boolean exists = inpatientDiseaseService.checkDiseaseExists(patientId, diseaseId, diseaseType);
        return ResultUtils.success(exists);
    }



    /**
     * 分页查询患者的诊断记录（应该有bug）
     */
    @GetMapping("/list/page")
    public BaseResponse<Page<InpatientDisease>> listDiseaseByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam Integer patientId,
            @RequestParam(required = false) Integer diseaseType) {

        Page<InpatientDisease> page = inpatientDiseaseService.listDiseaseByPage(current, size, patientId, diseaseType);
        return ResultUtils.success(page);
    }
}