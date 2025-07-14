package com.gjj.nmcbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.BusinessException;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.dto.Inpatient.AddInpatientDrugRequest;
import com.gjj.nmcbackend.model.vo.InpatientDrugVO;
import com.gjj.nmcbackend.service.InpatientDrugService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/inpatient/drug")
public class InpatientDrugController {

    @Resource
    InpatientDrugService inpatientDrugService;

    @PostMapping("/add")
    public BaseResponse<Boolean> addDrug(@RequestBody AddInpatientDrugRequest request) {

        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);

        boolean result = inpatientDrugService.addDrug(request);
        return ResultUtils.success(result);

    }


    /**
     * 检查患者是否已添加该疾病诊断
     */
    @GetMapping("/check")
    public BaseResponse<Boolean> checkDrugExists(@RequestParam Integer patientId , @RequestParam Integer drugId) {

        boolean exists = inpatientDrugService.checkDrugExists(patientId , drugId);
        ThrowUtils.throwIf(!exists , new BusinessException(ErrorCode.NOT_FOUND_ERROR));
        return ResultUtils.success(exists);
    }

    @GetMapping("/list/page/vo")
    public BaseResponse<Page<InpatientDrugVO>> listDrugInfoByPage(
            @RequestParam(defaultValue = "1") long current ,
            @RequestParam(defaultValue = "10") long size ,
            @RequestParam(required = false) String chineseName) {  // 添加可选搜索参数

        Page<InpatientDrugVO> page = inpatientDrugService.listInpatientDrugByPage(current , size , chineseName);
        return ResultUtils.success(page);
    }


    /**
     * 删除诊断记录
     */
    @PostMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteDrug(@PathVariable Integer id) {
        boolean result = inpatientDrugService.removeById(id);
        ThrowUtils.throwIf(!result , new BusinessException(ErrorCode.SYSTEM_ERROR));
        return ResultUtils.success(result);
    }
}
