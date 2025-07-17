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
import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Api(tags = "医疗服务管理", value = "医疗服务相关操作")
@RestController
@RequestMapping("/service")
public class MedicalServiceController {

    @Resource
    private MedicalServiceService medicalServiceService;

    /**
     * 添加医疗服务信息
     */
    @ApiOperation(value = "添加医疗服务", notes = "添加新的医疗服务信息")
    @ApiImplicitParam(name = "addMedicalServiceRequest", value = "医疗服务信息", required = true, dataType = "MedicalService")
    @UserAuthCheck
    @PostMapping("/add")
    public BaseResponse<Integer> addMedicalService(@RequestBody MedicalService addMedicalServiceRequest) {
        ThrowUtils.throwIf(addMedicalServiceRequest == null, ErrorCode.PARAMS_ERROR);
        medicalServiceService.save(addMedicalServiceRequest);
        return ResultUtils.success(addMedicalServiceRequest.getId());
    }

    /**
     * 删除医疗服务信息
     */
    @ApiOperation(value = "删除医疗服务", notes = "根据ID删除医疗服务信息")
    @ApiImplicitParam(name = "id", value = "医疗服务ID", required = true, paramType = "path", dataType = "Long")
    @UserAuthCheck
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteMedicalService(@PathVariable Long id) {
        ThrowUtils.throwIf(id == null, ErrorCode.PARAMS_ERROR, "医疗服务ID不能为空");
        MedicalService medicalService = medicalServiceService.getById(id);
        ThrowUtils.throwIf(medicalService == null, ErrorCode.NOT_FOUND_ERROR, "医疗服务信息不存在");
        boolean result = medicalServiceService.removeById(id);
        return ResultUtils.success(result);
    }

    /**
     * 修改医疗服务信息
     */
    @ApiOperation(value = "更新医疗服务", notes = "更新医疗服务信息")
    @ApiImplicitParam(name = "medicalService", value = "医疗服务信息", required = true, dataType = "MedicalService")
    @UserAuthCheck
    @PutMapping("/update")
    public BaseResponse<Boolean> updateMedicalService(@RequestBody MedicalService medicalService) {
        ThrowUtils.throwIf(medicalService == null, ErrorCode.PARAMS_ERROR);
        boolean result = medicalServiceService.updateById(medicalService);
        return ResultUtils.success(result);
    }

    /**
     * 根据医疗服务名称查询
     */
    @ApiOperation(value = "按名称查询医疗服务", notes = "根据中文名称查询医疗服务信息")
    @ApiImplicitParam(name = "chineseName", value = "医疗服务中文名称", required = true, paramType = "query", dataType = "String")
    @UserAuthCheck
    @GetMapping("/getByName")
    public BaseResponse<List<MedicalService>> getMedicalServiceByName(@RequestParam String chineseName) {
        ThrowUtils.throwIf(StrUtil.isEmpty(chineseName), ErrorCode.PARAMS_ERROR);
        List<MedicalService> medicalServiceList = medicalServiceService.getMedicalServiceByName(chineseName);
        return ResultUtils.success(medicalServiceList);
    }

    /**
     * 分页获取医疗服务信息
     */
    @ApiOperation(value = "分页查询医疗服务", notes = "分页获取医疗服务信息，可选项按服务名称筛选")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "current", value = "当前页码", defaultValue = "1", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "size", value = "每页数量", defaultValue = "10", paramType = "query", dataType = "long"),
            @ApiImplicitParam(name = "serviceName", value = "服务名称(可选)", paramType = "query", dataType = "String")
    })
    @UserAuthCheck
    @GetMapping("/list/page")
    public BaseResponse<Page<MedicalService>> listMedicalServiceByPage(
            @RequestParam(defaultValue = "1") long current,
            @RequestParam(defaultValue = "10") long size,
            @RequestParam(required = false) String serviceName) {
        Page<MedicalService> page = medicalServiceService.listMedicalServiceByPage(current, size, serviceName);
        return ResultUtils.success(page);
    }

    /**
     * 批量删除医疗服务信息
     */
    @ApiOperation(value = "批量删除医疗服务", notes = "根据ID列表批量删除医疗服务信息")
    @ApiImplicitParam(name = "ids", value = "医疗服务ID列表", required = true, dataType = "List<Long>")
    @UserAuthCheck
    @DeleteMapping("/delete/batch")
    public BaseResponse<Boolean> deleteMedicalServiceByIds(@RequestBody List<Long> ids) {
        ThrowUtils.throwIf(ids == null || ids.isEmpty(), ErrorCode.PARAMS_ERROR, "医疗服务ID列表不能为空");
        boolean result = medicalServiceService.deleteMedicalServiceByIds(ids);
        return ResultUtils.success(result);
    }
}