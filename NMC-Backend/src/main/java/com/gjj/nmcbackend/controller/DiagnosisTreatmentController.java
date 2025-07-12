package com.gjj.nmcbackend.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gjj.nmcbackend.annotion.UserAuthCheck;
import com.gjj.nmcbackend.common.BaseResponse;
import com.gjj.nmcbackend.common.ResultUtils;
import com.gjj.nmcbackend.exception.ErrorCode;
import com.gjj.nmcbackend.exception.ThrowUtils;
import com.gjj.nmcbackend.model.entity.DiagnosisTreatment;
import com.gjj.nmcbackend.service.DiagnosisTreatmentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/treatment")
public class DiagnosisTreatmentController {
    @Resource
    private DiagnosisTreatmentService diagnosisTreatmentService;

    /**
     * 添加诊疗项目信息
     *
     * @param addDiagnosisTreatmentRequest 诊疗项目信息
     * @return 添加结果
     */
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Integer> addDiagnosisTreatment(@RequestBody DiagnosisTreatment addDiagnosisTreatmentRequest) {
        ThrowUtils.throwIf(addDiagnosisTreatmentRequest == null , ErrorCode.PARAMS_ERROR);
        diagnosisTreatmentService.save(addDiagnosisTreatmentRequest);
        return ResultUtils.success(addDiagnosisTreatmentRequest.getId());
    }

    /**
     * 删除诊疗项目信息
     *
     * @param id 诊疗项目ID
     * @return 删除结果
     */
    @UserAuthCheck
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteDiagnosisTreatment(@PathVariable Long id) {
        // 1. 校验参数
        ThrowUtils.throwIf(id == null , ErrorCode.PARAMS_ERROR , "诊疗项目ID不能为空");

        // 2. 检查诊疗项目是否存在
        DiagnosisTreatment diagnosisTreatment = diagnosisTreatmentService.getById(id);
        ThrowUtils.throwIf(diagnosisTreatment == null , ErrorCode.NOT_FOUND_ERROR , "诊疗项目信息不存在");

        // 3. 删除诊疗项目信息
        boolean result = diagnosisTreatmentService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 修改诊疗项目信息
     *
     * @param diagnosisTreatment 诊疗项目信息
     * @return 修改结果
     */
    @UserAuthCheck
    @PutMapping("/update")
    public BaseResponse<Boolean> updateDiagnosisTreatment(@RequestBody DiagnosisTreatment diagnosisTreatment) {
        ThrowUtils.throwIf(diagnosisTreatment == null , ErrorCode.PARAMS_ERROR);
        boolean result = diagnosisTreatmentService.updateById(diagnosisTreatment);
        return ResultUtils.success(result);
    }

    /**
     * 根据诊疗项目名称查询
     *
     * @param chineseName 诊疗项目名称
     * @return 诊疗项目信息列表
     */
    @UserAuthCheck
    @GetMapping("/getByName")
    public BaseResponse<List<DiagnosisTreatment>> getDiagnosisTreatmentByName(@RequestParam String chineseName) {
        ThrowUtils.throwIf(StrUtil.isEmpty(chineseName) , ErrorCode.PARAMS_ERROR);
        List<DiagnosisTreatment> diagnosisTreatmentList = diagnosisTreatmentService.getDiagnosisTreatmentByName(chineseName);
        return ResultUtils.success(diagnosisTreatmentList);
    }

    /**
     * 分页获取诊疗项目信息
     *
     * @param current 当前页
     * @param size 每页大小
     * @return 分页结果
     */
    @UserAuthCheck
    @GetMapping("/list/page")
    public BaseResponse<Page<DiagnosisTreatment>> listDiagnosisTreatmentByPage(@RequestParam(defaultValue = "1") long current
            , @RequestParam(defaultValue = "10") long size,@RequestParam(required = false) String treatmentName) {
        Page<DiagnosisTreatment> page = diagnosisTreatmentService.listDiagnosisTreatmentByPage(current , size, treatmentName);
        return ResultUtils.success(page);
    }

    /**
     * 批量删除诊疗项目信息
     * @param ids 诊疗项目ID列表
     * @return 删除结果
     */
    @UserAuthCheck
    @DeleteMapping("/delete/batch")
    public BaseResponse<Boolean> deleteDiagnosisTreatmentByIds(@RequestBody List<Integer> ids) {

        ThrowUtils.throwIf(ids == null || ids.isEmpty() , ErrorCode.PARAMS_ERROR , "诊疗项目ID列表不能为空");
        boolean result = diagnosisTreatmentService.deleteDiagnosisTreatmentByIds(ids);
        return ResultUtils.success(result);
    }

}
