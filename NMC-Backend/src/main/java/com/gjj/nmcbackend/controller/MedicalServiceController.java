package com.gjj.nmcbackend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.entity.MedicalService;
import com.gjj.nmcbackend.service.MedicalServiceService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/service")
public class MedicalServiceController {

    @Resource
    private MedicalServiceService medicalServiceService;

    /**
     * 添加医疗服务信息
     *
     * @param addMedicalServiceRequest 医疗服务信息
     * @return 添加结果
     */
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Integer> addMedicalService(@RequestBody MedicalService addMedicalServiceRequest) {
        ThrowUtils.throwIf(addMedicalServiceRequest == null , ErrorCode.PARAMS_ERROR);
        medicalServiceService.save(addMedicalServiceRequest);
        return ResultUtils.success(addMedicalServiceRequest.getId());
    }

    /**
     * 删除医疗服务信息
     *
     * @param id 医疗服务ID
     * @return 删除结果
     */
    @UserAuthCheck
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteMedicalService(@PathVariable Long id) {
        // 1. 校验参数
        ThrowUtils.throwIf(id == null , ErrorCode.PARAMS_ERROR , "医疗服务ID不能为空");

        // 2. 检查医疗服务是否存在
        MedicalService medicalService = medicalServiceService.getById(id);
        ThrowUtils.throwIf(medicalService == null , ErrorCode.NOT_FOUND_ERROR , "医疗服务信息不存在");

        // 3. 删除医疗服务信息
        boolean result = medicalServiceService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 修改医疗服务信息
     *
     * @param medicalService 医疗服务信息
     * @return 修改结果
     */
    @UserAuthCheck
    @PutMapping("/update")
    public BaseResponse<Boolean> updateMedicalService(@RequestBody MedicalService medicalService) {
        ThrowUtils.throwIf(medicalService == null , ErrorCode.PARAMS_ERROR);
        boolean result = medicalServiceService.updateById(medicalService);
        return ResultUtils.success(result);
    }

    /**
     * 根据医疗服务名称查询
     *
     * @param chineseName 医疗服务名称
     * @return 医疗服务信息列表
     */
    @UserAuthCheck
    @GetMapping("/getByName")
    public BaseResponse<List<MedicalService>> getMedicalServiceByName(@RequestParam String chineseName) {
        ThrowUtils.throwIf(StrUtil.isEmpty(chineseName) , ErrorCode.PARAMS_ERROR);
        List<MedicalService> medicalServiceList = medicalServiceService.getMedicalServiceByName(chineseName);
        return ResultUtils.success(medicalServiceList);
    }

    /**
     * 分页获取医疗服务信息
     *
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    @UserAuthCheck
    @GetMapping("/list/page")
    public BaseResponse<Page<MedicalService>> listMedicalServiceByPage(@RequestParam(defaultValue = "1") long current
            , @RequestParam(defaultValue = "10") long size) {
        Page<MedicalService> page = medicalServiceService.listMedicalServiceByPage(current , size);
        return ResultUtils.success(page);
    }

    /**
     * 批量删除医疗服务信息
     * @param ids 医疗服务ID列表
     * @return 删除结果
     */
    @UserAuthCheck
    @DeleteMapping("/delete/batch")
    public BaseResponse<Boolean> deleteMedicalServiceByIds(@RequestBody List<Long> ids) {

        ThrowUtils.throwIf(ids == null || ids.isEmpty() , ErrorCode.PARAMS_ERROR , "医疗服务ID列表不能为空");
        boolean result = medicalServiceService.deleteMedicalServiceByIds(ids);
        return ResultUtils.success(result);
    }
}
